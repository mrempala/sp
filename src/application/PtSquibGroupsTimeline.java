package application;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PtSquibGroupsTimeline {
	// Class used by VcPtTimeline, contains all the elements necessary to draw a timeline for
	// an individual group of squibs.
	
	public Pane timelinePane; // Pane to contain the timeline
	public Label timelineLabel;
	public Group timelineGroup; // Group to hold the timeline drawing
	public Group timelinePlayOverlay; // Group to hold the overlay when the given group is actually playing	
	public SquibGroup squibGroup;
	
	PtSquibGroupsTimeline(SquibGroup squibGroup){
		timelinePane = new Pane();
		timelineGroup = new Group();
		timelinePlayOverlay = new Group();
		timelineLabel = new Label();
		
		timelineLabel.setText(squibGroup.getGroupName());
		timelineLabel.setLayoutX(5);
		timelineLabel.setLayoutY(10);
		
		timelinePane.setPrefHeight(35);
		timelinePane.setPrefWidth(675);
		timelinePane.getStyleClass().add("TimelinePane");
		
		this.squibGroup = squibGroup;
	}
	
	// TimelinePlayOverlay is the semi-transparent group play time overlay to show when a given group is firing
	public void updateTimelinePlayOverlay(int totalNumTimesteps, int insertedTimesteps){
		squibGroup.getSquibPlayGroups().add(insertedTimesteps);
		timelinePlayOverlay.getChildren().clear();

		// Calculate the step size based on the pixel width of the timeline
		// and the total number of timesteps
		float stepSize = 665 / (float)totalNumTimesteps;
		// Set the start position to draw at (the timeline starts at 70, we'll use 71 to get spacing between groups)
		int x = 71;
		// Draw a rectangle for each period during which the group is playing an animation
		for (Integer i : squibGroup.getSquibPlayGroups()){
			if (i > 0){
				Rectangle squibGroup = new Rectangle();
				squibGroup.setX(x);
				squibGroup.setY(5);
				squibGroup.setHeight(25);
				// Make the squib group overlay 2px narrower to make them more distinguishable
				squibGroup.setWidth(i * stepSize - 3);
				squibGroup.getStyleClass().add("squib-group-overlay");
				timelinePlayOverlay.getChildren().add(squibGroup);
				x += (i*stepSize);
			}
			else {
				x += (-i*stepSize);
			}
			
		}
		
		// Add the newly created overlay group to the pane
		timelinePane.getChildren().add(timelinePlayOverlay);
	}
	
    public void drawTimeline(int length, float timelineLength, float stepSize, int mark){
    	float step = 70;
    	
    	// Timeline width: 600 height: 35
        timelinePane.getChildren().clear();
        timelineGroup.getChildren().clear();
        
    	System.out.println("Length: " + length);
    	
    	// Add the label to the timeline
		timelinePane.getChildren().add(timelineLabel);
    	
    	Rectangle background = new Rectangle();
    	background.setX(70);
    	background.setY(0);
    	background.setWidth(665);
        background.setHeight(35);
        background.setStroke(Color.BLACK);
        background.setFill(Color.rgb(98, 208, 232));
        
        timelineGroup.getChildren().add(background);
        
        for (int i = 0; i < length; i++){
	        Line frameMarker = new Line();
	        
	        
        	if ((i % mark) == 0) {  // Draw a tall mark and text
        		frameMarker.setStrokeWidth(2);
        		frameMarker.setStartX(step);
        		frameMarker.setStartY(35);
		        frameMarker.setEndX(step);
		        frameMarker.setEndY(20);
		        
		        Text t = new Text();
	            t.setFill(Color.BLACK);
	            t.setX(step - 2);
	            t.setY(15);
	            t.setText(Integer.toString(i));
	            timelineGroup.getChildren().add(t);
        	}
        	else {  // Draw a short mark, no text
		        frameMarker.setStrokeWidth(1);
		        frameMarker.setStartX(step);
		        frameMarker.setStartY(35);
		        frameMarker.setEndX(step);
		        frameMarker.setEndY(30);
        	}
	        step += stepSize;
	        timelineGroup.getChildren().add(frameMarker);
        }
        timelinePane.getChildren().add(timelineGroup);
    }
    
    public void clear() {
    	timelineGroup.getChildren().clear();
    	timelinePlayOverlay.getChildren().clear();
    	squibGroup.getSquibPlayGroups().clear();
    	squibGroup.getTimeLine().clear();
    	timelinePane.getChildren().clear();
    }
}
