package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class VcPtTimeline extends Observable {
	@FXML Button playButton;
	@FXML Button pauseButton;
	@FXML VBox timelineContainer;
	@FXML Pane timelinePane;
	
	Timeline cursorAnimation = new Timeline();
	Line timelineCursor;
	
	Group timelineGroup = new Group();
	
	// List of squib group timelines to be drawn, including the main universe
	List<PtSquibGroupsTimeline> timelines = new ArrayList<PtSquibGroupsTimeline>();
	
	int timelineCursorHeight = 0;
	
    @FXML protected void playAnimation(ActionEvent event) {
    	setChanged();
    	notifyObservers("Play");
    	
    	// Reset cursor
    	timelineCursor.setStartX(10);
        timelineCursor.setStartY(0);
        timelineCursor.setEndX(10);
        timelineCursor.setEndY(-timelineCursorHeight);
    	
    	cursorAnimation.play();
    }
    
    @FXML protected void pauseAnimation(ActionEvent event) {
    	setChanged();
    	notifyObservers("Pause");
    	cursorAnimation.pause();
    }
    
    public void clearGroupTimeline(){
    	for (PtSquibGroupsTimeline t : timelines){
    		t.clear();
    	}
    	
    }
    public void addGroupTimeline(){
    	// Update the height of the cursor to span all timelines
    	timelineCursorHeight += 40;
    	PtSquibGroupsTimeline timeline = new PtSquibGroupsTimeline();
    	timelines.add(timeline);
    	timelineContainer.getChildren().add(timeline.timelinePane);
    }
    
    public void updatePlayOverlays(int totalNumTimesteps, int insertedTimesteps, int group){
    	int i = 0;
    	for (PtSquibGroupsTimeline t : timelines){
    		int tempTimesteps = insertedTimesteps;
    		// If the animation was not applied to the current group, make inserted timesteps negative
    		// PtSquibGroupsTimeline skips drawing an overlay if the input is negative
    		if (i != group){
    			tempTimesteps *= -1;
    		}
    		
    		t.updateTimelinePlayOverlay(totalNumTimesteps, tempTimesteps);
    		i++;
    	}
    }
    
    public void buildTimeline(int length){
    	float stepSize = 0;
    	float timelineLength; 
    	int mark;
    	
    	if (length != 0){
    		stepSize = 665 / (float)length;
    	}
    	
    	timelineLength = stepSize * length;
    	
    	if ((length/1000) >= 1) {
    		mark = 100;
    	}
    	else if((length/100) >= 1) {
    		mark = 10;
    	}
    	else if ((length/10) >= 1){
    		mark = 5;
    	}
    	else {
    		mark = 1;
    	}
    	
        timelineCursor = new Line();
        timelineCursor.setStrokeWidth(4);
        timelineCursor.setStartX(10);
        timelineCursor.setStartY(0);
        timelineCursor.setEndX(10);
        timelineCursor.setEndY(-timelineCursorHeight);
        timelineCursor.setStroke(Color.RED);
        
        timelinePane.getChildren().clear();
        timelineGroup.getChildren().clear();
        timelineGroup.getChildren().add(timelineCursor);
        timelinePane.getChildren().add(timelineGroup);

    	//drawTimeline(length, timelineLength, stepSize, mark);
    	for (PtSquibGroupsTimeline t : timelines){
    		t.drawTimeline(length, timelineLength, stepSize, mark);
    	}
    	
    	timelinePane.toFront();

        KeyValue kv1 = new KeyValue(timelineCursor.startXProperty(), timelineLength + 10 );
        KeyValue kv2 = new KeyValue(timelineCursor.endXProperty(), timelineLength + 10 );
        
        KeyFrame kf = new KeyFrame(Duration.millis(35 * length), kv1, kv2);
        cursorAnimation.getKeyFrames().clear();
        cursorAnimation.getKeyFrames().add(kf);
    }
    
    public void drawTimeline(int length, float timelineLength, float stepSize, int mark){
    	float step = 10;
    	
    	// Timeline width: 600 height: 35
        timelinePane.getChildren().clear();
        timelineGroup.getChildren().clear();
        
    	System.out.println("Length: " + length);
    	
    	Rectangle background = new Rectangle();
    	background.setX(10);
    	background.setY(0);
    	background.setWidth(665);
        background.setHeight(35);
        background.setStroke(Color.BLACK);
        background.setFill(Color.rgb(98, 208, 232));
        /*background.setFill(Color.rgb(0, 78, 97));*/
        
        timelineCursor = new Line();
        timelineCursor.setStrokeWidth(4);
        timelineCursor.setStartX(10);
        timelineCursor.setStartY(0);
        timelineCursor.setEndX(10);
        timelineCursor.setEndY(timelineCursorHeight);
        timelineCursor.setStroke(Color.RED);
        
        timelineGroup.getChildren().add(background);
        timelineGroup.getChildren().add(timelineCursor);
        
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
        	else {  // Draw a short market, no text
		        frameMarker.setStrokeWidth(1);
		        frameMarker.setStartX(step);
		        frameMarker.setStartY(35);
		        frameMarker.setEndX(step);
		        frameMarker.setEndY(30);
        	}
	        step += stepSize;
	        //System.out.println(step);
	        timelineGroup.getChildren().add(frameMarker);
        }
        timelinePane.getChildren().add(timelineGroup);
    }
}
