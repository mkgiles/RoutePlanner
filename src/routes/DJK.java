package routes;

import java.util.ArrayList;
import routes.Graph.Node;

public class DJK {

	Node startPoint;
	Node currentNode;
	Node endPoint;
	Node nextNode;
	Graph graph;

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

	public void dijkstra() {
		
		
		
		System.out.println(startPoint);
		System.out.println(endPoint);
		System.out.println(graph);

		
		ArrayList priorityQueue = new ArrayList<Node>();
		ArrayList nodesVisited = new ArrayList<Node>();
		ArrayList unvisited = new ArrayList<Node>();
		
		/*
		//EXPERIMENTATION
		while(currentNode != endPoint) {
			for (Graph.Way way : currentNode.ways) {
				if (way.nds.get(0) == currentNode) {
					nodesVisited.add(currentNode);
					System.out.println("Current node.");
					System.out.println(currentNode.id);
					System.out.println(way.id + ": " + way.nds.get(0) + " -> " + way.nds.get(way.nds.size() - 1));
					System.out.println("THE LENGTH IS " + way.length());
					
					currentNode = way.nds.get(way.nds.size() - 1);
				}
			}
			
		}
		nodesVisited.add(endPoint);
		System.out.println("ENDPOINT");
		System.out.println(currentNode.id);
		for(int i = 0; i < nodesVisited.size(); i++) {
		System.out.println(nodesVisited.get(i));
		}
		*/
		
		
		for(String key : graph.nodes.keySet()) 
		{
			unvisited.add(graph.nodes.get(key));
		}
		System.out.println("UNVISITED NODES: " + unvisited);
		nodesVisited.add(startPoint);
		priorityQueue.add(startPoint);
		
		while(unvisited != null) {
			System.out.println("////////////////////" + "CURRENT NODE: " + currentNode.id + "////////////////////");
			for (Graph.Way way : currentNode.ways) {
				
				if (way.nds.get(0) == currentNode) {
					nodesVisited.add(currentNode);
					System.out.println("Current node: " + currentNode.id);
					System.out.println("WAY " + way.id + ": " + way.nds.get(0) + " -> " + way.nds.get(way.nds.size() - 1));
					System.out.println("THE LENGTH IS " + way.length());
					
					priorityQueue.add(way.nds.get(way.nds.size() - 1));
				}
				priorityQueue.remove(currentNode);
				nodesVisited.add(currentNode);
				unvisited.remove(currentNode);
			}
			
			System.out.println("/////////////////////////////////");
			
			System.out.println("UNVISITED NODES: " + unvisited);
			System.out.println("PRIORITY QUEUE: " + priorityQueue);
			currentNode = (Node) priorityQueue.get(0);
		}
		
		System.out.println("ENDPOINT");
		System.out.println(currentNode.id);
		for(int i = 0; i < nodesVisited.size(); i++) {
		System.out.println(nodesVisited.get(i));
		}
		
	}
}
