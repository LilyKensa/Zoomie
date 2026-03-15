package dev.huey.zoomie.api.bases;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import lombok.Builder;
import lombok.Getter;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
public class Font {
  
  String source;
  
  @Getter
  int gridWidth, gridHeight, charWidth, charHeight, spaceWidth, spaceHeight, rows, columns;
  
  List<String> chars;
  
  public Map<String, Sprite> map = new HashMap<>();
  
  public Font() {
    Sprite sheet = Sprite.load(source);
    
    int index = 0;
    for (int y = 0; y < columns; ++y) {
      for (int x = 0; x < rows; ++x) {
        Vec.Int v = Vec.ofInt(x * gridWidth, y * gridHeight);
        
        map.put(chars.get(index++), sheet.slice(v, charWidth, charHeight));
      }
    }
  }
  
  public Sprite getSprite(String id) {
    return map.get(id);
  }
}
