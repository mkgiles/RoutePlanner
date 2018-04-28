package routes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Graph Class.
 */
public class Graph {
	
	/** The Earth radius constant */
	private final double R = 6371;
	
	/** The nodes. */
	public HashMap<String, Node> nodes;
	
	/** The ways. */
	public HashMap<String, Way> ways;
	
	/** The relations. */
	public HashMap<String, Relation> relations;

	/**
	 * The Member Interface.
	 */
	public interface Member {
		
		/**
		 * Load in tags.
		 *
		 * @param k the k
		 * @param v the v
		 */
		public void tag(String k, String v);
		
		/**
		 * Keep track of parent relations.
		 *
		 * @param r the r
		 */
		public void relation(Relation r);
	}

	/**
	 * The Way Class.
	 */
	public class Way implements Member {
		
		/** The id. */
		String id;
		
		/** The list of all names. */
		ArrayList<String> names;
		
		/** Is street one way. */
		Boolean oneWay = false;
		
		/** The type of road. */
		String highway;
		
		/** The ref no. */
		String ref;
		
		/** The max speed. */
		int maxspeed = -1;
		
		/** The list of children nodes. */
		ArrayList<Node> nds;
		
		/** The parent relations. */
		ArrayList<Relation> relations;

		/**
		 * Instantiates a new way.
		 *
		 * @param id the id
		 */
		public Way(String id) {
			this.id = id;
			nds = new ArrayList<Node>();
			names = new ArrayList<String>();
			relations = new ArrayList<Relation>();
		}

		/* (non-Javadoc)
		 * @see routes.Graph.Member#tag(java.lang.String, java.lang.String)
		 */
		@Override
		public void tag(String name, String value) {
			String prefix = name.split(":")[0];
			switch (prefix) {
			case "name":
				names.add(value);
				break;
			case "oneway":
				oneWay = value.equals("yes");
				break;
			case "highway":
				highway = value;
				break;
			case "ref":
				ref = value;
				break;
			case "maxspeed":
				String s = value.split("\\s")[0];
				maxspeed = s.matches("^\\d+$") ? Integer.parseInt(s) : -1;
				break;
			default:
				break;
			}
		}
		
		/* (non-Javadoc)
		 * @see routes.Graph.Member#relation(routes.Graph.Relation)
		 */
		@Override public void relation(Relation r){
			this.relations.add(r);
		}

		/**
		 * Add a node to the node list.
		 *
		 * @param id the id
		 */
		public void nd(String id) {
			try {
				if (nds.isEmpty())
					nodes.get(id).start(this);
				nds.add(nodes.get(id));
			} catch (Exception e) {
				System.err.println(e + ":" + id);
			}
		}

		/**
		 * Calculate time taken to traverse way at max speed.
		 *
		 * @return the big decimal
		 */
		public BigDecimal speed() {
			BigDecimal spd;
			try {
				spd = length().divide(BigDecimal.valueOf(maxspeed));
			}catch(ArithmeticException e) {
				System.err.println(e);
				spd = length().divideToIntegralValue(BigDecimal.valueOf(maxspeed));
			}
			return spd;
		}

		/**
		 * Calculate the length of the way in km using the Spherical Law of Cosines.
		 *
		 * @return the big decimal
		 */
		public BigDecimal length() {
			BigDecimal sum = BigDecimal.ZERO;
			for (int i = 0; i < nds.size() - 1; i++) {
				Node x = nds.get(i);
				Node y = nds.get(i + 1);
				double f1 = Math.toRadians(x.lat);
				double f2 = Math.toRadians(y.lat);
				double dl = Math.toRadians(y.lon - x.lon);
				sum = sum.add(BigDecimal.valueOf(
						Math.acos(Math.sin(f1) * Math.sin(f2) + Math.cos(f1) * Math.cos(f2) * Math.cos(dl)) * R));
			}
			return sum;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.id + " (" + this.length() + "): " + this.nds.get(0) + " -> "
					+ this.nds.get(this.nds.size() - 1);
		}

	}

	/**
	 * The Node Class.
	 */
	public class Node implements Member {
		
		/** The id. */
		String id;
		
		/** The latitude and longitude. */
		double lat, lon;
		
		/** The ways. */
		ArrayList<Way> ways;
		
