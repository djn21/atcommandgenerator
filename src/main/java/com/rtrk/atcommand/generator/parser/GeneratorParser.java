package com.rtrk.atcommand.generator.parser;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.Command;

public interface GeneratorParser {

	byte[] generateATCommand(Command command);

	void generateProtobufATCommand(Object commandBuilder);

}
