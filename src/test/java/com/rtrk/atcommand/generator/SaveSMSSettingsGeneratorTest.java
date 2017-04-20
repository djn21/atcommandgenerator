package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for SAVE_SMS_SETTINGS command generator
 * 
 * @author djekanovic
 *
 */
public class SaveSMSSettingsGeneratorTest extends TestCase {

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public SaveSMSSettingsGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(SaveSMSSettingsGeneratorTest.class);
	}

	/**
	 * 
	 * Testing generateATCommand method
	 * 
	 */
	public void testGenerateATCommand() {

		Generator generator = new SaveSMSSettingsGenerator();
		byte[] command = generator.generateATCommand();
		String commandString = new String(command);

		assertTrue(commandString.startsWith("AT+CSAS"));

	}

	/**
	 * 
	 * Testing generateProtobufATCommand method
	 * 
	 */
	public void testGenerateProtobufATCommand() {

		SMSCommand.Builder smsBuilder = SMSCommand.newBuilder();
		Generator generator = new SaveSMSSettingsGenerator();
		generator.generateProtobufATCommand(smsBuilder);

		if (smsBuilder.hasProfile()) {
			int profile = smsBuilder.getProfile();
			assertTrue(profile >= 0 && profile <= 3);
		}

	}

}
