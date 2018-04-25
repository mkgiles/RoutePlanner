package routes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class UIHandler {

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
	public void searchButton() {
		String startName = startLoc.getText();
		String destName = destination.getText();
		if (!startName.isEmpty() && !destName.isEmpty()) {
			//callDJK(startName,destName);
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
