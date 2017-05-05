package com.rtrk.atcommand.generator.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.common.base.CaseFormat;
import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.Command;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.CommandType;

public class TestEncodeDecodeTime {

	public static void testEncode(String path) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream("output/tests/test_encode_" + simpleDateFormat.format(new Date()) + ".txt")));
			InputStream reader = new FileInputStream(path);

			// set header
			writer.println(String.format("%-100s", "COMMAND") + " | " + String.format("%15s", "TIME (ns)"));
			for (int i = 0; i < 118; i++)
				writer.print("-");
			writer.println();

			// get command infos
			Command cmd = null;
			String messageType = "";
			String action = "";
			try {
				while ((cmd = Command.parseDelimitedFrom(reader)) != null) {

					Class<?> cmdClass = cmd.getClass();
					String cmdName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,
							cmd.getCommandType().toString());
					Object commandTypeObject = cmdClass.getMethod("get" + cmdName).invoke(cmd);
					messageType = commandTypeObject.getClass().getMethod("getMessageType").invoke(commandTypeObject)
							.toString();
					action = commandTypeObject.getClass().getMethod("getAction").invoke(commandTypeObject).toString();

					// measure encode time
					long starTime = System.nanoTime();
					ProtobufATCommandAdapter.encode(cmd.toByteArray());
					long estimatedTime = System.nanoTime() - starTime;

					// print results
					CommandType commandType = cmd.getCommandType();
					writer.println(String.format("%-100s", commandType + "." + messageType + "." + action) + " | "
							+ String.format("%15d", estimatedTime));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			reader.close();
			writer.close();
		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}

	public static void testDecode(String path) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream("output/tests/test_decode_" + simpleDateFormat.format(new Date()) + ".txt")));
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(path), Charset.forName("UTF-8")));
			String command = "";

			// set header
			writer.println(String.format("%-100s", "COMMAND") + " | " + String.format("%15s", "TIME (ns)"));
			for (int i = 0; i < 118; i++)
				writer.print("-");
			writer.println();

			while ((command = reader.readLine()) != null) {

				// measure decode time
				long starTime = System.nanoTime();
				byte[] decoded = ProtobufATCommandAdapter.decode(command.getBytes());
				long estimatedTime = System.nanoTime() - starTime;

				// get command infos
				Command cmd = null;
				String messageType = "";
				String action = "";
				try {
					cmd = Command.parseFrom(decoded);
					Class<?> cmdClass = cmd.getClass();
					String cmdName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,
							cmd.getCommandType().toString());
					Object commandTypeObject = cmdClass.getMethod("get" + cmdName).invoke(cmd);
					messageType = commandTypeObject.getClass().getMethod("getMessageType").invoke(commandTypeObject)
							.toString();
					action = commandTypeObject.getClass().getMethod("getAction").invoke(commandTypeObject).toString();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// print results
				CommandType commandType = cmd.getCommandType();
				writer.println(String.format("%-100s", commandType + "." + messageType + "." + action) + " | "
						+ String.format("%15d", estimatedTime));

			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void testEncodeThread(String path){
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
					"output/tests/test_encode_thread_" + simpleDateFormat.format(new Date()) + ".txt")));
			InputStream reader = new FileInputStream(path);
			writer.println(String.format("%-100s", "COMMAND") + " | " + String.format("%10s", "THERADS") + " | "
					+ String.format("%15s", "TIME (ns)"));
			for (int i = 0; i < 131; i++)
				writer.print("-");
			writer.println();
			ArrayList<TestEncodeThread> threads = new ArrayList<TestEncodeThread>();
			Command command=null;
			while ((command = Command.parseDelimitedFrom(reader)) != null) {
				TestEncodeThread t = new TestEncodeThread(command.toByteArray(), writer);
				t.start();
				threads.add(t);
			}
			for (TestEncodeThread t : threads) {
				try {
					t.join();
				} catch (InterruptedException e) {

				}
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testDecodeThread(String path) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
					"output/tests/test_decode_thread_" + simpleDateFormat.format(new Date()) + ".txt")));
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(path), Charset.forName("UTF-8")));
			String command = "";
			writer.println(String.format("%-100s", "COMMAND") + " | " + String.format("%10s", "THERADS") + " | "
					+ String.format("%15s", "TIME (ns)"));
			for (int i = 0; i < 131; i++)
				writer.print("-");
			writer.println();
			ArrayList<TestDecodeThread> threads = new ArrayList<TestDecodeThread>();
			while ((command = reader.readLine()) != null) {
				TestDecodeThread t = new TestDecodeThread(command.getBytes(), writer);
				t.start();
				threads.add(t);
			}
			for (TestDecodeThread t : threads) {
				try {
					t.join();
				} catch (InterruptedException e) {

				}
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
