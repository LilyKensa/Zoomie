module dev.huey.zoomie {
  requires static lombok;

  requires java.desktop;

  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.media;

  opens dev.huey.zoomie to javafx.fxml;
  
  exports dev.huey.zoomie;
}
