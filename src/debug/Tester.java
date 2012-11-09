package debug;

public class Tester {
	public void test(boolean assertion) {
		if(assertion) {
			System.err.println("Test fails!");
		}
		System.out.println("ok");
	}
}
