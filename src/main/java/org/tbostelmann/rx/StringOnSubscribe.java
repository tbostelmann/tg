package org.tbostelmann.rx;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbostelmann.Message;
import rx.Observable;
import rx.Subscriber;

import java.io.File;
import java.io.IOException;

/**
 * Created by tbostelmann on 8/14/15.
 */
public class StringOnSubscribe implements Observable.OnSubscribe<Long> {
	private static final Logger logger = LoggerFactory.getLogger(StringOnSubscribe.class);

	private final File file;

	public StringOnSubscribe(final File file) {
		this.file = file;
	}

	@Override
	public void call(Subscriber<? super Long> subscriber) {
		try {
			LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");
			while (iterator.hasNext() && !subscriber.isUnsubscribed()) {
				iterator.next();
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
