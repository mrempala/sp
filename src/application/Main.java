package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
//import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Views/UI-Sequence-Preview.fxml"));
        
        //Rectangle2D r = Screen.getPrimary().getBounds();
        
        stage.setTitle("E-Squib Controller");
        stage.setScene(new Scene(root,1000, 500));
        //stage.setMaximized(true);
        stage.show();
    }
    
    public static void main(String[] args) {    	
        Application.launch(Main.class, args);
    }

}