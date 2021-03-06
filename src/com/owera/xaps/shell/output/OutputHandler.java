package com.owera.xaps.shell.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.owera.xaps.shell.Context;
import com.owera.xaps.shell.Properties;
import com.owera.xaps.shell.command.Command;
import com.owera.xaps.shell.util.FileUtil;

public class OutputHandler {
	private FileWriter fw;
	private boolean printedHeading = false;
	private Context context = null;
	private String heading = null;
	private Listing listing;
	private Command command;

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public OutputHandler(Command command, Context context) throws IOException {
		this.context = context;
		this.command = command;
		this.listing = new Listing(context, command);
		if (command.getOutputFilename() != null) {
			if (Properties.isRestricted()) {
				File f = new File(command.getOutputFilename());
				if (!FileUtil.allowed("Write to " + command.getOutputFilename(), f))
					throw new IllegalArgumentException("Abort command execution due to access restriction violations");
			}
			fw = new FileWriter(command.getOutputFilename(), command.appendToOutput());
		}
	}

	public void print(String s) throws IOException {
		if (fw == null) {
			if (!printedHeading && heading != null) {
				context.print(heading);
				printedHeading = true;
			}
			context.print(s);
		} else {
			fw.write(s);
		}
	}

	public void close() throws IOException {
		if (fw != null)
			fw.close();
	}

	public Listing getListing() {
		return listing;
	}

	public Command getCommand() {
		return command;
	}

	public boolean toFile() {
		return (fw != null ? true : false);
	}
}
