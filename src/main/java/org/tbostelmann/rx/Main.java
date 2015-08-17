package org.tbostelmann.rx;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbostelmann.Message;
import org.tbostelmann.SearchJob;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by tbostelmann on 8/13/15.
 */
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static final void main(String[] args) throws IOException, InterruptedException {
		long startTime = System.nanoTime();

		SearchJob job = new SearchJob(args);
		List<File> files = job.getFiles();
		List<Observable<Message>> observables = new ArrayList<>();
		final Map<String, Long> fileCounts = new HashMap<>();
		for (final File file : files) {
			fileCounts.put(file.getAbsolutePath(), 0L);
			observables.add(
					Observable.create(new FileOnSubscribe(file))
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
						Message m = (Message) o;
						String path = (String) m.get(Message.FILE_PATH);
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
}
