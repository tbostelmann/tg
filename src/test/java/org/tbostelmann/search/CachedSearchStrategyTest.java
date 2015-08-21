package org.tbostelmann.search;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tbostelmann on 8/21/15.
 */
public class CachedSearchStrategyTest {
	@Test
	public void testSearch() throws Exception {
		CachedSearchStrategy searcher = new CachedSearchStrategy("./searchFiles");
		SearchResult result = searcher.search("Guide");
		assertEquals(8L, result.getResults().get(0).getScore());
	}
}