package org.tbostelmann.search;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by tbostelmann on 8/19/15.
 */
public abstract class GrepSearchStrategy extends BaseSearchStrategy {
	private static final Logger logger = LoggerFactory.getLogger(GrepSearchStrategy.class);

	private List<File> files;

	public GrepSearchStrategy(final String filePath) {
		super(filePath);
		init();
	}

	public void init() {
		List<File> listOfFiles = new ArrayList<>();
		for(File file : getFileDirectory().listFiles()) {
			if (file.isFile())
				listOfFiles.add(file);
		}
		files = listOfFiles;
	}

	List<File> getFiles() {
		return files;
	}
}
