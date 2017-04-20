package com.rtrk.atcommand.generator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.base.CaseFormat;
import com.mifmif.common.regex.Generex;
import com.rtrk.atcommand.ATCommand;
import com.rtrk.atcommand.Parameter;
import com.rtrk.atcommand.adapter.ProtobufATCommandAdapter;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.Action;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.Command;
import com.rtrk.atcommand.protobuf.ProtobufATCommand.CommandType;

/**
 * 
 * Utility class for generating AT Command. The class contains static methods
 * for generating AT Commands in original and protobuf format.
 * 
 * @author djekanovic
 *
 */

public class ATCommandGenerator {

	/**
	 * 
	 * Creates random AT Commands. Number, order and percentage of each command is defined in config file
	 * percentage-config.cfg.
	 * 
	 * @return List of AT Commands
	 * 
	 */
	public static ArrayList<byte[]> generateATCommands() {
		try {

			ArrayList<byte[]> cmds = new ArrayList<byte[]>();

			// read json file to string
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream("config\\percentage-config.json")));
			StringBuilder stringBuilder = new StringBuilder();
			int readed;
			while ((readed = reader.read()) != -1) {
				stringBuilder.append((char) readed);
			}
			reader.close();
			String jsonString = stringBuilder.toString();

			// get json attributes
			JSONObject json = new JSONObject(jsonString);
			int number = json.getInt("number");
			boolean shuffle = json.getBoolean("shuffle");

			JSONArray commands = json.getJSONArray("commands");
			for (int i = 0; i < commands.length(); i++) {

				// get command attributes
				JSONObject command = commands.getJSONObject(i);
				String commandType = command.getString("name");

				// get command type
				CommandType commandTypeEnum = CommandType.valueOf(commandType);

				// get message type
				Command.Builder commandBuilder = Command.newBuilder();
				Class<?> commandBuilderClass = commandBuilder.getClass();
				String commandName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, commandType);
				Object commandTypeObject = commandBuilderClass.getMethod("get" + commandName).invoke(commandBuilder);
				Class<?> commandTypeClass = commandTypeObject.getClass();

				// get message type enum
				Class<?> messageTypeEnumClass = commandTypeClass.getMethod("getMessageType").getReturnType();

