package com.rtrk.atcommandgenerator;

import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.generator.ATCommandGenerator;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.Action;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.CommandType;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		try {
			while (true) {
				byte[] command = ATCommandGenerator.generateATCommand();
				if (command != null) {
					System.out.println(new String(command));

					byte[] decoded = ProtobufATCommandAdapter.decode(command);
					byte[] encoded = ProtobufATCommandAdapter.encode(decoded);
					System.out.println(new String(encoded));
				}

				byte[] command1 = ATCommandGenerator.generateProtobufATCommand();
				if (command1 != null) {
					byte[] encoded1 = ProtobufATCommandAdapter.encode(command1);
					System.out.println(new String(encoded1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
