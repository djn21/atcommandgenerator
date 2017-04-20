package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.FTPCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for SET_PARAMETERS command generator
 * 
 * @author djekanovic
 *
 */
public class SetParametersFTPGeneratorTest extends TestCase {

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public SetParametersFTPGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(SetParametersFTPGeneratorTest.class);
	}

	/**
	 * 
	 * Testing generateATCommand method
	 * 
	 */
	public void testGenerateATCommand() {

		Generator generator = new SetParametersFTPGenerator();
		byte[] command = generator.generateATCommand();
		String commandString = new String(command);

		assertTrue(commandString.startsWith("AT+QFTPCFG="));
		int type = Integer.parseInt(commandString.split("=")[1].split(",")[0]);
		assertTrue(type >= 1 && type <= 4);

	}

	/**
	 * 
	 * Testing generateProtobufATCommand method
	 * 
	 */
	public void testGenerateProtobufATCommand() {

		FTPCommand.Builder ftpBuilder = FTPCommand.newBuilder();
		Generator generator = new SetParametersFTPGenerator();
		generator.generateProtobufATCommand(ftpBuilder);

		assertTrue(ftpBuilder.hasType());

	}

}
