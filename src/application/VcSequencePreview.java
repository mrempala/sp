package application;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;


public class VcSequencePreview implements Initializable, Observer {

	// Included VisualSchematic View reference
	@FXML TabPane visualSchematic;
	@FXML VcVisualSchematicView visualSchematicController;
	
	@FXML HBox timeLine;
	@FXML VcTimeline timeLineController;
	
	Sequence sequence;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        
	}
	
	public void setSequence(Sequence sequence){
		this.sequence = sequence;
	}
	
	// Temporarily draw all squibs in time line from sequence to be fired
	/*public void drawFiredSquibs(){
		for (TimeStep t : sequence.timeLine){
			for (Squib s : t.squibList){
				visualSchematicController.drawFiringSquib(s, new TimeStep());
			}
		}
	}*/
	
	public void playTimelineAnimation(){
		SequentialTransition animationTimeline = new SequentialTransition();
		
		int i = 0;
		for (TimeStep t : sequence.timeLine){
				TimeStep previousTimestep;
				if (i != 0) {
					previousTimestep = sequence.timeLine.get(i-1);
				}
				else {
					previousTimestep = null;
				}
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
		animationTimeline.play();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1.equals("Play")){
			System.out.println("Play button pressed");
			playTimelineAnimation();
		}
		
	}
}