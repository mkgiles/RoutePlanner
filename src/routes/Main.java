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

/**
 * The Main Class.
 */
public class Main extends Application {

	private static Graph graph;
	private static DBI dbi;
	public static BigDecimal shortestDist = BigDecimal.valueOf(-1);
	public static boolean quickest = false;
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
			dbi = new DBI("test.osm");
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

	public static ArrayList<Node> callDJK(String start, String destination, ArrayList<Node> wayPoints, ArrayList<Node> avoidedNodes) throws InterruptedException, ExecutionException {
		DJK djk = new DJK(graph);
		djk.DJKSEARCH(graph.nodes.get(start), graph.nodes.get(destination), wayPoints, avoidedNodes);
		if(djk.shortestRoute != null) {
		return djk.shortestRoute;
		}
		else 
		{
			 ArrayList <Node>  invalidRoute = null;
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
	public static Graph graph() throws InterruptedException, ExecutionException {
		if(graph == null)
			graph = dbi.get();
		return graph;
	}
}

