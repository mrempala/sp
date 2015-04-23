package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class VcPtMenu extends VcMainController implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@FXML public void Load(ActionEvent event) {
		(new BBLoad(currentStage)).click();
	}

	@FXML public void SaveAs(ActionEvent event) {
		(new BBSaveAS(sequence, currentStage)).click();
	}

	@FXML public void Exit(ActionEvent event) {
		(new BBExit()).click();
	}
	
	@FXML public void showAboutWindow(ActionEvent event) {
		new VcAboutWindow();
	}
	
	

}