
public class theServer extends Thread {
	public static void main(String[] args) {
		
		Thread t = new Thread(new clientListeningClass()); //listens on port 8080
		Thread s = new Thread(new phoneListeningClass()); //listens on port 8181
		t.start();
		s.start();
	}
}
