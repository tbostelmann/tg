package org.tbostelmann.search;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Created by tbostelmann on 8/20/15.
 */
public class CachedSearchStrategy extends GrepSearchStrategy {
	private static final Logger logger = LoggerFactory.getLogger(CachedSearchStrategy.class);

	private Map<File, Map<String, Long>> fileToWordHash = new ConcurrentHashMap<>();

	public CachedSearchStrategy(final String filePath) {
		super(filePath);
		Pattern pattern = Pattern.compile("\\w+");
		IntStream.range(0, getFiles().size()).forEach(
				i -> {
					File file = getFiles().get(i);
					try {
						LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");
						String nextLine;
						Matcher matcher;
						while (iterator.hasNext()) {
							nextLine = iterator.next()
											   .toLowerCase();
							matcher = pattern.matcher(nextLine);
							while (matcher.find())
								addWordCount(file, matcher.group().toLowerCase());
						}
						iterator.close();
					} catch (IOException e) {
						logger.error("Error in file" + getFiles().get(i)
																 .getAbsolutePath(), e);
					}
				}
		);
	}

	void addWordCount(final File file, final String word) {
		if (fileToWordHash.get(file) == null)
			fileToWordHash.put(file, new ConcurrentHashMap<String, Long>());
		Map<String, Long> wordHash = fileToWordHash.get(file);
		Long count = wordHash.get(word);
		if (count == null)
			count = 1l;
		else
			count++;
		wordHash.put(word, count);
	}

	Long getWordCount(final File file, final String word) {
		return fileToWordHash.get(file).get(word);
	}

	@Override
	public SearchResult search(final String term) {
		final String searchTerm = term.toLowerCase();
		long startTime = currentTime();
		SearchResult result = new SearchResult();
		for (final File file : fileToWordHash.keySet()) {
			Long count = getWordCount(file, searchTerm);
			if (count != null && count > 0)
				result.addResult(file, count);
		}
		getTiming().addTime(currentTime() - startTime);
		return result;
	}

	public static void main(String[] args) {
		final int num = 2000000;
		final int tic = num / 10;

		final String fileDir = "./searchFiles";
		CachedSearchStrategy searchStrategy = new CachedSearchStrategy(fileDir);

		Long startTime = currentTime();
		IntStream.range(0, num).forEach(
				nbr -> {
					searchStrategy.search(randomWord((nbr % 8) + 4));
					if (nbr % tic == 0)
						logger.info("count: " + nbr);
				}
		);
		Long endTime = currentTime();
		logger.info("CachedSearchStrategy time: " + ((endTime - startTime) / 1000000) + "ms");
		searchStrategy.getTiming().printTiming();
	}
}
