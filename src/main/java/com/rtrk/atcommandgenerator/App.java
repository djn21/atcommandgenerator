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
				byte[] command = ATCommandGenerator.generateATCommand("tcpipCommand", "SEND_DATA_THROUGH_TCP_OR_UDP_CONNECTION", "WRITE");
				System.out.println(new String(command));
				
				/*byte[] decoded=ProtobufATCommandAdapter.decode(command);
				byte[] encoded=ProtobufATCommandAdapter.encode(decoded);	
				System.out.println(new String(encoded));
				System.out.println();*/
				
				byte[] command1 = ATCommandGenerator.generateProtobufATCommand("tcpipCommand", "SEND_DATA_THROUGH_TCP_OR_UDP_CONNECTION", "WRITE");
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
