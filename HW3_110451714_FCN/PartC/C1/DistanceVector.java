import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

class DistanceVector {

	static Map<String, Integer> map;

	class Edge {
		String src, dest;
		int weight;

		Edge() {
			src = dest = null;
			weight = 0;
		}
	};

	int V, E;
	Edge edge[];

	DistanceVector(int v, int e) {
		V = v;
		E = e;
		edge = new Edge[e];
		for (int i = 0; i < e; ++i)
			edge[i] = new Edge();
	}

	void BellmanFord(DistanceVector graph, String src) {
		int V = graph.V, E = graph.E;
		int dist[] = new int[V];
		String[] nextHop = new String[V];

		for (int i = 0; i < V; ++i)
			dist[i] = Integer.MAX_VALUE;
		dist[map.get(src)] = 0;
		nextHop[map.get(src)] = src;

		for (int i = 1; i < V; ++i) {
			for (int j = 0; j < E; ++j) {
				int u = map.get(graph.edge[j].src);
				int v = map.get(graph.edge[j].dest);
				int weight = graph.edge[j].weight;
				if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v])
					dist[v] = dist[u] + weight;
				if (nextHop[v] == null)
					nextHop[v] = graph.edge[j].src;
			}
		}

		for (int j = 0; j < E; ++j) {
			int u = map.get(graph.edge[j].src);
			int v = map.get(graph.edge[j].dest);
			int weight = graph.edge[j].weight;
			if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v])
				System.out.println("Graph contains negative weight cycle");
		}

		for (int i = 0; i < V; ++i) {
			if (dist[i] == 2147483647)
				dist[i] = -1;
		}

		printArr(dist, V, src, nextHop);
	}

	void printArr(int dist[], int V, String src, String[] nextHop) {
		try {
			File file = new File(src + ".txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Vertex " + src + " distance from other nodes");
			for (String s : map.keySet())
				bw.write("\n" + s + " " + dist[map.get(s)] + " " + nextHop[map.get(s)]);

			System.out.println("Vertex " + src + " distance from other nodes");
			for (String s : map.keySet())
				System.out.println(s + " " + dist[map.get(s)] + " " + nextHop[map.get(s)]);

			System.out.println("\n\n");
			bw.close();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

		// create server socket and accept all the request client conn and fill
		// it to client conn array
		ServerSocketConn serverSocketConn = new ServerSocketConn();
		serverSocketConn.createServerSocket(Integer.parseInt(args[0]));

		ClientSocketConn clientSocketConn = new ClientSocketConn();
		clientSocketConn.createClientSocket(args[1], args[2], args[3], args[4]);

		while (true) {

			// launch a thead, Read from the given socket, parse and update
			// topology.txt
			// then write to the socket. Repeat this process after 30 seconds
			Advertisement ag = new Advertisement();
			ag.collect();

			Set<String> node = null;
			File file = null;
			try {
				file = new File("topology.txt");
			} catch (Exception e) {
				// e.printStackTrace();
				continue;
			}

			Scanner scan;
			DistanceVector graph = null;
			map = new TreeMap<String, Integer>();

			try {
				scan = new Scanner(file);
				node = new TreeSet<String>();
				int i = 0, j = 0;

				while (scan.hasNextLine()) {
					String line = scan.nextLine();
					String[] components = line.split("[ ]+");
					String host1 = components[0];
					String host2 = components[1];
					int distance = Integer.parseInt(components[2]);
					if (!node.contains(host1)) {
						node.add(host1);
						map.put(host1, j++);
					}
					if (!node.contains(host2)) {
						node.add(host2);
						map.put(host2, j++);
					}
					i++;
				}
				graph = new DistanceVector(node.size(), i);
				scan = new Scanner(file);

				i = 0;
				while (scan.hasNextLine()) {
					String line1 = scan.nextLine();
					String[] components1 = line1.split("[ ]+");
					String host11 = components1[0];
					String host21 = components1[1];
					int distance1 = Integer.parseInt(components1[2]);

					graph.edge[i].src = host11;
					graph.edge[i].dest = host21;
					graph.edge[i].weight = distance1;
					i++;
				}
				scan.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

			graph.BellmanFord(graph, "H1");

			try {
				Thread.currentThread().sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				continue;
			}
		}
	}

}