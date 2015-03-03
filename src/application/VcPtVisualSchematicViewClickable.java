package application;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class VcPtVisualSchematicViewClickable extends VcPtVisualSchematicView {
	
	// TODO: Figure out where this list of squibs should go, probably shouldn't stay here
	public ArrayList<Squib> selectedSquibs = new ArrayList<Squib>();
	public Boolean clickable = false;
	
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
                c.setRadius(1);
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
            
            // Setup an event listener to detect when this rectangle is clicked
            r.setOnMouseClicked(new EventHandler<MouseEvent>()
                    {
                        @Override
                        public void handle(MouseEvent t) {
                        	if (!clickable){
                        		return;
                        	}
                            r.setFill(Color.BLUE);
                            // Select each squib in the universe
                            for (Lunchbox lb : fb.lunchboxList){
                            	for (Squib s : lb.squibList){
                            		selectedSquibs.add(s);
                            	}
                            }
                            // Redraw the universe
                            drawUniverseSchematic();
                        }
                    });
            
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
	            	// used to draw each squib in appropriate channel
	            	int cX = (s.getChannel() * 10) + x + 83;
	            	
	            	// Draw the individual squibs in the lunchboxes
		        	Rectangle squibRectangle = new Rectangle();
		        	squibRectangle.setX(cX);
		        	//squibRectangle.setX(x + 93);
		            squibRectangle.setY(y + 7);
		            squibRectangle.setWidth(10);
		            squibRectangle.setHeight(15);
		            squibRectangle.setStroke(Color.BLACK);
		            // TODO: Change color here when simulating firing if squib is dead
		            squibRectangle.getStyleClass().add("universe-green");
		            if (selectedSquibs.contains(s)){
		            	squibRectangle.getStyleClass().add("universe-selected");
		            }

		            Text t = new Text();
		            t.setFill(Color.BLACK);
		            t.setX(cX + 2);
		            //t.setX(x + 95);
		            t.setY(y + 19);
		            t.setText(Integer.toString(s.getSquib()));
		            
		            // Setup an event listener to detect when this rectangle is clicked
		            t.setOnMouseClicked(new EventHandler<MouseEvent>() {
		                        @Override
		                        public void handle(MouseEvent t) {
		                        	if (!clickable){
		                        		return;
		                        	}
		                            if (selectedSquibs.contains(s)){
		                            	squibRectangle.getStyleClass().remove("universe-selected");
		                            	selectedSquibs.remove(s);
		                            }
		                            else {
		                            	squibRectangle.getStyleClass().add("universe-selected");
		                            	selectedSquibs.add(s);
		                            }
		                        }
		            });
		            
		            universeSchematic.getChildren().add(squibRectangle);
		            universeSchematic.getChildren().add(t);
		            
		            //x += 10;
		            squibcount++;
	            }  
	            
	            // set squibcount to 0 to draw in all boxes
	            squibcount = 0;
	            
	            // Draw blank squibs to keep alignment easy to maintain
	            while (squibcount < 8){
	            	// Check to see if the lunchbox already has a squib at the channel,
	            	// if no draw a blank squib to keep alignment
	            	if (!lb.hasSquibAtChannel(squibcount + 1)){
		            	Rectangle squibRectangle = new Rectangle();
			            squibRectangle.setX(x + 93);
			            squibRectangle.setY(y + 7);
			            squibRectangle.setWidth(10);
			            squibRectangle.setHeight(15);
			            squibRectangle.setStroke(Color.BLACK);
			            squibRectangle.setFill(Color.TRANSPARENT);
			            universeSchematic.getChildren().add(squibRectangle);   
	            	}

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
}