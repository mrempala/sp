package application;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VcSequenceEditor implements Initializable, Observer {
	// TODO: 	At the moment, this class is almost an exact duplicate of VcSequencePreview.
	// 			in the future, may be able to have the two visual views use the same controller,
	//			or perhaps create a base class for sequence and have the two extend it.
	
	@FXML ToggleGroup sequenceSelection;
	@FXML Button button_loadSequencePreview;

	// Included VisualSchematic View reference
	@FXML TabPane visualSchematic;
	@FXML VcVisualSchematicView visualSchematicController;
	
	@FXML HBox timeLine;
	@FXML VcTimeline timeLineController;
	
	Sequence sequence;
	SequentialTransition animationTimeline = new SequentialTransition();
	String animationID;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Setup listeners for changing radio button
		sequenceSelection.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		        Toggle old_toggle, Toggle new_toggle) {
			            if (sequenceSelection.getSelectedToggle() != null) {
			            	System.out.println(sequenceSelection.getSelectedToggle().getUserData());
			            	setAnimation(sequenceSelection.getSelectedToggle().getUserData().toString());
			            }
		            }                
		        });
	}
	
	public void setSequence(Sequence sequence){
		this.sequence = sequence;
	}
	
	public void setAnimation(String s){
		animationID = s;
		if (s.equals("fullUniverseSweep")){
			sequence.loadUniverseSweep();
		}
		else if (s.equals("simultaneousUniverseSweep")){
			sequence.loadUniverseSimultaneousSweep();
		}
		else if (s.equals("randomUniverseSequence")){
			sequence.loadRandomOneAtATimeSequence();
		}
		else if (s.equals("randomPerFireboxUniverseSequence")){
			sequence.loadRandomOnePerFireboxSequence(100);
		}
		else if (s.equals("zigZag")){
			sequence.loadUniverseZigZag();
		}
		else if (s.equals("alternate")){
			sequence.loadUniverseAlternate();
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
	
	@FXML protected void loadSequencePreview(ActionEvent event) throws IOException {
		//Sequence sequence = new Sequence(universe);
		//sequence.loadUniverseSweep();

		Parent root;
    	// Load the next window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-SequencePreview.fxml"));
        root = (Parent)loader.load();
        
        // Get a reference to the VisualSchematic controller so we can pass a reference of the universe to it.
        VcSequencePreview seqPreviewController = loader.<VcSequencePreview>getController();
        // And hackily push the necessary variables into it
        seqPreviewController.visualSchematicController.setUniverse(this.visualSchematicController.universe);
        seqPreviewController.visualSchematicController.drawUniverseSchematic();
        
        // Hack to get sequence into the sequence previewer
        seqPreviewController.setSequence(sequence);
        seqPreviewController.buildTimelineAnimation();
        
        // Register the sequence preview as an observer of the time line to get play and pause events
        seqPreviewController.timeLineController.addObserver(seqPreviewController);
        
        Scene scene = new Scene(root, 1000, 500);
        Stage stage = new Stage();
        stage.setTitle("Sequence Preview");
        stage.setScene(scene);
        stage.show();
        
        // Close the current window
        // get a handle to the stage
        Stage currentstage = (Stage) button_loadSequencePreview.getScene().getWindow();
        // and close it
        currentstage.close();
	}
}
