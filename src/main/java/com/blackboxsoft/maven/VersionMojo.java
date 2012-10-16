package com.blackboxsoft.maven;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

	public void setProject(String project) {
		this.project = project;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}
}
