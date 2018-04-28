package routes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import routes.Graph.Node;
import routes.Graph.Way;

// TODO: Auto-generated Javadoc
/**
 * The Class DJK.
 */
public class DJK {

	/** The distances. */
	public HashMap<Node, Double> distances;
	
	/** The start point. */
	Node startPoint;
	
	/** The current node. */
	Node currentNode;
	
	/** The end point. */
	Node endPoint;
	
	/** The next node. */
	Node nextNode;
	
	/** The graph. */
	Graph graph;
	
	/** The distance. */
	double distance;
	
	/** The shortest route. */
	ArrayList<Node> shortestRoute = null;
	
	/** The Way points. */
	ArrayList<Node> WayPoints = null;
	
	/** The avoided nodes. */
	ArrayList<Node> avoidedNodes = null;

	/**
	 * Instantiates a new djk.
	 *
	 * @param graph the graph
	 */
	public DJK(Graph graph) {

		this.graph = getGraph(graph);

	}

	/**
	 * Initialises a route search.
	 *
	 * @param selStartPoint the sel start point
	 * @param selEndPoint the sel end point
	 * @param selWayPoints the sel way points
	 * @param selAvoided the sel avoided
	 */
	public void DJKSEARCH(Node selStartPoint, Node selEndPoint, ArrayList<Node> selWayPoints,
			ArrayList<Node> selAvoided) {

		this.startPoint = selStartPoint;
		this.endPoint = selEndPoint;
		this.currentNode = startPoint;
		shortestRoute = new ArrayList<Node>();

		if (selWayPoints != null) {
			WayPoints = selWayPoints;

		} else {

		}

		if (selAvoided != null) {
			avoidedNodes = selAvoided;

		} else {

		}

		if (graph.nodes.containsValue(selStartPoint) && graph.nodes.containsValue(selEndPoint)) {
			// dijkstra();
			if (!WayPoints.isEmpty()) {

				// Prepending and Appending waypoints with start and end nodes.
				WayPoints.add(0, startPoint);
				WayPoints.add(WayPoints.size(), endPoint);

				while (WayPoints.size() > 1) {

					startPoint = WayPoints.get(0);
					endPoint = WayPoints.get(1);
					edsger(startPoint, endPoint, avoidedNodes);
					WayPoints.remove(0);

				}

			} else {

				edsger(selStartPoint, selEndPoint, avoidedNodes);
			}
			BigDecimal sum = BigDecimal.ZERO;
			for (int i = 0; i < shortestRoute.size() - 1; i++) {
				Node node = shortestRoute.get(i);
				Node next = shortestRoute.get(i + 1);
				List<Way> a = node.ways.stream()
						.filter((x) -> x.nds.get(0).equals(node) ? x.nds.get(x.nds.size() - 1).equals(next)
								: x.nds.get(0).equals(next))
						.collect(Collectors.toList());
				if(a.isEmpty())
					continue;
				if (Main.quickest)
					sum = sum.add(a.get(0).speed());
				else
					sum = sum.add(a.get(0).length());
			}
			Main.shortestDist = sum;
		}
	}

	/**
	 * Gets the graph.
	 *
	 * @param graph the graph
	 * @return the graph
	 */
	public Graph getGraph(Graph graph) {
		return graph;
	}

	// ESTABLISHING SHORTEST PATH TO POINT FROM START
	/*
	 * public void dijkstra() {
	 * 
	 * distances = new HashMap<Node, Double>(); ArrayList <Node> priorityQueue = new
	 * ArrayList<Node>(); ArrayList <Node> nodesVisited = new ArrayList<Node>();
	 * ArrayList <Node> unvisited = new ArrayList<Node>(); shortestRoute = new
	 * ArrayList<Node>(); for(Way way : currentNode.ways) { Node dest =
	 * way.nds.get(way.nds.size()-1); if(!unvisited.contains(dest) &&
	 * !nodesVisited.contains(dest) && !priorityQueue.contains(dest)) {
	 * unvisited.add(dest); distances.put(dest, Double.POSITIVE_INFINITY); } }
	 * 
	 * distances.put(startPoint, (double) 0); priorityQueue.add(startPoint);
	 * 
	 * while (!unvisited.isEmpty()) {
	 * 
	 * for (Graph.Way way : currentNode.ways) {
	 * 
	 * if (way.nds.get(0) != currentNode) { if
	 * (nodesVisited.contains(way.nds.get(0))) continue;
	 * Collections.reverse(way.nds); }
	 * 
	 * Node dest = way.nds.get(way.nds.size()-1); if(!unvisited.contains(dest) &&
	 * !nodesVisited.contains(dest) && !priorityQueue.contains(dest)) {
	 * unvisited.add(dest); distances.put(dest, Double.POSITIVE_INFINITY); }
	 * 
	 * priorityQueue.add(dest);
	 * 
	 * // Removing midpoints // if (way.nds.size() > 2) { // for (int i = 0; i <
	 * way.nds.size(); i++) { // Node removalNode = way.nds.get(i); // if
	 * (removalNode != way.nds.get(0) || removalNode != dest) { //
	 * unvisited.remove(removalNode); // } // } // } // if (way.length() <
	 * distances.get(dest)) { // distances.put(dest, distances.get(currentNode) +
	 * way.length()); // System.out.println("THE DISTANCE FROM START: " +
	 * distances.get(dest)); // } priorityQueue.remove(currentNode);
	 * 
	 * }
	 * 
	 * priorityQueue.remove(currentNode); nodesVisited.add(currentNode);
	 * unvisited.remove(currentNode); currentNode = priorityQueue.get(0); }
	 * 
	 * 
	 * 
	 * // Main.shortestDist = distances.get(endPoint);
	 * 
	 * traceBack(); }
	 */

