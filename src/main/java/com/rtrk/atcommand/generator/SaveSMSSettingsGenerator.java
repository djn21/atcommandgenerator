package com.rtrk.atcommand.generator;

import java.util.Random;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

/**
 * 
 * Class for generating SAVE_SMS_SETTINGS command in original and protobuf
 * format
 * 
 * @author djekanovic
 *
 */
public class SaveSMSSettingsGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+CSAS";
		if (Math.random() < 0.5) {
			command += "=" + new Random().nextInt(4);
		}
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		SMSCommand.Builder smsBuilder = (SMSCommand.Builder) commandBuilder;
		if (Math.random() < 0.5) {
			smsBuilder.setProfile(new Random().nextInt(4));
		}
	}

}
