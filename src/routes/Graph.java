package routes;

import java.util.ArrayList;
import java.util.HashMap;
public class Graph {
	private final double R = 6371e3;
	public HashMap<String,Node> nodes;
	public HashMap<String,Way> ways;
	public HashMap<String,Relation> relations;
	
	public interface Member {
		public void tag(String k, String v);
	}

	public class Way implements Member{
		String id;
		String name;
		Boolean oneWay = false;
		String highway;
		String ref;
		String maxspeed = "50 mph";
		ArrayList<Node> nds;

		public Way(String id) {
			this.id = id;
			nds = new ArrayList<Node>();
		}

		@Override
		public void tag(String name, String value) {
			switch(name) {
			case "oneway":
				oneWay=value.equals("yes");
				break;
			case "highway":
				highway = value;
				break;
			case "ref":
				ref = value;
				break;
			case "maxspeed":
				maxspeed = value;
				break;
			default:
				break;
			}
		}
		
		public void nd(String id) {
			try{
				if(nds.isEmpty())
					nodes.get(id).start(this);
				nds.add(nodes.get(id));
			}catch(Exception e) {
				System.err.println(e + ":" + id);
			}
		}
		
		public double length() {
			double sum = 0;
			for(int i=0;i<nds.size()-1;i++) {
				Node x = nds.get(i);
				Node y = nds.get(i+1);
				double df = Math.toRadians(y.lat-x.lat);
				double dl = Math.toRadians(y.lon-x.lon);
				double a = Math.pow(Math.sin(df/2),2) + Math.cos(Math.toRadians(x.lat)) * Math.cos(Math.toRadians(y.lat)) * Math.pow(Math.sin(dl/2), 2);
				double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
				sum+= R*c;
			}
			return sum;
		}
		
		@Override public String toString(){
			return	this.id + " (" + this.length() + "): " + this.nds.get(0) + " -> " + this.nds.get(this.nds.size()-1);
		}

	}

	public class Node implements Member{
		String id;
		double lat, lon;
		ArrayList<Way> ways;

		public Node(String id, String lat, String lon) {
			this.id = id;
			this.lat = Double.parseDouble(lat);
			this.lon = Double.parseDouble(lon);
			this.ways = new ArrayList<Way>();
		}
		
		@Override
		public void tag(String key, String value) {
		}
		
		public void start(Way way) {
			ways.add(way);
		}
		@Override
		public String toString() {
			return this.id + ": " + this.lat + "," + this.lon;
		}
	}
	
	public class Relation implements Member{
		String id;
		ArrayList<Member> members;
		
		@Override
		public void tag(String k, String v) {
			
		}
		public Relation(String id){
			this.id=id;
			members = new ArrayList<Member>();
		}
		public void member(String id, String type) {
			switch(type) {
			case "node":
				members.add(nodes.get(id));
				break;
			case "way":
				members.add(ways.get(id));
				break;
			case "relation":
				members.add(relations.get(id));
				break;
			default:
				break;
			}
		}
		
	}

	public Graph() {
		nodes = new HashMap<String,Node>();
		ways = new HashMap<String,Way>();
		relations = new HashMap<String,Relation>();
	}
	public void node(String id, Node node) {
		nodes.put(id, node);
	}
	public void way(String id, Way way) {
		ways.put(id, way);
	}
	public void relation(String id, Relation relation) {
		relations.put(id, relation);
	}

}
