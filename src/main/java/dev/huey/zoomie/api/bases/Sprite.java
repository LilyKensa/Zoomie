package dev.huey.zoomie.api.bases;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;

import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Set;

public class Sprite {
  
  static public Sprite load(String path) {
    return new Sprite(new Image(
      Sprite.class.getResource("/assets/%s.png".formatted(path)).toExternalForm()
    ));
  }

  public int width, height;
  public IntBuffer buffer;
  
  Set<Integer> flags;
  
  public Sprite(int width, int height, IntBuffer buffer) {
    this.width = width;
    this.height = height;
    this.buffer = buffer;
  }
  
  public Sprite(int width, int height) {
    this(width, height, IntBuffer.allocate(width * height));
  }

  public Sprite(Image image) {
    width = (int) image.getWidth();
    height = (int) image.getHeight();
    
    buffer = IntBuffer.allocate(width * height);
    
    image.getPixelReader().getPixels(
      0, 0,
      width, height,
      PixelFormat.getIntArgbInstance(),
      buffer,
      width
    );
  }
  
  public Sprite flag(int flag) {
    flags.add(flag);
    return this;
  }
  
  public Sprite unflag(int flag) {
    flags.remove(flag);
    return this;
  }
  
  public Sprite slice(Vec.Int v, int width, int height) {
    IntBuffer buffer = IntBuffer.allocate(width * height);
    int[] pixels = buffer.array();
    
    for (int dy = 0; dy < height; ++dy) {
      for (int dx = 0; dx < width; ++dx) {
        Vec.Int source = Vec.ofInt(v.x + dx, v.y + dy);
        
        pixels[dy * width + dx] = this.buffer.get(source.y * this.width + source.x);
      }
    }
    
    return new Sprite(width, height, buffer);
  }
}
