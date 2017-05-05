package com.rtrk.atcommand.generator;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.MessageStatusList;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

/**
 * 
 * Class for generating WRITE_SMS_MESSAGE_TO_MEMORY command in original and
 * protobuf format
 * 
 * @author djekanovic
 *
 */
public class WriteSMSMessageGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+CMGW";
		if (ATCommandGenerator.smsMessageFormat.equals("TEXT_MODE")) {
			ProtobufATCommandAdapter.environmentVariables.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat",
					"TEXT_MODE".getBytes());
			if (Math.random() < 0.5) {
				command += "=" + RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5);
				if (Math.random() < 0.5) {
					command += "," + new Random().nextInt(Integer.MAX_VALUE);
					if (Math.random() < 0.5) {
						MessageStatusList[] ms = MessageStatusList.values();
						int randomIndex = new Random().nextInt(ms.length);
						command += "," + ms[randomIndex].getNumber();
					}
				}
			}
		} else {
			ProtobufATCommandAdapter.environmentVariables.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat",
					"PDU_MODE".getBytes());
			command += "=" + new Random().nextInt(Integer.MAX_VALUE);
			if (Math.random() < 0.5) {
				MessageStatusList[] ms = MessageStatusList.values();
				int randomIndex = new Random().nextInt(ms.length);
				command += "," + ms[randomIndex].getNumber();
			}
		}
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		SMSCommand.Builder smsBuilder = (SMSCommand.Builder) commandBuilder;
		if (ATCommandGenerator.smsMessageFormat.equals("TEXT_MODE")) {
			ProtobufATCommandAdapter.environmentVariables.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat",
					"TEXT_MODE".getBytes());
			if (Math.random() < 0.5) {
				smsBuilder.setDestinationAddress(RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5));
				if (Math.random() < 0.5) {
					smsBuilder.setTypeOfDestinationAddress(new Random().nextInt(Integer.MAX_VALUE));
					if (Math.random() < 0.5) {
						MessageStatusList[] ms = MessageStatusList.values();
						int randomIndex = new Random().nextInt(ms.length);
						smsBuilder.setMessageStatusListPDU(ms[randomIndex]);
					}
				}
			}
		} else {
			ProtobufATCommandAdapter.environmentVariables.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat",
					"PDU_MODE".getBytes());
			smsBuilder.setLength(new Random().nextInt(Integer.MAX_VALUE));
			if (Math.random() < 0.5) {
				MessageStatusList[] ms = MessageStatusList.values();
				int randomIndex = new Random().nextInt(ms.length);
				smsBuilder.setMessageStatusListPDU(ms[randomIndex]);
			}
		}
	}

}
