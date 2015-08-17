package org.tbostelmann.rx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbostelmann.SearchJob;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.observables.MathObservable;

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
public class Main2 {
	private static final Logger logger = LoggerFactory.getLogger(Main2.class);

	public static final void main(String[] args) throws IOException, InterruptedException {
		long startTime = System.nanoTime();

		SearchJob job = new SearchJob(args);
		List<File> files = job.getFiles();
		CountDownLatch latch = new CountDownLatch(files.size());
		for (final File file : files) {
			MathObservable.sumLong(
					Observable.create(new StringOnSubscribe(file))
											 .onBackpressureBuffer()
			).subscribe(new Subscriber<Long>() {
				@Override
				public void onCompleted() {
					latch.countDown();
				}

				@Override
				public void onError(Throwable e) {
					logger.error("Error with file: " + file, e);
					latch.countDown();
				}

				@Override
				public void onNext(Long aLong) {
					logger.info(file.getAbsolutePath() + ": " + aLong);
				}
			});
		}
		latch.await();

		long endTime = System.nanoTime();

		logger.info("Completion time: " + ((endTime - startTime)/1000000) + "ms");
	}
}
