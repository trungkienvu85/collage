package com.example.simas.collage.SmartProcessBuilder;

import java.io.IOException;
import java.util.List;

/**
 * Created by Simas Abramovas on 2015 Mar 03.
 */

/**
 * Convenienvce class to replace ProcessBuilder. This separates each argument with a space but
 * also allows space inside the arguments.
 */
public class SmartProcessBuilder {

	private String[] mArguments;

	/**
	 *
	 * @param args    argument array containing at least 1 argument - full path to file
	 */
	public SmartProcessBuilder(List<String> args) {
		this(args.toArray(new String[args.size()]));
	}

	/**
	 *
	 * @param args    argument array containing at least 1 argument - full path to file
	 */
	public SmartProcessBuilder(String... args) {
		if (args == null || args.length < 1) {
			throw new IllegalArgumentException("Must provide at least 1 argument!");
		}
		mArguments = args;
	}

	private String combineArguments() {
		if (mArguments == null || mArguments.length < 1) {
			throw new IllegalStateException("There must be at least 1 argument!");
		}
		String cmd = "";
		for (String arg : mArguments) {
			cmd += arg + ' ';
		}
		// Remove the last space
		cmd = cmd.substring(0, cmd.length() - 1);

		return cmd;
	}

	public Process start() throws IOException {
		String cmd = combineArguments();
		return Runtime.getRuntime().exec(cmd);
	}

}
