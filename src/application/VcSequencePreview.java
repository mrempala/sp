package application;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Duration;


public class VcSequencePreview extends VcMainController implements Observer {

	// Included VisualSchematic View reference
	@FXML TabPane visualSchematic;
	@FXML VcPtVisualSchematicView visualSchematicController;
	@FXML HBox timeLine;
	@FXML VcPtTimeline timeLineController;
	@FXML VcPtMenu PTMenuController;
	
	// Sequence info labels
	@FXML Label lbProjectName;
	@FXML Label lbProjectDetails;
	@FXML Button buttonSendToUniverse;
	
	@FXML TextField tfPortNum;
	@FXML public TextArea taUniverseFeedback;
   
	private BBSendTimelineToUniverse bbSendToUniverse = new BBSendTimelineToUniverse("COM1"); 
	private boolean portSet = false;
	public static String universeFeedback = "";
	
	SequentialTransition animationTimeline = new SequentialTransition();
	
	public void loadProjectInfo(){
		lbProjectName.setText(this.sequence.getProjectName());
		lbProjectDetails.setText(this.sequence.getProjectDetails());
	}
	
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
		timeLineController.addGroupTimeline(sequence.getSquibGroups().get(0));
		timeLineController.buildTimeline(sequence.getTimeLine().size());
	}
	
	public void playTimelineAnimation(){
		animationTimeline.play();
	}
	
	public void pauseTimelineAnimation(){
		animationTimeline.pause();
	}
	
	

	@FXML public void sendToUniverse(ActionEvent event) {		
		bbSendToUniverse.click(sequence.getTimeLine());
		// Require the user to reset their port if they want to run a sequence again
		buttonSendToUniverse.setDisable(true);
		portSet = false;
	}
	
	@FXML public void setComPort() {
		// Reset the universe output string
		universeFeedback = "";
		
		String port = tfPortNum.getText();
		bbSendToUniverse = new BBSendTimelineToUniverse(port, sequence.getTimeLine());
		
		// Setup an event listener to see when the we have a message from the universe thread
		BBSendTimelineToUniverse.universeMessageProperty.addListener(new ChangeListener<String>() {
			@Override
		      public void changed(final ObservableValue<? extends String> observable,
		          final String oldValue, final String newValue) {

		          Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		              taUniverseFeedback.setText(newValue);
		            }
		          });          

		      }
		    });
		portSet = bbSendToUniverse.isConnected();
		if (portSet) {
			universeFeedback += ("COM Port Set!" + System.getProperty("line.separator"));
			taUniverseFeedback.setText(universeFeedback);
			buttonSendToUniverse.setDisable(false);
		}
		else {
			//Prompt users with a message that port isn't set
			universeFeedback += ("Error: Port not found or cannot connect to universe!" + System.getProperty("line.separator"));
			taUniverseFeedback.setText(universeFeedback);
		}		
	}
	
	@FXML public void returnToSequenceEditor(ActionEvent event){
		stop();
		try {
			openSequenceEditor(event);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop() {
		bbSendToUniverse.stop();
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