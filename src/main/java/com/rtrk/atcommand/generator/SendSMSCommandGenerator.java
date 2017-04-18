package com.rtrk.atcommand.generator;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

/**
 * 
 * Class for generating SEND_SMS_COMMAND command in original and protobuf format
 * 
 * @author djekanovic
 *
 */
public class SendSMSCommandGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+CMGC=";
		Random random = new Random();
		if (Math.random() < 0.5) {
			ProtobufATCommandAdapter.environmentVariables.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat",
					"TEXT_MODE".getBytes());
			command += random.nextInt(Integer.MAX_VALUE); // fo
			if (Math.random() < 0.5) {
				command += "," + random.nextInt(Integer.MAX_VALUE); // ct
				command += "," + random.nextInt(Integer.MAX_VALUE); // pid
				command += "," + random.nextInt(Integer.MAX_VALUE); // mn
				command += "," + RandomStringUtils.randomAlphabetic(random.nextInt(10) + 5); // da
				command += "," + random.nextInt(Integer.MAX_VALUE); // toda
			}
		} else {
			ProtobufATCommandAdapter.environmentVariables.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat",
					"PDU_MODE".getBytes());
			command += random.nextInt(Integer.MAX_VALUE); // length
		}
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		SMSCommand.Builder smsBuilder = (SMSCommand.Builder) commandBuilder;
		Random random = new Random();
		if (Math.random() < 0.5) {
			ProtobufATCommandAdapter.environmentVariables.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat",
					"TEXT_MODE".getBytes());
			smsBuilder.setFirstOctet(random.nextInt(Integer.MAX_VALUE));
			if (Math.random() < 0.5) {
				smsBuilder.setCommandType(random.nextInt(Integer.MAX_VALUE)); // ct
				smsBuilder.setProtocolIdentifier(random.nextInt(Integer.MAX_VALUE)); // pid
				smsBuilder.setMessageNumber(random.nextInt(Integer.MAX_VALUE)); // mn
				smsBuilder.setDestinationAddress(RandomStringUtils.randomAlphabetic(random.nextInt(10) + 5)); // da
				smsBuilder.setTypeOfDestinationAddress(random.nextInt(Integer.MAX_VALUE)); // toda
			}
		} else {
			ProtobufATCommandAdapter.environmentVariables.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat",
					"PDU_MODE".getBytes());
			smsBuilder.setLength(random.nextInt(Integer.MAX_VALUE));
		}
	}

}
