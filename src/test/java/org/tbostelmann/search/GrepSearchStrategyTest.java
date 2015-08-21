package org.tbostelmann.search;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by tbostelmann on 8/21/15.
 */
public class GrepSearchStrategyTest {

	@Test
	public void testInit() throws Exception {
		GrepSearchStrategy s = new GrepSearchStrategy("./searchFiles") {
			@Override
			public SearchResult search(String searchTerm) {
				return null;
			}
		};
		assertEquals(3,
					 s.getFiles()
					  .size());
		List<String> fileNames = new ArrayList<>();
		fileNames.add("french_armed_forces.txt");
		fileNames.add("hitchhikers.txt");
		fileNames.add("warp_drive.txt");
		for (File file : s.getFiles())
			assertTrue(fileNames.contains(file.getName()));
	}
}