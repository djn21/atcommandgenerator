package com.rtrk.atcommand.generator;

import java.io.FileOutputStream;
import java.util.ArrayList;

import com.rtrk.atcommand.generator.test.TestEncodeDecodeTime;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.Command;

/**
 *
 * Generate files with original and protobuf ATCommandss
 *
 * @author djekanovic
 *
 */
public class App {

	public static void main(String[] args) {
		try {
			FileOutputStream originalCommandsFile = new FileOutputStream("output\\originalcommands");
			FileOutputStream protobufCommandsFile = new FileOutputStream("output\\protobufcommands");

			ATCommandGenerator.enableMultipleTCPIPSession = true;
			ATCommandGenerator.smsMessageFormat = "PDU_MODE";

			ArrayList<byte[]> originalCommands = ATCommandGenerator.generateATCommands();
			ArrayList<byte[]> protobufCommands = ATCommandGenerator.generateProtobufATCommands();

			// write original commands to file
			for (byte[] command : originalCommands) {
				originalCommandsFile.write(command);
				originalCommandsFile.write(System.getProperty("line.separator").getBytes());
			}

			// write protobuf commands to file
			for (byte[] command : protobufCommands) {
				Command cmd = Command.parseFrom(command);
				cmd.writeDelimitedTo(protobufCommandsFile);
			}

			originalCommandsFile.close();
			protobufCommandsFile.close();

			TestEncodeDecodeTime.testDecodeThread("output/originalcommands");
			System.out.println("DONE!");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
