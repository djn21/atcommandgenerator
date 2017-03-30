package com.rtrk.atcommand.generator;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

public class SendSMSMessageGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+CMGS=";
		if (Math.random() < 0.5) {
			ProtobufATCommandAdapter.environmentVariables
			.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat","TEXT_MODE".getBytes());
			command += RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5); // da
			if (Math.random() < 0.5) {
				command += "," + new Random().nextInt(Integer.MAX_VALUE); // toda
			}
		} else {
			ProtobufATCommandAdapter.environmentVariables
			.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat","PDU_MODE".getBytes());
			command += new Random().nextInt(Integer.MAX_VALUE); //length
		}
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		SMSCommand.Builder smsBuilder = (SMSCommand.Builder) commandBuilder;
		if (Math.random() < 0.5) {
			ProtobufATCommandAdapter.environmentVariables
			.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat","TEXT_MODE".getBytes());
			smsBuilder.setDestinationAddress(RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5));
			if (Math.random() < 0.5) {
				smsBuilder.setTypeOfDestinationAddress(new Random().nextInt(Integer.MAX_VALUE));
			}
		} else {
			ProtobufATCommandAdapter.environmentVariables
			.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat","PDU_MODE".getBytes());
			smsBuilder.setLength(new Random().nextInt(Integer.MAX_VALUE));
		}
	}

}
