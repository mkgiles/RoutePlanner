package routes;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
	HashMap<String,Node> nodes;
	HashMap<String,Way> ways;
	HashMap<String,Relation> relations;
	
	public interface Member {
		public void tag(String k, String v);
	}

	public class Way implements Member{
		String name;
		Boolean oneWay = false;
		String highway;
		String ref;
		String maxspeed = "50 mph";
		ArrayList<Node> nds;

		public Way() {
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
				nds.add(nodes.get(id));
			}catch(Exception e) {
				System.err.println(e + ":" + id);
			}
		}

	}

	public class Node implements Member{
		double lat, lon;

		public Node(String lat, String lon) {
			// TODO Auto-generated constructor stub
			this.lat = Double.parseDouble(lat);
			this.lon = Double.parseDouble(lon);
		}
		
		@Override
		public void tag(String key, String value) {
		}

	}
	
	public class Relation implements Member{
		ArrayList<Member> members;
		
		@Override
		public void tag(String k, String v) {
			
		}
		public Relation(){
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
