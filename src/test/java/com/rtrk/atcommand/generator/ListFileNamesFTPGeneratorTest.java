package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.FTPCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for LIST_FILE_NAMES command generator
 * 
 * @author djekanovic
 *
 */
public class ListFileNamesFTPGeneratorTest extends TestCase {

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public ListFileNamesFTPGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(ListFileNamesFTPGeneratorTest.class);
	}

	/**
	 * 
	 * Testing generateATCommand method
	 * 
	 */
	public void testGenerateATCommand() {

		Generator generator = new ListFileNamesFTPGenerator();
		byte[] command = generator.generateATCommand();
		String commandString = new String(command);

		assertTrue(commandString.startsWith("AT+QFTPNLST"));

	}

	/**
	 * 
	 * Testing generateProtobufATCommand method
	 * 
	 */
	public void testGenerateProtobufATCommand() {

		FTPCommand.Builder ftpBuilder = FTPCommand.newBuilder();
		Generator generator = new ListFileNamesFTPGenerator();
		generator.generateProtobufATCommand(ftpBuilder);

		if (ftpBuilder.hasDirectoryName()) {
			assertFalse(ftpBuilder.getDirectoryName().equals(""));
		}

	}

}
