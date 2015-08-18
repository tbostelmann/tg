package org.tbostelmann.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tbostelmann on 8/14/15.
 */
public class BaseEvent {
	private final Map<String, Serializable> properties;

	public BaseEvent() {
		properties = new HashMap<>();
	}

	public BaseEvent(final Map properties) {
		this.properties = properties;
	}

	public BaseEvent set(final String key, final Serializable value) {
		properties.put(key, value);
		return this;
	}

	public Serializable get(final String key) {
		return properties.get(key);
	}
}
