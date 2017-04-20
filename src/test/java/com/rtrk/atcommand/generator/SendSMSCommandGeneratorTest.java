package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for SEND_SMS_COMMAND command generator
 * 
 * @author djekanovic
 *
 */
public class SendSMSCommandGeneratorTest extends TestCase {

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public SendSMSCommandGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(SendSMSCommandGeneratorTest.class);
	}

	public void testGenerateATCommand() {

		Generator generator = new SendSMSCommandGenerator();
		byte[] command = generator.generateATCommand();
		String commandString = new String(command);

		assertTrue(commandString.startsWith("AT+CMGC="));

	}

	public void testGenerateProtobufATCommand() {

		SMSCommand.Builder smsBuilder = SMSCommand.newBuilder();
		Generator generator = new SendSMSCommandGenerator();
		generator.generateProtobufATCommand(smsBuilder);

		String mode = new String(ProtobufATCommandAdapter.environmentVariables
				.get("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat"));
		if (mode.equals("PDU_MODE")) {
			assertTrue(smsBuilder.hasLength());
		} else {
			assertTrue(smsBuilder.hasFirstOctet());
		}
	}

}
