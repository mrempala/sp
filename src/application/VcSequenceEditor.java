package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
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
	@FXML VcPtVisualSchematicView visualSchematicController;
	
	@FXML HBox timeLine;
	@FXML VcPtTimeline timeLineController;
	
	@FXML Label labelOutput;
	
	SequentialTransition animationTimeline = new SequentialTransition();
	String animationID;
	
	Group squibGroups = new Group();
	// TODO: Squib group data should go into sequence, but we'll probably want to  save more info
	public List<Integer> squibGroupSizes = new ArrayList<Integer>();

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
		            }                
		        });
	}
	
	// Load the groups of squibs stored in the sequence
	// and create radio buttons to select individual groups
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
	
	// Add an animation to the timeline based on the selected animation and selected group
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
		timeLineController.timelinePane.getChildren().remove(squibGroups);
		setAnimation(animation, u);
		timeLineController.timelinePane.getChildren().add(squibGroups);
	}
	
	// Clear all animation data and rebuild the timeline
	@FXML
	public void clearAnimation(){
		sequence.timeLine.clear();
		buildTimelineAnimation();
	}
	
	// Load the currently selected animation
	public void setAnimation(String s, Universe u){
		// numTimesteps tracks how many timesteps inserted for new sequence part
		int numTimesteps = 0;
		animationID = s;
		if (s.equals("fullUniverseSweep")){
			numTimesteps = sequence.loadUniverseSweep(u);
		}
		else if (s.equals("simultaneousUniverseSweep")){
			numTimesteps = sequence.loadUniverseSimultaneousSweep(u);
		}
		else if (s.equals("randomUniverseSequence")){
			numTimesteps = sequence.loadRandomOneAtATimeSequence(u);
		}
		else if (s.equals("randomPerFireboxUniverseSequence")){
			numTimesteps = sequence.loadRandomOnePerFireboxSequence(u, 100);
		}
		else if (s.equals("zigZag")){
			numTimesteps = sequence.loadUniverseZigZag(u);
		}
		else if (s.equals("alternate")){
			numTimesteps = sequence.loadUniverseAlternate(u);
		}
		else {
			// Clear the timeline
			sequence.timeLine.clear();
		}
		buildTimelineAnimation();
		drawAnimationSubgroups(numTimesteps);
	}
	
	// Build the animation, setting up calls to draw the currently firing squibs
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
	
	public void drawAnimationSubgroups(int numTimesteps){
		squibGroupSizes.add(numTimesteps);
		squibGroups.getChildren().clear();
		// Get the total number of time steps in the animation sequence
		int totalNumTimesteps = sequence.timeLine.size();
		// Calculate the step size based on the pixel width of the timeline
		// and the total number of timesteps
		float stepSize = 665 / totalNumTimesteps;
		// Set the start position to draw at (10 is the same as the timeline)
		int x = 10;
		// Draw a rectangle for each sub animation
		for (Integer i : squibGroupSizes){
			Rectangle squibGroup = new Rectangle();
			squibGroup.setX(x);
			squibGroup.setY(5);
			squibGroup.setHeight(25);
			squibGroup.setWidth(i * stepSize);
			squibGroup.getStyleClass().add("squib-group-overlay");
			squibGroups.getChildren().add(squibGroup);
			x += (i*stepSize);
		}
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
