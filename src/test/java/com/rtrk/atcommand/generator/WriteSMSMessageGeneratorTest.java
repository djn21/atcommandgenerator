package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for WRITE_SMS_MESSAGE_TO_MEMORY command generator
 * 
 * @author djekanovic
 *
 */
public class WriteSMSMessageGeneratorTest extends TestCase {

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public WriteSMSMessageGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(WriteSMSMessageGeneratorTest.class);
	}

	public void testGenerateATCommand() {

		Generator generator = new WriteSMSMessageGenerator();
		byte[] command = generator.generateATCommand();
		String commandString = new String(command);

		assertTrue(commandString.startsWith("AT+CMGW"));

	}

	public void testGenerateProtobufATCommand() {

		SMSCommand.Builder smsBuilder = SMSCommand.newBuilder();
		Generator generator = new WriteSMSMessageGenerator();
		generator.generateProtobufATCommand(smsBuilder);

		String mode = new String(ProtobufATCommandAdapter.environmentVariables
				.get("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat"));
		if (mode.equals("PDU_MODE")) {
			assertTrue(smsBuilder.hasLength());
		}

	}

}
