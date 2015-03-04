package application;

import java.util.Observable;
import java.util.Observer;

import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;


public class VcSequencePreview extends VcMainController implements Observer {

	// Included VisualSchematic View reference
	@FXML TabPane visualSchematic;
	@FXML VcPtVisualSchematicView visualSchematicController;
	
	@FXML HBox timeLine;
	@FXML VcPtTimeline timeLineController;
	
	// Sequence info labels
	@FXML Label lbProjectName;
	@FXML Label lbVenue;
	@FXML Label lbShow;
	@FXML Label lbDj;

	SequentialTransition animationTimeline = new SequentialTransition();
	
	public void loadProjectInfo(){
		lbProjectName.setText(this.sequence.getProjectName());
		lbVenue.setText(this.sequence.getVenue());
		lbShow.setText(this.sequence.getShow());
		lbDj.setText(this.sequence.getDj());
	}
	
	public void buildTimelineAnimation(){
		// Reset timeline
		animationTimeline.getChildren().clear();
		
		// Build new animation timeline
		int i = 0;
		for (TimeStep t : sequence.timeLine){
			// Use previous timestep to redraw previously fired squibs to green
			TimeStep previousTimestep;
			if (i != 0) {
				previousTimestep = sequence.timeLine.get(i-1);
			}
			else {
				previousTimestep = null;
			}
			
			// Create a keyframe to color in fired squibs.  Currently we are just redrawing over the top of old universe
			// TODO: May create memory problems in the future?
			KeyFrame kf = new KeyFrame(Duration.millis(35), new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent actionEvent) {
	                visualSchematicController.drawFiringSquib(t, previousTimestep);
	                System.out.println("Timeline time step");
	            }
	        });
			Timeline tempTimeline = new Timeline();
			tempTimeline.getKeyFrames().add(kf);
			animationTimeline.getChildren().add(tempTimeline);
			i++;
		}
		
		// Update the physical time line in the view
		timeLineController.addGroupTimeline();
		timeLineController.buildTimeline(sequence.timeLine.size());
	}
	
	public void playTimelineAnimation(){
		animationTimeline.play();
	}
	
	public void pauseTimelineAnimation(){
		animationTimeline.pause();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1.equals("Play")){
			System.out.println("Play button pressed");
			playTimelineAnimation();
		}
		else if (arg1.equals("Pause")){
			pauseTimelineAnimation();
		}
	}
}