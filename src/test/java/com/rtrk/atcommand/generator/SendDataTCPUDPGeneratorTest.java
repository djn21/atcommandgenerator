package com.rtrk.atcommand.generator;

import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.TCPIPCommand;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for SEND_DATA_THROUGH_TCP_OR_UDP_CONNECTION command generator
 * 
 * @author djekanovic
 *
 */
public class SendDataTCPUDPGeneratorTest extends TestCase {

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public SendDataTCPUDPGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(SendDataTCPUDPGeneratorTest.class);
	}

	/**
	 * 
	 * Testing generateATCommand method
	 * 
	 */
	public void testGenerateATCommand() {

		Generator generator = new SendDataTCPUDPGenerator();
		byte[] command = generator.generateATCommand();
		String commandString = new String(command);

		assertTrue(commandString.startsWith("AT+QISEND="));

		boolean mux = Boolean.parseBoolean(new String(ProtobufATCommandAdapter.environmentVariables
				.get("tcpipCommand.ENABLE_MULTIPLE_TCPIP_SESSION.enableMultipleTCPIPSession")));

		if (mux) {
			int index = Integer.parseInt(commandString.split("=")[1].split(",")[0]);
			assertTrue(index >= 0);
		} else {
			int length = Integer.parseInt(commandString.split("=")[1]);
			assertTrue(length >= 0);
			assertTrue(length < 1460);
		}

	}

	/**
	 * 
	 * Test generateProtobufATCommand method
	 * 
	 */
	public void testGenerateProtobufATCommand() {

		TCPIPCommand.Builder tcpipBuilder = TCPIPCommand.newBuilder();
		Generator generator = new SendDataTCPUDPGenerator();
		generator.generateProtobufATCommand(tcpipBuilder);

		boolean mux = Boolean.parseBoolean(new String(ProtobufATCommandAdapter.environmentVariables
				.get("tcpipCommand.ENABLE_MULTIPLE_TCPIP_SESSION.enableMultipleTCPIPSession")));

		if (mux) {
			assertTrue(tcpipBuilder.hasIndex());
			assertTrue(tcpipBuilder.getIndex() > 0);
		} else {
			assertTrue(tcpipBuilder.hasLength());
			assertTrue(tcpipBuilder.getLength() >= 0);
			assertTrue(tcpipBuilder.getLength() < 1460);
		}

	}

}
