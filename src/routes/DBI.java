package routes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
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

public class DBI extends Task<Void>{
	public class Handler implements ContentHandler {
		Stack<Member> stack;
		List<String> keys;
		Graph graph;

		public Handler() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
			// TODO Auto-generated method stub

		}

		@Override
		public void endDocument() throws SAXException {
			System.out.println("Finished Document");
			System.out.println("Node count: " + graph.nodes.size());
			System.out.println("Way count: " + graph.ways.size());
			System.out.println("Relation Count: " + graph.relations.size());
		}

		@Override
		public void endElement(String arg0, String arg1, String arg2) throws SAXException {
			// TODO Auto-generated method stub
			stack.pop();

		}

		@Override
		public void endPrefixMapping(String arg0) throws SAXException {
			// TODO Auto-generated method stub

		}

		@Override
		public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
			// TODO Auto-generated method stub

		}

		@Override
		public void processingInstruction(String arg0, String arg1) throws SAXException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setDocumentLocator(Locator arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void skippedEntity(String arg0) throws SAXException {
			// TODO Auto-generated method stub

		}

		@Override
		public void startDocument() throws SAXException {
			System.out.println("Started Document");
			stack = new Stack<Member>();
			keys = new ArrayList<String>();
			graph = new Graph();

		}

		@Override
		public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
			// TODO Auto-generated method stub
			if(arg2.equals("node")) {
				Node node = graph.new Node(arg3.getValue("lat"), arg3.getValue("lon"));
				graph.node(arg3.getValue("id"),node);
				stack.add(node);
			}
			else if(arg2.equals("way")){
				Way way = graph.new Way();
				graph.way(arg3.getValue("id"),way);
				stack.add(way);
			}
			else if(arg2.equals("relation")) {
				Relation rel = graph.new Relation();
				graph.relation(arg3.getValue("id"),rel);
				stack.add(rel);
			}
			else {
				if(arg2.equals("tag")) {
					stack.peek().tag(arg3.getValue("k"), arg3.getValue("v"));
					if(stack.peek().getClass().equals(Relation.class)) {
						if(!keys.contains(arg3.getValue("k")))
							keys.add(arg3.getValue("k"));
					}
				}
				if(arg2.equals("member") && stack.peek().getClass()==Relation.class)
					((Relation) stack.peek()).member(arg3.getValue("ref"), arg3.getValue("type"));
				if(arg2.equals("nd") && stack.peek().getClass()==Way.class)
					((Way) stack.peek()).nd(arg3.getValue("ref"));
				stack.add(null);
			}
		}

		@Override
		public void startPrefixMapping(String arg0, String arg1) throws SAXException {
			// TODO Auto-generated method stub

		}

	}
	InputSource in;
	XMLReader xml;

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
	@Override
	protected Void call() throws Exception {
		xml.parse(in);
		return null;
	}

}
