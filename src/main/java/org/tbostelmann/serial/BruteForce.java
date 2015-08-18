package org.tbostelmann.serial;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbostelmann.SearchIndex;

import java.io.File;
import java.io.IOException;

/**
 * Created by tbostelmann on 8/17/15.
 */
public class BruteForce {
	private static final Logger logger = LoggerFactory.getLogger(BruteForce.class);

	public static void linearBrutForceSearch(final SearchIndex job, final String term) throws IOException {
		int count;
		LineIterator iterator;
		for (File file : job.getFiles()) {
			count = 0;
			iterator = FileUtils.lineIterator(file, "UTF-8");
			while(iterator.hasNext()) {
				iterator.next();
				count++;
			}
			logger.info(file.getAbsolutePath() + ": " + count);
		}
	}

	public static void main(String[] args) throws IOException {
		long startTime = System.nanoTime();

		SearchIndex job = new SearchIndex(args);

		linearBrutForceSearch(job, "foo");

		long endTime = System.nanoTime();

		logger.info("Completion time: " + ((endTime - startTime)/1000000) + "ms");
	}
}
