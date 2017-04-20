package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for DELETE_ALL_SMS command generator
 * 
 * @author djekanovic
 *
 */
public class DeleteAllSMSGeneratorTest extends TestCase {

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public DeleteAllSMSGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(DeleteAllSMSGeneratorTest.class);
	}
	
	/**
	 * 
	 * Testing generateATCommand method
	 * 
	 */
	public void testGenerateATCommand() {

		Generator generator = new DeleteAllSMSGenerator();
		byte[] command = generator.generateATCommand();
		String commandString = new String(command);

		assertTrue(commandString.startsWith("AT+QMGDA="));

		String mode = new String(ProtobufATCommandAdapter.environmentVariables
				.get("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat"));

		if (mode.equals("PDU_MODE")) {
			int type = Integer.parseInt(commandString.split("=")[1]);
			assertTrue(type >= 1 && type <= 6);
		} else {
			String type = commandString.split("=")[1];
			assertFalse(type.equals(""));
		}
	}

	/**
	 * 
	 * Testing generateProtobufATCommand method
	 * 
	 */
	public void testGenerateProtobufATCommand() {

		SMSCommand.Builder smsBuilder = SMSCommand.newBuilder();

		Generator generator = new DeleteAllSMSGenerator();
		generator.generateProtobufATCommand(smsBuilder);

		String mode = new String(ProtobufATCommandAdapter.environmentVariables
				.get("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat"));

		if (mode.equals("PDU_MODE")) {
			assertTrue(smsBuilder.hasMessageStatusDeletePDU());
		} else {
			assertTrue(smsBuilder.hasMessageStatusText());
		}

	}

}
