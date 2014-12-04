package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class MainController implements Initializable {

	@FXML Button button_newproject;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Load the next window
		Parent root;
        try {
			root = FXMLLoader.load(getClass().getResource("Views/UI-New-Project.fxml"));
			Scene scene = new Scene(root, 500, 300);
			Stage stage = new Stage();
			stage.setTitle("New Project");
			stage.setScene(scene);
			stage.show();
        }
        catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}