package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.awt.MouseInfo;
import java.awt.Point;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class VcPtVisualSchematicView implements Initializable
{

	@FXML AnchorPane visualContainer;
	@FXML AnchorPane schematicContainer;
	
	public Universe universe;
	public Group universeSchematic;
	public Group universeVisual;
	public Group firingSquibs;

	public int numSelected = 0; // number of currently selected squibs
	
	public Boolean clickable = true;
	public Boolean leftHeld = false; // whether or not to be drawin the selected box
	
	MousePosition mouseInfo = new MousePosition();
	
	// TODO: Add integer values x & y for location to start drawing universe, hard coded at the moment

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		universeSchematic = new Group();
		universeVisual = new Group();
		firingSquibs = new Group();
	}
	
	public void setUniverse (Universe universe)
	{
		this.universe = universe;
	}
	
	public void drawUniverseVisual()
	{
		
		visualContainer.getChildren().clear();
		
		// the origin position for drawing some of the elements off of
		//double originX = 20 + mouseInfo.offX();
		//double originY = 20 + mouseInfo.offY();
		
		// reset button to reset all squibs to the default schematic view layout
		Button resetB = new Button("Default");
		//resetB.setX(10);
		//resetB.setY(visualContainer.getHeight() - 30);
		
		// origin button to reset window to origin
		//Button originB = new Button("Origin");
		//originB.setX(10);
		//originB.setY(visualContainer.getHeight() - 70);
		
		// deselect button deselects all squibs
		Button deselectB = new Button("Deselect All");
		//deselectB.setX(10);
		//deselectB.setY(visualContainer.getHeight() - 55);
   	
		VBox vertBox = new VBox(4);
		
		vertBox.getChildren().addAll(resetB, deselectB);
		
       // draw each squib in its correct location
       for (Firebox fb : universe.getFireboxList())
       {
           for (Lunchbox lb : fb.getLunchboxList())
           {
	            for (Squib s : lb.getSquibList())
	            {
	            	// create a rectangle to represent the squib
	            	Rectangle r1 = new Rectangle();
	        		r1.setX(s.getXPos());
	        		r1.setY(s.getYPos());
	                r1.setWidth(10);
	                r1.setHeight(15);
	                r1.setFill(Color.GREY);
	                
	                if(s.getSelected() == 1)
	                {
	                	r1.setStroke(Color.BLUE);
	                }
	                else
	                {
	                	r1.setStroke(Color.BLACK);
	                }
	                
	                
	               
	                universeVisual.getChildren().add(r1);
	               
	                // squib becomes selected when clicked on so it can be moved
	        		r1.setOnMouseClicked(new EventHandler<MouseEvent>()
   				{
   				    @Override
   				    public void handle(MouseEvent t)
   				    {
   				    	if (!clickable)
   				    	{
   				    		return;
   				    	}
   				    	
				    		mouseInfo.start = true;

				    		s.setSelected(1);
				    		numSelected++;
				    		
				    		// temp
				    		System.out.println("Selected squib info: f" + fb.getId() + " l" + lb.getId() + " s" + s.getSquib());

				    		drawUniverseVisual();
   				    }
   				});
	        		
	            }
           }
       }
      
       // draw selection box if user is holding down left button
       if(leftHeld == true)
       {
       		Rectangle selectBox = new Rectangle();
       		selectBox.setX(mouseInfo.getStartX());
			selectBox.setY(mouseInfo.getStartY());
	        selectBox.setWidth(mouseInfo.getDifX()); // need to confirm if it works for negative values
	        selectBox.setHeight(mouseInfo.getDifY());
	        selectBox.setStroke(Color.RED);
	        selectBox.setFill(Color.TRANSPARENT); // make the selection box transparent
	       
	        // add the selection box
	        universeVisual.getChildren().add(selectBox);
       }
      
       // add the reset, origin, and deselect buttons
       universeVisual.getChildren().add(vertBox);
      
		visualContainer.getChildren().add(universeVisual);
		
		// when reset button is clicked, reset all squibs to default positions
		// and deselect all squibs
		resetB.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
		    public void handle(MouseEvent t)
		    {
		    	if (!clickable)
		    	{
		    		return;
		    	}
		    	
		    	// reset squibs
		    	for (Firebox fb : universe.getFireboxList())
		        {
		            for (Lunchbox lb : fb.getLunchboxList())
		            {
			            for (Squib s : lb.getSquibList())
			            {
			            	s.setXPos(100 + (s.getLunchbox() * 100) + (12 * s.getSquib()));
			            	s.setYPos(50 + (s.getFirebox() * 50));
			            }
			        }
			    }
		    	
		    	// deselects all squibs
				for (Firebox fb : universe.getFireboxList())
				{
					for (Lunchbox lb : fb.getLunchboxList())
				    {
						for (Squib s : lb.getSquibList())
				        {
							s.setSelected(0);
				        }
				    }
				}
		    	
		    	numSelected = 0;
		    	
		    	// temp
		    	System.out.println("All Squibs Deselected!");
		    	
		    	resetB.fire();
		    	
            	universeVisual.getChildren().clear();
            	visualContainer.getChildren().clear();
            	
            	drawUniverseVisual();
		    }
		});

		/*
		// when origin button is clicked, set the visual back to the origin location
		originB.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
		    public void handle(MouseEvent t)
		    {
		    	if (!clickable)
		    	{
		    		return;
		    	}	    	

		    	originB.fire();
		    }
		});
		*/
		
		// for now, when r is clicked, it deselects the squibs
		deselectB.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
		    @Override
		    public void handle(MouseEvent t)
		    {
		    	if (!clickable)
		    	{
		    		return;
		    	}
		    	
		    	// deselects all squibs
				for (Firebox fb : universe.getFireboxList())
				{
					for (Lunchbox lb : fb.getLunchboxList())
				    {
						for (Squib s : lb.getSquibList())
				        {
							s.setSelected(0);
				        }
				    }
				}
		    	
		    	numSelected = 0;
		    	
		    	// temp
		    	System.out.println("All Squibs Deselected!");
		    	
		    	deselectB.fire();
		    }
		});

		// sets the start of a mouse drag
       visualContainer.setOnMouseClicked(new EventHandler<MouseEvent>()
   	{
           @Override
           public void handle(MouseEvent t)
           {
           	if (!clickable)
           	{
           		return;
           	}
				else if(t.getButton() == MouseButton.SECONDARY) // on right click
				{
					mouseInfo.start = true;
	       		}
	       		else
	       		{
	       			// get origin for potential selection box
	       			Point mPoint = MouseInfo.getPointerInfo().getLocation();
	       			mouseInfo.setStartX(mPoint.getX());
			        mouseInfo.setStartY(mPoint.getY());
			           
			        leftHeld = true;
	       		}
           	}
       });

		// Setup an event listener to detect when mouse has been dragged
       visualContainer.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
           @Override
           public void handle(MouseEvent t)
           {
           	Point mPoint = MouseInfo.getPointerInfo().getLocation();
           	
           	if (!clickable)
           	{
           		return;
           	} //t.getButton() == MouseEvent.BUTTON3 might be the correct way
           	else if(t.getButton() == MouseButton.SECONDARY) // on right click
            {
            	//Point mPoint = MouseInfo.getPointerInfo().getLocation();
            	
            	if(mouseInfo.start == true)
            	{
	            	mouseInfo.setStartX(mPoint.getX());
	            	mouseInfo.setStartY(mPoint.getY());
	            	
	            	mouseInfo.start = false;
            	}
            	else
            	{
	            	mouseInfo.setEndX(mPoint.getX());
	            	mouseInfo.setEndY(mPoint.getY());
	            	//mouseInfo.calcOffX();
	            	//mouseInfo.calcOffY();
	            	
	                for (Firebox fb : universe.getFireboxList())
	                {
	                    for (Lunchbox lb : fb.getLunchboxList())
	                    {
	        	            for (Squib s : lb.getSquibList())
	        	            {
	        	            	if(numSelected == 0 || s.getSelected() == 1)
	        	            	{	        	            		
				        			s.setXPos(s.getXPos() + mouseInfo.getDifX());
				        			s.setYPos(s.getYPos() + mouseInfo.getDifY());
	        	            	}
	        	            }
	                    }
	                }
        			
        			mouseInfo.setStartX(mPoint.getX());
	            	mouseInfo.setStartY(mPoint.getY());
	            	
        			//mouseInfo.clear();
            	}                 
            	
            	universeVisual.getChildren().clear();
            	visualContainer.getChildren().clear();
            	
            	drawUniverseVisual();
	            }
	            else // it is a left click and being dragged, get update position
	            {
	            	mouseInfo.setEndX(mPoint.getX());
		            mouseInfo.setEndY(mPoint.getY());
	            }
           }
       });
      
       visualContainer.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
           @Override
           public void handle(MouseEvent t)
           {
           	if (!clickable)
           	{
           		return;
           	}

				leftHeld = false;
				
           	mouseInfo.clear(); // test if this does anything
           }
       });

	}
	
	public void drawUniverseSchematic(){
		// Clear any previously loaded universe schematic data
		schematicContainer.getChildren().clear();
		
		// x and y location to draw components, xt is used to reset x back to original value
		int x, y, xt;
		
		// Positions to start drawing
       x = 50 + (int)mouseInfo.offX();
       y = 50 + (int)mouseInfo.offY();
       xt = x;
      
		//Draw some schematic layout stuff

       boolean firstFirebox = true;
       for (Firebox fb : universe.getFireboxList()){
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
           fireboxText.setText("Firebox " + Integer.toString(fb.getId()));
          
           universeSchematic.getChildren().add(r);
           universeSchematic.getChildren().add(fireboxText);
          
           for (Lunchbox lb : fb.getLunchboxList()) {
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
	            for (Squib s : lb.getSquibList()){
	            	// used to draw each squib in appropriate channel
	            	int cX = (s.getChannel() * 10) + x + 83;
	            	
	            	// Draw the individual squibs in the lunchboxes
		        	Rectangle squibRectangle = new Rectangle();
		            squibRectangle.setX(cX);
		            squibRectangle.setY(y + 7);
		            squibRectangle.setWidth(10);
		            squibRectangle.setHeight(15);
		            squibRectangle.setStroke(Color.BLACK);
		            // TODO: Change color here when simulating firing if squib is dead
		            squibRectangle.getStyleClass().add("universe-green");

		            Text t = new Text();
		            t.setFill(Color.BLACK);
		            t.setX(cX + 2);
		            t.setY(y + 19);
		            t.setText(Integer.toString(s.getSquib()));
		           
		            universeSchematic.getChildren().add(squibRectangle);
		            universeSchematic.getChildren().add(t);
		           
		            //x += 10;
		            squibcount++;
	            }
	           
	            // set squibcount to 0 to draw in all boxes
	            squibcount = 0;
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
      
    	// sets the start of a mouse drag
       schematicContainer.setOnMouseClicked(new EventHandler<MouseEvent>()
   	{
           @Override
           public void handle(MouseEvent t)
           {
           	if (!clickable)
           	{
           		return;
           	}
           	mouseInfo.start = true;     
           }
       });
      
		// Setup an event listener to detect when mouse has been dragged
       schematicContainer.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
           @Override
           public void handle(MouseEvent t)
           {
           	if (!clickable)
           	{
           		return;
           	}

           	Point mPoint = MouseInfo.getPointerInfo().getLocation();
           	
           	if(mouseInfo.start == true)
           	{
	            	mouseInfo.setStartX(mPoint.getX());
	            	mouseInfo.setStartY(mPoint.getY());
	            	
	            	mouseInfo.start = false;
           	}
           	else
           	{
	            	mouseInfo.setEndX(mPoint.getX());
	            	mouseInfo.setEndY(mPoint.getY());
	            	mouseInfo.calcOff();
	            	
	            	mouseInfo.start = true;
           	}
           	
           	//System.out.println("Mouse has been moved! ");
           	//System.out.println(mouseInfo.getEndX() + " " + mouseInfo.getEndY());
           	
           	universeSchematic.getChildren().clear();
           	schematicContainer.getChildren().clear();
           	
           	drawUniverseSchematic();
           }
       });
	}
	
	public void drawFiringSquib(TimeStep timestep, TimeStep previousTimestep){
		schematicContainer.getChildren().clear();
		firingSquibs.getChildren().clear();
		
		//Draw squibs to be fired
		for (Squib squib : timestep.getSquibList()) {
			int x = 50 + (int)mouseInfo.offX(), y = 50 + (int)mouseInfo.offY();
			
			Rectangle squibRectangle = new Rectangle();
	        squibRectangle.setX(x + 93 + (squib.getLunchbox() * 93) + ((squib.getChannel() - 1) * 10));
	        squibRectangle.setY(y + 7 + (squib.getFirebox() * 52));
	        squibRectangle.setWidth(10);
	        squibRectangle.setHeight(15);
	        squibRectangle.setStroke(Color.BLACK);

	        squibRectangle.getStyleClass().add("universe-firing");
	       
	        Text t = new Text();
	        t.setFill(Color.BLACK);
	        t.setX(x + 95 + (squib.getLunchbox() * 93) + ((squib.getChannel() - 1) * 10));
	        t.setY(y + 19 + (squib.getFirebox() * 52));
	        t.setText("F");
	        //t.setText(Integer.toString(s.getSquib()));
	        firingSquibs.getChildren().add(squibRectangle);
	        firingSquibs.getChildren().add(t);
		}
		
		//Redraw previously drawn squibs to their old state
		if (previousTimestep != null){
			for (Squib squib : previousTimestep.getSquibList()){
				int x = 50 + (int)mouseInfo.offX(), y = 50 + (int)mouseInfo.offY();
				
				Rectangle squibRectangle = new Rectangle();
		        squibRectangle.setX(x + 93 + (squib.getLunchbox() * 93) + ((squib.getChannel() - 1) * 10));
		        squibRectangle.setY(y + 7 + (squib.getFirebox() * 52));
		        squibRectangle.setWidth(10);
		        squibRectangle.setHeight(15);
		        squibRectangle.setStroke(Color.BLACK);
		       
		        squibRectangle.getStyleClass().add("universe-green");
		       
		        Text t = new Text();
		        t.setFill(Color.BLACK);
		        t.setX(x + 95 + (squib.getLunchbox() * 93) + ((squib.getChannel() - 1) * 10));
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
}