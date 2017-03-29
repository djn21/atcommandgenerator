package com.rtrk.atcommand.generator;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

public class SendSMSMessageGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+CMGS=";
		if (Math.random() < 0.5) {
			command += RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5);
			if (Math.random() < 0.5) {
				command += "," + new Random().nextInt(Integer.MAX_VALUE);
			}
		} else {
			command += new Random().nextInt(Integer.MAX_VALUE);
		}
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		SMSCommand.Builder smsBuilder = (SMSCommand.Builder) commandBuilder;
		if (Math.random() < 0.5) {
			smsBuilder.setDestinationAddress(RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5));
			if (Math.random() < 0.5) {
				smsBuilder.setTypeOfDestinationAddress(new Random().nextInt(Integer.MAX_VALUE));
			}
		} else {
			smsBuilder.setLength(new Random().nextInt(Integer.MAX_VALUE));
		}
	}

}
