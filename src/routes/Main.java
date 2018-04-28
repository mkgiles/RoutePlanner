package routes;

import java.util.concurrent.ExecutionException;
import java.math.BigDecimal;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

import routes.Graph.Node;

// TODO: Auto-generated Javadoc
/**
 * The Main Class.
 */
public class Main extends Application {

	/** The graph. */
	private static Graph graph;
	
	/** The dbi. */
	private static DBI dbi;
	
	/** The shortest distance. */
	public static BigDecimal shortestDist = BigDecimal.valueOf(-1);
	
	/** Quickest or shortest route */
	public static boolean quickest = false;
	
	//BigDecimal doesn't have an equivalent of Double.POSITIVE_INFINITY so an arbitrarily high number was used for infinity.
	/** The Constant INFINITY. */
	public static final BigDecimal INFINITY = BigDecimal.valueOf(1000000000000000000L).scaleByPowerOfTen(1000000000);

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root, 600, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			dbi = new DBI("highways.osm");
			Thread t = new Thread(dbi);
			t.start();
			// for(Graph.Node node: graph.nodes.values()) {
			// System.out.println(node.id);
			// System.out.println("---------------");
			// for(Graph.Way way: node.ways) {
			// System.out.println(way);
			// }
			// System.out.println("---------------");
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Call DJK.
	 *
	 * @param start the start
	 * @param destination the destination
	 * @param wayPoints the way points
	 * @param avoidedNodes the avoided nodes
	 * @return the array list
	 * @throws InterruptedException the interrupted exception
	 * @throws ExecutionException the execution exception
	 */
	public static ArrayList<Node> callDJK(String start, String destination, ArrayList<Node> wayPoints,
			ArrayList<Node> avoidedNodes) throws InterruptedException, ExecutionException {
		DJK djk = new DJK(graph);
		djk.DJKSEARCH(graph.nodes.get(start), graph.nodes.get(destination), wayPoints, avoidedNodes);
		if (djk.shortestRoute != null) {
			return djk.shortestRoute;
		} else {
			ArrayList<Node> invalidRoute = null;
			return invalidRoute;
		}

	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Returns the graph, halting the thread to wait for the DBI if necessary.
	 *
	 * @return the graph
	 * @throws InterruptedException the interrupted exception
	 * @throws ExecutionException the execution exception
	 */
	public static Graph graph() throws InterruptedException, ExecutionException {
		if (graph == null)
			graph = dbi.get();
		return graph;
	}
}
