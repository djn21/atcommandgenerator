package com.rtrk.atcommand.generator;

import java.util.Random;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.TCPIPCommand;

public class SendDataTCPUDPGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+QISEND=";
		Random random = new Random();
		if (Math.random() < 0.5) {
			command += random.nextInt(1460);
		} else {
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
			tcpipBuilder.setLength(random.nextInt(1460));
		} else {
			tcpipBuilder.setIndex(random.nextInt(Integer.MAX_VALUE));
			if (Math.random() < 0.5) {
				tcpipBuilder.setLength(random.nextInt(1460));
			}
		}
	}

}