	/*
	 * // TRACING BACK THE SHORTEST PATH FROM THE ENDPOINT public void traceBack() {
	 * currentNode = endPoint;
	 * 
	 * Node nextNode = null; double shortestCurrentWayDist = -1; while
	 * (!shortestRoute.contains(startPoint)) {
	 * 
	 * 
	 * for (Graph.Way way : currentNode.ways) { Node dest = way.nds.get(0); if
	 * (shortestCurrentWayDist == -1.0) {
	 * 
	 * if(!(distances.get(dest)==null)) { shortestCurrentWayDist =
	 * distances.get(dest);
	 * 
	 * nextNode = dest; }
	 * 
	 * } else if
	 * ((distances.get(dest)==null?Double.POSITIVE_INFINITY:distances.get(dest)) <
	 * shortestCurrentWayDist) {
	 * 
	 * shortestCurrentWayDist = distances.get(way.nds.get(0)); nextNode = dest;
	 * 
	 * } } shortestRoute.add(currentNode); currentNode = nextNode;
	 * 
	 * } double sum=0; for(int i = 0; i<shortestRoute.size()-1;i++) { Node node =
	 * (Node) shortestRoute.get(i); Node next = (Node) shortestRoute.get(i+1);
	 * List<Way> a =
	 * node.ways.stream().filter((x)->x.nds.get(0).equals(node)?x.nds.get(x.nds.size
	 * ()-1).equals(next):x.nds.get(0).equals(next)).collect(Collectors.toList());
	 * // sum+=a.get(0).length(); } // Main.shortestDist = sum;
	 * 
	 * }
	 * 
	 */

	/**
	 * Dijkstra implementation using Priority queue, memory efficient UCS-style.
	 *
	 * @param source the source
	 * @param dest the dest
	 * @param avoidances the avoidances
	 */
	public void edsger(Node source, Node dest, ArrayList<Node> avoidances) {
		HashMap<Node, Node> prev = new HashMap<Node, Node>();
		HashMap<Node, BigDecimal> dist = new HashMap<Node, BigDecimal>();
		PriorityQueue<Node> queue = new PriorityQueue<Node>(11, (a, b) -> {
			if (dist.get(a) == null)
				dist.put(a, Main.INFINITY);
			if (dist.get(b) == null)
				dist.put(b, Main.INFINITY);
			return dist.get(a).compareTo(dist.get(b));
		});
		ArrayList<Node> visited = new ArrayList<Node>();
		Node temp;
		dist.put(source, BigDecimal.ZERO);
		queue.add(source);
		while (!queue.isEmpty()) {
			temp = queue.poll();
			if (temp.equals(dest))
				break;
			for (Way way : temp.ways) {
				Node stop = way.nds.get(0) == temp ? way.nds.get(way.nds.size() - 1) : way.nds.get(0);
				if (!visited.contains(stop) && !avoidances.contains(stop)) {
					queue.add(stop);
					visited.add(stop);
				}
				if (dist.get(stop) == null)
					dist.put(stop, Main.INFINITY);
				if ((dist.get(temp).add(Main.quickest ? way.speed() : way.length())).compareTo(dist.get(stop)) < 0) {
					dist.put(stop, dist.get(temp).add(Main.quickest ? way.speed() : way.length()));
					prev.put(stop, temp);
				}
			}
		}
		ArrayList<Node> path = new ArrayList<Node>();
		temp = dest;
		while (!(prev.get(temp) == null)) {
			path.add(temp);
			temp = prev.get(temp);
		}
		path.add(temp);
		if (!shortestRoute.isEmpty())
			path.addAll(shortestRoute.subList(1, shortestRoute.size()));
		shortestRoute = path;
	}

}
