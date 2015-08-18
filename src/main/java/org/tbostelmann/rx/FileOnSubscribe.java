package org.tbostelmann.rx;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbostelmann.SearchIndex;
import org.tbostelmann.event.LineEvent;
import rx.Observable;
import rx.Subscriber;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by tbostelmann on 8/14/15.
 */
public class FileOnSubscribe implements Observable.OnSubscribe<LineEvent> {
	private static final Logger logger = LoggerFactory.getLogger(FileOnSubscribe.class);

	private final File file;
	private final String searchTerm;

	public FileOnSubscribe(final File file, final String searchTerm) {
		this.file = file;
		this.searchTerm = searchTerm.toLowerCase();
	}

	public static final void main(String[] args) throws IOException, InterruptedException {
		long startTime = System.nanoTime();

		SearchIndex job = new SearchIndex(args);
		List<File> files = job.getFiles();
		List<Observable<LineEvent>> observables = new ArrayList<>();
		final Map<String, Long> fileCounts = new HashMap<>();
		for (final File file : files) {
			fileCounts.put(file.getAbsolutePath(), 0L);
			observables.add(
					Observable.create(new FileOnSubscribe(file, "Guide"))
							  .onBackpressureBuffer()
			);
		}

		CountDownLatch latch = new CountDownLatch(1);

		Observable.merge(observables.toArray(new Observable[observables.size()]))
//				  .observeOn(Schedulers.io())
//				  .subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Object>() {
					@Override
					public void onStart() {
						request(1);
					}

					@Override
					public void onCompleted() {
						latch.countDown();
					}

					@Override
					public void onError(Throwable e) {
						logger.info("Error", e);
						latch.countDown();
					}

					@Override
					public void onNext(Object o) {
						LineEvent m = (LineEvent) o;
						String path = (String) m.get(LineEvent.FILE_PATH);
						fileCounts.put(path, (Long) fileCounts.get(path) + 1);
						request(1);
					}
				});
		latch.await();

		for (String filePath : fileCounts.keySet())
			logger.info(filePath + ": " + fileCounts.get(filePath));

		long endTime = System.nanoTime();

		logger.info("Completion time: " + ((endTime - startTime)/1000000) + "ms");
	}

	@Override
	public void call(Subscriber<? super LineEvent> subscriber) {
		logger.info("Subscribing");
		try {
			LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");
			String nextLine;
			while (iterator.hasNext() && !subscriber.isUnsubscribed()) {
				nextLine = iterator.next().toLowerCase();
				if (nextLine.contains(searchTerm)) {
					subscriber.onNext(
							new LineEvent()
									.set(LineEvent.FILE_PATH, nextLine)
									.set(LineEvent.FILE_PATH, file.getAbsolutePath())
					);
				}
			}
			if (!subscriber.isUnsubscribed())
				subscriber.onCompleted();
		} catch (IOException e) {
			if (!subscriber.isUnsubscribed())
				subscriber.onError(e);
			else
				logger.warn("Dropped exception", e);
		}
	}
}
