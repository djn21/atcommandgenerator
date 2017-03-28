package com.rtrk.atcommand.generator;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import com.rtrk.atcommand.protobuf.ProtobufATCommand.MMSCommand;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.OperateFunction;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.OperateWriteMMS;

public class WriteMMSMessageGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+QMMSW=";
		Random random = new Random();

		// set function
		OperateFunction[] ofs = OperateFunction.values();
		int randomIndex = random.nextInt(ofs.length);
		int function = ofs[randomIndex].getNumber();
		command += function;

		// set operate
		if (function != 0) {
			randomIndex = random.nextInt(OperateWriteMMS.values().length);
			int operate = OperateWriteMMS.valueOf(randomIndex).getNumber();
			command += "," + operate;
			if (function != 4 && operate != 0) {
				String opstring = RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5);
				command += "," + opstring;
			}
		}
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		MMSCommand.Builder mmsCommandBuilder = (MMSCommand.Builder) commandBuilder;
		Random random = new Random();

		// set function
		OperateFunction[] ofs = OperateFunction.values();
		int randomIndex = random.nextInt(ofs.length);
		int function = ofs[randomIndex].getNumber();
		mmsCommandBuilder.setOperateFunction(ofs[randomIndex]);

		// set operate
		if (function != 0) {
			OperateWriteMMS[] owms = OperateWriteMMS.values();
			randomIndex = random.nextInt(owms.length);
			int operate = owms[randomIndex].getNumber();
			mmsCommandBuilder.setOperateWriteMMS(owms[randomIndex]);
			if (function != 4 && operate != 0) {
				String opstring = RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5);
				mmsCommandBuilder.setOpstring(opstring);
			}
		}
	}

}
