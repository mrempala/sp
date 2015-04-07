package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.awt.MouseInfo;
import java.awt.Point;

import javafx.scene.input.MouseEvent;

public class VcPtVisualSchematicView implements Initializable {

	@FXML AnchorPane visualContainer;
	@FXML AnchorPane schematicContainer;
	
	public Universe universe;
	public Group universeSchematic;
	public Group universeVisual;
	public Group firingSquibs;
	public Boolean clickable = true;
	public int moveLocked = 0; // 0 - not locked. 1 - everything moves together. 2 - only selected squibs can move
	
	MousePosition mouseInfo = new MousePosition();
	MousePosition squibMouseInfo = new MousePosition();
	
	// TODO: Add integer values x & y for location to start drawing universe, hard coded at the moment

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		universeSchematic = new Group();
		universeVisual = new Group();
		firingSquibs = new Group();
	}
	
	public void setUniverse (Universe universe){
		this.universe = universe;
	}
	
	public void drawUniverseVisual(){
		
		visualContainer.getChildren().clear();
		
		int originX = 20 + mouseInfo.offX();
		int originY = 20 + mouseInfo.offY();
		
		Rectangle r = new Rectangle();
		r.setX(originX - 15);
		r.setY(originY - 15);
        r.setWidth(10);
        r.setHeight(15);
        r.setStroke(Color.BLACK);
        r.setFill(Color.RED);
        
        // draw each squib in its correct location
        for (Firebox fb : universe.getFireboxList())
        {
            for (Lunchbox lb : fb.getLunchboxList())
            {
	            for (Squib s : lb.getSquibList())
	            {
	            	Rectangle r1 = new Rectangle();
	        		r1.setX(s.getXPos() + originX);
	        		r1.setY(s.getYPos() + originY);
	                r1.setWidth(10);
	                r1.setHeight(10);
	                r1.setStroke(Color.BLUE);
	                r1.setFill(Color.GREEN);
	                
	                universeVisual.getChildren().add(r1);
	                
	        		r1.setOnMouseClicked(new EventHandler<MouseEvent>()
    				{
    				    @Override
    				    public void handle(MouseEvent t)
    				    {
    				    	if (!clickable)
    				    	{
    				    		return;
    				    	}
    				    	
    				    	if (moveLocked == 0)
    				    	{
    				    		moveLocked = 2;
    				    		squibMouseInfo.start = true; 
    				    		
    				    		System.out.println("Only squibs can move now");
    				    		System.out.println("Squib info: f" + fb.getId() + " l" + lb.getId() + " s" + s.getSquib());
    				    	}

    				    }
    				}); 
	        		
	        		r1.setOnMouseDragged(new EventHandler<MouseEvent>()
    				{
    				    @Override
    				    public void handle(MouseEvent t)
    				    {
    				    	if (!clickable)
    				    	{
    				    		return;
    				    	}
    				    	
    				    	if (moveLocked == 2)
    				    	{
    				    		Point mPoint = MouseInfo.getPointerInfo().getLocation();
    			            	
    			            	if(squibMouseInfo.start == true)
    			            	{
    			            		squibMouseInfo.setStartX(mPoint.getX());
    			            		squibMouseInfo.setStartY(mPoint.getY());
    				            	
    			            		squibMouseInfo.start = false;
    			            	}
    			            	else
    			            	{
    			            		squibMouseInfo.setEndX(mPoint.getX());
    			            		squibMouseInfo.setEndY(mPoint.getY());
    			            		squibMouseInfo.calcOff();
    				            	
    			            		squibMouseInfo.start = true;
    			            	}
    			            	
    			            	universeVisual.getChildren().clear();
    			            	visualContainer.getChildren().clear();
    			            	
    			            	drawUniverseVisual();
    				    	}

    				    }
    				});
	        		
    				r1.setOnMouseReleased(new EventHandler<MouseEvent>()
            		{
                        @Override
                        public void handle(MouseEvent t)
                        {
                        	if (!clickable)
                        	{
                        		return;
                        	}

                    		moveLocked = 0;
                    		System.out.println("Lock released!");
                        }
                    });
	            }
            } 
        }

        universeVisual.getChildren().add(r);
        
		visualContainer.getChildren().add(universeVisual);
		
		r.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
		    @Override
		    public void handle(MouseEvent t)
		    {
		    	if (!clickable)
		    	{
		    		return;
		    	}
		
		    	System.out.println("Fake Squib!");
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

            	if(moveLocked == 0)
            	{
            		moveLocked = 1;
            		mouseInfo.start = true; 
            		System.out.println("Everything will now move together");
            	}
            }
        });
        
		// Setup an event listener to detect when mouse has been dragged
        visualContainer.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
            @Override
            public void handle(MouseEvent t)
            {
            	if (!clickable)
            	{
            		return;
            	}

            	if(moveLocked == 1)
            	{
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
	            	
	            	universeVisual.getChildren().clear();
	            	visualContainer.getChildren().clear();
	            	
	            	drawUniverseVisual();
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

            		moveLocked = 0;
            		System.out.println("Lock released!");
            }
        });
        
		/*
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
        */
	}
	
	public void drawUniverseSchematic(){
		// Clear any previously loaded universe schematic data
		schematicContainer.getChildren().clear();
		
		// x and y location to draw components, xt is used to reset x back to original value
		int x, y, xt;
		
		// Positions to start drawing
        x = 50 + mouseInfo.offX();
        y = 50 + mouseInfo.offY();
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
			int x = 50 + mouseInfo.offX(), y = 50 + mouseInfo.offY();
			
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
				int x = 50 + mouseInfo.offX(), y = 50 + mouseInfo.offY();
				
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