package dev.huey.zoomie;

import dev.huey.zoomie.api.Entry;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Zoomie extends Application {
  
  public static void main(String[] args) {
    launch();
  }
  
  Entry entry = new Entry();
  
  @Override
  public void start(Stage stage) {
    stage.getIcons().add(new Image("icon.png"));
    stage.setTitle("Super Disc Box - Zoomie");
    
    Screen screen = Screen.getPrimary();
    stage.setWidth(screen.getBounds().getWidth() * 0.5);
    stage.setHeight(screen.getBounds().getHeight() * 0.6);
    
    stage.setFullScreenExitHint("");
//    stage.setFullScreen(true);
    
    entry.start(stage);
  }
}