package com.blackboxsoft.maven;

/*
 * #%L
 * maven-version-plugin
 * %%
 * Copyright (C) 2012 BlackBox Software, Inc
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Creates the version files to be included into the artifact.
 * 
 * @goal version
 */
public class VersionMojo extends AbstractMojo {

	private static final String VERSION_FILE_PREFIX = "version.";

	private static final String targetDirectory = "src/main/webapp";
	
    /**
     * The project name to insert into the version files.
     * @parameter expression="${version.project}" default-value="unknown"
     */
	private String project;

	/**
     * The version value to insert into the version files.
     * @parameter expression="${version.version}" default-value="1.0"
     */
	private String version;

	/**
     * The branch to insert into the version files.
     * @parameter expression="${version.branch}" default-value="HEAD"
     */
	private String branch;

	/**
	 * The types of files we will create.
     * @parameter expression="${version.types}" default-value="txt"
	 */
	private List<String> types;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		writeHeader();		writeTheVersionFiles(createFileContents());
	}

	/*
	 * Writes the files to the target directory
	 */
	protected void writeTheVersionFiles(List<VersionFile> outputFiles) {
		for(VersionFile outputFile : outputFiles) {
			try {
				IOUtils.write(outputFile.getContents(), new FileOutputStream(targetDirectory + "/" + VERSION_FILE_PREFIX + outputFile.getType()), "UTF-8");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected List<VersionFile> createFileContents() {
		List<VersionFile> outputFiles = new ArrayList<VersionFile>();
		try {
			for(String type : types) {
				String fileContents = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(VERSION_FILE_PREFIX + type));
				fileContents = fileContents.replace("@project@", project);
				fileContents = fileContents.replace("@version@", version);
				fileContents = fileContents.replace("@branch@", branch);
				outputFiles.add(new VersionFile(type, fileContents));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputFiles;
	}

	private void writeHeader() {
		writeLog("Versioning");
		writeLog(String.format("Using values: Project[%s] Version[%s] Branch[%s]", project, version, branch));
		writeLog(String.format("For types: %s", types));
	}
	
	protected static class VersionFile {
		private String type;
		private String contents;
		
		public VersionFile(String type, String contents) {
			this.type = type;
			this.contents = contents;
		}
		
		public String getType() {
			return type;
		}
		public String getContents() {
			return contents;
		}
	}
	
	protected void writeLog(String message) {
		getLog().info(message);
	}

	public String getProject() {
		return project;
	}

	public String getVersion() {
		return version;
	}

	public String getBranch() {
		return branch;
	}

	public List<String> getTypes() {
		return types;
	}
}
