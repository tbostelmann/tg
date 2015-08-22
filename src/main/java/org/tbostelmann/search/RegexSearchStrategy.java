package org.tbostelmann.search;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Created by tbostelmann on 8/20/15.
 */
public class RegexSearchStrategy extends GrepSearchStrategy {
	private static final Logger logger = LoggerFactory.getLogger(RegexSearchStrategy.class);

	public RegexSearchStrategy(final String filePath) {
		super(filePath);
	}

	@Override
	public SearchResult search(final String searchTerm) {
		SearchResult result = new SearchResult();
		long startTime = currentTime();
		Pattern pattern = Pattern.compile(searchTerm);
		IntStream.range(0, getFiles().size()).forEach(
				i -> {
					Long count = 0L;
					try {
						LineIterator iterator = FileUtils.lineIterator(getFiles().get(i), "UTF-8");
						String nextLine;
						while(iterator.hasNext()) {
							nextLine = iterator.next();
							if (nextLine.matches(searchTerm)) {
								count++;
							}
						}
						iterator.close();
						if (count > 0)
							result.addResult(getFiles().get(i), count);
					} catch (IOException e) {
						logger.error("Error in file" + getFiles().get(i).getAbsolutePath(), e);
					}
				}
		);
		getTiming().addTime(currentTime() - startTime);
		return result;
	}

	public static void main(String[] args) {
		final int num = 2000000;
		final int tic = num / 10;

		final String fileDir = "./searchFiles";
		RegexSearchStrategy searchStrategy = new RegexSearchStrategy(fileDir);

		Long startTime = currentTime();
		IntStream.range(0, num).parallel()
				 .forEach(
						 nbr -> {
							 searchStrategy.search(".*(" + randomWord((nbr % 8) + 4) + ").*");
							 if (nbr % tic == 0)
								 logger.info("count: " + nbr);
				}
		);
		Long endTime = currentTime();
		logger.info("RegexSearchStrategy time: " + ((endTime - startTime) / 1000000) + "ms");
		searchStrategy.getTiming().printTiming();
	}
}
