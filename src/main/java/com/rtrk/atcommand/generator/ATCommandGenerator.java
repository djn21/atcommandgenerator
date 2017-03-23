package com.rtrk.atcommand.generator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import org.apache.commons.lang.RandomStringUtils;

import com.google.common.base.CaseFormat;
import com.mifmif.common.regex.Generex;
import com.rtrk.atcommand.ATCommand;
import com.rtrk.atcommand.Parameter;
import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.generator.parser.GeneratorParser;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.Action;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.Command;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.CommandType;

public class ATCommandGenerator {

	public static byte[] generateATCommand() {
		ATCommand atCommand = getRandomATCommand();
		return createATCommand(atCommand);

	}

	public static byte[] generateATCommand(String commandName, String commandType, String commandAction) {
		ATCommand atCommand = ProtobufATCommandAdapter.encodeMap.get(commandName).get(commandType).get(commandAction);
		return createATCommand(atCommand);
	}

	public static byte[] generateProtobufATCommand() {
		ATCommand atCommand = getRandomATCommand();
		return createProtobufATCommand(atCommand);
	}

	public static byte[] generateProtobufATCommand(String commandName, String commandType, String commandAction) {
		ATCommand atCommand = ProtobufATCommandAdapter.encodeMap.get(commandName).get(commandType).get(commandAction);
		return createProtobufATCommand(atCommand);
	}

	private static byte[] createATCommand(ATCommand atCommand) {
		String command = "";

		// get command builder and command builder class
		Command.Builder commandBuilder = Command.newBuilder();
		Class<?> commandBuilderClass = commandBuilder.getClass();

		try {

			// get command type builder and command type builder class
			String commandType = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, atCommand.getName());
			Object commandTypeBuilder = commandBuilderClass.getMethod("get" + commandType + "Builder")
					.invoke(commandBuilder);
			Class<?> commandTypeBuilderClass = commandTypeBuilder.getClass();

			// set prefix
			command += atCommand.getPrefix();

			// set parameters
			Vector<Parameter> parameters = atCommand.getParameters();
			for (int i = 0; i < parameters.size(); i++) {
				Parameter parameter = parameters.get(i);

				// set optional parameter probability
				if (parameter.isOptional() && Math.random() > 0.5) {
					break;
				}

				// set delimiter
				if (i != 0) {
					command += atCommand.getDelimiter();
				}

				// get parameter class
				String parameterName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, parameter.getName());
				Class<?> parameterClass = commandTypeBuilderClass.getMethod("get" + parameterName).getReturnType();

				// set value if type is primitive
				if (parameterClass.isPrimitive()) {
					Class<?> parameterWrepperClass = ProtobufATCommandAdapter.getWrepperClass(parameterClass);

					// set value if type is boolean
					if (parameterClass.equals(boolean.class)) {
						if (Math.random() < 0.5) {
							command += parameter.getTrueValue();
						} else {
							command += parameter.getFalseValue();
						}
					}

					// set value if type is primitive
					else {
						double min;
						double max;
						if (parameter.hasMinValue()) {
							min = parameter.getMinValue();
						} else {
							min = parameterWrepperClass.getField("MIN_VALUE").getDouble(parameterClass);
						}
						if (parameter.hasMaxValue()) {
							max = parameter.getMaxValue();
						} else {
							max = parameterWrepperClass.getField("MAX_VALUE").getDouble(parameterClass);
						}

						// random value beetween min and max
						Double value = Math.random() * (max - min) + min;
						Class<?> valueClass = value.getClass();
						Object valueObject = valueClass.getMethod(parameterClass.getName() + "Value").invoke(value);
						command += valueObject.toString();
					}

				}

				// set value if type is string
				else if (parameterClass.equals(String.class)) {
					if (parameter.hasPattern()) {
						Generex generex = new Generex(parameter.getPattern().replace("\"", "\\\""));
						command += generex.random();
					} else {
						command += RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 10);
					}
				}

