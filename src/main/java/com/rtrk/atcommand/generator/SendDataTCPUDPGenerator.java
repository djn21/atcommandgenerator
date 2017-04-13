package com.rtrk.atcommand.generator;

import java.util.Random;

import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.TCPIPCommand;

/**
 * 
 * Class for generating SEND_DATA_THROUGH_TCP_OR_UDP_CONNECTION command in
 * original and protobuf format
 * 
 * @author djekanovic
 *
 */
public class SendDataTCPUDPGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+QISEND=";
		Random random = new Random();
		if (Math.random() < 0.5) {
			ProtobufATCommandAdapter.environmentVariables
					.put("tcpipCommand.ENABLE_MULTIPLE_TCPIP_SESSION.enableMultipleTCPIPSession", "false".getBytes());
			command += random.nextInt(1460);
		} else {
			ProtobufATCommandAdapter.environmentVariables
					.put("tcpipCommand.ENABLE_MULTIPLE_TCPIP_SESSION.enableMultipleTCPIPSession", "true".getBytes());
			command += random.nextInt(Integer.MAX_VALUE);
			if (Math.random() < 0.5) {
				command += "," + random.nextInt(1460);
			}
		}
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		TCPIPCommand.Builder tcpipBuilder = (TCPIPCommand.Builder) commandBuilder;
		Random random = new Random();
		if (Math.random() < 0.5) {
			ProtobufATCommandAdapter.environmentVariables
					.put("tcpipCommand.ENABLE_MULTIPLE_TCPIP_SESSION.enableMultipleTCPIPSession", "false".getBytes());
			tcpipBuilder.setLength(random.nextInt(1460));
		} else {
			ProtobufATCommandAdapter.environmentVariables
					.put("tcpipCommand.ENABLE_MULTIPLE_TCPIP_SESSION.enableMultipleTCPIPSession", "true".getBytes());
			tcpipBuilder.setIndex(random.nextInt(Integer.MAX_VALUE));
			if (Math.random() < 0.5) {
				tcpipBuilder.setLength(random.nextInt(1460));
			}
		}
	}

}
