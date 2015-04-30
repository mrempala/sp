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
		new VcWebWindow("About", "http://ericbskis.com/soap");
	}
	
	@FXML public void showUserManual(ActionEvent event) {
		new VcWebWindow("User Manual", "http://ericbskis.com/soap/?page_id=85");
	}
}