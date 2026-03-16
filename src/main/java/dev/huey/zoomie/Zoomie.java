package dev.huey.zoomie;

import dev.huey.zoomie.api.Entry;
import dev.huey.zoomie.api.modules.Utils;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Zoomie extends Application {
  
  public static void main(String[] args) {
    launch();
  }
  
  Entry entry = new Entry("Super Disc Box");
  
  @Override
  public void start(Stage stage) {
    entry.start(stage);
  }
}