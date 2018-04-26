package routes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import routes.Graph.Node;
import routes.Graph.Way;

public class DJK {

	public HashMap<Node, Double> distances;
	Node startPoint;
	Node currentNode;
	Node endPoint;
	Node nextNode;
	Graph graph;
	double distance;
	ArrayList <Node> shortestRoute = null;

	public DJK(Graph graph) {

		this.graph = getGraph(graph);

	}

	public void DJKSEARCH(Node selStartPoint, Node selEndPoint) {
		System.out.println("DJK NODES ASSIGNED");
		this.startPoint = selStartPoint;
		this.endPoint = selEndPoint;
		this.currentNode = startPoint;

		System.out.println("START POINT: " + selStartPoint);
		System.out.println(graph.nodes.containsValue(selStartPoint));
		
		System.out.println("End POINT: " + selEndPoint);
		System.out.println(graph.nodes.containsValue(selEndPoint));
		
		if (graph.nodes.containsValue(selStartPoint) && graph.nodes.containsValue(selEndPoint)) {
			dijkstra();
		} else {
			System.out.println("One or both nodes do not exist in database.");
		}
	}

	public Graph getGraph(Graph graph) {
		return graph;
	}

	// ESTABLISHING SHORTEST PATH TO POINT FROM START
	public void dijkstra() {

		distances = new HashMap<Node, Double>();

		System.out.println(startPoint);
		System.out.println(endPoint);
		System.out.println(graph);

		ArrayList <Node> priorityQueue = new ArrayList<Node>();
		ArrayList <Node> nodesVisited = new ArrayList<Node>();
		ArrayList <Node> unvisited = new ArrayList<Node>();
		shortestRoute = new ArrayList<Node>();

		for (String key : graph.nodes.keySet()) {
			unvisited.add(graph.nodes.get(key));
			distances.put(graph.nodes.get(key), Double.POSITIVE_INFINITY);
		}

		distances.put(startPoint, (double) 0);
		priorityQueue.add(startPoint);

		while (!unvisited.isEmpty()) {
			System.out.println("////////////////////" + "CURRENT NODE: " + currentNode.id + "////////////////////");
			for (Graph.Way way : currentNode.ways) {

				if (way.nds.get(0) != currentNode) {
					if (nodesVisited.contains(way.nds.get(0)))
						continue;
					Collections.reverse(way.nds);
				}

				System.out.println("Current node: " + currentNode.id);
				System.out.println("WAY " + way.id + ": " + way.nds.get(0) + " -> " + way.nds.get(way.nds.size() - 1));
				System.out.println("THE LENGTH IS " + way.length());

				priorityQueue.add(way.nds.get(way.nds.size() - 1));

				// Removing midpoints
				if (way.nds.size() > 2) {
					for (int i = 0; i < way.nds.size(); i++) {
						Node removalNode = way.nds.get(i);
						if (removalNode != way.nds.get(0) || removalNode != way.nds.get(way.nds.size() - 1)) {
							unvisited.remove(removalNode);
						}
					}
				}
				if (way.length() < distances.get(way.nds.get(way.nds.size() - 1))) {
					distances.put(way.nds.get(way.nds.size() - 1), distances.get(currentNode) + way.length());
					System.out.println("THE DISTANCE FROM START: " + distances.get(way.nds.get(way.nds.size() - 1)));

				}
				priorityQueue.remove(currentNode);

			}

			System.out.println("/////////////////////////////////");
			System.out.println("VISITED NODES: " + nodesVisited);
			System.out.println("UNVISITED NODES: " + unvisited);
			System.out.println("PRIORITY QUEUE: " + priorityQueue);
			priorityQueue.remove(currentNode);
			nodesVisited.add(currentNode);
			unvisited.remove(currentNode);
			currentNode = (Node) priorityQueue.get(0);
		}

		System.out.println("ENDPOINT");
		System.out.println(currentNode.id);
		for (int i = 0; i < nodesVisited.size(); i++) {
			System.out.println(nodesVisited.get(i));
		}

		System.out.println("DISTANCES" + Arrays.asList(distances));
		Main.shortestDist = distances.get(endPoint);

		traceBack();
	}

	// TRACING BACK THE SHORTEST PATH FROM THE ENDPOINT
	public void traceBack() {

		currentNode = endPoint;
		System.out.println("CURRENT NODE >>>> " + currentNode);
		Node nextNode = null;
		double shortestCurrentWayDist = -1;
		while (!shortestRoute.contains(startPoint)) {
			System.out.println("SHORTEST ROUTE BEFORE: " + shortestRoute);
			System.out.println(shortestCurrentWayDist);

			for (Graph.Way way : currentNode.ways) {
				if (shortestCurrentWayDist == -1) {
					System.out.println("INIT");
					shortestCurrentWayDist = distances.get(way.nds.get(0));
					System.out.println(distances.get(way.nds.get(0)));
					nextNode = way.nds.get(0);
					System.out.println("NEXT NODE");

				} else if (distances.get(way.nds.get(0)) < shortestCurrentWayDist) {
					System.out.println("LOOP");
					shortestCurrentWayDist = distances.get(way.nds.get(0));

					nextNode = way.nds.get(0);
					System.out.println("NEXT NODE");
				}

			}
			shortestRoute.add(currentNode);
			currentNode = nextNode;
		}
		double sum=0;
		for(int i = 0; i<shortestRoute.size()-1;i++) {
			Node node = (Node) shortestRoute.get(i);
			Node next = (Node) shortestRoute.get(i+1);
			List<Way> a = node.ways.stream().filter((x)->x.nds.get(0).equals(node)?x.nds.get(x.nds.size()-1).equals(next):x.nds.get(0).equals(next)).collect(Collectors.toList());
			sum+=a.get(0).length();
		}
		System.out.println("LENGTH:"+sum);
		System.out.println("SHORTEST ROUTE FINAL: " + shortestRoute);

	}

}
