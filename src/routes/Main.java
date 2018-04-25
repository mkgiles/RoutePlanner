package routes;
	
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


/**
 * The Main Class.
 */
public class Main extends Application {

	private static Graph graph;
	private static DBI dbi;
	
	

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);

			primaryStage.show();

			dbi = new DBI("test.osm");

			Thread t = new Thread(dbi);
			t.start();
//			for(Graph.Node node: graph.nodes.values()) {
//				System.out.println(node.id);
//				System.out.println("---------------");
//				for(Graph.Way way: node.ways) {
//					System.out.println(way);
//				}
//				System.out.println("---------------");
//			}

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	
		
		

		
		
	}
	
	public static ArrayList callDJK(String start, String destination) throws InterruptedException, ExecutionException
	{
		graph = dbi.get();
		DJK djk = new DJK(graph);
		djk.DJKSEARCH(graph.nodes.get(start),graph.nodes.get(destination));
		return djk.shortestRoute;
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
}
