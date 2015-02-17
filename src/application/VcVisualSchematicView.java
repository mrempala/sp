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


public class VcVisualSchematicView implements Initializable {

	@FXML AnchorPane visualContainer;
	@FXML AnchorPane schematicContainer;
	
	public Universe universe;
	public Group universeSchematic;
	public Group firingSquibs;
	
	// TODO: Add integer values x & y for location to start drawing universe, hard coded at the moment

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		universeSchematic = new Group();
		firingSquibs = new Group();
		drawUniverseVisual();
	}
	
	public void setUniverse (Universe universe){
		this.universe = universe;
	}
	
	public void drawUniverseVisual(){
		//TODO: Hard coded drawing of a rough visual view, this needs
		//		to be updated once we figure out how to store the visual
		//		configuration of the world.
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
	}
	
	public void drawFiringSquib(TimeStep timestep, TimeStep previousTimestep){
		schematicContainer.getChildren().clear();
		firingSquibs.getChildren().clear();
		
		//Draw squibs to be fired
		for (Squib squib : timestep.squibList) {
			int x=50, y=50;
			
			Rectangle squibRectangle = new Rectangle();
	        squibRectangle.setX(x + 93 + (squib.getLunchbox() * 93) + (squib.getSquib()*10));
	        squibRectangle.setY(y + 7 + (squib.getFirebox() * 52));
	        squibRectangle.setWidth(10);
	        squibRectangle.setHeight(15);
	        squibRectangle.setStroke(Color.BLACK);

	        squibRectangle.getStyleClass().add("universe-firing");
	        
	        Text t = new Text();
	        t.setFill(Color.BLACK);
	        t.setX(x + 95 + (squib.getLunchbox() * 93) + (squib.getSquib() * 10));
	        t.setY(y + 19 + (squib.getFirebox() * 52));
	        t.setText("F");
	        //t.setText(Integer.toString(s.getSquib()));
	        firingSquibs.getChildren().add(squibRectangle);
	        firingSquibs.getChildren().add(t);
		}
		
		//Redraw previously drawn squibs to their old state
		if (previousTimestep != null){
			for (Squib squib : previousTimestep.squibList){
				int x=50, y=50;
				
				Rectangle squibRectangle = new Rectangle();
		        squibRectangle.setX(x + 93 + (squib.getLunchbox() * 93) + (squib.getSquib()*10));
		        squibRectangle.setY(y + 7 + (squib.getFirebox() * 52));
		        squibRectangle.setWidth(10);
		        squibRectangle.setHeight(15);
		        squibRectangle.setStroke(Color.BLACK);
		        
		        squibRectangle.getStyleClass().add("universe-green");
		        
		        Text t = new Text();
		        t.setFill(Color.BLACK);
		        t.setX(x + 95 + (squib.getLunchbox() * 93) + (squib.getSquib() * 10));
		        t.setY(y + 19 + (squib.getFirebox() * 52));
		        t.setText(Integer.toString(squib.getSquib()));
		        //t.setText(Integer.toString(s.getSquib()));
		        firingSquibs.getChildren().add(squibRectangle);
		        firingSquibs.getChildren().add(t);
			}
		}
		schematicContainer.getChildren().add(universeSchematic);
        schematicContainer.getChildren().add(firingSquibs);
	}
	
	public void drawUniverseSchematic(){
		// Clear any previously loaded universe schematic data
		schematicContainer.getChildren().clear();
		
		// x and y location to draw components, xt is used to reset x back to original value
		int x, y, xt;
		
		// Positions to start drawing
        x = 50;
        y = 50;
        xt = x;
		//Draw some schematic layout stuff
        
        boolean firstFirebox = true;
        for (Firebox fb : universe.fireboxList){
        	// Draw Firebox shape
        	if (!firstFirebox){
        		//Draw schematic connector between FB's
                Line l = new Line();
                l.setStrokeWidth(2);
                l.setStartX(x + 40);
                l.setStartY(y-20);
                l.setEndX(x + 40);
                l.setEndY(y - 1);
                
                Circle c = new Circle();
                c.setCenterX(x + 40);
                c.setCenterY(y - 1);
                c.setRadius(3);
                c.setFill(Color.BLACK);
                
                universeSchematic.getChildren().add(l);
                universeSchematic.getChildren().add(c);
        	}
        	firstFirebox = false;
            Rectangle r = new Rectangle();
            r.setWidth(80);
            r.setHeight(30);
            r.setX(x);
            r.setY(y);
            r.setStroke(Color.BLACK);
            r.getStyleClass().add("universe-green");
            
            Text fireboxText = new Text();
            fireboxText.setFill(Color.BLACK);
            fireboxText.setX(x + 15);
            fireboxText.setY(y + 20);
            fireboxText.setText("Firebox " + Integer.toString(fb.id));
            
            universeSchematic.getChildren().add(r);
            universeSchematic.getChildren().add(fireboxText);
            
            for (Lunchbox lb : fb.lunchboxList) {
            	// Draw Lunchboxes //
            	
            	// Draw lunchbox connecting wire
	            Line l2 = new Line();
	            l2.setStrokeWidth(2);
	            l2.setStartX(x + 80);
	            l2.setStartY(y + 14);
	            l2.setEndX(x + 90);
	            l2.setEndY(y + 14);
	            
	            //Draw connecting circle
	            Circle c2 = new Circle();
	            c2.setCenterX(x + 90);
	            c2.setCenterY(y + 14);
	            c2.setRadius(3);
	            c2.setFill(Color.BLACK);
	            
	            universeSchematic.getChildren().add(l2);
	            universeSchematic.getChildren().add(c2);
	            
	            int squibcount = 0;
	            for (Squib s : lb.squibList){
	            	// Draw the individual squibs in the lunchboxes
		        	Rectangle squibRectangle = new Rectangle();
		            squibRectangle.setX(x + 93);
		            squibRectangle.setY(y + 7);
		            squibRectangle.setWidth(10);
		            squibRectangle.setHeight(15);
		            squibRectangle.setStroke(Color.BLACK);
		            // TODO: Change color here when simulating firing if squib is dead
		            squibRectangle.getStyleClass().add("universe-green");
		            
		            Text t = new Text();
		            t.setFill(Color.BLACK);
		            t.setX(x + 95);
		            t.setY(y + 19);
		            t.setText(Integer.toString(s.getSquib()));
		            universeSchematic.getChildren().add(squibRectangle);
		            universeSchematic.getChildren().add(t);
		            
		            x += 10;
		            squibcount++;
	            }
	            
	            // Draw blank squibs to keep alignment easy to maintain
	            while (squibcount < 8){
	            	Rectangle squibRectangle = new Rectangle();
		            squibRectangle.setX(x + 93);
		            squibRectangle.setY(y + 7);
		            squibRectangle.setWidth(10);
		            squibRectangle.setHeight(15);
		            squibRectangle.setStroke(Color.BLACK);
		            squibRectangle.setFill(Color.TRANSPARENT);
		            
		            universeSchematic.getChildren().add(squibRectangle);
		            x += 10;
	            	squibcount++;
	            }
	            
	            // Draw lunchbox label x-x
	            Text id = new Text();
		        id.setFill(Color.WHITESMOKE);
		        // Some weird off setting to draw the label in the right location,
		        // essentially move cursor back to 0, and build offset from there.
		        id.setX((x-(squibcount*10)) + 85 + squibcount*5);
		        id.setY(y + 35);
		        id.setText(lb.getGrandParent() + "-" + lb.getId());
		        universeSchematic.getChildren().add(id);
		        
		        x += 13;
            }
        	x = xt;
        	
            y = y + 52;    
        }
        
        schematicContainer.getChildren().add(universeSchematic);
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