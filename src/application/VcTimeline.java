package application;

import java.util.Observable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class VcTimeline extends Observable {
	@FXML Button playButton;
	@FXML Button pauseButton;
	
    @FXML protected void playAnimation(ActionEvent event) {
    	setChanged();
    	notifyObservers("Play");
        //System.out.println("Play button pressed.");
    }
    
    @FXML protected void pauseAnimation(ActionEvent event) {
    	setChanged();
    	notifyObservers("Pause");
    }
}
