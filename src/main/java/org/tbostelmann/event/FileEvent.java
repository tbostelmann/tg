package org.tbostelmann.event;

/**
 * Created by tbostelmann on 8/14/15.
 */
public class FileEvent extends BaseEvent {
	public static final String FILE_PATH = "file.path";

	public String getFilePath() {
		return (String)get(FILE_PATH);
	}
	public BaseEvent setFilePath(final String filePath) {
		return set(FILE_PATH, filePath);
	}
}
