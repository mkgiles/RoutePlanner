package routes;

import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collection;
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
import routes.Graph.Way;

public class UIHandler {

	ArrayList<Node> shortestRouteExported;

	public Graph graph;

	@FXML
	private Button dropdownFieldButton;
	@FXML
	private Button genRouteButton;
	@FXML
	private Button searchAvoid;
	@FXML
	private Button searchWaypoint;
	@FXML
	private Button clearFields;
	@FXML
	private Button dispPath;
	@FXML
	private ListView<String> listViewAvoid;
	@FXML
	private ListView<String> listViewWaypoint;
	@FXML
	private ListView<Node> listViewPath;
	@FXML
	private TextField startLoc;
	@FXML
	private TextField destination;
	@FXML
	private TextField inputAvoid;
	@FXML
	private TextField inputWaypoint;
	@FXML
	private Label totalDist;

	@FXML
	private MenuButton startLocDrop;
	@FXML
	private MenuButton destDrop;

	@FXML
	private MenuButton avoidDrop;
	@FXML
	private MenuButton wayPointDrop;

	private Node currentSelectionStart;

	private Node currentSelectionDest;

	public ArrayList<Node> wayPoints = new ArrayList<Node>();
	public ArrayList<Node> avoidedNodes = new ArrayList<Node>();

	@FXML
	public void fillDropdowns() throws InterruptedException, ExecutionException {
		startLocDrop.getItems().clear();
		destDrop.getItems().clear();
		String startName = startLoc.getText();
		String destName = destination.getText();
		System.out.println(startName);
		System.out.println(destName);
		List<Node> snds = Main.graph().nodes.values().parallelStream().filter((x)->x.names.contains(startName)).collect(Collectors.toList());
		List<Node> dnds = Main.graph().nodes.values().parallelStream().filter((x)->x.names.contains(destName)).collect(Collectors.toList());
		snds.addAll((Collection<? extends Node>) Main.graph().ways.values().parallelStream().filter((x)->x.names.contains(startName)).map((x)->x.nds.get(0)).collect(Collectors.toList()));
		dnds.addAll((Collection<? extends Node>) Main.graph().ways.values().parallelStream().filter((x)->x.names.contains(destName)).map((x)->x.nds.get(0)).collect(Collectors.toList()));
		for(Relation rel : Main.graph().relations.values()) {
			if(!rel.names.contains(startName)) {
				if(!rel.names.contains(destName))
					continue;
				if(rel.members.get(0).getClass().equals(Node.class))
					dnds.add((Node) rel.members.get(0));
				if(rel.members.get(0).getClass().equals(Way.class))
					dnds.add(((Way) rel.members.get(0)).nds.get(0));
			}
			else {
				if(rel.members.get(0).getClass().equals(Node.class))
					snds.add((Node) rel.members.get(0));
				if(rel.members.get(0).getClass().equals(Way.class))
					snds.add(((Way) rel.members.get(0)).nds.get(0));
			}
		}
		snds.addAll((Collection<? extends Node>) Main.graph().ways.values().parallelStream().filter((x)->x.names.contains(startName)).map((x)->x.nds.get(0)).collect(Collectors.toList()));
		dnds.addAll((Collection<? extends Node>) Main.graph().ways.values().parallelStream().filter((x)->x.names.contains(destName)).map((x)->x.nds.get(0)).collect(Collectors.toList()));	
		for(Node node : snds) {
			MenuItem mi = new MenuItem();
			mi.setText(node.toString());
			mi.setOnAction((x) -> {
				currentSelectionStart = node;
				startLocDrop.setText(mi.getText());
			});
			startLocDrop.getItems().add(mi);
		}
		for (Node node : dnds) {
			MenuItem mi = new MenuItem();
			mi.setText(node.toString());
			mi.setOnAction((x) -> {
				currentSelectionDest = node;
				destDrop.setText(mi.getText());
			});
			destDrop.getItems().add(mi);
		}
	}

