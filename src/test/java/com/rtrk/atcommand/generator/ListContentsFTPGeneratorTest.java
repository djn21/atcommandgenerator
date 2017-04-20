package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.FTPCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for LIST_CONTENTS_OF_DIRECTORY_OR_FILE command generator
 * 
 * @author djekanovic
 *
 */
public class ListContentsFTPGeneratorTest extends TestCase {

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public ListContentsFTPGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(ListContentsFTPGeneratorTest.class);
	}

	/**
	 * 
	 * Testing generateATCommand method
	 * 
	 */
	public void testGenerateATCommand() {
		
		Generator generator=new ListContentsFTPGenerator();
		byte[] command=generator.generateATCommand();
		String commandString=new String(command);
		
		assertTrue(commandString.startsWith("AT+QFTPLIST"));
	}

	/**
	 * 
	 * Testing generateProtobufATCommand method
	 * 
	 */
	public void testGenerateProtobufATCommand() {
		
		FTPCommand.Builder ftpBuilder=FTPCommand.newBuilder();
		
		Generator generator=new ListContentsFTPGenerator();
		generator.generateProtobufATCommand(ftpBuilder);
		
		if(ftpBuilder.hasName()){
			assertFalse(ftpBuilder.getName().equals(""));
		}
	}

}
