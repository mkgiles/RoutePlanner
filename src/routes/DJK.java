package routes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
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
	ArrayList <Node> WayPoints = null;
	ArrayList <Node> avoidedNodes = null;

	public DJK(Graph graph) {

		this.graph = getGraph(graph);

	}

	public void DJKSEARCH(Node selStartPoint, Node selEndPoint, ArrayList<Node> selWayPoints, ArrayList<Node> selAvoided) {
		System.out.println("DJK NODES ASSIGNED");
		this.startPoint = selStartPoint;
		this.endPoint = selEndPoint;
		this.currentNode = startPoint;
		shortestRoute = new ArrayList<Node>();

		System.out.println("START POINT: " + selStartPoint);
		System.out.println(graph.nodes.containsValue(selStartPoint));
		
		System.out.println("End POINT: " + selEndPoint);
		System.out.println(graph.nodes.containsValue(selEndPoint));
		
		if(selWayPoints != null) 
		{
			WayPoints = selWayPoints;
			System.out.println("WAYPOINTS: " + WayPoints);
		}else 
		{
			System.out.println("NO WAYPOINTS SET");
		}
		
		if(selAvoided != null) 
		{
			avoidedNodes = selAvoided;
			System.out.println("AVOIDED NODES: " + avoidedNodes);
		}else 
		{
			System.out.println("NO NODES AVOIDED");
		}
		
		if (graph.nodes.containsValue(selStartPoint) && graph.nodes.containsValue(selEndPoint)) {
			// dijkstra();
			if (!WayPoints.isEmpty()) {
				System.out.println("WAYPOINT EDSGER");
				System.out.println("PRE PREPEND/APPEND: " + WayPoints);
				//Prepending and Appending waypoints with start and end nodes.
				WayPoints.add(0, startPoint);
				WayPoints.add(WayPoints.size(), endPoint);
				System.out.println("POST PREPEND/APPEND: " + WayPoints);
				while (WayPoints.size() > 1) {
					System.out.println("PRE CYCLE :" + WayPoints);
					startPoint = WayPoints.get(0);
					endPoint = WayPoints.get(1);
					edsger(startPoint, endPoint, avoidedNodes);
					WayPoints.remove(0);
					System.out.println("POST CYCLE :" + WayPoints);
					System.out.println("SHORTEST ROUTE IN :" + shortestRoute);
				}
				
				System.out.println("FINISHED :" + WayPoints);
				System.out.println("SHORTEST ROUTE OUT :" + shortestRoute);
			} else {
				System.out.println("NON-WAYPOINT EDSGER	");
				edsger(selStartPoint, selEndPoint, avoidedNodes);
			}
		}else {
			System.out.println("One or both nodes do not exist in database.");
		}
	}

	public Graph getGraph(Graph graph) {
		return graph;
	}

	// ESTABLISHING SHORTEST PATH TO POINT FROM START
	public void dijkstra() {

		distances = new HashMap<Node, Double>();
		ArrayList <Node> priorityQueue = new ArrayList<Node>();
		ArrayList <Node> nodesVisited = new ArrayList<Node>();
		ArrayList <Node> unvisited = new ArrayList<Node>();
		shortestRoute = new ArrayList<Node>();
		for(Way way : currentNode.ways) {
			Node dest = way.nds.get(way.nds.size()-1);
			if(!unvisited.contains(dest) && !nodesVisited.contains(dest) && !priorityQueue.contains(dest)) {
				unvisited.add(dest);
				distances.put(dest, Double.POSITIVE_INFINITY);
			}
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
				
				Node dest = way.nds.get(way.nds.size()-1);
				if(!unvisited.contains(dest) && !nodesVisited.contains(dest) && !priorityQueue.contains(dest)) {
					unvisited.add(dest);
					distances.put(dest, Double.POSITIVE_INFINITY);
				}
				System.out.println("WAY " + way);

				priorityQueue.add(dest);

//				 Removing midpoints
//				if (way.nds.size() > 2) {
//					for (int i = 0; i < way.nds.size(); i++) {
//						Node removalNode = way.nds.get(i);
//						if (removalNode != way.nds.get(0) || removalNode != dest) {
//							unvisited.remove(removalNode);
//						}
//					}
//				}
//				if (way.length() < distances.get(dest)) {
//					distances.put(dest, distances.get(currentNode) + way.length());
//					System.out.println("THE DISTANCE FROM START: " + distances.get(dest));
//				}
				priorityQueue.remove(currentNode);

			}

			System.out.println("/////////////////////////////////");
			System.out.println("VISITED NODES: " + nodesVisited);
			System.out.println("UNVISITED NODES: " + unvisited);
			System.out.println("PRIORITY QUEUE: " + priorityQueue);
			priorityQueue.remove(currentNode);
			nodesVisited.add(currentNode);
			unvisited.remove(currentNode);
			currentNode = priorityQueue.get(0);
		}

		System.out.println("ENDPOINT");
		System.out.println(currentNode.id);
		for (int i = 0; i < nodesVisited.size(); i++) {
			System.out.println(nodesVisited.get(i));
		}

		System.out.println("DISTANCES" + Arrays.asList(distances));
		System.out.println(endPoint);
		System.out.println(distances.get(endPoint)==null?"null":distances.get(endPoint));
