package org.tbostelmann.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbostelmann.event.LineEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * Created by tbostelmann on 7/31/15.
 */
public abstract class BaseWorker implements Callable {
	private static final Logger logger = LoggerFactory.getLogger(BaseWorker.class);

	private final BlockingQueue<LineEvent> queue;
	private final boolean exitOnFailure;
	private volatile boolean shutdownRequested = false;

	public BaseWorker(final BlockingQueue<LineEvent> queue) {
		this.queue = queue;
		this.exitOnFailure = false;
	}

	public abstract Object onCall() throws Exception;

	public Object call() throws Exception {
		return onCall();
	}

	public boolean isExitOnFailure() {
		return exitOnFailure;
	}

	public boolean isShutdownRequested() {
		//return Thread.interrupted();
		return shutdownRequested;
	}

	public void requestShutdown() {
		shutdownRequested = true;
	}

	protected void checkExitOnFailure(final Exception failure) throws Exception {
		if (isExitOnFailure()) {
			logger.error("Unexpected exception", failure);
			throw failure;
		}
		else {
			logger.warn("Unexpected exception", failure);
		}
	}
}
