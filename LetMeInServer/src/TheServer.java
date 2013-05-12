
public class TheServer extends Thread {
	public static void main(String[] args) { 
		
		Thread t = new Thread(new ListeningClass()); //listens on port 8080
		t.start();
		
	}
}
