import java.io.IOException;
import java.net.Socket;

public class ClientSocketConn {

	public void createClientSocket(String sock1, String port1, String sock2, String port2) {

		Socket client1 = null;
		Socket client2 = null;
		try {
			if (!sock1.equals("none")) {
				client1 = new Socket(sock1, Integer.parseInt(port1));
				Advertisement.outgoing.add(client1);
			}

		} catch (NumberFormatException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (!sock2.equals("none")) {
				client2 = new Socket(sock2, Integer.parseInt(port2));
				Advertisement.outgoing.add(client2);
			}
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
