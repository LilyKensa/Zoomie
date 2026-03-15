package dev.huey.zoomie.api.modules;

import dev.huey.zoomie.api.Entry;
import dev.huey.zoomie.api.bases.Font;
import dev.huey.zoomie.api.bases.Sprite;
import dev.huey.zoomie.api.bases.TileMap;
import dev.huey.zoomie.api.bases.Vec;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Graphics {
  
  static Vec cam = Vec.zero();
  
  static public void camera(Vec v) {
    cam = v;
  }
  
  static public void camera() {
    camera(Vec.zero());
  }
  
  static public Vec.Int applyCam(Vec vec) {
    return vec.minus(cam).toInt();
  }
  
  static public boolean isOnScreen(Vec.Int v) {
    return v.x >= 0 && v.x < Config.size.width() &&
           v.y >= 0 && v.y < Config.size.height();
  }
  
  static public Color defColor = Color.RED;
  
  static public Color color() {
    return defColor;
  }
  
  static public void color(Color color) {
    defColor = color;
  }
  
  static public int toARGB(Color color) {
    int a = (int) (color.getOpacity() * 255);
    int r = (int) (color.getRed() * 255);
    int g = (int) (color.getGreen() * 255);
    int b = (int) (color.getBlue() * 255);
    return (a << 24) | (r << 16) | (g << 8) | b;
  }
  
  static Map<Color, Color> paletteMappings = new HashMap<>();
  
  static public void map(Color from, Color to) {
    if (from.equals(to)) {
      paletteMappings.remove(from);
    }
    else {
      paletteMappings.put(from, to);
    }
  }
  
  static public Color applyPalette(Color color) {
    if (paletteMappings.containsKey(color)) {
      return paletteMappings.get(color);
    }
    
    return color;
  }
  
  static public void absPixel(Vec.Int v, Color color) {
    color(color);
    
    if (!isOnScreen(v)) return;
    
    int[] pixels = Entry.instance.currentScreenBuffer().getBuffer().array();
    pixels[v.y * Config.size.width() + v.x] = toARGB(applyPalette(color));
  }
  
  static public void pixel(Vec v, Color color) {
    absPixel(applyCam(v), color);
  }
  
  static public void pixel(Vec v) {
    pixel(v, defColor);
  }
  
  static public void absFillRect(Vec.Int u, Vec.Int v, Color color) {
    color(color);
    
    Vec.Int
      lower = Vec.of(
        Math.min(u.x, v.x),
        Math.min(u.y, v.y)
      ).toInt(),
      upper = Vec.of(
        Math.max(u.x, v.x),
        Math.max(u.y, v.y)
      ).toInt();
    
    if (
      upper.x < 0 || upper.y < 0 ||
      lower.x >= Config.size.width() || lower.y >= Config.size.height()
    ) return;
    
    lower.x = Math.max(0, lower.x);
    lower.y = Math.max(0, lower.y);
    upper.x = Math.min(Config.size.width() - 1, upper.x);
    upper.y = Math.min(Config.size.height() - 1, upper.y);
    
    int argb = toARGB(applyPalette(color));
    int[] pixels = Entry.instance.currentScreenBuffer().getBuffer().array();
    
    int rectWidth = upper.x - lower.x + 1;
    int[] rowSource = new int[rectWidth];
    Arrays.fill(rowSource, argb);
    
    for (int y = lower.y; y <= upper.y; ++y) {
      System.arraycopy(
        rowSource,
        0,
        pixels,
        y * Config.size.width() + lower.x,
        rectWidth
      );
    }
  }
  
  static public void fillRect(Vec u, Vec v, Color color) {
    absFillRect(applyCam(u), applyCam(v), color);
  }
  
  static public void fillRect(Vec u, Vec v) {
    fillRect(u, v, defColor);
  }
  
  static public void rect(Vec u, Vec v, Color color) {
    Vec.Int
      p = applyCam(u),
      q = applyCam(v),
      r = applyCam(Vec.of(u.x, v.y)),
      s = applyCam(Vec.of(v.x, u.y));
    
    absFillRect(p, r, color);
    absFillRect(r, q, color);
    absFillRect(q, s, color);
    absFillRect(s, p, color);
  }
  
  static public void rect(Vec u, Vec v) {
    rect(u, v, defColor);
  }
  
  static public void clear(Color color) {
    int argb = toARGB(applyPalette(color));
    int[] pixels = Entry.instance.currentScreenBuffer().getBuffer().array();
    
    Arrays.fill(pixels, 0, pixels.length, argb);
  }
  
  static public void clear() {
    clear(Color.BLACK);
  }
  
  static public void absLine(Vec.Int u, Vec.Int v, Color color) {
    color(color);
    
    u = u.clone();
    v = v.clone();
    
    int dx = Math.abs(v.x - u.x);
    int dy = -Math.abs(v.y - u.y);
    int sx = u.x < v.x ? 1 : -1;
    int sy = u.y < v.y ? 1 : -1;
    int err = dx + dy;
    
    while (true) {
      absPixel(u, color);
      
      if (u.equals(v)) break;
      
      int e2 = 2 * err;
      if (e2 >= dy) {
        err += dy;
        u.x += sx;
      }
      if (e2 <= dx) {
        err += dx;
        u.y += sy;
      }
    }
  }
  
  static public void line(Vec u, Vec v, Color color) {
    absLine(applyCam(u), applyCam(v), color);
  }
  
  static public void line(Vec u, Vec v) {
    line(u, v, defColor);
  }
  
  static public void absCircle(Vec.Int center, int radius, Color color) {
    color(color);
    
    int cx = center.x, cy = center.y;
    int dx = radius, dy = 0;
    int err = 1 - radius;
    
    while (dx >= dy) {
      absPixel(Vec.ofInt(cx + dx, cy + dy), color);
      absPixel(Vec.ofInt(cx + dy, cy + dx), color);
      absPixel(Vec.ofInt(cx - dy, cy + dx), color);
      absPixel(Vec.ofInt(cx - dx, cy + dy), color);
      absPixel(Vec.ofInt(cx - dx, cy - dy), color);
      absPixel(Vec.ofInt(cx - dy, cy - dx), color);
      absPixel(Vec.ofInt(cx + dy, cy - dx), color);
      absPixel(Vec.ofInt(cx + dx, cy - dy), color);
      
      dy++;
      
      if (err < 0) {
        err += 2 * dy + 1;
      }
      else {
        dx--;
        err += 2 * (dy - dx) + 1;
      }
    }
  }
  
  static public void circle(Vec center, double radius, Color color) {
    absCircle(applyCam(center), (int) radius, color);
  }
  
  static public void circle(Vec center, double radius) {
    circle(center, radius, defColor);
  }
  
  static public void absFillCircle(Vec.Int center, int radius, Color color) {
    color(color);
    
    int cx = center.x, cy = center.y;
    int dx = radius, dy = 0;
    int err = 1 - radius;
    
    while (dx >= dy) {
      absFillRect(
        Vec.ofInt(cx - dx, cy + dy),
        Vec.ofInt(cx + dx, cy + dy),
        color
      );
      absFillRect(
        Vec.ofInt(cx - dx, cy - dy),
        Vec.ofInt(cx + dx, cy - dy),
        color
      );
      absFillRect(
        Vec.ofInt(cx - dy, cy + dx),
        Vec.ofInt(cx + dy, cy + dx),
        color
      );
      absFillRect(
        Vec.ofInt(cx - dy, cy - dx),
        Vec.ofInt(cx + dy, cy - dx),
        color
      );
      
      dy++;
      
      if (err < 0) {
        err += 2 * dy + 1;
      }
      else {
        dx--;
        err += 2 * (dy - dx) + 1;
      }
    }
  }
  
  static public void fillCircle(Vec center, double radius, Color color) {
    absFillCircle(applyCam(center), (int) radius, color);
  }
  
  static public void fillCircle(Vec center, double radius) {
    fillCircle(center, radius, defColor);
  }
  
  static public void absSprite(Sprite spr, Vec.Int v, boolean mirror, boolean flip) {
    int[] pixels = Entry.instance.currentScreenBuffer().getBuffer().array();
    int[] source = spr.buffer.array();
    int screenWidth = Config.size.width();
    
    for (int dy = 0; dy < spr.height; ++dy) {
      int sourceY = flip ? (spr.height - 1 - dy) : dy;
      
      for (int dx = 0; dx < spr.width; ++dx) {
        int sourceX = mirror ? (spr.width - 1 - dx) : dx;
        
        Vec.Int target = Vec.ofInt(v.x + dx, v.y + dy);
        if (!isOnScreen(target)) continue;
        
        int argb = source[sourceY * spr.width + sourceX];
        if ((argb & 0x11000000) == 0) continue;
        
        pixels[target.y * screenWidth + target.x] = argb;
      }
    }
  }
  
  static public void sprite(Sprite spr, Vec v, boolean mirror, boolean flip) {
    absSprite(spr, applyCam(v), mirror, flip);
  }
  
  static public void sprite(Sprite spr, Vec v, boolean mirror) {
    sprite(spr, v, mirror, false);
  }
  
  static public void sprite(Sprite spr, Vec v) {
    sprite(spr, v, false);
  }
  
  static public void easeSprite(Sprite spr, Vec v, double width, double height, boolean mirror, boolean flip) {
  
  }
  
  static public void easeSprite(Sprite spr, Vec v, double width, double height, boolean mirror) {
    easeSprite(spr, v, width, height, mirror, false);
  }
  
  static public void easeSprite(Sprite spr, Vec v, double width, double height) {
    easeSprite(spr, v, width, height, false);
  }
  
  static public void tiles(TileMap tiles, Vec v, int width, int height, Set<Integer> flags) {
  
  }
  
  static Font defFont;
  
  static public void font(Font font) {
    defFont = font;
  }
  
  static public void absPrint(String text, Vec.Int v, Color color, Font font) {
    color(color);
    
    if (font == null) return;
    
    int argb = toARGB(applyPalette(color));
    
    int[] source = font.getSprite(text.substring(0, 1)).buffer.array();
    int[] pixels = Entry.instance.currentScreenBuffer().getBuffer().array();
    
    final int originX = v.x;
    
    int index = 0;
    
    while (index < text.length()) {
      if (text.charAt(index) == '\n') {
        v.x = originX;
        v.y += font.getSpaceHeight();
        index++;
        continue;
      }
      
      Sprite spr = null;
      for (int i = 0; i < 4; ++i) {
        spr = font.getSprite(text.substring(index, index + i));
        if (spr != null) {
          index += i;
          break;
        }
      }
      if (spr == null) {
        index++;
        continue;
      }
      
      for (int dy = 0; dy < spr.height; ++dy) {
        for (int dx = 0; dx < spr.width; ++dx) {
          Vec.Int target = Vec.ofInt(v.x + dx, v.y + dy);
          if (!isOnScreen(target)) continue;
          
          if ((source[dy * font.getCharWidth() + dx] & 0x11000000) == 0) continue;
          
          pixels[target.y * font.getCharWidth() + target.x] = argb;
        }
      }
      
      v.x += font.getSpaceWidth();
    }
  }
  
  static public void print(String text, Vec v, Color color, Font font) {
    absPrint(text, applyCam(v), color, font);
  }
  
  static public void print(String text, Vec v, Color color) {
    absPrint(text, applyCam(v), color, defFont);
  }
  
  static public void print(String text, Vec v) {
    print(text, v, defColor);
  }
}
