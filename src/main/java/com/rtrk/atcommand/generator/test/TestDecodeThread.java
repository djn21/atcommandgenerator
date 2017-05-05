package com.rtrk.atcommand.generator.test;

import java.io.PrintWriter;

import com.google.common.base.CaseFormat;
import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.Command;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.CommandType;

public class TestDecodeThread extends Thread {

	public static int numOfThreads;

	public static Object lock = new Object();

	private byte[] command;
	private PrintWriter writer;

	public TestDecodeThread(byte[] command, PrintWriter writer) {
		this.command = command;
		this.writer = writer;
	}
	
	@Override
	public void run() {
		int threads;
		synchronized (lock) {
			numOfThreads++;
			threads=numOfThreads;
		}

		// measure decode time
		long starTime = System.nanoTime();
		byte[] decoded = ProtobufATCommandAdapter.decode(command);
		long estimatedTime = System.nanoTime() - starTime;

		// get command infos
		Command cmd = null;
		String messageType = "";
		String action = "";
		try {
			cmd = Command.parseFrom(decoded);
			Class<?> cmdClass = cmd.getClass();
			String cmdName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, cmd.getCommandType().toString());
			Object commandTypeObject = cmdClass.getMethod("get" + cmdName).invoke(cmd);
			messageType = commandTypeObject.getClass().getMethod("getMessageType").invoke(commandTypeObject).toString();
			action = commandTypeObject.getClass().getMethod("getAction").invoke(commandTypeObject).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommandType commandType = cmd.getCommandType();

		// print result
		writer.println(String.format("%-100s", commandType + "." + messageType + "." + action) + " | "
				+ String.format("%10d", threads) + " | " + String.format("%15d", estimatedTime));
		synchronized (lock) {
			numOfThreads--;
		}
	}

}
