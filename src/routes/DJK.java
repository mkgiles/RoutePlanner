package routes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import routes.Graph.Node;
import routes.Graph.Way;

public class DJK {

	public HashMap<Node, Double> distances;
	Node startPoint;
	Node currentNode;
	Node endPoint;
	Node nextNode;
	Graph graph;
	ArrayList shortestRoute;

	public DJK(Graph graph) {

		this.graph = getGraph(graph);

	}

	public void DJKSEARCH(Node selStartPoint, Node selEndPoint) {
		System.out.println("DJK NODES ASSIGNED");
		this.startPoint = selStartPoint;
		this.endPoint = selEndPoint;
		this.currentNode = startPoint;
		dijkstra();
	}

	public Graph getGraph(Graph graph) {
		return graph;
	}

	
	//ESTABLISHING SHORTEST PATH TO POINT FROM START
	public void dijkstra() {

		distances = new HashMap<Node, Double>();

		System.out.println(startPoint);
		System.out.println(endPoint);
		System.out.println(graph);

		ArrayList priorityQueue = new ArrayList<Node>();
		ArrayList nodesVisited = new ArrayList<Node>();
		ArrayList unvisited = new ArrayList<Node>();
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
					if(nodesVisited.contains(way.nds.get(0)))
						continue;
					Collections.reverse(way.nds);
				}

					System.out.println("Current node: " + currentNode.id);
					System.out.println(
							"WAY " + way.id + ": " + way.nds.get(0) + " -> " + way.nds.get(way.nds.size() - 1));
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
						System.out
								.println("THE DISTANCE FROM START: " + distances.get(way.nds.get(way.nds.size() - 1)));

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

	//TRACING BACK THE SHORTEST PATH FROM THE ENDPOINT
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
		System.out.println("SHORTEST ROUTE FINAL: " + shortestRoute);
		
	}
}
