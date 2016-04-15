import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Advertisement extends Thread {

	public static List<Socket> incoming = new ArrayList<Socket>();
	public static List<Socket> outgoing = new ArrayList<Socket>();;

	public void collect() throws IOException {

		if (incoming != null && incoming.size() > 0) {
			for (Socket inc : incoming) {
				// read from server socket, parse and write to the topology.txt
				DataInputStream in = new DataInputStream(inc.getInputStream());
				String read = in.readUTF();

				if (read.length() > 0) {

					// read from file to map
					Map<String, Integer> map = new HashMap<String, Integer>();
					File file = null;
					try {
						file = new File("topology.txt");
					} catch (Exception e) {
						// e.printStackTrace();

					}

					Scanner scan = new Scanner(file);
					while (scan.hasNextLine()) {
						String line = scan.nextLine();
						map.put(line.substring(0, 5), Integer.parseInt(line.substring(6)));
					}

					// compare map with read from socket and update map
					String[] readArr = read.split(" ");
					for (int i = 0; i < readArr.length; i += 3) {
						String srcdst = readArr[0] + readArr[1];
						if (map.containsKey(srcdst)) {
							if (Integer.parseInt(readArr[2]) < map.get(srcdst))
								map.put(srcdst, Integer.parseInt(readArr[2]));
						} else
							map.put(srcdst, Integer.parseInt(readArr[2]));
					}

					// write map to file
					try {
						file = new File("topology.txt");
					} catch (Exception e) {
						// e.printStackTrace();
					}
					FileWriter fw = new FileWriter(file);
					BufferedWriter bw = new BufferedWriter(fw);
					for (Map.Entry<String, Integer> entry : map.entrySet()) {
						bw.write(entry.getKey() + " " + entry.getValue());
					}

					// read from topology.txt and write to client socket

					try {
						file = new File("topology.txt");
					} catch (Exception e) {
						// e.printStackTrace();

					}

					String writeData = "";
					scan = new Scanner(file);
					while (scan.hasNextLine()) {
						String line = scan.nextLine();
						writeData = writeData + line + " ";
					}

					writeData = writeData.substring(0, writeData.length() - 1);

					if (outgoing != null || outgoing.size() > 0) {
						for (Socket ots : outgoing) {
							try {
								// write to the client socket
								DataOutputStream out = new DataOutputStream(ots.getOutputStream());
								out.writeUTF(writeData);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								// e.printStackTrace();

							}
						}
					}
				}
			}
		}

	}

}