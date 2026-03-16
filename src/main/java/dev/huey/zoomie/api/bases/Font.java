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

@Builder(buildMethodName = "internalBuild")
public class Font {

  public static class FontBuilder {
    public Font build() {
      Font font = this.internalBuild();
      font.init();
      return font;
    }
  }
  
  String source;
  
  @Getter
  int gridWidth, gridHeight, charWidth, charHeight, spaceWidth, spaceHeight, rows, columns;
  
  List<String> chars;

  public final Map<String, Sprite> map = new HashMap<>();
  
  public void init() {
    Sprite sheet = new Sprite(new Image(
      Sprite.class.getResource("/assets/fonts/%s.png".formatted(source)).toExternalForm()
    ));

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
