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
//import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class UniverseVisualController implements Initializable {

	@FXML AnchorPane visualContainer;
	@FXML AnchorPane schematicContainer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Draw some visual layout stuff
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
        
        visualContainer.getChildren().add(g);
        
        //Draw some schematic layout stuff
        Group f = new Group();
        
        x = 50;
        y = 50;
        xt = x;
        yt = y;
        
        // Draw Firebox shape
        Rectangle r = new Rectangle();
        r.setWidth(80);
        r.setHeight(30);
        r.setX(x);
        r.setY(y);
        r.setStroke(Color.BLACK);
        r.setFill(Color.GREEN);
        
        for (int k = 0; k < 4; k++){ 
	        for (int i = 0; i < 6; i++){
	        	// Draw 6 lunch boxes
	            Line l2 = new Line();
	            l2.setStrokeWidth(2);
	            l2.setStartX(x + 80);
	            l2.setStartY(y + 14);
	            l2.setEndX(x + 90);
	            l2.setEndY(y + 14);
	            
	            Circle c2 = new Circle();
	            c2.setCenterX(x + 90);
	            c2.setCenterY(y + 14);
	            c2.setRadius(3);
	            c2.setFill(Color.BLACK);
	            
	            f.getChildren().add(l2);
	            f.getChildren().add(c2);
	
		        for (int j = 0; j < 8; j++){
		        	// Draw individual lunch boxes
		        	Rectangle s = new Rectangle();
		            s.setX(x + 93);
		            s.setY(y + 7);
		            s.setWidth(10);
		            s.setHeight(15);
		            s.setStroke(Color.BLACK);
		            s.setFill(Color.GREEN);
		            
		            Text t = new Text();
		            t.setFill(Color.BLACK);
		            t.setX(x + 95);
		            t.setY(y + 19);
		            t.setText(String.valueOf(j));
		            f.getChildren().add(s);
		            f.getChildren().add(t);
		            
		            x += 10;
		        }
		        
		        Text id = new Text();
		        id.setFill(Color.BLACK);
		        id.setX(x + 45);
		        id.setY(y + 35);
		        id.setText(String.valueOf(k) + "-" + String.valueOf(i));
		        f.getChildren().add(id);
		        
		        x += 13;
	        } 
        	x = xt;
            y = yt;
            
            Line l = new Line();
            l.setStrokeWidth(2);
            l.setStartX(x + 40);
            l.setStartY(y + 30);
            l.setEndX(x + 40);
            l.setEndY(y + 50);
            
            Circle c = new Circle();
            c.setCenterX(x + 40);
            c.setCenterY(y + 50);
            c.setRadius(3);
            c.setFill(Color.BLACK);
            
            Rectangle r2 = new Rectangle();
            r2.setWidth(80);
            r2.setHeight(30);
            r2.setX(x);
            r2.setY(y + 52);
            r2.setStroke(Color.BLACK);
            r2.setFill(Color.GREEN);
            
            y = y + 52;
            yt = y;
            
            f.getChildren().add(r2);
            f.getChildren().add(l);
            f.getChildren().add(c);
        }
        
        f.getChildren().add(r);
        
        schematicContainer.getChildren().add(f);
        
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