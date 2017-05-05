package com.rtrk.atcommand.generator.test;

import java.io.PrintWriter;

import com.google.common.base.CaseFormat;
import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.Command;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.CommandType;

public class TestEncodeThread extends Thread {

	public static int numOfThreads;

	public static Object lock = new Object();

	private byte[] command;
	private PrintWriter writer;

	public TestEncodeThread(byte[] command, PrintWriter writer) {
		super();
		this.command = command;
		this.writer = writer;
	}

	@Override
	public void run() {
		int threads;
		synchronized (lock) {
			numOfThreads++;
			threads = numOfThreads;
		}

		// get command infos
		Command cmd = null;
		String messageType = "";
		String action = "";
		try {
			cmd = Command.parseFrom(command);
			Class<?> cmdClass = cmd.getClass();
			String cmdName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, cmd.getCommandType().toString());
			Object commandTypeObject = cmdClass.getMethod("get" + cmdName).invoke(cmd);
			messageType = commandTypeObject.getClass().getMethod("getMessageType").invoke(commandTypeObject).toString();
			action = commandTypeObject.getClass().getMethod("getAction").invoke(commandTypeObject).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommandType commandType = cmd.getCommandType();

		// measure encode time
		long starTime = System.nanoTime();
		ProtobufATCommandAdapter.encode(command);
		long estimatedTime = System.nanoTime() - starTime;

		// print result
		writer.println(String.format("%-100s", commandType + "." + messageType + "." + action) + " | "
				+ String.format("%10d", threads) + " | " + String.format("%15d", estimatedTime));
		synchronized (lock) {
			numOfThreads--;
		}
	}

}
