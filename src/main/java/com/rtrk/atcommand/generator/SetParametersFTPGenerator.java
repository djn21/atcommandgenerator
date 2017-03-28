package com.rtrk.atcommand.generator;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.DataConnectionMode;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.FTPCommand;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.TransferType;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.TypeOfConfigurableParameters;

public class SetParametersFTPGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+QFTPCFG=";
		TypeOfConfigurableParameters[] tocp=TypeOfConfigurableParameters.values();
		int randomIndex = new Random().nextInt(tocp.length);
		int type=tocp[randomIndex].getNumber();
		command += type;
		if (type == 1 || type == 2) {
			int value = new Random().nextInt(2);
			command += "," + value;
		} else if (type == 3) {
			int value = new Random().nextInt(Integer.MAX_VALUE);
			command += "," + value;
		} else if (type == 4) {
			String localPosition = RandomStringUtils.randomAlphabetic(5);
			command += "," + localPosition;
		}
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		FTPCommand.Builder ftpCommandBuilder=(FTPCommand.Builder)commandBuilder;
		TypeOfConfigurableParameters[] tocp=TypeOfConfigurableParameters.values();
		int randomIndex = new Random().nextInt(tocp.length);
		int type=tocp[randomIndex].getNumber();
		System.out.println(type);
		ftpCommandBuilder.setType(TypeOfConfigurableParameters.valueOf(type));
		if (type == 1) {
			int dataConnectionMode = new Random().nextInt(2);
			ftpCommandBuilder.setDataConnectionMode(DataConnectionMode.valueOf(dataConnectionMode));
		} else if (type == 2) {
			int transferType = new Random().nextInt(2);
			ftpCommandBuilder.setTransferType(TransferType.valueOf(transferType));
		} else if (type == 3) {
			int resumingPoint =  new Random().nextInt(Integer.MAX_VALUE);
			ftpCommandBuilder.setResumingPoint(resumingPoint);
		} else if (type == 4) {
			String localPosition = RandomStringUtils.randomAlphabetic(5);
			ftpCommandBuilder.setLocalPosition(localPosition);
		}
	}

}
