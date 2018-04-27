package routes;

import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import routes.Graph.Node;
import routes.Graph.Relation;

public class UIHandler {
	
	ArrayList<Node> shortestRouteExported;

	public Graph graph;

	@FXML
	private Button dropdownFieldButton;
	@FXML
	private Button genRouteButton;
	@FXML
	private Button addAvoid;
	@FXML
	private Button clearFields;
	@FXML
	private Button dispPath;
	@FXML
	private ListView<String> listViewAvoid;
	@FXML
	private ListView<Node> listViewPath;
	@FXML
	private TextField startLoc;
	@FXML
	private TextField destination;
	@FXML
	private TextField inputAvoid;
	@FXML
	private Label totalDist;
	
	@FXML
	private MenuButton startLocDrop;
	@FXML
	private MenuButton destDrop;
	
	
	private Node currentSelectionStart;
	
	private Node currentSelectionDest;
	
	
	@FXML
	public void fillDropdowns()
	{
		String startName = startLoc.getText();
		String destName = destination.getText();
		
		System.out.println(startName);
		System.out.println(destName);
		List<Node> nds = Main.graph.nodes.values().parallelStream().filter((x)->x.names.contains(startName)).collect(Collectors.toList());
		for(Node node : nds) {
			MenuItem mi = new MenuItem();
			mi.setText(node.toString());
			mi.setOnAction((x)->{});
			startLocDrop.getItems().add(mi);
		}
	}
	
	@FXML
	public void menuItem()
	{
		
	}
	
	
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
	public void genRoute() throws InterruptedException, ExecutionException {
		String startName = currentSelectionStart.id;
		String destName = currentSelectionDest.id;
		if (!startName.isEmpty() && !destName.isEmpty()) {
			shortestRouteExported = null;
			listViewPath.getItems().clear();
			shortestRouteExported = Main.callDJK(startName,destName);
			if(shortestRouteExported != null) {
			Collections.reverse(shortestRouteExported);
			for(Node node : shortestRouteExported) 
			{
				
				listViewPath.getItems().add(node);
			}
			totalDist.setText(Double.toString(Main.shortestDist));
		} else {
			System.out.println("Please fill in BOTH fields.");
		}
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
