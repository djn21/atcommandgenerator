package com.rtrk.atcommand.generator;

import java.util.Random;

import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.MessageStatusDelete;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.SMSCommand;

/**
 * 
 * Class for generating DELETE_ALL_SMS command in original and protobuf format
 * 
 * @author djekanovic
 *
 */
public class DeleteAllSMSGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+QMGDA=";
		if (ATCommandGenerator.smsMessageFormat.equals("PDU_MODE")) {
			ProtobufATCommandAdapter.environmentVariables.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat",
					"PDU_MODE".getBytes());
			MessageStatusDelete[] ms = MessageStatusDelete.values();
			int randomIndex = new Random().nextInt(ms.length);
			command += ms[randomIndex].getNumber();
		} else {
			ProtobufATCommandAdapter.environmentVariables.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat",
					"TEXT_MODE".getBytes());
			double random = Math.random();
			if (random < 0.2) {
				command += "\"DEL UNREAD\"";
			} else if (random < 0.4) {
				command += "\"DEL READ\"";
			} else if (random < 0.6) {
				command += "\"DEL UNSENT\"";
			} else if (random < 0.8) {
				command += "\"DEL SENT\"";
			} else {
				command += "\"DEL ALL\"";
			}
		}
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		SMSCommand.Builder smsBuilder = (SMSCommand.Builder) commandBuilder;
		if (ATCommandGenerator.smsMessageFormat.equals("PDU_MODE")) {
			ProtobufATCommandAdapter.environmentVariables.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat",
					"PDU_MODE".getBytes());
			MessageStatusDelete[] ms = MessageStatusDelete.values();
			int randomIndex = new Random().nextInt(ms.length);
			smsBuilder.setMessageStatusDeletePDU(ms[randomIndex]);
		} else {
			ProtobufATCommandAdapter.environmentVariables.put("smsCommand.SELECT_SMS_MESSAGE_FORMAT.messageFormat",
					"TEXT_MODE".getBytes());
			double random = Math.random();
			if (random < 0.2) {
				smsBuilder.setMessageStatusText("\"REC UNREAD\"");
			} else if (random < 0.4) {
				smsBuilder.setMessageStatusText("\"REC READ\"");
			} else if (random < 0.6) {
				smsBuilder.setMessageStatusText("\"STO UNSENT\"");
			} else if (random < 0.8) {
				smsBuilder.setMessageStatusText("\"STO SENT\"");
			} else {
				smsBuilder.setMessageStatusText("\"ALL\"");
			}
		}
	}

}