				// set value if type is enum
				else {
					Random random = new Random();
					Object[] values = (Object[]) parameterClass.getMethod("values").invoke(parameterClass);
					int randomIndex = random.nextInt(values.length);
					int value = (int) parameterClass.getMethod("getNumber").invoke(values[randomIndex]);
					command += value;
				}

			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
		}

		return command.getBytes();
	}

	private static byte[] createProtobufATCommand(ATCommand atCommand) {
		Command.Builder commandBuilder = Command.newBuilder();
		Class<?> commandBuilderClass = commandBuilder.getClass();

		try {

			// get command type builder and command type builder class
			String commandTypeUpperCamel = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, atCommand.getName());
			Object commandTypeBuilder = commandBuilderClass.getMethod("get" + commandTypeUpperCamel + "Builder")
					.invoke(commandBuilder);
			Class<?> commandTypeBuilderClass = commandTypeBuilder.getClass();

			// set command type
			String commandTypeUpperUnderscore = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE,
					atCommand.getName());
			commandBuilderClass.getMethod("setCommandType", CommandType.class).invoke(commandBuilder,
					CommandType.valueOf(commandTypeUpperUnderscore));

			// set command message type
			Class<?> messageTypeClass = commandTypeBuilderClass.getMethod("getMessageType").getReturnType();
			Object messageTypeObject = messageTypeClass.getMethod("valueOf", String.class).invoke(messageTypeClass,
					atCommand.getType());
			commandTypeBuilderClass.getMethod("setMessageType", messageTypeClass).invoke(commandTypeBuilder,
					messageTypeObject);

			// set command action
			commandTypeBuilderClass.getMethod("setAction", Action.class).invoke(commandTypeBuilder,
					Action.valueOf(atCommand.getClazz()));

			// set parameters with parser
			/*
			 * if (atCommand.hasParser()) { Class<?> parserClass =
			 * Class.forName(atCommand.getParser()); GeneratorParser parser =
			 * (GeneratorParser) parserClass.newInstance();
			 * parser.generateProtobufATCommand(commandTypeBuilder); }
			 */

			// set parameters
			Vector<Parameter> parameters = atCommand.getParameters();
			for (int i = 0; i < parameters.size(); i++) {
				Parameter parameter = parameters.get(i);

				// set opetional parameter probability
				if (parameter.isOptional() && Math.random() > 0.5) {
					break;
				}

				// get parameter class
				String parameterName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, parameter.getName());
				Class<?> parameterClass = commandTypeBuilderClass.getMethod("get" + parameterName).getReturnType();

				// set value if type is primitive
				if (parameterClass.isPrimitive()) {
					Class<?> parameterWrepperClass = ProtobufATCommandAdapter.getWrepperClass(parameterClass);

					// set value if type is boolean
					if (parameterClass.equals(boolean.class)) {
						if (Math.random() < 0.5) {
							commandTypeBuilderClass.getMethod("set" + parameterName, parameterClass)
									.invoke(commandTypeBuilder, true);
						} else {
							commandTypeBuilderClass.getMethod("set" + parameterName, parameterClass)
									.invoke(commandTypeBuilder, false);
						}
					}

					// set value if type is primitive
					else {
						double min;
						double max;
						if (parameter.hasMinValue()) {
							min = parameter.getMinValue();
						} else {
							min = parameterWrepperClass.getField("MIN_VALUE").getDouble(parameterClass);
						}
						if (parameter.hasMaxValue()) {
							max = parameter.getMaxValue();
						} else {
							max = parameterWrepperClass.getField("MAX_VALUE").getDouble(parameterClass);
						}

						// random value beetween min and max
						Double value = Math.random() * (max - min) + min;
						Class<?> valueClass = value.getClass();
						Object valueObject = valueClass.getMethod(parameterClass.getName() + "Value").invoke(value);
						commandTypeBuilderClass.getMethod("set" + parameterName, parameterClass)
								.invoke(commandTypeBuilder, valueObject);
					}

				}

				// set value if type is string
				else if (parameterClass.equals(String.class)) {
					if (parameter.hasPattern()) {
						Generex generex = new Generex(parameter.getPattern().replace("\"", "\\\""));
						commandTypeBuilderClass.getMethod("set" + parameterName, parameterClass)
								.invoke(commandTypeBuilder, generex.random());
					} else {
						String randomString = RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 10);
						commandTypeBuilderClass.getMethod("set" + parameterName, parameterClass)
								.invoke(commandTypeBuilder, randomString);
					}
				}

				// set value if type is enum
				else {
					Random random = new Random();
					Object[] values = (Object[]) parameterClass.getMethod("values").invoke(parameterClass);
					int randomIndex = random.nextInt(values.length);
					commandTypeBuilderClass.getMethod("set" + parameterName, parameterClass).invoke(commandTypeBuilder,
							values[randomIndex]);
				}
			}

			// build message type
			Class<?> commandTypeClass = commandTypeBuilderClass.getMethod("build").getReturnType();
			Object commandType = commandTypeBuilderClass.getMethod("build").invoke(commandTypeBuilder);

			// set command type
			commandBuilderClass.getMethod("set" + commandTypeUpperCamel, commandTypeClass).invoke(commandBuilder,
					commandType);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return commandBuilder.build().toByteArray();
	}

	/**
	 * 
	 * Returns radnom ATCommand defined in XML file
	 * 
	 * @return ATCommand
	 * 
	 */
	private static ATCommand getRandomATCommand() {
		Random random = new Random();

		// get cmd map
		Map<String, Map<String, Map<String, ATCommand>>> cmdMap = ProtobufATCommandAdapter.encodeMap;

		// get random type map
		int cmdIndex = random.nextInt(cmdMap.size());
		Map<String, Map<String, ATCommand>> typeMap = new ArrayList<Map<String, Map<String, ATCommand>>>(
				cmdMap.values()).get(cmdIndex);

		// get random action map
		int actionIndex = random.nextInt(typeMap.size());
		Map<String, ATCommand> actionMap = new ArrayList<Map<String, ATCommand>>(typeMap.values()).get(actionIndex);

		// get random ATCommand
		int commandIndex = random.nextInt(actionMap.size());
		ATCommand atCommand = new ArrayList<ATCommand>(actionMap.values()).get(commandIndex);

		return atCommand;
	}

}
