package com.blackboxsoft.maven;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.blackboxsoft.maven.VersionMojo;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

public class VersionMojoTest {

	VersionMojo mojo;
	
	@Before
	public void setup() {
		mojo = spy(new VersionMojo());
		mojo.setBranch("Head");
		mojo.setProject("Test Project");
		mojo.setVersion("1.0");
		List<String> types = new ArrayList<String>();
		types.add("txt");
		mojo.setTypes(types);
	}
	
	@Test
	public void shouldCreateMojo() {
		assertThat(mojo, notNullValue());
	}

	@Test
	public void shouldGenerateTheProperFileName() {
		VersionMojo.VersionFile outputFile = new VersionMojo.VersionFile("txt", "These are the contents");
		String actual = mojo.generateVersionFilename(outputFile);
		assertThat("Does not generate the proper file name", actual, equalTo("src/main/webapp/version.txt"));
	}
	
	@Test 
	public void shouldGenerateTheProperFileContents() throws Exception {
		String actual = mojo.generateFileContents("txt");
		assertThat("Does not generate the proper file contents", actual, equalTo("Test Project Version 1.0 Branch Head"));
	}
	
	@Test
	public void shouldCreateAListOfTwoVersionFiles() throws Exception {
		mojo.getTypes().add("htm");
		List<VersionMojo.VersionFile> files = mojo.createFileContents();
		assertThat("Incorrect number of verion files created", files.size(), equalTo(2));
	}
	
//	@Test(expected=MojoExecutionException.class)
//	public void shouldPukeOnUnknownFileType() throws Exception {
//		mojo.getTypes().add("xml");
//		List<VersionMojo.VersionFile> files = mojo.createFileContents();
//		fail("Should not create file of type xml");
//	}
	
}
