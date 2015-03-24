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
import javafx.scene.control.ScrollBar;
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
	@FXML Label labelOutput;
	@FXML ScrollBar scroll_sequenceRate;
	@FXML AnchorPane paneSquibGroupContainer;
	
	// Included VisualSchematic View reference
	@FXML TabPane visualSchematic;
	@FXML VcPtVisualSchematicView visualSchematicController;
	@FXML VcPtMenu PTMenuController;
	@FXML HBox timeLine;
	@FXML VcPtTimeline timeLineController;
	
	SequentialTransition animationTimeline = new SequentialTransition();
	String animationID;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Setup listeners for changing radio button
		sequenceSelection.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		        Toggle old_toggle, Toggle new_toggle) {
		    			stopAnimation();
		            }                
		        });
	}
	
	// Load the groups of squibs stored in the sequence
	// and create radio buttons to select individual groups
	public void loadGroups () {	
		// y is the starting position of the radio buttons
		int y = 40;
		
		// Add a timeline for the main universe
		timeLineController.addGroupTimeline(sequence.getSquibGroups().get(0));
		
		// Populate the radio button group
		for (int i = 1; i < sequence.getSquibGroups().size(); i++) {
			// Create a radio button for each group
			RadioButton rb = new RadioButton();
			rb.toggleGroupProperty().set(groupSelection);
			rb.setLayoutX(5.0);
			rb.setLayoutY(y);
			y += 30;
			rb.setText("Group " + i);
			rb.setUserData(i);
			paneSquibGroupContainer.getChildren().add(rb);
			
			// Add a timeline for each subgroup of squibs
			timeLineController.addGroupTimeline(sequence.getSquibGroups().get(i));
		}
		
		// Draw the timelines initially so they aren't blank on window load
		//buildTimelineAnimation();
		timeLineController.buildTimeline(sequence.getTimeLine().size());
	}
	
	// Add an animation to the timeline based on the selected animation and selected group
	@FXML
	public void addAnimation(){
		String animation = sequenceSelection.getSelectedToggle().getUserData().toString();
		String group = groupSelection.getSelectedToggle().getUserData().toString();
		int rate = (int) scroll_sequenceRate.getValue();
		Universe u;
		int numTimesteps;
		int selectedGroup;
		
		u = sequence.getSquibGroups().get(Integer.parseInt(group)).getSquibs();
		selectedGroup = Integer.parseInt(group);
		
		// Stop the currently playing animation (if there is one)
		stopAnimation();
		
		numTimesteps = setAnimation(animation, u, rate, Integer.parseInt(group));
		buildTimelineAnimation();
		// Update the physical time line in the view
		timeLineController.buildTimeline(sequence.getTimeLine().size());
		// Update the groups playing overlays
		timeLineController.updatePlayOverlays(sequence.getTimeLine().size(), numTimesteps, selectedGroup);
	}
	
	// Clear all animation data and rebuild the timeline
	@FXML
	public void clearAnimation(){
		stopAnimation();
		sequence.getTimeLine().clear();
		timeLineController.clearGroupTimeline();
		buildTimelineAnimation();
		timeLineController.buildTimeline(sequence.getTimeLine().size());
	}
	
	// Load the currently selected animation
	public int setAnimation(String s, Universe u, int rate, int squibGroup){
		// numTimesteps tracks how many timesteps inserted for new sequence part
		int numTimesteps = 0;
		animationID = s;
		if (s.equals("fullUniverseSweep")){
			numTimesteps = sequence.loadUniverseSweep(u, rate, squibGroup);
		}
		else if (s.equals("simultaneousUniverseSweep")){
			numTimesteps = sequence.loadUniverseSimultaneousSweep(u, rate, squibGroup);
		}
		else if (s.equals("randomUniverseSequence")){
			numTimesteps = sequence.loadRandomOneAtATimeSequence(u, rate, squibGroup);
		}
		else if (s.equals("randomPerFireboxUniverseSequence")){
			numTimesteps = sequence.loadRandomOnePerFireboxSequence(u, 100, rate, squibGroup);
		}
		else if (s.equals("zigZag")){
			numTimesteps = sequence.loadUniverseZigZag(u, rate, squibGroup);
		}
		else if (s.equals("alternate")){
			numTimesteps = sequence.loadUniverseAlternate(u);
		}
		else {
			// Clear the timeline
			sequence.getTimeLine().clear();
		}
		return numTimesteps;
	}
	
	// Build the animation, setting up calls to draw the currently firing squibs
	public void buildTimelineAnimation(){
		// Reset timeline
		animationTimeline.getChildren().clear();
		
		// Build new animation timeline
		int i = 0;
		for (TimeStep t : sequence.getTimeLine()){
			// Use previous timestep to redraw previously fired squibs to green
			TimeStep previousTimestep;
			if (i != 0) {
				previousTimestep = sequence.getTimeLine().get(i-1);
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
	}
	
	public void playTimelineAnimation(){
		animationTimeline.play();
	}
	
	public void pauseTimelineAnimation(){
		animationTimeline.pause();
	}
	
	public void stopAnimation(){
		// Stop the currently playing animation
		timeLineController.cursorAnimation.stop();
		animationTimeline.stop();
		
		// Redraw universe to avoid drawing firing squib from previous sequence
		visualSchematicController.drawUniverseSchematic();
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
