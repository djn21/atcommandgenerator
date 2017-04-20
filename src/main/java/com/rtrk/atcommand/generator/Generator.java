package com.rtrk.atcommand.generator;

/**
 * 
 * The interface contains methods for generating AT Command in text and protobuf
 * format.
 * 
 * @author djekanovic
 *
 */

public interface Generator {

	/**
	 * 
	 * Generating AT Command in original format.
	 * 
	 * @return AT Command as byte array
	 * 
	 */
	byte[] generateATCommand();

	/**
	 * 
	 * Generating AT Command in protobuf format.
	 * 
	 * @param commandBuilder
	 *            Builder of specific AT Command
	 * 
	 */
	void generateProtobufATCommand(Object commandBuilder);

}
