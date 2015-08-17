package org.tbostelmann;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tbostelmann on 8/14/15.
 */
public class Message {
	public static final String FILE_PATH = "file.path";
	public static final String FILE_LINE = "file.line";

	private final Map<String, Serializable> properties;

	public Message() {
		properties = new HashMap<>();
	}

	public Message(final Map properties) {
		this.properties = properties;
	}

	public Message set(final String key, final Serializable value) {
		properties.put(key, value);
		return this;
	}

	public Serializable get(final String key) {
		return properties.get(key);
	}
}
