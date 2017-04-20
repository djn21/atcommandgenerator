package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.MMSCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for WRITE_MMS_MESSAGE command generator
 * 
 * @author djekanovic
 *
 */
public class WriteMMSMessageGeneratorTest extends TestCase {

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public WriteMMSMessageGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(WriteMMSMessageGeneratorTest.class);
	}

	/**
	 * 
	 * Testing generateATCommand
	 * 
	 */
	public void testGenerateATCommand() {

		Generator generator = new WriteMMSMessageGenerator();
		byte[] command = generator.generateATCommand();
		String commandString = new String(command);

		assertTrue(commandString.startsWith("AT+QMMSW="));

		int function = Integer.parseInt(commandString.split("=")[1].split(",")[0]);
		assertTrue(function >= 0 && function <= 5);
		if (function == 4) {
			int operate = Integer.parseInt(commandString.split(",")[1]);
			assertTrue(operate >= 0 && operate <= 1);
		}
	}

	/**
	 * 
	 * Testing generateProtobufATCommand
	 * 
	 */
	public void testGenerateProtobufATCommand() {

		MMSCommand.Builder mmsBuilder = MMSCommand.newBuilder();
		Generator generator = new WriteMMSMessageGenerator();
		generator.generateProtobufATCommand(mmsBuilder);

		assertTrue(mmsBuilder.hasOperateFunction());

		if (mmsBuilder.getOperateFunction().getNumber() == 4) {
			assertTrue(mmsBuilder.hasOperateWriteMMS());
		}

	}

}