	@FXML
	public void fillAvoidance() throws InterruptedException, ExecutionException {
		avoidDrop.getItems().clear();
		String avoidName = inputAvoid.getText();
		List<Node> ands = Main.graph().nodes.values().parallelStream().filter((x) -> x.names.contains(avoidName))
				.collect(Collectors.toList());
		for(Relation rel : Main.graph().relations.values()) {
			if(!rel.names.contains(avoidName))
				continue;
			for(int i = 0; i<rel.members.size();i++) {
				if(rel.members.get(i).getClass().equals(Node.class))
					ands.add((Node) rel.members.get(0));
				if(rel.members.get(i).getClass().equals(Way.class))
					ands.add(((Way) rel.members.get(0)).nds.get(0));
			}
		}
		for(ArrayList<Node> a : Main.graph().ways.values().parallelStream().filter((x)->x.names.contains(avoidName)).map((x)->x.nds).collect(Collectors.toList()))
			ands.addAll(a);
		for (Node node : ands) {
			MenuItem mi = new MenuItem();
			mi.setText(node.toString());
			mi.setOnAction((x) -> {
				avoidedNodes.add(node);
				listViewAvoid.getItems().add(mi.getText());
				avoidDrop.setText(mi.getText());
			});
			avoidDrop.getItems().add(mi);

		}		
		inputAvoid.clear();
	}

	@FXML
	public void fillWaypoint() throws InterruptedException, ExecutionException {
		wayPointDrop.getItems().clear();
		String waypointName = inputWaypoint.getText();
		List<Node> wnds = Main.graph().nodes.values().parallelStream().filter((x) -> x.names.contains(waypointName))
				.collect(Collectors.toList());
		for(Relation rel : Main.graph().relations.values()) {
			if(!rel.names.contains(waypointName))
				continue;
			for(int i = 0; i<rel.members.size();i++) {
				if(rel.members.get(i).getClass().equals(Node.class))
					wnds.add((Node) rel.members.get(0));
				if(rel.members.get(i).getClass().equals(Way.class))
					wnds.add(((Way) rel.members.get(0)).nds.get(0));
			}
		}
		for(ArrayList<Node> a : Main.graph().ways.values().parallelStream().filter((x)->x.names.contains(waypointName)).map((x)->x.nds).collect(Collectors.toList()))
			wnds.addAll(a);
		for (Node node : wnds) {
			MenuItem mi = new MenuItem();
			mi.setText(node.toString());
			mi.setOnAction((x) -> {
				wayPoints.add(node);
				listViewWaypoint.getItems().add(mi.getText());
				wayPointDrop.setText(mi.getText());
			});
			wayPointDrop.getItems().add(mi);
		}
		inputWaypoint.clear();
	}

	@FXML
	public void genRoute() throws InterruptedException, ExecutionException {

		String startName = currentSelectionStart.id;
		String destName = currentSelectionDest.id;
		if (!startLocDrop.getText().equals("Starting Location") && !destDrop.getText().equals("Destination")) {
			shortestRouteExported = null;
			listViewPath.getItems().clear();
			shortestRouteExported = Main.callDJK(startName, destName, wayPoints, avoidedNodes);
			if (shortestRouteExported != null) {
				Collections.reverse(shortestRouteExported);
				for (Node node : shortestRouteExported) {

					listViewPath.getItems().add(node);
				}
				totalDist.setText((Main.quickest ? "Total Duration: " : "Total Distance: ")
						+ Main.shortestDist.toString().split("\\.")[0] + (Main.quickest ? "hrs." : "km."));
			} else {
				System.out.println("Please select two points from dropdown menu.");
			}
		}

	}

	@FXML
	public void clearFields() {
		listViewWaypoint.getItems().clear();
		listViewAvoid.getItems().clear();
		listViewPath.getItems().clear();
		inputWaypoint.clear();
		inputAvoid.clear();
		startLoc.clear();
		destination.clear();
	}

	@FXML
	public void dispPathType() {
		if (dispPath.getText().equals("Path: Shortest")) {
			dispPath.setText("Path: Quickest");
			Main.quickest = true;
		} else {
			dispPath.setText("Path: Shortest");
			Main.quickest = false;
		}
	}

}
