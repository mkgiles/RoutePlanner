package routes;

import java.util.ArrayList;
import routes.Graph.Node;

public class DJK {

	Node startPoint;
	Node endPoint;
	Graph graph;
	
	public DJK(Graph graph)
	{
		
		this.graph = getGraph(graph);
		
	}
	
	public void DJKSEARCH(Node selStartPoint, Node selEndPoint) 
	{
		System.out.println("DJK NODES ASSIGNED");
		this.startPoint = selStartPoint;
		this. endPoint = selEndPoint;
		dijkstra();
	}
	
	
	public Graph getGraph(Graph graph) 
	{
		return graph;
	}
	
	public void dijkstra() 
	{
		System.out.println(startPoint);
		System.out.println(endPoint);
		
		System.out.println(graph);
		/*
		for (String key : graph.nodes.keySet()) {
		    System.out.println("Key = " + key);
		}
		*/
	}
}
