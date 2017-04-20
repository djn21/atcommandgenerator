package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for LIST_SMS_MESSAGE_FROM_PREFERRED_STORAGE command generator
 * 
 * @author djekanovic
 *
 */
public class ListSMSMessageGeneratorTest extends TestCase {

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public ListSMSMessageGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(ListSMSMessageGeneratorTest.class);
	}

	/**
	 * 
	 * Testing generateATCommand method
	 * 
	 */
	public void testGenerateATCommand() {

		Generator generator = new ListSMSMessageGenerator();
		byte[] command = generator.generateATCommand();
		String commandString = new String(command);

		assertTrue(commandString.startsWith("AT+CMGL="));

		String mode = new String(ProtobufATCommandAdapter.environmentVariables
				.get("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat"));

		if (mode.equals("PDU_MODE")) {
			int stat = Integer.parseInt(commandString.split("=")[1].split(",")[0]);
			assertTrue(stat >= 0 && stat <= 4);
		} else {
			assertFalse(commandString.split("=")[1].equals(""));
		}
		if (commandString.split(",").length == 2) {
			int modeint = Integer.parseInt(commandString.split("=")[1].split(",")[1]);
			assertTrue(modeint >= 0 && modeint <= 1);
		}

	}

	/**
	 * 
	 * Testing generateProtobufATCommand method
	 * 
	 */
	public void testGenerateProtobufATCommand() {

		SMSCommand.Builder smsBuilder = SMSCommand.newBuilder();
		Generator generator = new ListSMSMessageGenerator();
		generator.generateProtobufATCommand(smsBuilder);

		String mode = new String(ProtobufATCommandAdapter.environmentVariables
				.get("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat"));
		if (mode.equals("PDU_MODE")) {
			assertTrue(smsBuilder.hasMessageStatusListPDU());
		} else {
			assertTrue(smsBuilder.hasMessageStatusText());
		}

	}

}
