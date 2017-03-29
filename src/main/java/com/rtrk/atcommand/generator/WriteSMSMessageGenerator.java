package com.rtrk.atcommand.generator;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.MessageStatus;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

public class WriteSMSMessageGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+CMGW";
		if (Math.random() < 0.5) {
			if (Math.random() < 0.5) {
				command += "=" + RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5);
				if (Math.random() < 0.5) {
					command += "," + new Random().nextInt(Integer.MAX_VALUE);
					if (Math.random() < 0.5) {
						MessageStatus[] ms = MessageStatus.values();
						int randomIndex = new Random().nextInt(ms.length);
						command += ms[randomIndex].getNumber();
					}
				}
			}
		} else {
			command += "=" + new Random().nextInt(Integer.MAX_VALUE);
			if (Math.random() < 0.5) {
				MessageStatus[] ms = MessageStatus.values();
				int randomIndex = new Random().nextInt(ms.length);
				command += ms[randomIndex].getNumber();
			}
		}
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		SMSCommand.Builder smsBuilder = (SMSCommand.Builder) commandBuilder;
		if (Math.random() < 0.5) {
			if (Math.random() < 0.5) {
				smsBuilder.setDestinationAddress(RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5));
				if (Math.random() < 0.5) {
					smsBuilder.setTypeOfDestinationAddress(new Random().nextInt(Integer.MAX_VALUE));
					if (Math.random() < 0.5) {
						MessageStatus[] ms = MessageStatus.values();
						int randomIndex = new Random().nextInt(ms.length);
						smsBuilder.setMessageStatusPDU(ms[randomIndex]);
					}
				}
			}
		} else {
			smsBuilder.setLength(new Random().nextInt(Integer.MAX_VALUE));
			if (Math.random() < 0.5) {
				MessageStatus[] ms = MessageStatus.values();
				int randomIndex = new Random().nextInt(ms.length);
				smsBuilder.setMessageStatusPDU(ms[randomIndex]);
			}
		}
	}

}