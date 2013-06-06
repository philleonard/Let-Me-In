
/* Class for centralising the IP and Port of the server
 * If in the future a domain is secured then the methods could
 * resolve an IP address from this domain so that updating the 
 * program is not required if the IP of the server changes
 */
public class GetAddress {

	public String server() {

		return "192.168.100.6";
	}
	
	public int serverPort() {
		
		return 55555;	
	}

}
