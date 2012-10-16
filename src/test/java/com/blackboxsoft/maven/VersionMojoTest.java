package com.blackboxsoft.maven;

import org.junit.Before;
import org.junit.Test;

import com.blackboxsoft.maven.VersionMojo;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class VersionMojoTest {

	private String versionFileDir = "/hudson/version-files";
	
	VersionMojo mojo;
	
	@Before
	public void setup() {
		mojo = new VersionMojo();
	}
	
	@Test
	public void shouldCreateMojo() {
		assertThat(mojo, notNullValue());
	}

	
}
