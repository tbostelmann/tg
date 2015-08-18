package org.tbostelmann.rx;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbostelmann.SearchIndex;
import rx.Observable;
import rx.Subscriber;
import rx.observables.MathObservable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by tbostelmann on 8/14/15.
 */
public class StringOnSubscribe implements Observable.OnSubscribe<Long> {
	private static final Logger logger = LoggerFactory.getLogger(StringOnSubscribe.class);

	private final File file;
	private final String searchTerm;

	public StringOnSubscribe(final File file, final String searchTerm) {
		this.file = file;
		this.searchTerm = searchTerm;
	}

	public static final void main(String[] args) throws IOException, InterruptedException {
		long startTime = System.nanoTime();

		SearchIndex job = new SearchIndex(args);
		List<File> files = job.getFiles();
		CountDownLatch latch = new CountDownLatch(files.size());
		for (final File file : files) {
			MathObservable.sumLong(
					Observable.create(new StringOnSubscribe(file, "Guide"))
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

	@Override
	public void call(Subscriber<? super Long> subscriber) {
		try {
			LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");
			while (iterator.hasNext() && !subscriber.isUnsubscribed()) {
				if (iterator.next().toLowerCase().contains(searchTerm))
					subscriber.onNext(1L);
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
