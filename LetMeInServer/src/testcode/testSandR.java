package testcode;


public class testSandR extends Thread{
	public static void main(String[] args) { 

	Thread t1 = new Thread(new testSend());
	Thread t2 = new Thread(new testReceive());

	t2.start();
	//t1.start();
	
	}

}
