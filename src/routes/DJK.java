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

		
		while(currentNode != endPoint) {
			for (Graph.Way way : currentNode.ways) {
				if (way.nds.get(0) == currentNode) {
					System.out.println("Current node.");
					System.out.println(currentNode.id);
					System.out.println(way.id + ": " + way.nds.get(0) + " -> " + way.nds.get(way.nds.size() - 1));
					currentNode = way.nds.get(way.nds.size() - 1);
				}
			}
			
		}
		System.out.println("ENDPOINT");
		System.out.println(currentNode.id);

	}
}
