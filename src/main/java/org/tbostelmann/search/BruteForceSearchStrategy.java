package org.tbostelmann.search;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

/**
 * Created by tbostelmann on 8/20/15.
 */
public class BruteForceSearchStrategy extends GrepSearchStrategy {
	private static final Logger logger = LoggerFactory.getLogger(BruteForceSearchStrategy.class);

	public BruteForceSearchStrategy(final String filePath) {
		super(filePath);
	}

	@Override
	public SearchResult search(final String searchTerm) {
		long startTime = currentTime();
		String searchTermToLower = searchTerm.toLowerCase();
		SearchResult results = new SearchResult();
		IntStream.range(0, getFiles().size()).forEach(
				i -> {
					Long count = 0L;
					try {
						File file = getFiles().get(i);
						LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");
						String nextLine;
						while (iterator.hasNext()) {
							nextLine = iterator.next()
											   .toLowerCase();
							if (nextLine.contains(searchTermToLower)) {
								count++;
							}
						}
						iterator.close();
						if (count > 0)
							results.addResult(file, count);
					} catch (IOException e) {
						logger.error("Error in file" + getFiles().get(i)
																 .getAbsolutePath(), e);
					}
				}
		);
		getTiming().addTime(currentTime() - startTime);
		return results;
	}

	public static void main(String[] args) {
		final int num = 2000000;
		final int tic = num / 10;

		final String fileDir = "./searchFiles";
		BruteForceSearchStrategy searcher = new BruteForceSearchStrategy(fileDir);

		Long startTime = currentTime();
		IntStream.range(0, num).forEach(
				nbr -> {
					searcher.search(randomWord((nbr % 8) + 4));
					if (nbr % tic == 0)
						logger.info("count: " + nbr);
				}
		);
		Long endTime = currentTime();
		logger.info("BruteForceSearchStrategy time: " + ((endTime - startTime) / 1000000) + "ms");
		searcher.getTiming().printTiming();
	}
}
