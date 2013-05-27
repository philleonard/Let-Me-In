/**
 * @author Philip Leonard
 */
public class OpenDoor implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		/*
		 * Here a controller for a motor on the door can be implemented.
		 * Below is a sleep to imitate the door being closed.
		 * Ideally a sensor would be implemented so when the door closes the 
		 * thread can be resumed & can finish.
		 */
		
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
