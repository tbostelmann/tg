package org.tbostelmann;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tbostelmann on 8/13/15.
 */
public class SearchJobTest {

	@Test(expected = IllegalArgumentException.class)
	public void initFiles_FileDoesNotExist_ShouldThrowException() throws Exception {
		SearchJob job = new SearchJob(new String[]{"fooFileNoWhereToBeFound.log"});
	}

	@Test
	public void initFiles_FileExists_Success() {
		SearchJob job = new SearchJob(new String[]{"./docs/hitchhikers.txt"});
		assertNotNull(job);
	}
}