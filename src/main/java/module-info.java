module dev.huey.zoomie {
  // 依賴 JavaFX 基礎模組
  requires javafx.controls;
  requires javafx.fxml;
  requires static lombok;
  
  opens dev.huey.zoomie to javafx.fxml;
  
  exports dev.huey.zoomie;
}
