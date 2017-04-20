package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.TCPIPCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for SET_UP_TCP_OR_UDP_CONNECTION command generator
 * 
 * @author djekanovic
 *
 */
public class StartConnectionGeneratorTest extends TestCase {

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public StartConnectionGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(StartConnectionGeneratorTest.class);
	}

	/**
	 * 
	 * Testing generateATCommand method
	 * 
	 */
	public void testGenerateATCommand() {

		Generator generator = new StartConnectionGenerator();
		byte[] command = generator.generateATCommand();
		String commandString = new String(command);

		assertTrue(commandString.startsWith("AT+QIOPEN"));
		assertTrue(commandString.split(",").length >= 3 && commandString.split(",").length <= 4);

	}

	/**
	 * 
	 * Testing generateProtobufATCommand method
	 * 
	 */
	public void testGenerateProtobufATCommand() {

		TCPIPCommand.Builder tcpipBuilder = TCPIPCommand.newBuilder();
		Generator generator = new StartConnectionGenerator();
		generator.generateProtobufATCommand(tcpipBuilder);

		assertTrue(tcpipBuilder.hasMode());
		assertTrue(tcpipBuilder.hasDomainName());
		assertTrue(tcpipBuilder.hasPort());
	}

}
