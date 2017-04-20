package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.CallRelatedCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for MOBILE_ORIGINATED_CALL_TO_DIAL_A_NUMBER command generator
 * 
 * @author djekanovic
 *
 */
public class MobileOriginatedCallGeneratorTest extends TestCase{

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public MobileOriginatedCallGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(MobileOriginatedCallGeneratorTest.class);
	}
	
	/**
	 * 
	 * Testing generateATCommand method
	 * 
	 */
	public void testGenerateATCommand(){
		
		Generator generator=new MobileOriginatedCallGenerator();
		byte[] command=generator.generateATCommand();
		String commandString=new String(command);
		
		assertTrue(commandString.startsWith("ATD"));
		
	}
	
	/**
	 * 
	 * Testing generateProtobufATCommand method
	 * 
	 */
	public void testGenerateProtobufATCommand(){
		
		CallRelatedCommand.Builder callBuilder=CallRelatedCommand.newBuilder();
		Generator generator=new MobileOriginatedCallGenerator();
		generator.generateProtobufATCommand(callBuilder);
		
		assertTrue(callBuilder.hasNumber());
		
	}
	
}
