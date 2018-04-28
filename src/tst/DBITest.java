package tst;
import routes.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Application;
import javafx.stage.Stage;

class DBITest {
	DBI dbi;
	Graph graph;
	
	public static class AsNonApp extends Application {
	    @Override
	    public void start(Stage primaryStage) throws Exception {
	    }
	}

	@BeforeAll
	public static void initJFX() {
	    Thread t = new Thread("JavaFX Init Thread") {
	        public void run() {
	            Application.launch(AsNonApp.class, new String[0]);
	        }
	    };
	    t.setDaemon(true);
	    t.start();
	}

	@BeforeEach
	void setUp() throws Exception {
		dbi = new DBI("test.osm");
		graph = null;
	}

	@Test
	void testCall() {
		dbi.run();
		try {
			graph = dbi.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		assertArrayEquals(new String[]{"0", "1", "2", "3", "4", "5", "6", "7"},graph.nodes.keySet().toArray());
		for(Entry<String,Graph.Node> e : graph.nodes.entrySet())
			assertNotNull(e.getValue());
	}

}
