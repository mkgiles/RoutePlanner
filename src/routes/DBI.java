package routes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class DBI {
	public class Handler implements ContentHandler {

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

		}

		@Override
		public void endElement(String arg0, String arg1, String arg2) throws SAXException {
			// TODO Auto-generated method stub

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

		}

		@Override
		public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
			// TODO Auto-generated method stub
			System.out.print(arg2 + ": ");
			for(int i = 0; i<arg3.getLength(); i++) {
				System.out.print(arg3.getQName(i) + "=" +arg3.getValue(i) + ", ");
			}
			System.out.print("\n");
		}

		@Override
		public void startPrefixMapping(String arg0, String arg1) throws SAXException {
			// TODO Auto-generated method stub

		}

	}
	PrintStream out = System.out;
	File file;
	SAXParser sax;
	XMLReader xml;

	public DBI() {
		// TODO Auto-generated constructor stub
	}
	public void loadFile(String filename) throws SAXException, FileNotFoundException, IOException, ParserConfigurationException {
		file = new File(filename);
		sax = SAXParserFactory.newDefaultInstance().newSAXParser();
		xml = sax.getXMLReader();
		xml.setContentHandler(new Handler());
		FileReader f = new FileReader(file);
		InputSource i = new InputSource(f);
		xml.parse(i);
	}

}