				JSONArray types = command.getJSONArray("types");
				for (int j = 0; j < types.length(); j++) {
					JSONObject type = types.getJSONObject(j);

					String typeName = type.getString("name");
					double percentage = type.getDouble("percentage");

					Object messageTypeEnumObject = messageTypeEnumClass.getMethod("valueOf", String.class)
							.invoke(messageTypeEnumClass, typeName);

					// generate at commands
					for (int k = 0; k < number * (percentage / 100); k++) {
						byte[] generated = ATCommandGenerator.generateATCommand(commandTypeEnum, messageTypeEnumObject);
						cmds.add(generated);
					}

				}
			}

			// shuffle commands
			if (shuffle) {
				Collections.shuffle(cmds);
			}

			return cmds;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * Creates random protobuf AT Commands. Number, order and percenage of each command is defined in
	 * config file percentage-config.cfg.
	 * 
	 * @return List of AT Commands in protobuf format
	 * 
	 */
	public static ArrayList<byte[]> generateProtobufATCommands() {
		try {

			ArrayList<byte[]> cmds = new ArrayList<byte[]>();

			// read json file to string
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream("config\\percentage-config.json")));
			StringBuilder stringBuilder = new StringBuilder();
			int readed;
			while ((readed = reader.read()) != -1) {
				stringBuilder.append((char) readed);
			}
			reader.close();
			String jsonString = stringBuilder.toString();

			// get json attributes
			JSONObject json = new JSONObject(jsonString);
			int number = json.getInt("number");
			boolean shuffle = json.getBoolean("shuffle");

			JSONArray commands = json.getJSONArray("commands");
			for (int i = 0; i < commands.length(); i++) {

				// get command attributes
				JSONObject command = commands.getJSONObject(i);
				String commandType = command.getString("name");

				// get command type
				CommandType commandTypeEnum = CommandType.valueOf(commandType);

				// get message type
				Command.Builder commandBuilder = Command.newBuilder();
				Class<?> commandBuilderClass = commandBuilder.getClass();
				String commandName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, commandType);
				Object commandTypeObject = commandBuilderClass.getMethod("get" + commandName).invoke(commandBuilder);
				Class<?> commandTypeClass = commandTypeObject.getClass();

				// get message type enum
				Class<?> messageTypeEnumClass = commandTypeClass.getMethod("getMessageType").getReturnType();

				JSONArray types = command.getJSONArray("types");
				for (int j = 0; j < types.length(); j++) {
					JSONObject type = types.getJSONObject(j);

					String typeName = type.getString("name");
					double percentage = type.getDouble("percentage");

					Object messageTypeEnumObject = messageTypeEnumClass.getMethod("valueOf", String.class)
							.invoke(messageTypeEnumClass, typeName);

					// generate at commands
					for (int k = 0; k < number * (percentage / 100); k++) {
						byte[] generated = ATCommandGenerator.generateProtobufATCommand(commandTypeEnum,
								messageTypeEnumObject);
						cmds.add(generated);
					}

				}
			}

			// shuffle commands
			if (shuffle) {
				Collections.shuffle(cmds);
			}

			return cmds;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * Creates random ATCommand.
	 * 
	 * @return ATCommand as byte array
	 * 
	 */
	public static byte[] generateATCommand() {
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

		return createATCommand(atCommand);
	}

	/**
	 * 
	 * Creates random ATCommand with specific action.
	 * 
	 * @param action
	 *            TEST, READ, WRITE or EXECUTION ATCommand
	 * 
	 * @return ATCommand as byte array or null if action not exists
	 * 
	 */
	public static byte[] generateATCommand(Action action) {
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

		if (!actionMap.containsKey(action.toString())) {
			return null;
		}

		ATCommand atCommand = actionMap.get(action.toString());

		return createATCommand(atCommand);
	}

	/**
	 * 
	 * Creates random ATCommand with specific command type.
	 * 
	 * @param commandType
	 *            Type of ATCommand
	 * 
	 * @return ATCommand as byte array
	 * 
	 */
	public static byte[] generateATCommand(CommandType commandType) {
		String commandTypeLowerCamel = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, commandType.toString());
		Map<String, Map<String, ATCommand>> typeMap = ProtobufATCommandAdapter.encodeMap.get(commandTypeLowerCamel);

		// get random action map
		int actionIndex = new Random().nextInt(typeMap.size());
		Map<String, ATCommand> actionMap = new ArrayList<Map<String, ATCommand>>(typeMap.values()).get(actionIndex);

		// get random ATCommand
		int commandIndex = new Random().nextInt(actionMap.size());
		ATCommand atCommand = new ArrayList<ATCommand>(actionMap.values()).get(commandIndex);

		return createATCommand(atCommand);
	}

	/**
	 * 
	 * Creates random ATCommand with specific command type and message type.
	 * 
	 * @param commandType
	 *            Type of ATCommand
	 * 
	 * @param messageType
	 *            Message type of ATCommand
	 * 
	 * @return ATCommand as byte array
	 * 
	 */
	public static byte[] generateATCommand(CommandType commandType, Object messageType) {
		String commandTypeLowerCamel = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, commandType.toString());
		Map<String, ATCommand> actionMap = ProtobufATCommandAdapter.encodeMap.get(commandTypeLowerCamel)
				.get(messageType.toString());
		Set<String> actionSet=actionMap.keySet();
		
		// get random ATCommand
		int commandIndex = new Random().nextInt(actionMap.size());
		ATCommand atCommand = actionMap.get(actionSet.toArray()[commandIndex].toString());

		return createATCommand(atCommand);
	}

	/**
	 * 
	 * Creates ATCommand with specific command type, message type and action.
	 * 
	 * @param commandType
	 *            Type of ATCommand
	 * 
	 * @param messageType
	 *            Message type of ATCommand
	 * 
	 * @param action
	 *            TEST, READ, WRITE or EXECUTION ATCommand
	 * 
	 * @return ATCommand as byte array or null if action not exists
	 * 
	 */
	public static byte[] generateATCommand(CommandType commandType, Object messageType, Action action) {
		String commandTypeLowerCamel = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, commandType.toString());
		Map<String, ATCommand> actionMap = ProtobufATCommandAdapter.encodeMap.get(commandTypeLowerCamel)
				.get(messageType.toString());
		if (!actionMap.containsKey(action.toString())) {
			return null;
		}
		ATCommand atCommand = actionMap.get(action.toString());
		return createATCommand(atCommand);
	}

	/**
	 * 
	 * Creates random ATCommand in protobuf format.
	 * 
	 * @return ATCommand as byte array
	 * 
	 */
	public static byte[] generateProtobufATCommand() {
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

		return createProtobufATCommand(atCommand);
	}

	/**
	 * 
	 * Creates ATCommand in protobuf format with specific action.
	 * 
	 * @param action
	 *            TEST, READ, WRITE or EXECUTION ATCommand
	 * 
	 * @return ATCommand in protobuf format as byte array or null if action not
	 *         exists
	 * 
	 */
	public static byte[] generateProtobufATCommand(Action action) {
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

		if (!actionMap.containsKey(action.toString())) {
			return null;
		}

		ATCommand atCommand = actionMap.get(action.toString());

		return createProtobufATCommand(atCommand);
	}

	/**
	 * 
	 * Creates ATCommand in protobuf format with specific command type.
	 * 
	 * @param commandType
	 *            Type of ATCommand
	 * 
	 * @return ATCommand in protobuf format as byte array
	 * 
	 */
	public static byte[] generateProtobufATCommand(CommandType commandType) {
		String commandTypeLowerCamel = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, commandType.toString());
		Map<String, Map<String, ATCommand>> typeMap = ProtobufATCommandAdapter.encodeMap.get(commandTypeLowerCamel);

		// get random action map
		int actionIndex = new Random().nextInt(typeMap.size());
		Map<String, ATCommand> actionMap = new ArrayList<Map<String, ATCommand>>(typeMap.values()).get(actionIndex);

		// get random ATCommand
		int commandIndex = new Random().nextInt(actionMap.size());
		ATCommand atCommand = new ArrayList<ATCommand>(actionMap.values()).get(commandIndex);

		return createProtobufATCommand(atCommand);
	}

	/**
	 * 
	 * Creates ATCommand in protobuf format with specific command type and
	 * message type.
	 * 
	 * @param commandType
	 *            Type of ATCommand
	 * 
	 * @param messageType
	 *            Message type of ATCommand
	 * 
	 * @return ATCommand in protobuf format as byte array
	 * 
	 */
	public static byte[] generateProtobufATCommand(CommandType commandType, Object messageType) {
		String commandTypeLowerCamel = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, commandType.toString());
		Map<String, ATCommand> actionMap = ProtobufATCommandAdapter.encodeMap.get(commandTypeLowerCamel)
				.get(messageType.toString());
		Set<String> actionSet=actionMap.keySet();
		
		// get random ATCommand
		int commandIndex = new Random().nextInt(actionMap.size());
		ATCommand atCommand = actionMap.get(actionSet.toArray()[commandIndex].toString());

		return createProtobufATCommand(atCommand);
	}

	/**
	 * 
	 * Creates ATCommand in protobuf format with specific command type, message
	 * type and action.
	 * 
	 * @param commandType
	 *            Type of ATCommand
	 * 
	 * @param messageType
	 *            Message type of ATCommand
	 * 
	 * @param action
	 *            TEST, READ, WRITE or EXECUTION ATCommand
	 * 
	 * @return ATCommand in protobuf format as byte array or null if action not
	 *         exists
	 * 
	 */
	public static byte[] generateProtobufATCommand(CommandType commandType, Object messageType, Action action) {
		String commandTypeLowerCamel = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, commandType.toString());
		Map<String, ATCommand> actionMap = ProtobufATCommandAdapter.encodeMap.get(commandTypeLowerCamel)
				.get(messageType.toString());

		if (!actionMap.containsKey(action.toString())) {
			return null;
		}
		ATCommand atCommand = actionMap.get(action.toString());
		return createProtobufATCommand(atCommand);
	}

	/**
	 * 
	 * Creates ATCommand with random parameters. Sets optional parameter with
	 * probability of 50 %. Description of specific ATCommand is passed as a
	 * funciton parameter.
	 * 
	 * @param atCommand
	 *            Description of ATCommand
	 * 
	 * @return ATCommand as byte array
	 * 
	 */
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

			// set parameters with generator
			if (atCommand.hasGenerator()) {
				Class<?> generatorClass = Class.forName(atCommand.getGenerator());
				Generator generator = (Generator) generatorClass.newInstance();
				return generator.generateATCommand();
			}

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
						Double value;
						if (parameterClass.equals(double.class) || parameterClass.equals(float.class)) {
							value = Math.random() * (max - min) + min;
						} else {
							value = Math.random() * (max - min + 1) + min;
						}

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
						command += RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5);
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
				| InvocationTargetException | NoSuchFieldException | ClassNotFoundException
				| InstantiationException e) {
			e.printStackTrace();
		}

		return command.getBytes();
	}

	/**
	 * 
	 * Creates ATCommand in protobuf format with random parameters. Sets
	 * optional parameter with probability of 50 %. Description of specific
	 * ATCommand is passed as a funciton parameter.
	 * 
	 * @param atCommand
	 *            Description of ATCommand
	 * 
	 * @return ATCommand in protobuf format as byte array
	 * 
	 */
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

			// set parameters with generator
			if (atCommand.hasGenerator()) {
				Class<?> generatorClass = Class.forName(atCommand.getGenerator());
				Generator generator = (Generator) generatorClass.newInstance();
				generator.generateProtobufATCommand(commandTypeBuilder);
			}

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
						String randomString = RandomStringUtils.randomAlphabetic(new Random().nextInt(10) + 5);
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
				| SecurityException | NoSuchFieldException | ClassNotFoundException | InstantiationException e) {
			e.printStackTrace();
		}
		return commandBuilder.build().toByteArray();
	}

}
