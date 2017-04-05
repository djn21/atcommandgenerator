package com.rtrk.atcommandgenerator;

import java.io.File;

import com.rtrk.atcommand.generator.ATCommandGenerator;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {

		ATCommandGenerator.generateATCommandsFile(new File("output\\commands"));

	}

}
