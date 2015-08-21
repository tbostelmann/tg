package org.tbostelmann.search;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tbostelmann on 8/21/15.
 */
public class RegexSearchStrategyTest {

	@Test
	public void search_ValidTerm_ReturnsResults() throws Exception {
		RegexSearchStrategy searcher = new RegexSearchStrategy("./searchFiles");
		SearchResult result = searcher.search(".*(Guide).*");
		assertEquals(1, result.getResults().size());
		assertEquals(3L, result.getResults().get(0).getScore());
		assertEquals("hitchhikers.txt", result.getResults().get(0).getDoc().getName());
	}
}