package org.tbostelmann.search;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by tbostelmann on 8/21/15.
 */
public class BruteForceSearchStrategyTest {
	BruteForceSearchStrategy s;
	String fileDirectory = "./searchFiles";

	@Before
	public void setUp() throws Exception {
		s = new BruteForceSearchStrategy(fileDirectory);
	}

	@Test
	public void search_UsingExistingTerm_ShouldReturnValidResult() throws Exception {
		SearchResult results = s.search("Guide");
		assertEquals(1, results.getResults().size());
		SearchResult.Result result = results.getResults().get(0);
		assertEquals(3L, result.getScore());
		assertEquals("hitchhikers.txt", result.getDoc().getName());
	}
}