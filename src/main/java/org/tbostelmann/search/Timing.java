package org.tbostelmann.search;

import org.apache.commons.math3.stat.StatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

/**
 * Created by tbostelmann on 8/21/15.
 */
public class Timing {
	private static final Logger logger = LoggerFactory.getLogger(Timing.class);

	private List<Long> timeList;

	public Timing() { timeList = new ArrayList<>(); }

	public synchronized void addTime(final Long time) {
		timeList.add(time);
	}

	public void printTiming() {
		double[] times = new double[timeList.size()];
		for (int i=0; i<timeList.size(); i++)
			times[i] = (double)timeList.get(i);
		double min, max, mean, p50, p75, p95, p98, p99;
		min = StatUtils.min(times);
		max = StatUtils.max(times);
		mean = StatUtils.mean(times);
		p50 = StatUtils.percentile(times, 50);
		p75 = StatUtils.percentile(times, 75);
		p95 = StatUtils.percentile(times, 95);
		p98 = StatUtils.percentile(times, 98);
		p99 = StatUtils.percentile(times, 99);
		logger.info(String.format("min=%dns max=%dns mean=%dns", (int) min, (int) max, (int) mean));
		logger.info(String.format("50=%dns 75=%dns 95=%dns 98=%dns 99=%dns", (int)p50, (int)p75, (int)p95, (int)p98, (int)p99));
	}
}
