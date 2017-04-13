package com.rtrk.atcommand.generator;

import com.mifmif.common.regex.Generex;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.CallRelatedCommand;

/**
 * 
 * Class for generating MOBILE_ORIGINATED_CALL_TO_DIAL_A_NUMBER command in
 * original and protobuf format
 * 
 * @author djekanovic
 *
 */
public class MobileOriginatedCallGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "ATD";
		Generex generex = new Generex("([0-9]?\\*?#?\\+?A?B?C?)*");
		String digits = generex.random();
		command += digits;
		if (Math.random() > 0.5) {
			generex = new Generex("i|g|I|G");
			command += generex.random();
			if (Math.random() > 0.5) {
				command += ";";
			}
		}
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		CallRelatedCommand.Builder callBuilder = (CallRelatedCommand.Builder) commandBuilder;
		Generex generex = new Generex("([0-9]?\\*?#?\\+?A?B?C?)*");
		String digits = generex.random();
		callBuilder.setNumber(digits);
		if (Math.random() > 0.5) {
			generex = new Generex("i|g|I|G");
			callBuilder.setGSMModifier(generex.random());
			if (Math.random() > 0.5) {
				callBuilder.setSetUpVoiceCall(true);
			}
		}
	}

}
