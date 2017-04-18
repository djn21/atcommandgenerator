package com.rtrk.atcommand.generator;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.FTPCommand;

/**
 * 
 * Class for generating LIST_CONTENTS_OF_DIRECTORY_OR_FILE in original and
 * protobuf format
 * 
 * @author djekanovic
 *
 */
public class ListContentsFTPGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+QFTPLIST";
		if (Math.random() > 0.5) {
			command += "=" + RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5);
		}
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		FTPCommand.Builder ftpCommandBuilder = (FTPCommand.Builder) commandBuilder;
		if (Math.random() > 0.5) {
			String name = RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5);
			ftpCommandBuilder.setName(name);
		}
	}

}
