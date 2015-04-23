package application;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
 
 
public class VcAboutWindow {
    private Scene scene;
    
    VcAboutWindow() {
    	Stage stage = new Stage();
        // create the scene
        stage.setTitle("About SOAP");
        scene = new Scene(new Browser(),800,500, Color.web("#666970"));
        stage.setScene(scene);
        //scene.getStylesheets().add("webviewsample/BrowserToolbar.css");        
        stage.show();
    }
}
class Browser extends Region {
 
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
     
    public Browser() {
        //apply the styles
        getStyleClass().add("browser");
        // load the web page
        webEngine.load("http://ericbskis.com/soap/?page_id=9");
        //add the web view to the scene
        getChildren().add(browser);
 
    }
 
    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }
 
    @Override protected double computePrefWidth(double height) {
        return 800;
    }
 
    @Override protected double computePrefHeight(double width) {
        return 500;
    }
}