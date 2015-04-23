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
import javafx.geometry.Insets;

import java.awt.MouseInfo;
import java.awt.Point;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.lang.Math;

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
		        //selectBox.setFill(Color.TRANSPARENT); // make the selection box transparent
		       	selectBox.setFill(Color.BLUE); // temp
		     
				//temp
				System.out.println("s: x " + mouseInfo.getStartX() + mouseInfo.getEndX() + " y " + mouseInfo.getStartY() + mouseInfo.getEndX());
		       	System.out.println("drawing selection box");
		       	
		        // add the selection box
		        universeVisual.getChildren().add(selectBox);
	      }

    
	      // add the reset, origin, and deselect buttons
	      //universeVisual.getChildren().add(vertBox);
	   
			visualContainer.getChildren().add(universeVisual);

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
		       			// if ctrl is not being held, clear previous selection
			           	if(!t.isControlDown())
			           	{
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
			           	}
		           	
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
	          	}
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
	            	// temp
	            	System.out.println("updating drag selection...");
	            	
	            	mouseInfo.setEndX(mPoint.getX());
		            mouseInfo.setEndY(mPoint.getY());
		           
		            // might need to call drawUniverseVisual(); here
	            }
	          }
	      });
	   
	      visualContainer.setOnMouseReleased(new EventHandler<MouseEvent>()
			{
				double squibDifX;
				double squibDifY;
				
	          	@Override
	          	public void handle(MouseEvent t)
				{
	          	if (!clickable)
	          	{
	          		return;
	          	}

				if(leftHeld == true)
				{	
		    	    numSelected = 0;
		    	  
					// select all squibs within the selection box
					for (Firebox fb : universe.getFireboxList())
		            {
		                for (Lunchbox lb : fb.getLunchboxList())
		                {
		    	            for (Squib s : lb.getSquibList())
		    	            {
								squibDifX = s.getXPos() - mouseInfo.getStartX();
								squibDifY = s.getYPos() - mouseInfo.getStartY();
								
								// if x position is within range & y position is within range
								if(Math.abs(squibDifX) < Math.abs(mouseInfo.getDifX()) && Math.abs(squibDifY) < Math.abs(mouseInfo.getDifY()))
								{
									s.setSelected(1);
									numSelected++;
								}
		    	            }
		    	        }
		    	    }
		        	  
					leftHeld = false;
					
					drawUniverseVisual();
				}
	          }
	      });

		}
	
	public void drawUniverseSchematic(){

		schematicContainer.getChildren().clear();
		
		
		// x and y location to draw components, xt is used to reset x back to original value
		int x, y, xt;
		
		// Positions to start drawing
		x = 50 + (int)mouseInfo.offX();
		y = 50 + (int)mouseInfo.offY();
		xt = x;
  
		// reset button to reset view to default origin
		Button resetB = new Button("Reset");
		
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
      
         for (Lunchbox lb : fb.getLunchboxList())
         {
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
	           
	            for (Squib s : lb.getSquibList())
	            {
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
	
	// reset view to default (which is the origin)
    @FXML
    public void goOrigin() // probably need to move outside
    {	
    	mouseInfo.clear();
	    drawUniverseSchematic();
	}
    
	// when reset button is clicked, reset all squibs to default positions
	// and deselect all squibs
	@FXML
	public void resetPos() // might need to move this outside
	{   	
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
	    	
       	universeVisual.getChildren().clear();
       	visualContainer.getChildren().clear();
       	
       	drawUniverseVisual();
	}
	
	// for now, when r is clicked, it deselects the squibs
	@FXML
	public void deselect() // might need to move outside
	{   	
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

