package com.rtrk.atcommand.generator;

import java.util.Random;

import com.mifmif.common.regex.Generex;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.TCPIPCommand;

/**
 * 
 * Class for generating SET_UP_TCP_OR_UDP_CONNECTION command in original and
 * protobuf format
 * 
 * @author djekanovic
 *
 */
public class StartConnectionGenerator implements Generator {

	@Override
	public byte[] generateATCommand() {
		String command = "AT+QIOPEN=";
		// set index
		if (Math.random() < 0.5) {
			command += new Random().nextInt(Integer.MAX_VALUE) + ",";
		}
		// set mode
		if (Math.random() < 0.5) {
			command += "\"TCP\"";
		} else {
			command += "\"UDP\"";
		}
		// set ip address
		if (Math.random() < 0.5) {
			String regexp = "\\\"((25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])(\\.)){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])\\\"";
			Generex generex = new Generex(regexp);
			command += "," + generex.random();
		}
		// set domain name
		else {
			String regexp = "\\\"([a-z]){10}\\.([a-z]){3}\\\"";
			Generex generex = new Generex(regexp);
			command += "," + generex.random();
		}
		// set port
		command += "," + new Random().nextInt(65536);
		return command.getBytes();
	}

	@Override
	public void generateProtobufATCommand(Object commandBuilder) {
		TCPIPCommand.Builder tcpipBuilder = (TCPIPCommand.Builder) commandBuilder;
		// set index
		if (Math.random() < 0.5) {
			tcpipBuilder.setIndex(new Random().nextInt(Integer.MAX_VALUE));
		}
		// set mode
		if (Math.random() < 0.5) {
			tcpipBuilder.setMode("\"TCP\"");
		} else {
			tcpipBuilder.setMode("\"UDP\"");
		}
		// set ip address
		if (Math.random() < 0.5) {
			String regexp = "\\\"((25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])(\\.)){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])\\\"";
			Generex generex = new Generex(regexp);
			tcpipBuilder.setDomainName(generex.random());
		}
		// set domain name
		else {
			String regexp = "\\\"([a-z]){10}\\.([a-z]){3}\\\"";
			Generex generex = new Generex(regexp);
			tcpipBuilder.setDomainName(generex.random());
		}
		// set port
		tcpipBuilder.setPort(new Random().nextInt(65536));
	}

}
