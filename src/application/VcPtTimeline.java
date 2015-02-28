package application;

import java.util.Observable;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class VcPtTimeline extends Observable {
	@FXML Button playButton;
	@FXML Button pauseButton;
	@FXML Pane timelinePane;
	
	Timeline cursorAnimation = new Timeline();
	Line timelineCursor;
	
	Group timelineGroup = new Group();
	
    @FXML protected void playAnimation(ActionEvent event) {
    	setChanged();
    	notifyObservers("Play");
    	
    	// Reset cursor
    	// Currently throws exception when animation is played in sequence preview b/c time line isn't implemented there yet
    	timelineCursor.setStartX(10);
        timelineCursor.setStartY(0);
        timelineCursor.setEndX(10);
        timelineCursor.setEndY(35);
    	
    	cursorAnimation.play();
        //System.out.println("Play button pressed.");
    }
    
    @FXML protected void pauseAnimation(ActionEvent event) {
    	setChanged();
    	notifyObservers("Pause");
    	cursorAnimation.pause();
    }
    
    public void buildTimeline(int length){
    	float timelineLength = drawTimeline(length);
        
        KeyValue kv1 = new KeyValue(timelineCursor.startXProperty(), timelineLength + 10 );
        KeyValue kv2 = new KeyValue(timelineCursor.endXProperty(), timelineLength + 10 );
        
        KeyFrame kf = new KeyFrame(Duration.millis(35 * length), kv1, kv2);
        cursorAnimation.getKeyFrames().clear();
        cursorAnimation.getKeyFrames().add(kf);
    }
    
    public float drawTimeline(int length){
    	// Timeline width: 600 height: 35
        timelinePane.getChildren().clear();
        timelineGroup.getChildren().clear();
        
    	System.out.println("Length: " + length);
    	float stepSize = 0;
    	float step = 10;
    	int mark;
    	float timelineLength;
    	
    	if (length != 0){
    		stepSize = 665 / (float)length;
    	}
    	
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
    	timelineLength = stepSize * length;
    	
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
        timelineCursor.setEndY(35);
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
        return timelineLength;
    }
}