package com.rtrk.atcommand.generator;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.FTPCommand;

public class ListFileNamesFTPGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+QFTPNLST";
		if (Math.random() > 0.5) {
			command += "=" + RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5);
		}
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		FTPCommand.Builder ftpCommandBuilder = (FTPCommand.Builder) commandBuilder;
		if (Math.random() > 0.5) {
			String directoryName = RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5);
			ftpCommandBuilder.setDirectoryName(directoryName);
		}
	}

}
