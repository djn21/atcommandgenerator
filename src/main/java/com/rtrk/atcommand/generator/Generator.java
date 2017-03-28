package com.rtrk.atcommand.generator;

public interface Generator{

	byte[] generateATCommand();

	void generateProtobufATCommand(Object commandBuilder);

}
