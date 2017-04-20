package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for SEND_SMS_MESSAGE_FROM_STORAGE command generator
 * 
 * @author djekanovic
 *
 */
public class SendSMSMessageGeneratorTest extends TestCase {

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public SendSMSMessageGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(SendSMSMessageGeneratorTest.class);
	}

	/**
	 * 
	 * Testing generateATCommand method
	 * 
	 */
	public void testGenerateATCommand() {

		Generator generator = new SendSMSMessageGenerator();
		byte[] command = generator.generateATCommand();
		String commandString = new String(command);

		assertTrue(commandString.startsWith("AT+CMGS="));

	}

	/**
	 * 
	 * Testing generateProtobufATCommand method
	 * 
	 */
	public void testGenerateProtobufATCommand() {

		SMSCommand.Builder smsBuilder = SMSCommand.newBuilder();
		Generator generator = new SendSMSMessageGenerator();
		generator.generateProtobufATCommand(smsBuilder);

		String mode = new String(ProtobufATCommandAdapter.environmentVariables
				.get("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat"));
		if (mode.equals("PDU_MODE")) {
			assertTrue(smsBuilder.hasLength());
		} else {
			assertTrue(smsBuilder.hasDestinationAddress());
		}

	}

}
