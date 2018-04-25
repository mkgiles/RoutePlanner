package routes;

import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.Collections;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import routes.Graph.Node;

public class UIHandler {
	
	ArrayList<Node> shortestRouteExported;

	public Graph graph;

	@FXML
	private Button startSearch;
	@FXML
	private Button addAvoid;
	@FXML
	private Button clearFields;
	@FXML
	private Button dispPath;
	@FXML
	private ListView listViewAvoid;
	@FXML
	private ListView listViewPath;
	@FXML
	private TextField startLoc;
	@FXML
	private TextField destination;
	@FXML
	private TextField inputAvoid;
	@FXML
	private Label totalDist;

	@FXML
	public void addAvoidedArea() {
		String avoidName = inputAvoid.getText();
		if (!avoidName.isEmpty()) {
			listViewAvoid.getItems().add(avoidName);
			inputAvoid.clear();
		} else {
			System.out.println("Empty Input for Avoidance.");
		}
	}

	@FXML
	public void searchButton() throws InterruptedException, ExecutionException {
		String startName = startLoc.getText();
		String destName = destination.getText();
		if (!startName.isEmpty() && !destName.isEmpty()) {
			shortestRouteExported = null;
			listViewPath.getItems().clear();
			shortestRouteExported = Main.callDJK(startName,destName);
			Collections.reverse(shortestRouteExported);
			for(Node node : shortestRouteExported) 
			{
				listViewPath.getItems().add(node);
			}
		} else {
			System.out.println("Please fill in BOTH fields.");
		}

	}

	@FXML
	public void clearFields() {
		listViewAvoid.getItems().clear();
		listViewPath.getItems().clear();
		inputAvoid.clear();
		startLoc.clear();
		destination.clear();
	}

	@FXML
	public void dispPathType() {
		if(dispPath.getText() == "Path: Shortest") 
		{
			dispPath.setText("Path: Quickest");
		}
		else 
		{
			dispPath.setText("Path: Shortest");
		}
	}

}
