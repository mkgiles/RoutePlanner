package routes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javafx.concurrent.Task;
import routes.Graph.Member;
import routes.Graph.Node;
import routes.Graph.Relation;
import routes.Graph.Way;

// TODO: Auto-generated Javadoc
/**
 * The DBI Class.
 */
public class DBI extends Task<Graph> {
	
	/** The graph. */
	Graph graph;

	/**
	 * The Handler Class.
	 */
	public class Handler implements ContentHandler {
		
		/** The stack. */
		Stack<Member> stack;
		
		/** The keys. */
		List<String> keys;

		/**
		 * Instantiates a new handler.
		 */
		public Handler() {
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
		 */
		@Override
		public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
			// TODO Auto-generated method stub

		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#endDocument()
		 */
		@Override
		public void endDocument() throws SAXException {
			System.out.println("Finished Document");
			System.out.println("Node count: " + graph.nodes.size());
			System.out.println("Way count: " + graph.ways.size());
			System.out.println("Relation Count: " + graph.relations.size());
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public void endElement(String arg0, String arg1, String arg2) throws SAXException {
			// TODO Auto-generated method stub

			if (stack.peek() != null && stack.peek().getClass() == Way.class) {
				Way way = (Way) stack.peek();
				Boolean driveable;
				switch (way.highway) {
				case "pedestrian":
				case "track":
				case "bus_guideway":
				case "escape":
				case "raceway":
				case "road":
				case "footway":
				case "bridleway":
				case "steps":
				case "path":
				case "cycleway":
				case "proposed":
				case "construction":
					driveable = false;
				default:
					driveable = true;
				}
				if (driveable) {
					way.nds.get(way.nds.size() - 1).start(way);
					if (way.maxspeed == -1) {
						if (way.ref != null) {
							switch (way.ref.charAt(0)) {
							case 'M':
								way.maxspeed = 100;
								break;
							case 'N':
								way.maxspeed = 80;
								break;
							case 'R':
								way.maxspeed = 60;
								break;
							case 'L':
								way.maxspeed = 50;
							default:
								way.maxspeed = -1;
								break;
							}
						}
						if (way.maxspeed == -1) {
							switch (way.highway) {
							case "motorway":
							case "trunk":
								way.maxspeed = 100;
								break;
							case "primary":
								way.maxspeed = 80;
								break;
							case "secondary":
								way.maxspeed = 60;
								break;
							case "tertiary":
							case "unclassified":
							case "residential":
							case "service":
							default:
								way.maxspeed = 50;
							}
						}
					}
				} else
					graph.ways.remove(way.id);
			}
			if (stack.peek() != null && stack.peek().getClass() == Relation.class) {
				Relation rel = (Relation) stack.peek();
				if (rel.members.isEmpty() || rel.members.get(0) == null)
					graph.relations.remove(rel.id);
			}
			stack.pop();
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
		 */
		@Override
		public void endPrefixMapping(String arg0) throws SAXException {
			// TODO Auto-generated method stub

		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
		 */
		@Override
		public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
			// TODO Auto-generated method stub

		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
		 */
		@Override
		public void processingInstruction(String arg0, String arg1) throws SAXException {
			// TODO Auto-generated method stub

		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
		 */
		@Override
		public void setDocumentLocator(Locator arg0) {
			// TODO Auto-generated method stub

		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
		 */
		@Override
		public void skippedEntity(String arg0) throws SAXException {
			// TODO Auto-generated method stub

		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#startDocument()
		 */
		@Override
		public void startDocument() throws SAXException {
			System.out.println("Started Document");
			stack = new Stack<Member>();
			keys = new ArrayList<String>();
			graph = new Graph();

		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
			// TODO Auto-generated method stub
			if (arg2.equals("node")) {
				Node node = graph.new Node(arg3.getValue("id"), arg3.getValue("lat"), arg3.getValue("lon"));
				graph.node(arg3.getValue("id"), node);
				stack.add(node);
			} else if (arg2.equals("way")) {
				Way way = graph.new Way(arg3.getValue("id"));
				graph.way(arg3.getValue("id"), way);
				stack.add(way);
			} else if (arg2.equals("relation")) {
				Relation rel = graph.new Relation(arg3.getValue("id"));
				graph.relation(arg3.getValue("id"), rel);
				stack.add(rel);
			} else {
				if (arg2.equals("tag")) {
					stack.peek().tag(arg3.getValue("k"), arg3.getValue("v"));
					if (stack.peek().getClass().equals(Way.class)) {
						if (!keys.contains(arg3.getValue("k")))
							keys.add(arg3.getValue("k"));
					}
				}
				if (arg2.equals("member") && stack.peek().getClass() == Relation.class)
					((Relation) stack.peek()).member(arg3.getValue("ref"), arg3.getValue("type"));
				if (arg2.equals("nd") && stack.peek().getClass() == Way.class)
					((Way) stack.peek()).nd(arg3.getValue("ref"));
				stack.add(null);
			}
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
		 */
		@Override
		public void startPrefixMapping(String arg0, String arg1) throws SAXException {
			// TODO Auto-generated method stub

		}

	}

	/** The input stream. */
	InputSource in;
	
	/** The xml reader. */
	XMLReader xml;

	/**
	 * Instantiates a new dbi.
	 *
	 * @param filename the filename
	 */
	public DBI(String filename) {
		// TODO Auto-generated constructor stub
		try {
			xml = SAXParserFactory.newDefaultInstance().newSAXParser().getXMLReader();
		} catch (SAXException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xml.setContentHandler(new Handler());
		try {
			in = new InputSource(new FileReader(new File(filename)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see javafx.concurrent.Task#call()
	 */
	@Override
	protected Graph call() throws Exception {
		xml.parse(in);
		return graph;
	}

}
