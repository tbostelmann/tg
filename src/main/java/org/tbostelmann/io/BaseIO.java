package org.tbostelmann.io;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public abstract class BaseIO {
	private static final Logger logger = LoggerFactory.getLogger(BaseIO.class);

	protected final Boolean exitOnFailure;

	private ListeningExecutorService executor;
	private int numberThreads = 1;
	private final BlockingQueue<String> queue;
	private List<BaseWorker> workers;

	private int executorServiceWaitTimeInSeconds;

	public BaseIO(final BlockingQueue<String> queue) {
		this.queue = queue;
		this.exitOnFailure = false;
	}

	public int getNumberThreads() {
		return numberThreads;
	}

	public void setNumberThreads(final int numberThreads) {
		this.numberThreads = numberThreads;
	}

	public void start(FutureCallback futureCallback) {
		logger.info("begin start()");
		//executorServiceWaitTimeInSeconds = ConfigurationManager.getConfigInstance().getInt("aqueduct.executor.timeoutwaittime", 2);
		final ThreadFactory threadFactory = new ThreadFactoryBuilder()
				.setNameFormat(this.getClass().getSimpleName() + "-pool-%d")
				.build();
		executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(getNumberThreads(), threadFactory));
		workers = getWorkers();
		for (BaseWorker worker : workers) {
			ListenableFuture workerListener = executor.submit(worker);
			Futures.addCallback(workerListener, futureCallback);
		}
		logger.info("end start()");
	}

	public boolean isShutdown() {
		if (executor == null)
			return false;
		return executor.isShutdown();
	}

	public boolean isTerminated() {
		if (executor == null)
			return false;
		return executor.isTerminated();
	}

	public boolean shutdown() {
		logger.info(this.getClass().getCanonicalName() + ".beforeShutdown()");
		boolean beforeShutdownSuccess;
		try {
			beforeShutdownSuccess = beforeShutdown();
		}
		catch (Throwable e) {
			logger.error("Error before close", e);
			beforeShutdownSuccess = false;
		}

		logger.info(this.getClass().getCanonicalName() + ".executor.shutdown()");
		if (workers != null) {
			for (BaseWorker worker : workers)
				worker.requestShutdown();
		}
		boolean shutdownSuccess = true;
		if (executor != null) {
			executor.shutdown();
			try {
				if (!executor.awaitTermination(executorServiceWaitTimeInSeconds, TimeUnit.SECONDS)) {
					logger.warn("Executor did not terminate in the specified time.");
					List<Runnable> droppedTasks = executor.shutdownNow();
					logger.warn("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed.");
				}
			} catch (InterruptedException e) {
				logger.error("Executor was interrupted", e);
				shutdownSuccess = false;
			}
		}

		logger.info(this.getClass().getCanonicalName() + ".afterShutdown()");
		boolean afterShutdownSuccess;
		try {
			afterShutdownSuccess = afterShutdown();
		}
		catch (Throwable e) {
			logger.error("Error after close", e);
			afterShutdownSuccess = false;
		}

		return beforeShutdownSuccess && shutdownSuccess && afterShutdownSuccess;
	}

	public boolean beforeShutdown() {
		return true;
	}

	public boolean afterShutdown() {
		return true;
	}

	public abstract List<BaseWorker> getWorkers();
}

