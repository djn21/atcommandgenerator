package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for RESTORE_SMS_SETTINGS command gnerator
 * 
 * @author djekanovic
 *
 */
public class RestoreSMSSettingsGeneratorTest extends TestCase{

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public RestoreSMSSettingsGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(RestoreSMSSettingsGeneratorTest.class);
	}
	
	/**
	 * 
	 * Testing generateATCommand method
	 * 
	 */
	public void testGenerateATCommand(){
		
		Generator generator=new RestoreSMSSettingsGenerator();
		byte[] command=generator.generateATCommand();
		String commandString=new String(command);
		
		assertTrue(commandString.startsWith("AT+CRES"));
		
	}
	
	/**
	 * 
	 * Testing generateProtobufATCommand method
	 * 
	 */
	public void testGenerateProtobufATCommand(){
		
		SMSCommand.Builder smsBuilder=SMSCommand.newBuilder();
		Generator generator=new RestoreSMSSettingsGenerator();
		generator.generateProtobufATCommand(smsBuilder);
		
		if(smsBuilder.hasProfile()){
			int profile=smsBuilder.getProfile();
			assertTrue(profile>=0 && profile<=3);
		}
		
	}
	
}