		/** The names. */
		ArrayList<String> names;
		
		/** The relations. */
		ArrayList<Relation> relations;

		/**
		 * Instantiates a new node.
		 *
		 * @param id the id
		 * @param lat the lat
		 * @param lon the lon
		 */
		public Node(String id, String lat, String lon) {
			this.id = id;
			this.lat = Double.parseDouble(lat);
			this.lon = Double.parseDouble(lon);
			this.ways = new ArrayList<Way>();
			this.names = new ArrayList<String>();
			this.relations = new ArrayList<Relation>();
		}

		/* (non-Javadoc)
		 * @see routes.Graph.Member#tag(java.lang.String, java.lang.String)
		 */
		@Override
		public void tag(String key, String value) {
			String prefix = key.split(":")[0];
			switch (prefix) {
			case "name":
				names.add(value);
				break;
			default:
				break;
			}
		}
		
		/* (non-Javadoc)
		 * @see routes.Graph.Member#relation(routes.Graph.Relation)
		 */
		@Override public void relation(Relation r){
			this.relations.add(r);
		}
		
		/**
		 * Add way to list of ways, signifying this node as a start point.
		 *
		 * @param way the way
		 */
		public void start(Way way) {
			ways.add(way);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			String str = "";
			if(this.names.isEmpty()) {
				for(Way way: this.ways)
					if(!way.names.isEmpty()) {
						str = way.names.get(0);
						break;
					}
				for(Relation rel: this.relations)
					if(!rel.names.isEmpty()) {
						str = rel.names.get(0);
						break;
					}
				if(str.equals(""))
					return id;
			}
			else
				str += this.names.get(0);
			return str + "(" + id + ")";
		}
	}

	/**
	 * The Relation Class.
	 */
	public class Relation implements Member {
		
		/** The id. */
		String id;
		
		/** The type. */
		String type = "";
		
		/** The names. */
		ArrayList<String> names;
		
		/** The members. */
		ArrayList<Member> members;
		
		/** The parent relations. */
		ArrayList<Relation> rels;
		
		/* (non-Javadoc)
		 * @see routes.Graph.Member#tag(java.lang.String, java.lang.String)
		 */
		@Override
		public void tag(String k, String v) {
			String prefix = k.split(":")[0];
			switch (prefix) {
			case "name":
				names.add(v);
				break;
			case "alt_name":
				names.add(v);
				break;
			case "official_name":
				names.add(v);
				break;
			case "type":
				type = v;
				break;
			default:
				break;
			}
		}

		/**
		 * Instantiates a new relation.
		 *
		 * @param id the id
		 */
		public Relation(String id) {
			this.id = id;
			members = new ArrayList<Member>();
			names = new ArrayList<String>();
			rels = new ArrayList<Relation>();
		}

		/**
		 * Adds a child member to list of members.
		 *
		 * @param id the id
		 * @param type the type
		 */
		public void member(String id, String type) {
			switch (type) {
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
			if(members.get(members.size()-1)!=null)
				members.get(members.size()-1).relation(this);
		}
		
		/* (non-Javadoc)
		 * @see routes.Graph.Member#relation(routes.Graph.Relation)
		 */
		@Override public void relation(Relation r){
			this.rels.add(r);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return type + " " + id + ": " + (names.isEmpty() ? "anonyme" : names.get(0))
					+ (members.isEmpty() ? "" : " [" + members.get(0) + "; " + members.get(members.size() - 1) + "]");
		}

	}

	/**
	 * Instantiates a new graph.
	 */
	public Graph() {
		nodes = new HashMap<String, Node>();
		ways = new HashMap<String, Way>();
		relations = new HashMap<String, Relation>();
	}

	/**
	 * Adds a Node.
	 *
	 * @param id the id
	 * @param node the node
	 */
	public void node(String id, Node node) {
		nodes.put(id, node);
	}

	/**
	 * Adds a Way.
	 *
	 * @param id the id
	 * @param way the way
	 */
	public void way(String id, Way way) {
		ways.put(id, way);
	}

	/**
	 * Adds a Relation.
	 *
	 * @param id the id
	 * @param relation the relation
	 */
	public void relation(String id, Relation relation) {
		relations.put(id, relation);
	}

}
