package org.tbostelmann;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tbostelmann on 8/13/15.
 */
public class SearchJob {
	private static final Logger logger = LoggerFactory.getLogger(SearchJob.class);

	private final List<File> files;

	public SearchJob(final String[] filePaths) {
		files = initFiles(filePaths);
	}

	List<File> initFiles(final String[] filePaths) {
		List<File> files = new ArrayList<>(filePaths.length);
		File file;
		for (String filePath : filePaths) {
			file = new File(filePath);
			if (!file.exists() || file.isDirectory())
				throw new IllegalArgumentException("File does not exist: " + filePath);
			else {
				logger.info("Adding file: " + filePath);
				files.add(file);
			}
		}
		return files;
	}

	public List<File> getFiles() {
		return files;
	}
}
