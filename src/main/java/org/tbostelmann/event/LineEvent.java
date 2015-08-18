package org.tbostelmann.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tbostelmann on 8/14/15.
 */
public class LineEvent extends FileEvent {
	public static final String FILE_LINE = "file.line";

	public String getFileLine() {
		return (String)get(FILE_LINE);
	}
	public BaseEvent setFileLine(final String line) {
		return set(FILE_LINE, line);
	}
}
