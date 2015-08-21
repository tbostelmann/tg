package org.tbostelmann.search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tbostelmann on 8/21/15.
 */
public class SearchResult {
	private final List<Result> results;
	public SearchResult() {
		results = new ArrayList<>();
	}

	public void addResult(final File file, final Number score) {
		results.add(new Result(file, score));
	}

	public List<Result> getResults() {
		return results;
	}

	public class Result {
		private final File doc;
		private final Number score;
		Result(final File doc, final Number score) {
			this.doc = doc;
			this.score = score;
		}

		public Number getScore() {
			return score;
		}

		public File getDoc() {
			return doc;
		}
	}
}
