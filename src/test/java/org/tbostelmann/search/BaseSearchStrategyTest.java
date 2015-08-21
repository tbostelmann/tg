package org.tbostelmann.search;

import org.junit.Before;
import org.junit.Test;

import java.io.IOError;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Created by tbostelmann on 8/21/15.
 */
public class BaseSearchStrategyTest {
	private BaseSearchStrategy s;
	private String fileDirectory;

	@Before
	public void setUp() {
		fileDirectory = ".";
		s = new BaseSearchStrategy(fileDirectory) {
			@Override
			public SearchResult search(String searchTerm) {
				return null;
			}
		};
	}

	@Test
	public void testGetFileDirectory() throws Exception {
		assertTrue(s.getFileDirectory().getAbsolutePath().contains(fileDirectory));
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor_InvalidDirectory_ShouldThrowException() {
		String fileDir = "/this_dont_exist";
		s = new BaseSearchStrategy(fileDir) {
			@Override
			public SearchResult search(String searchTerm) {
				return null;
			}
		};
	}

	@org.junit.Test
	public void testRandomWord() throws Exception {
		assertTrue(s.randomWord(4)
					.length() == 4);
	}
}