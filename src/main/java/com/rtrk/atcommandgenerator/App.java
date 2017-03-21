package com.rtrk.atcommandgenerator;

import com.google.protobuf.InvalidProtocolBufferException;
import com.rtrk.atcommand.generator.ATCommandGenerator;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.Command;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.MMSCommand;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		
		byte[] command=ATCommandGenerator.generateProtobufATCommand("mmsCommand", "SET_MMS_PROXY", "WRITE");
		try {
			Command cmd=Command.parseFrom(command);
			System.out.println(cmd.getCommandType());
			MMSCommand mms=cmd.getMmsCommand();
			System.out.println(mms.getMessageType());
			System.out.println(mms.getAction());
			
			if(mms.hasProtocolType()){
				System.out.println(mms.getProtocolType());
			}
			if(mms.hasGateway()){
				System.out.println(mms.getGateway());
			}
			if(mms.hasPort()){
				System.out.println(mms.getPort());
			}
			
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*Integer i=0;
		Class<?> cls=i.getClass();
		try {
			double value=cls.getField("MIN_VALUE").getDouble(cls);
			Object obj=cls;
			System.out.println(obj);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();*/
	}
}
