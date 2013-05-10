package test;

import java.io.PrintStream;

public class LogTest {
	public static void main(String[] args) {
		MyLogger log = MyLogger.getLogger();
		log.setLevel(MyLogger.Level.DEBUG);
		log.debug("ただいまマイクのテスト中");
		log.info("こんにちは");
		log.warning("緊急事態発生");
	}

}

class MyLogger {
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

	public void write(String msg) {
		StackTraceElement st = new Throwable().getStackTrace()[2];
		String cname = st.getFileName();
		String mname = st.getMethodName();
		out.println(msg + " : " + mname + " " + cname);
	}
}