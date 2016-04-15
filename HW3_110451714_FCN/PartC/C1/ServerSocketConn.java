import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerSocketConn extends Thread {
	private ServerSocket serverSocket;

	public ServerSocketConn() {

	}

	public ServerSocketConn(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	public void run() {
		while (true) {
			try {
				Socket server = serverSocket.accept();
				Advertisement.incoming.add(server);

			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public void createServerSocket(int port) {
		try {
			Thread t = new ServerSocketConn(port);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}