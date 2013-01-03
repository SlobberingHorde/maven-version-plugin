package com.blackboxsoft.maven;

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

	/**
	 * The directory into which the version file should be placed.
	 * @parameter expression="${version.targetDirectory}" default-value="src/main/webapp" 
	 */
	private String targetDirectory = "src/main/webapp";
	
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
     * The build timestamp value to insert into the version files.
     * @parameter expression="${env.BUILD.STARTED}" default-value="unknown"
     */
	private String buildTimestamp;

	/**
     * The build tag name value to insert into the version files.
     * @parameter expression="${env.BUILD_TAG}" default-value="unknown"
     */
	private String buildTag;
	
	/**
     * The branch to insert into the version files.
     * @parameter expression="${version.branch}" default-value="HEAD"
     */
	private String branch;

	/**
	 * The types of files we will create.
     * @parameter alias="extensions" default-value="txt"
	 */
	private String providedTypes;

	private String[] extensions;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		setExtensions(providedTypes.split(" "));
		writeHeader();
		writeTheVersionFiles(createFileContents());
		writeLog("Versioning complete");
	}

	/*
	 * Writes the files to the target directory
	 */
	protected void writeTheVersionFiles(List<VersionFile> outputFiles) throws MojoExecutionException {
		for(VersionFile outputFile : outputFiles) {
			try {
				String fileName = generateVersionFilename(outputFile);
				IOUtils.write(outputFile.getContents(), getOutputFileStream(fileName), "UTF-8");
			} catch (Exception e) {
				throw new MojoExecutionException("Could not write out ", e);
			}
		}
	}

	protected OutputStream getOutputFileStream(String filename) throws FileNotFoundException {
		return new FileOutputStream(filename);
	}
	
	protected String generateVersionFilename(VersionFile outputFile) {
		return targetDirectory + "/" + VERSION_FILE_PREFIX + outputFile.getType();
	}

	protected List<VersionFile> createFileContents() throws MojoExecutionException {
		List<VersionFile> outputFiles = new ArrayList<VersionFile>();
		try {
			for(String extension : extensions) {
				outputFiles.add(new VersionFile(extension, generateFileContents(extension)));
			}
		} catch (Exception e) {
			throw new MojoExecutionException("Unknown filetype specified",  e);
		}
		return outputFiles;
	}

	protected String generateFileContents(String type) throws IOException {
		String fileContents = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(VERSION_FILE_PREFIX + type));
		fileContents = fileContents.replace("@project@", project);
		fileContents = fileContents.replace("@version@", version);
		fileContents = fileContents.replace("@branch@", branch);
		fileContents = fileContents.replace("@buildTimestamp@", buildTimestamp);
		fileContents = fileContents.replace("@buildTag@", buildTag);
		return fileContents;
	}

	private void writeHeader() {
		writeLog("Versioning");
		writeLog(String.format("Using values: Project[%s] Version[%s] Build Timestamp[%s] Branch[%s] Build Tag Name [%s]", project, version, buildTimestamp, branch, buildTag));
		writeLog("For extensions:");
		for(String extension : extensions) {
			writeLog(String.format("      %s", extension));
		}
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
	
	public String[] getExtensions() {
		return this.extensions;
	}

	public void setExtensions(String[] extensions) {
		this.extensions = extensions;
	}

	public String getProvidedTypes() {
		return providedTypes;
	}

	public void setProvidedTypes(String providedTypes) {
		this.providedTypes = providedTypes;
	}

	public String getBuildTimestamp() {
		return buildTimestamp;
	}

	public void setBuildTimestamp(String buildTimestamp) {
		this.buildTimestamp = buildTimestamp;
	}

	public String getBuildTag() {
		return buildTag;
	}

	public void setBuildTag(String buildTag) {
		this.buildTag = buildTag;
	}

	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

}
