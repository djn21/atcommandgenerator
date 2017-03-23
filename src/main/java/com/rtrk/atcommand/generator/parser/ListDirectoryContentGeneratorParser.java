package com.rtrk.atcommand.generator.parser;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.rtrk.atcommand.ATCommand;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.Command;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.FTPCommand;

public class ListDirectoryContentGeneratorParser implements GeneratorParser {

	@Override
	public byte[] generateATCommand(Command command) {

		return null;
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		FTPCommand.Builder ftpCommandBuilder = (FTPCommand.Builder) commandBuilder;
		if (Math.random() > 0.5) {
			String name = RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 10);
			ftpCommandBuilder.setName(name);
		}
	}

}
