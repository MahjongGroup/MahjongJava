package util;

import java.io.PrintStream;

public class MyLogger {
	private static MyLogger instance;
	private Level level;
	private PrintStream out;

	public static enum Level {
		WARNING, INFO, DEBUG
	}

	public MyLogger() {
		this.level = Level.DEBUG;
		this.out = System.out;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void setPrintStream(PrintStream ps) {
		this.out = ps;
	}

	public static MyLogger getLogger() {
		if (instance == null) {
			instance = new MyLogger();
		}
		return instance;
	}

	public void info(String msg) {
		if (Level.INFO.ordinal() <= this.level.ordinal())
			write(msg);
	}

	public void debug(String msg) {
		if (Level.DEBUG.ordinal() <= this.level.ordinal())
			write(msg);
	}

	public void warning(String msg) {
		if (Level.WARNING.ordinal() <= this.level.ordinal())
			write(msg);
	}

	public void info() {
		if (Level.INFO.ordinal() <= this.level.ordinal())
			write("");
	}

	public void debug() {
		if (Level.DEBUG.ordinal() <= this.level.ordinal())
			write("");
	}

	public void warning() {
		if (Level.WARNING.ordinal() <= this.level.ordinal())
			write("");
	}

	public void write(String msg) {
		StackTraceElement st = new Throwable().getStackTrace()[2];
		String cname = st.getFileName();
		String mname = st.getMethodName();
		out.println(msg + " : " + mname + " " + cname);
	}
}