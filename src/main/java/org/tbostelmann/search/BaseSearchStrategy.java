package org.tbostelmann.search;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.math3.stat.StatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.stream.IntStream;

/**
 * Created by tbostelmann on 8/19/15.
 */
public abstract class BaseSearchStrategy implements SearchStrategy {
	private static final Logger logger = LoggerFactory.getLogger(BaseSearchStrategy.class);

	private final File fileDirectory;
	private final Timing timing;

	public BaseSearchStrategy(final String filePath) {
		File fileDirectory = new File(filePath);
		if (!fileDirectory.exists() || !fileDirectory.isDirectory())
			throw new IllegalArgumentException("Directory does not exist: " + filePath);
		this.fileDirectory = fileDirectory;
		this.timing = new Timing();
	}

	public Timing getTiming() {
		return timing;
	}

	public File getFileDirectory() {
		return this.fileDirectory;
	}

	public static Long currentTime() {
		return System.nanoTime();
	}

	public static String randomWord(final int size) {
		return RandomStringUtils.randomAlphabetic(size);
	}
}
