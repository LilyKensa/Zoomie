package dev.huey.zoomie.api.modules;

public class Config {
  public record ScreenConfig(
    int width,
    int height
  ) {}
  
  static public final ScreenConfig size = new ScreenConfig(128, 128);
}
