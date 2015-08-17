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
public class FileOnSubscribe implements Observable.OnSubscribe<Message> {
	private static final Logger logger = LoggerFactory.getLogger(FileOnSubscribe.class);

	private final File file;

	public FileOnSubscribe(final File file) {
		this.file = file;
	}

	@Override
	public void call(Subscriber<? super Message> subscriber) {
		logger.info("Subscribing");
		try {
			LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");
			while (iterator.hasNext() && !subscriber.isUnsubscribed()) {
				subscriber.onNext(
						new Message()
								.set(Message.FILE_PATH, iterator.next())
								.set(Message.FILE_PATH, file.getAbsolutePath())
				);
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
