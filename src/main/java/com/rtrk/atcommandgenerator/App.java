package com.rtrk.atcommandgenerator;

import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.generator.ATCommandGenerator;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		try {
			while (true) {
				byte[] command = ATCommandGenerator.generateATCommand("callRelatedCommand", "MOBILE_ORIGINATED_CALL_TO_DIAL_A_NUMBER", "EXECUTION");
				System.out.println(new String(command));
				
				byte[] decoded=ProtobufATCommandAdapter.decode(command);
				byte[] encoded=ProtobufATCommandAdapter.encode(decoded);	
				System.out.println(new String(encoded));
				System.out.println();
				
				byte[] command1 = ATCommandGenerator.generateProtobufATCommand("callRelatedCommand", "MOBILE_ORIGINATED_CALL_TO_DIAL_A_NUMBER", "EXECUTION");
				byte[] encoded1 = ProtobufATCommandAdapter.encode(command1);
				System.out.println(new String(encoded1));
				System.out.println();
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
