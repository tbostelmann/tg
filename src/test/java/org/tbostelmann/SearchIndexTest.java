package org.tbostelmann;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tbostelmann on 8/13/15.
 */
public class SearchIndexTest {

	@Test(expected = IllegalArgumentException.class)
	public void initFiles_FileDoesNotExist_ShouldThrowException() throws Exception {
		SearchIndex job = new SearchIndex(new String[]{"fooFileNoWhereToBeFound.log"});
	}

	@Test
	public void initFiles_FileExists_Success() {
		SearchIndex job = new SearchIndex(new String[]{"./docs/hitchhikers.txt"});
		assertNotNull(job);
	}
}