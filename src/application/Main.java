package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {	
    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-ProgramStart.fxml"));
        root = (Parent)loader.load();
        
        VcProgramStart newProject = loader.<VcProgramStart>getController();
        newProject.setCurrentStage(stage);
        //Rectangle2D r = Screen.getPrimary().getBounds();
        
        stage.setTitle("E-Squib Controller");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Styles/dssp-square.png")));
        stage.setScene(new Scene(root,500, 300));
        //stage.setMaximized(true);
        stage.show();
    }
    
    public static void main(String[] args) {    	
        Application.launch(Main.class, args);
    }

}