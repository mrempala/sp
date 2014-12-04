package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class UniverseLayoutController implements Initializable {

	@FXML Pane sceneContainer;
	@FXML AnchorPane squibsToPlace;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
        Group g = new Group();
        g.setBlendMode(BlendMode.SRC_OVER);

        int x = 20, y = 20, xt = x+2, yt = y+12;
        for (int j = 0; j < 5; j++){
	        for (int i = 0; i < 8; i++){
	        	Rectangle r = new Rectangle();
	            r.setX(x);
	            r.setY(y);
	            r.setWidth(10);
	            r.setHeight(15);
	            r.setStroke(Color.BLACK);
	            r.setFill(null);
	            
	            Text t = new Text();
	            t.setFill(Color.BLACK);
	            t.setX(xt);
	            t.setY(yt);
	            t.setText(String.valueOf(i));
	            g.getChildren().add(r);
	            g.getChildren().add(t);
	            
	            x += 10;
	            xt = x+2;
	        }
	        Text id = new Text();
	        id.setFill(Color.BLACK);
	        id.setX(x-50);
	        id.setY(y+25);
	        id.setText("1-" + String.valueOf(j));
	        g.getChildren().add(id);
	        x += 20;
	        y += 20;
	        xt = x + 2;
            yt = y + 12;
        }
        
        sceneContainer.getChildren().add(g);
        
        Group f = new Group();
        x = 10;
        y = 10;
        xt = x + 2;
        yt = y + 12;
        for (int i = 0; i < 8; i++){
        	Rectangle r = new Rectangle();
            r.setX(x);
            r.setY(y);
            r.setWidth(10);
            r.setHeight(15);
            r.setStroke(Color.BLACK);
            r.setFill(null);
            
            Text t = new Text();
            t.setFill(Color.BLACK);
            t.setX(xt);
            t.setY(yt);
            t.setText(String.valueOf(i));
            f.getChildren().add(r);
            f.getChildren().add(t);
            
            x += 10;
            xt = x+2;
        }
        
        Text id = new Text();
        id.setFill(Color.BLACK);
        id.setX(x - 50);
        id.setY(y + 25);
        id.setText("1-5");

        f.getChildren().add(id);
        
        squibsToPlace.getChildren().add(f);
	}
	
	@FXML 
	protected void openSequenceEditor(ActionEvent event) throws IOException{
		Parent root;
    	// Load the next window
        root = FXMLLoader.load(getClass().getResource("Views/UI-Universe-Visual-Layout.fxml"));
        Scene scene = new Scene(root, 1000, 500);
        Stage stage = new Stage();
        stage.setTitle("New Project - Details");
        stage.setScene(scene);
        stage.show();
        
        // Close the current window
        // get a handle to the stage
        //Stage currentstage = (Stage) button_openVisualOrganizer.getScene().getWindow();
        // and close it
        //currentstage.close();
	}
}