//		Main.shortestDist = distances.get(endPoint);

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
				Node dest = way.nds.get(0);
				if (shortestCurrentWayDist == -1.0) {
					System.out.println("INIT");
					if(!(distances.get(dest)==null)) {
						shortestCurrentWayDist = distances.get(dest);
						System.out.println(distances.get(dest));
						nextNode = dest;
					}
					System.out.println("NEXT NODE");
				} else if ((distances.get(dest)==null?Double.POSITIVE_INFINITY:distances.get(dest)) < shortestCurrentWayDist) {
					System.out.println("LOOP");
					shortestCurrentWayDist = distances.get(way.nds.get(0));
					nextNode = dest;
					System.out.println("NEXT NODE");
				}
			}
			shortestRoute.add(currentNode);
			currentNode = nextNode;
			System.out.println(currentNode);
		}
		double sum=0;
		for(int i = 0; i<shortestRoute.size()-1;i++) {
			Node node = (Node) shortestRoute.get(i);
			Node next = (Node) shortestRoute.get(i+1);
			List<Way> a = node.ways.stream().filter((x)->x.nds.get(0).equals(node)?x.nds.get(x.nds.size()-1).equals(next):x.nds.get(0).equals(next)).collect(Collectors.toList());
//			sum+=a.get(0).length();
		}
//		Main.shortestDist = sum;
		System.out.println("SHORTEST ROUTE FINAL: " + shortestRoute);

	}
	
	public void edsger(Node source, Node dest, ArrayList<Node> avoidances) {
		HashMap<Node, Node> prev = new HashMap<Node,Node>();
		HashMap<Node, BigDecimal> dist = new HashMap<Node,BigDecimal>();
		PriorityQueue<Node> queue = new PriorityQueue<Node>(11, (a,b)-> {if(dist.get(a)==null)dist.put(a,Main.INFINITY);if(dist.get(b)==null)dist.put(b,Main.INFINITY);return dist.get(a).compareTo(dist.get(b));});
		ArrayList<Node> visited = new ArrayList<Node>();
		Node temp;
		dist.put(source, BigDecimal.ZERO);
		queue.add(source);
		while(!queue.isEmpty()) {
			temp = queue.poll();
			if(temp.equals(dest))
				break;
			for(Way way : temp.ways) {
				Node stop = way.nds.get(0)==temp?way.nds.get(way.nds.size()-1):way.nds.get(0);
				if(!visited.contains(stop) && !avoidances.contains(stop)) {
					queue.add(stop);
					visited.add(stop);
				}
				if(dist.get(stop)==null)
					dist.put(stop,Main.INFINITY);
				if((dist.get(temp).add(Main.quickest?way.speed():way.length())).compareTo(dist.get(stop))<0) {
					dist.put(stop, dist.get(temp).add(Main.quickest?way.speed():way.length()));
					prev.put(stop, temp);
				}
			}
		}
		ArrayList<Node> path = new ArrayList<Node>();
		temp = dest;
		while(!(prev.get(temp)==null)) {
			path.add(temp);
			temp = prev.get(temp);
		}
		path.add(temp);
		BigDecimal sum= BigDecimal.ZERO;
		for(int i = 0; i<path.size()-1;i++) {	
			Node node = path.get(i);
			Node next = path.get(i+1);
			List<Way> a = node.ways.stream().filter((x)->x.nds.get(0).equals(node)?x.nds.get(x.nds.size()-1).equals(next):x.nds.get(0).equals(next)).collect(Collectors.toList());
			if(Main.quickest)
				sum= sum.add(a.get(0).speed());
			else
				sum= sum.add(a.get(0).length());
		}
		Main.shortestDist = sum;
		if(!shortestRoute.isEmpty())
		    path.addAll(shortestRoute.subList(1, shortestRoute.size()));
		shortestRoute = path;
		System.out.println("done");
	}

}
