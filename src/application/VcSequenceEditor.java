package application;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class VcSequenceEditor extends VcMainController implements Observer {
	// TODO: 	At the moment, this class is almost an exact duplicate of VcSequencePreview.
	// 			in the future, may be able to have the two visual views use the same controller,
	//			or perhaps create a base class for sequence and have the two extend it.
	
	@FXML ToggleGroup sequenceSelection;
	@FXML Button button_loadSequencePreview;
	
	@FXML ToggleGroup groupSelection;
	@FXML AnchorPane SeqEditorLeftPane;

	// Included VisualSchematic View reference
	@FXML TabPane visualSchematic;
	@FXML VcVisualSchematicView visualSchematicController;
	
	@FXML HBox timeLine;
	@FXML VcTimeline timeLineController;
	
	@FXML Label labelOutput;
	
	SequentialTransition animationTimeline = new SequentialTransition();
	String animationID;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Setup listeners for changing radio button
		sequenceSelection.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		        Toggle old_toggle, Toggle new_toggle) {
		    			// Stop the currently playing animation
		    			timeLineController.cursorAnimation.stop();
		    			animationTimeline.stop();
		    			
		    			// Redraw universe to avoid drawing firing squib from previous sequence
		    			visualSchematicController.drawUniverseSchematic();
		    			
			            if (sequenceSelection.getSelectedToggle() != null) {
			            	System.out.println(sequenceSelection.getSelectedToggle().getUserData());
			            	//setAnimation(sequenceSelection.getSelectedToggle().getUserData().toString());
			            }
		            }                
		        });
	}
	
	public void loadGroups () {
		buildTimelineAnimation();
		
		int y = 280;
		// Populate the radio button group
		for (int i = 0; i < sequence.squibGroups.size(); i++) {
			RadioButton rb = new RadioButton();
			rb.toggleGroupProperty().set(groupSelection);
			rb.setLayoutX(20.0);
			rb.setLayoutY(y);
			y += 30;
			rb.setText("Group " + i);
			rb.setUserData(i);
			SeqEditorLeftPane.getChildren().add(rb);
		}
	}
	
	@FXML
	public void addAnimation(){
		String animation = sequenceSelection.getSelectedToggle().getUserData().toString();
		String group = groupSelection.getSelectedToggle().getUserData().toString();
		Universe u;
		if (group.equals("Universe")) {
			u = sequence.universe;
		}
		else {
			u = sequence.squibGroups.get(Integer.parseInt(group));
		}
		setAnimation(animation, u);
	}
	
	@FXML
	public void clearAnimation(){
		sequence.timeLine.clear();
		buildTimelineAnimation();
	}
	
	public void setAnimation(String s, Universe u){
		animationID = s;
		if (s.equals("fullUniverseSweep")){
			sequence.loadUniverseSweep(u);
		}
		else if (s.equals("simultaneousUniverseSweep")){
			sequence.loadUniverseSimultaneousSweep(u);
		}
		else if (s.equals("randomUniverseSequence")){
			sequence.loadRandomOneAtATimeSequence(u);
		}
		else if (s.equals("randomPerFireboxUniverseSequence")){
			sequence.loadRandomOnePerFireboxSequence(u, 100);
		}
		else if (s.equals("zigZag")){
			sequence.loadUniverseZigZag(u);
		}
		else if (s.equals("alternate")){
			sequence.loadUniverseAlternate(u);
		}
		else {
			// Clear the timeline
			sequence.timeLine.clear();
		}
		buildTimelineAnimation();
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
			
			// Create a keyframe to color in fired squibs.
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
