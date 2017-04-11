package com.rtrk.atcommand.generator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.protobuf.InvalidProtocolBufferException;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.Action;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.Command;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.CommandType;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.FileCommand;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.FileMessageType;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.MMSCommand;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.MMSMessageType;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.ProtocolType;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * Unit test for ProtobufATCommandAdapter
 * 
 * @author djekanovic
 *
 */

public class ATCommandGeneratorTest extends TestCase {

	private byte[] generated;
	String textcommand;
	Command protocommand;

	/**
	 * 
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 * 
	 */
	public ATCommandGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * 
	 * @return the suite of tests being tested
	 * 
	 */
	public static Test suite() {
		return new TestSuite(ATCommandGeneratorTest.class);
	}

	public void testCreateATCommand() {
		// 1. branch
		generated = ATCommandGenerator.generateATCommand(CommandType.MMS_COMMAND, MMSMessageType.SET_MMS_PROXY,
				Action.WRITE);
		textcommand = new String(generated);
		assertTrue(textcommand.startsWith("AT+QMMPROXY="));

		String protocolType = textcommand.split(",")[0].split("AT\\+QMMPROXY=")[1];
		assertEquals("1", protocolType);

		String gateway = textcommand.split(",")[1];
		Pattern pattern = Pattern
				.compile("\"((25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])(\\.)){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])\"");
		Matcher matcher = pattern.matcher(gateway);
		assertTrue(matcher.find());

		int port;
		if (textcommand.split(",").length == 3) {
			port = Integer.parseInt(textcommand.split(",")[2]);
			assertTrue(port >= 0);
			assertTrue(port <= 65535);
		}

		// 2. branch
		generated = ATCommandGenerator.generateATCommand(CommandType.FILE_COMMAND, FileMessageType.MOVE_FILE,
				Action.WRITE);
		textcommand = new String(generated);
		assertTrue(textcommand.startsWith("AT+QFMOV="));

		String copy = textcommand.split(",")[2];
		String overwrite = textcommand.split(",")[3];

		assertTrue(copy.equals("1") || copy.equals("0"));
		assertTrue(overwrite.equals("1") || overwrite.equals("0"));

	}

	public void testCreateProtobufATCommand() {
		// 1. branch
		generated = ATCommandGenerator.generateProtobufATCommand(CommandType.MMS_COMMAND, MMSMessageType.SET_MMS_PROXY,
				Action.WRITE);
		try {
			protocommand = Command.parseFrom(generated);
			assertEquals(CommandType.MMS_COMMAND, protocommand.getCommandType());
			assertTrue(protocommand.hasMmsCommand());

			MMSCommand mmsCommand = protocommand.getMmsCommand();
			assertEquals(MMSMessageType.SET_MMS_PROXY, mmsCommand.getMessageType());
			assertEquals(Action.WRITE, mmsCommand.getAction());

			assertTrue(mmsCommand.hasProtocolType());
			assertEquals(ProtocolType.HHTP_PROTOCOL, mmsCommand.getProtocolType());

			assertTrue(mmsCommand.hasGateway());
			String gateway = mmsCommand.getGateway();
			Pattern pattern = Pattern.compile(
					"\"((25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])(\\.)){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])\"");
			Matcher matcher = pattern.matcher(gateway);
			assertTrue(matcher.find());

			int port;
			if (mmsCommand.hasPort()) {
				port = mmsCommand.getPort();
				assertTrue(port >= 0);
				assertTrue(port <= 65535);
			}

			// 2. branch
			generated = ATCommandGenerator.generateProtobufATCommand(CommandType.FILE_COMMAND,
					FileMessageType.MOVE_FILE, Action.WRITE);
			protocommand = Command.parseFrom(generated);
			assertEquals(CommandType.FILE_COMMAND, protocommand.getCommandType());
			assertTrue(protocommand.hasFileCommand());

			FileCommand fileCommand = protocommand.getFileCommand();
			assertEquals(FileMessageType.MOVE_FILE, fileCommand.getMessageType());
			assertEquals(Action.WRITE, fileCommand.getAction());

			assertTrue(fileCommand.hasSourceFileName());
			assertTrue(fileCommand.hasDestinationFileName());
			assertTrue(fileCommand.hasCopy());
			assertTrue(fileCommand.hasOwerwrite());

			assertTrue(fileCommand.getCopy() == true || fileCommand.getCopy() == false);
			assertTrue(fileCommand.getOwerwrite() == true || fileCommand.getOwerwrite() == false);

		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}

}
