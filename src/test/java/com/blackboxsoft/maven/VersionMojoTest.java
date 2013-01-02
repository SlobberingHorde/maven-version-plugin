package com.blackboxsoft.maven;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.spy;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class VersionMojoTest {

	VersionMojo mojo;
	
	@Before
	public void setup() {
		mojo = spy(new VersionMojo());
		mojo.setBranch("Head");
		mojo.setProject("Test Project");
		mojo.setVersion("1.0");
		mojo.setBuildTimestamp("2012-10-19 11:57:00");
		mojo.setBuildTag("Test Tag");
		String[] types = new String[]{"txt"};
		mojo.setExtensions(types);
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
		assertThat("Does not generate the proper file contents", actual, equalTo("Test Project Version 1.0 Build Timestamp 2012-10-19 11:57:00 Branch Head Build Tag Name Test Tag"));
	}
	
	@Test
	public void shouldCreateAListOfThreeVersionFiles() throws Exception {
		mojo.setExtensions(new String[] {"txt", "htm", "properties"});
		List<VersionMojo.VersionFile> files = mojo.createFileContents();
		assertThat("Incorrect number of verion files created", files.size(), equalTo(3));
	}
	
	@Test
	public void shouldAllowOverrideOfDefaultTargetDirectory(){
		VersionMojo modMojo = new VersionMojo();
		modMojo.setBranch("Head");
		modMojo.setProject("Test Project");
		modMojo.setVersion("1.0");
		modMojo.setTargetDirectory("some/other/path");
		
		VersionMojo.VersionFile outputFile = new VersionMojo.VersionFile("txt", "These are the contents");
		
		String actual = modMojo.generateVersionFilename(outputFile);	
		assertThat("Does not generate the proper file name", actual, equalTo("some/other/path/version.txt"));
	}
	
//	@Test(expected=MojoExecutionException.class)
//	public void shouldPukeOnUnknownFileType() throws Exception {
//		mojo.getTypes().add("xml");
//		List<VersionMojo.VersionFile> files = mojo.createFileContents();
//		fail("Should not create file of type xml");
//	}
	
}
