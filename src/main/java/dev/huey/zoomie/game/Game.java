package dev.huey.zoomie.game;

import dev.huey.zoomie.api.Entry;
import dev.huey.zoomie.api.bases.Font;
import dev.huey.zoomie.api.bases.Lifecycle;
import dev.huey.zoomie.api.bases.Sprite;
import dev.huey.zoomie.api.bases.Vec;
import dev.huey.zoomie.api.modules.Audios;
import dev.huey.zoomie.api.modules.Graphics;
import dev.huey.zoomie.api.modules.Inputs;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import lombok.Builder;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Game implements Lifecycle {
  
  static public Game instance;
  
  public Game() {
    instance = this;
  }
  
  Font font = Font.builder()
    .source("font")
    .gridWidth(8).gridHeight(8)
    .charWidth(7).charHeight(5)
    .spaceWidth(4).spaceHeight(6)
    .chars(List.of(
      "▮", "■", "□", "⁙", "⁘", "‖", "◀", "▶", "「", "」", "¥", "•", "、", "。", "゛", "゜",
      " ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/",
      "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?",
      "@", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
      "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "[", "\\", "]", "^", "_",
      "`", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
      "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "{", "|", "}", "~", "○",
      "█", "▒", "🐱", "⬇️", "░", "✽", "●", "♥", "☉", "웃", "⌂", "⬅️", "😐", "♪", "🅾️", "◆",
      "…", "➡️", "★", "⧗", "⬆️", "ˇ", "∧", "❎", "▤", "▥", "あ", "い", "う", "え", "お", "か",
      "き", "く", "け", "こ", "さ", "し", "す", "せ", "そ", "た", "ち", "つ", "て", "と", "な", "に",
      "ぬ", "ね", "の", "は", "ひ", "ふ", "へ", "ほ", "ま", "み", "む", "め", "も", "や", "ゆ", "よ",
      "ら", "り", "る", "れ", "ろ", "わ", "を", "ん", "っ", "ゃ", "ゅ", "ょ", "ア", "イ", "ウ", "エ",
      "オ", "カ", "キ", "ク", "ケ", "コ", "サ", "シ", "ス", "セ", "ソ", "タ", "チ", "ツ", "テ", "ト",
      "ナ", "ニ", "ヌ", "ネ", "ノ", "ハ", "ヒ", "フ", "ヘ", "ホ", "マ", "ミ", "ム", "メ", "モ", "ヤ",
      "ユ", "ヨ", "ラ", "リ", "ル", "レ", "ロ", "ワ", "ヲ", "ン", "ッ", "ャ", "ュ", "ョ", "◜", "◝"
    ))
    .build();
  
  Sprite sheet = Sprite.load("spritesheet");
  
  enum SpriteID {
    PL_RUN_0,
    PL_RUN_1,
    PL_RUN_2,
    PL_RUN_3,
    PL_RUN_4,
    PL_IDLE_0,
    PL_IDLE_1,
    PL_IDLE_2,
    PL_IDLE_3,
    PL_IDLE_4,
    DISC_BIG_0,
    DISC_BIG_1,
    DISC_BIG_2,
    DISC_BIG_3,
    DISC_SEEK_0,
    DISC_SEEK_1,
    DISC_SEEK_2,
    DISC_SEEK_3,
    DISC_0,
    DISC_1,
    DISC_2,
    DISC_3,
    WEAPON_PISTOL,
    WEAPON_REVOLVER,
    WEAPON_MACHINE_GUN,
    WEAPON_MINI_GUN,
    WEAPON_SHOT_GUN,
    WEAPON_DISC_GUN,
    WEAPON_KATANA_0,
    WEAPON_KATANA_1,
    WEAPON_BAZOOKA,
    WEAPON_GRENADE_LAUNCHER,
    WEAPON_MINES,
    WEAPON_FLAME_THROWER,
    WEAPON_LASER_RIFLE,
    WEAPON_LASER_RIFLE_MUZZLE
  }
  
  EnumMap<SpriteID, Sprite> spriteCache = new EnumMap<>(SpriteID.class);
  
  List<SpriteID> runAnimation = List.of(
    SpriteID.PL_RUN_0,
    SpriteID.PL_RUN_1,
    SpriteID.PL_RUN_2,
    SpriteID.PL_RUN_3,
    SpriteID.PL_RUN_4
  );
  List<SpriteID> idleAnimation = List.of(
    SpriteID.PL_IDLE_0,
    SpriteID.PL_IDLE_1,
    SpriteID.PL_IDLE_2,
    SpriteID.PL_IDLE_3,
    SpriteID.PL_IDLE_4
  );
  List<SpriteID> discBigAnimation = List.of(
    SpriteID.DISC_BIG_0,
    SpriteID.DISC_BIG_1,
    SpriteID.DISC_BIG_2,
    SpriteID.DISC_BIG_3
  );
  List<SpriteID> discSeekAnimation = List.of(
    SpriteID.DISC_SEEK_0,
    SpriteID.DISC_SEEK_1,
    SpriteID.DISC_SEEK_2,
    SpriteID.DISC_SEEK_3
  );
  List<SpriteID> discAnimation = List.of(
    SpriteID.DISC_0,
    SpriteID.DISC_1,
    SpriteID.DISC_2,
    SpriteID.DISC_3
  );
  
  @Override
  public void start() {
    for (int i = 0; i < 5; ++i) {
      spriteCache.put(
        SpriteID.valueOf("PL_RUN_" + i),
        sheet.slice(Vec.ofInt(48 + i * 16, 0), 16, 16)
      );
    }
    for (int i = 0; i < 5; ++i) {
      spriteCache.put(
        SpriteID.valueOf("PL_IDLE_" + i),
        sheet.slice(Vec.ofInt(i * 16, 16), 16, 16)
      );
    }
    
    for (int i = 0; i < 4; ++i) {
      spriteCache.put(
        SpriteID.valueOf("DISC_BIG_" + i),
        sheet.slice(Vec.ofInt(i * 32, 32), 32, 32)
      );
    }
    for (int i = 0; i < 4; ++i) {
      spriteCache.put(
        SpriteID.valueOf("DISC_SEEK_" + i),
        sheet.slice(Vec.ofInt(64 + i * 16, 80), 16, 16)
      );
    }
    for (int i = 0; i < 4; ++i) {
      spriteCache.put(
        SpriteID.valueOf("DISC_" + i),
        sheet.slice(Vec.ofInt(i * 16, 80), 16, 16)
      );
    }
    
    spriteCache.put(
      SpriteID.WEAPON_PISTOL,
      sheet.slice(Vec.ofInt(0, 64), 8, 8)
    );
    spriteCache.put(
      SpriteID.WEAPON_REVOLVER,
      sheet.slice(Vec.ofInt(48, 72), 16, 8)
    );
    spriteCache.put(
      SpriteID.WEAPON_MACHINE_GUN,
      sheet.slice(Vec.ofInt(0, 72), 16, 8)
    );
    spriteCache.put(
      SpriteID.WEAPON_MINI_GUN,
      sheet.slice(Vec.ofInt(96, 64), 16, 16)
    );
    spriteCache.put(
      SpriteID.WEAPON_SHOT_GUN,
      sheet.slice(Vec.ofInt(32, 72), 16, 8)
    );
    spriteCache.put(
      SpriteID.WEAPON_DISC_GUN,
      sheet.slice(Vec.ofInt(80, 72), 16, 8)
    );
    spriteCache.put(
      SpriteID.WEAPON_KATANA_0,
      sheet.slice(Vec.ofInt(56, 96), 16, 8)
    );
    spriteCache.put(
      SpriteID.WEAPON_KATANA_1,
      sheet.slice(Vec.ofInt(48, 96), 8, 16)
    );
    spriteCache.put(
      SpriteID.WEAPON_BAZOOKA,
      sheet.slice(Vec.ofInt(16, 72), 16, 8)
    );
    spriteCache.put(
      SpriteID.WEAPON_MINES,
      sheet.slice(Vec.ofInt(72, 64), 8, 8)
    );
    spriteCache.put(
      SpriteID.WEAPON_FLAME_THROWER,
      sheet.slice(Vec.ofInt(64, 72), 16, 8)
    );
    spriteCache.put(
      SpriteID.WEAPON_LASER_RIFLE,
      sheet.slice(Vec.ofInt(72, 96), 16, 8)
    );
    spriteCache.put(
      SpriteID.WEAPON_LASER_RIFLE_MUZZLE,
      sheet.slice(Vec.ofInt(80, 64), 8, 8)
    );
    
    Graphics.font(font);
    
    initSplashScreen();
  }
  
  Color[] palette = {
    Color.web("#000000ff"), Color.web("#422136ff"),
    Color.web("#7e2553ff"), Color.web("#742f29ff"),
    Color.web("#ff6e59ff"), Color.web("#5f574fff"),
    Color.web("#c2c3c7ff"), Color.web("#fff1e8ff"),
    Color.web("#be1250ff"), Color.web("#ff9d81ff"),
    Color.web("#ffec27ff"), Color.web("#ab5236ff"),
    Color.web("#29adffff"), Color.web("#83768cff"),
    Color.web("#ff77a8ff"), Color.web("#ffccaaff")
  };
  
  int lastHighScore = 0;
  
  enum GameState {
    SPLASH,
    TITLE,
    GAME;
  }
  
  GameState state = GameState.SPLASH;
  
  int time() {
    return (int) Entry.instance.time() / 1000_000_000;
  }
  
  static class ButtonsManager {
    
    public boolean up() {
      return Inputs.key(KeyCode.UP) || Inputs.key(KeyCode.I);
    }
    
    public boolean upOnce()  {
      return Inputs.keyOnce(KeyCode.UP) || Inputs.keyOnce(KeyCode.I);
    }
    
    public boolean down()  {
      return Inputs.key(KeyCode.DOWN) || Inputs.key(KeyCode.K);
    }
    
    public boolean downOnce()  {
      return Inputs.keyOnce(KeyCode.DOWN) || Inputs.keyOnce(KeyCode.K);
    }
    
    public boolean left()  {
      return Inputs.key(KeyCode.LEFT) || Inputs.key(KeyCode.J);
    }
    
    public boolean leftOnce()  {
      return Inputs.keyOnce(KeyCode.LEFT) || Inputs.keyOnce(KeyCode.J);
    }
    
    public boolean right()  {
      return Inputs.key(KeyCode.RIGHT) || Inputs.key(KeyCode.L);
    }
    
    public boolean rightOnce()  {
      return Inputs.keyOnce(KeyCode.RIGHT) || Inputs.keyOnce(KeyCode.L);
    }
    
    public boolean o()  {
      return Inputs.key(KeyCode.C) || Inputs.key(KeyCode.S);
    }
    
    public boolean oOnce()  {
      return Inputs.keyOnce(KeyCode.C) || Inputs.keyOnce(KeyCode.S);
    }
    
    public boolean x()  {
      return Inputs.key(KeyCode.X) || Inputs.key(KeyCode.D);
    }
    
    public boolean xOnce()  {
      return Inputs.keyOnce(KeyCode.X) || Inputs.keyOnce(KeyCode.D);
    }
  }
  
  ButtonsManager buttons = new ButtonsManager();
  
  boolean alwaysPlusWeapon = false;
  boolean alwaysPlusDisc = false;
  
  String version = "0.8";
  
  Vec camera = Vec.zero();
  
  class Player {
    Vec pos = Vec.of(64, 64);
    Vec vel = Vec.zero();
    
    Sprite spr;
  }
  
  Player pl = new Player();
  
  Vec input = Vec.zero();
  
  @Builder
  class Disc {
    @Builder.Default
    final boolean plus = false;
    final int radius;
    final boolean seek;
    
    Vec pos;
    @Builder.Default
    Vec vel = Vec.zero();
    
    int health;
    @Builder.Default
    boolean blood = false;
    
    @Builder.Default
    int hitCooldown = 0;
    int cooldown;
    
    @Builder.Default
    int frame = (int) Math.floor(Math.random() * 8);
  }
  
  List<Disc> discs = new ArrayList<>();
  
  @Builder
  class Bullet implements Lifecycle {
    @Builder.Default
    boolean removed = false;
    
    Sprite sprite;
    
    @Override
    public void start() {
    
    }
    
    @Override
    public void update() {
    
    }
    
    @Override
    public void render() {
    
    }
  }
  
  List<Bullet> bullets = new ArrayList<>();
  
  double boxCooldown = 1;
  Vec weaponGrab = Vec.zero();
  
  int frame = 0;
  
  Vec box = Vec.of(
    Math.random() * 92 + 16,
    Math.random() * 32 + 16
  );
  
  double spawnCooldown = 0;
  double laserCooldown = 0;
  
  boolean dead = false;
  int deadCooldown = 0;
  int restartCooldown = 0;
  
  boolean newHighScore = false;
  boolean showingBest = false;
  int score = 0;
  
  @Builder
  class Particle implements Lifecycle {
    @Builder.Default
    boolean removed = false;
    
    final Runnable tick, draw;
    
    @Override
    public void start() {
    
    }
    
    @Override
    public void update() {
      tick.run();
    }
    
    @Override
    public void render() {
      draw.run();
    }
  }
  
  List<Particle> particles = List.of();
  
  double shake = 0;
  
  boolean flipX = false;
  
  enum WeaponID {
    PISTOL,
    REVOLVER,
    MACHINE_GUN,
    MINI_GUN,
    DUAL_PISTOL,
    SHOT_GUN,
    DISC_GUN,
    KATANA,
    BAZOOKA,
    GRENADE_LAUNCHER,
    MINES,
    FLAME_THROWER,
    LASER_RIFLE
  }
  
  boolean weaponPlusByChance() {
    double plusRand = Math.random();
    return alwaysPlusWeapon || (
      score >= 10 && ((
        score < 50 &&
        plusRand < 0.05 + (score - 10) * 0.005
      ) || plusRand < 0.25)
    );
  }
  
  @Builder
  class Weapon implements Lifecycle {
    final WeaponID id;
    final String name;
    Sprite sprite;
    Vec.Int spriteOffset;
    @Builder.Default
    final boolean plus = weaponPlusByChance();
    @Builder.Default
    final boolean autoFire = false;
    final int firePeriod;
    
    @Builder.Default
    int cooldown = 0;
    
    void baseDraw() {
      double drawX = pl.pos.x;
      double drawY = pl.pos.y - 4 + spriteOffset.y;
      if (flipX) {
        drawX -= 4 + 8 + spriteOffset.x + 8 * (sprite.width - 1);
      }
      else {
        drawX += 4 + spriteOffset.x;
      }
      Graphics.sprite(
        sprite,
        Vec.of(drawX, drawY),
        flipX
      );
    }
    
    @Builder.Default
    final Consumer<Weapon>
      tick = w -> {
      
      },
      draw = w -> {
        baseDraw();
      },
      fire = w -> {
      
      };
    
    @Override
    public void start() {}
    
    @Override
    public void update() {
      tick.accept(this);
    }
    
    @Override
    public void render() {
      draw.accept(this);
    }
  }
  
  List<Supplier<Weapon>> weapons = List.of(
    this::getRevolver,
    this::getMachineGun,
    this::getMiniGun,
    this::getDualPistol,
    this::getShotGun,
    this::getDiscGun,
    this::getKatana,
    this::getBazooka,
    this::getGrenadeLauncher,
    this::getMines,
    this::getFlameThrower,
    this::getLaserRifle
  );
  Weapon currentWeapon = getPistol();
  double weaponCooldown = 0;
  
  void restart() {
    Audios.emit(17);
    
    pl.pos = Vec.of(64, 64);
    pl.vel = Vec.zero();
    pl.spr = spriteCache.get(SpriteID.PL_RUN_0);
    
    input = Vec.zero();
    
    discs.clear();
    bullets.clear();
    
    // weapons = List.of(...);
    currentWeapon = getPistol();
    
    weaponCooldown = 0;
    boxCooldown = 1;
    weaponGrab = Vec.zero();
  
    frame = 0;
    
    box = Vec.of(
      Math.random() * 92 + 16,
      Math.random() * 32 + 16
    );
    
    spawnCooldown = 60;
    laserCooldown = 0;
    
    boxParticles();
    
    dead = false;
    deadCooldown = 0;
    restartCooldown = 0;
    
    newHighScore = false;
    showingBest = false;
    score = 0;
    
    particles.clear();
    
    shake = 0;
    
    flipX = false;
  }
  
  @Override
  public void update() {
    switch (state) {
      case SPLASH -> {
        updateSplashScreen();
      }
      case TITLE -> {
        updateTitleScreen();
      }
      case GAME -> {
        updateGame();
      }
    }
  }
  
  void initGame() {
    Audios.music(13);
    restart();
  }
  
  void addScore() {
    score++;
    if (score > lastHighScore) {
      newHighScore = true;
      lastHighScore = score;
    }
  }
  
  void shake(double value) {
    shake = Math.max(shake, value);
  }
  
  void updateGame() {
    if (Inputs.keyOnce(KeyCode.M)) alwaysPlusWeapon = !alwaysPlusWeapon;
    if (Inputs.keyOnce(KeyCode.N)) alwaysPlusDisc = !alwaysPlusDisc;
    if (Inputs.keyRepeat(KeyCode.B)) {
      addScore();
    }
    
    if (dead) {
      deadCooldown--;
      if (deadCooldown < -120) {
        deadCooldown = 30;
        showingBest = !showingBest;
      }
      restartCooldown = Math.max(restartCooldown - 1, 0);
      
      // restart input
      if (deadCooldown <= 0 && (buttons.oOnce() || buttons.xOnce())) {
        restart();
        return;
      }
    }
    else {
      input = Vec.zero();
      
      if (buttons.leftOnce()) {
        flipX = true;
      }
      if (buttons.rightOnce()) {
        flipX = false;
      }
      
      if (buttons.left()) {
        input.x--;
      }
      if (buttons.right()) {
        input.x++;
      }
      if (buttons.up()) {
        input.y--;
      }
      if (buttons.down()) {
        input.y++;
      }
      
      if (buttons.upOnce()) {
        Audios.emit(18);
      }
      
      if (input.lenSq() > 0) {
        // chording
        input.x *= 0.7071;
        input.y *= 0.7071;
      }
      
      // accelerate
      pl.vel = pl.vel.add(input);
      pl.vel.y += 0.25;
      
      // cap speed
      if (input.len() == 0) {
        if (pl.vel.lenSq() <= 0.1 * 0.1) {
          pl.vel = Vec.zero();
        }
        else {
          double speed = pl.vel.len();
          pl.vel = pl.vel.multiply((speed - 0.1) / speed);
        }
      }
      if (pl.vel.lenSq() > 2 * 2) {
        pl.vel = pl.vel.resize(2);
      }
      
      // step
      pl.pos = pl.pos.add(pl.vel);
      
      // walls bounce
      if (pl.pos.x < 0) {
        pl.pos.x = 0;
        pl.vel.x = Math.abs(pl.vel.x);
      }
      if (pl.pos.x > 128) {
        pl.pos.x = 128;
        pl.vel.x = -Math.abs(pl.vel.x);
      }
      if (pl.pos.y < 0) {
        pl.pos.y = 0;
        pl.vel.y = Math.abs(pl.vel.y);
      }
      if (pl.pos.y > 128) {
        pl.pos.y = 128;
        pl.vel.y = -Math.abs(pl.vel.y);
      }
      
      if (input.lenSq() > 1e-6) {
        // animate
        pl.spr = spriteCache.get(
          runAnimation.get((int) Math.floor(frame / 3d) % runAnimation.size())
        );
      } else {
        pl.spr = spriteCache.get(
          idleAnimation.get((int) Math.floor(frame / 4d) % idleAnimation.size())
        );
      }
      
      // smoke
      if (input.y < 0) {
        Particle spark = oldSpark();
        spark.a = 0;
        spark.cooldown = 5 + Math.floor(Math.random() * 5);
        spark.x = pl.pos.x - 1;
        spark.y = pl.pos.y + 2;
        if (flipX) {
          spark.x += 5;
        } else {
          spark.x -= 5;
        }
        spark.c = palette[7];
        
        particles.add(spark);
      }
      
      // laser autoFire
      if (laserCooldown > 0) {
        laserCooldown--;
        
        if (laserCooldown <= 0) {
          // fire!
          double xStart = pl.pos.x, xEnd = 0;
          if (flipX) {
            xStart -= 4;
            xEnd = 0;
          } else {
            xStart += 4;
            xEnd = 128;
          }
          bullets.add(makeLaser(xStart, xEnd, pl.pos.y, currentWeapon.plus));
          shake(3);
        }
      }
    }
    
    // particle update && finish
    particles.forEach(Particle::update);
    particles.removeIf(p -> p.removed);
    
    // check for box hit
    weaponCooldown = Math.max(weaponCooldown - 0.015, 0);
    boxCooldown = Math.max(boxCooldown - 0.05, 0);
    if (Math.abs(pl.pos.x - box.x) < 8 && Math.abs(pl.pos.y - box.y) < 8) {
      boxParticles(); // exit particles
      double boxA = Math.random();
      box = Vec.of(
        (Math.sin(boxA) * 60 + box.x - 4) % 120 + 4,
        (Math.cos(boxA) * 60 + box.y - 4) % 120 + 4
      );
      boxParticles(); // entry particles
      addScore();
      
      // new weapon!
      List<Weapon> options = new ArrayList<>();
      for (Supplier<Weapon> supplier : weapons) {
        Weapon weapon = supplier.get();
        if (weapon.id != currentWeapon.id && (score > 10 || weapon.id != WeaponID.DISC_GUN)) {
          options.add(weapon);
        }
      }
      currentWeapon = options.get((int) (Math.random() * options.size()));
      if (currentWeapon.id == WeaponID.DISC_GUN) {
        Audios.emit(22);
      } else {
        Audios.emit(14);
      }
      weaponGrab = pl.pos.clone();
      weaponCooldown = 1;
      boxCooldown = 1;
    }
    
    // move discs
    for (Disc d : discs) {
      // cool down || move
      if (d.hitCooldown > 0) {
        d.hitCooldown--;
      }
      if (d.cooldown > 0) {
        d.cooldown -= d.plus ? 2 : 1;
      } else {
        // seeking!
        if (d.seek) {
          d.vel = d.vel.multiply(d.plus ? 0.92 : 0.99);
          if (!d.pos.inRange(pl.pos, 5)) {
            Vec sd = pl.pos.minus(d.pos);
            double sdlen = sd.len();
            d.vel = d.vel.multiply((d.plus ? 0.08 : 0.01) / sdlen);
          }
        }
        d.pos = d.pos.add(d.vel);
        boolean hit = false;
        if (d.pos.x < 0) {
          d.pos.x = 0;
          d.vel.x = Math.abs(d.vel.x);
          hit = true;
        }
        if (d.pos.x > 128) {
          d.pos.x = 128;
          d.vel.x = -Math.abs(d.vel.x);
          hit = true;
        }
        if (d.pos.y < 0) {
          d.pos.y = 0;
          d.vel.y = Math.abs(d.vel.y);
          hit = true;
        }
        if (d.pos.y > 128) {
          d.pos.y = 128;
          d.vel.y = -Math.abs(d.vel.y);
          hit = true;
        }
        if (hit && d.radius > 10) {
          shake(4);
          Audios.emit(15);
        }
        // check for player collision
        if (!dead) {
          if (pl.pos.inRange(d.pos, d.radius)) {
            dead = true;
            deadCooldown = 60;
            restartCooldown = 60;
            Audios.emit(16);
            d.blood = true;
            for (int i = 0; i < 5; ++i) {
              addSparks(d, palette[2], 10, 0, 1);
              addSparks(d, palette[2], 20, 0, 1);
              addSparks(d, palette[2], 30, 0, 1);
              addSparks(d, palette[8], 40, 0, 1);
              addSparks(d, palette[8], 50, 0, 1);
            }
            shake = 15;
          }
        }
      }
      // spin regardless of movement
      d.frame = (d.frame + 1) % 8;
    }
    
    // spawn discs
    if (!dead) {
      spawnCooldown--;
      if (spawnCooldown <= 0) {
        if (score >= 10 || discs.size() < Math.max(score, 1)) {
          Vec spawn = Vec.of(
            Math.random() * 128,
            Math.random() * 128
          );
          
          double plusRand = Math.random();
          boolean plus = alwaysPlusDisc || (
            score >= 10 && (
              (score < 50 && plusRand < 0.05 + (score - 10) * 0.005)
                || plusRand < 0.25
            )
          );
          
          if (Math.random() < 0.5) {
            // three discs
            double angle = Math.random() * 2 * Math.PI, speed = 1.5;
            makeDisc(7, 20, false, 60, spawn, Vec.ofPolar(angle, speed), plus);
            makeDisc(7, 20, false, 75, spawn, Vec.ofPolar(angle, speed), plus);
            makeDisc(7, 20, false, 90, spawn, Vec.ofPolar(angle, speed), plus);
          }
          else {
            if (Math.random() < 0.5) {
              // big disc
              makeDisc(13, 100, false, 60, spawn, Vec.ofPolar(Math.random() * 2 * Math.PI, 0.75), plus);
            }
            else {
              // seeker
              makeDisc(7, 20, true, 60, spawn, Vec.zero(), plus);
            }
          }
        }
        
        spawnCooldown = 180 / (1 + Math.sqrt(score) * 0.1);
      }
      
      frame++;
    }
    
    // fire weapon
    if (currentWeapon.cooldown > 0) {
      // cooldown, don"t fire
      currentWeapon.cooldown--;
    }
    else {
      // fire?
      if (!dead) {
        boolean fire = false;
        if (currentWeapon.autoFire) {
          fire = buttons.x() || buttons.o();
        }
        else {
          fire = buttons.xOnce() || buttons.oOnce();
        }
        
        if (fire) {
          currentWeapon.fire.run();
          currentWeapon.cooldown = currentWeapon.firePeriod;
        }
      }
    }
    
    if (!dead) {
      currentWeapon.update();
    }
    
    // move bullets
    for (Bullet b : bullets) {
      b.update();
    }
    bullets.removeIf(b -> b.removed);
    
    // camera motion
    camera = camera.multiply(0.9)
      .add(pl.pos.minus(Vec.of(64, 64)).multiply(0.0375));
    
    if (shake > 0) {
      double angle = Math.random() * 2 * Math.PI;
      camera = camera.add(Vec.ofPolar(angle, shake / 2));
      shake--;
    }
  }
  
  void makeDisc(int radius, int health, boolean seek, int cooldown, Vec pos, Vec vel, boolean plus) {
    Disc disc = Disc.builder()
      .pos(pos)
      .vel(vel)
      .radius(radius)
      .seek(seek)
      .health(health)
      .cooldown(cooldown)
      .plus(plus)
      .build();
    discs.add(disc);
  }
  
  @Override
  public void render() {
    switch (state) {
      case SPLASH -> {
        drawSplashScreen();
      }
      case TITLE -> {
        drawTitleScreen();
      }
      case GAME -> {
        drawGame();
      }
    }
  }
  
  void drawGame() {
    Graphics.clear(palette[12]);
    Graphics.camera(camera);
    
    // background tiles
    Graphics.tiles(
      gameTiles,
      Vec.of(-64, -64),
      32, 32,
      Set.of(1)
    );
    
    // box strings
    Graphics.line(box.add(-3, 0), Vec.of(box.x - 3, 0), palette[12]);
    Graphics.line(box.add(-2, 0), Vec.of(box.x - 2, 0), palette[13]);
    Graphics.line(box.add(-1, 0), Vec.of(box.x - 1, 0), palette[12]);
    
    Graphics.line(box.add(0, 0), Vec.of(box.x + 0, 0), palette[12]);
    Graphics.line(box.add(1, 0), Vec.of(box.x + 1, 0), palette[13]);
    Graphics.line(box.add(2, 0), Vec.of(box.x + 2, 0), palette[12]);
    
    // background discs
    for (Disc d : discs) {
      if (d.cooldown > 0) {
        Vec r = Vec.of (
          Math.floor(d.pos.x - 0.5),
          Math.floor(d.pos.y - 0.5)
        );
        Color col = palette[d.plus && frame % 32 < 16 ? 11 : 13];
        Graphics.circle(r.add(0, 0), d.cooldown * 0.5 + d.radius, col);
        Graphics.circle(r.add(1, 0), d.cooldown * 0.5 + d.radius, col);
        Graphics.circle(r.add(0, 1), d.cooldown * 0.5 + d.radius, col);
        Graphics.circle(r.add(1, 1), d.cooldown * 0.5 + d.radius, col);
      }
    }
    drawDiscs(true);
    
    // foreground tiles
    Graphics.tiles(
      gameTiles,
      Vec.of(-64, -64),
      32, 32,
      Set.of(2)
    );
    
    // player
    if (!dead) {
      // body
      Graphics.sprite(pl.spr, pl.pos.minus(8, 8), flipX);
      
      // weapon
      if (currentWeapon.plus && frame % 12 < 5) {
        Graphics.map(palette[7], palette[15]);
        Graphics.map(palette[6], palette[4]);
        Graphics.map(palette[5], palette[11]);
        Graphics.map(palette[0], palette[3]);
      }
      
      currentWeapon.draw.run();
      
      Graphics.map(palette[7], palette[7]);
      Graphics.map(palette[6], palette[6]);
      Graphics.map(palette[5], palette[5]);
      Graphics.map(palette[0], palette[0]);
    } else {
      Graphics.circle(pl.pos, 10, palette[2]);
    }
    
    // foreground discs
    drawDiscs(false);
    
    // box
    Graphics.sprite(
      boxSprite,
      Vec.of(
        box.x - 4,
        box.y - 4 + boxCooldown * Math.sin(boxCooldown * -2 * Math.PI) * -8
      )
    );
    
    // bullets
    for (Bullet b : bullets) {
      b.render();
    }
    
    // particles
    for (Particle p : particles) {
      p.render();
    }
    
    // hud
    if (weaponCooldown > 0) {
      double lift = ((1 - weaponCooldown) * 0.0 + 1.0 * (1 - weaponCooldown) * (1 - weaponCooldown)) * -128;
      shadowPrint(
        currentWeapon.name + (currentWeapon.plus ? "++" : ""),
        Vec.of(
          weaponGrab.x - currentWeapon.name.length() * 2,
          weaponGrab.y - 16 + lift
        ),
        palette[(currentWeapon.plus && frame % 12 < 4) ? 4 : 7]
      );
    }
    
    Graphics.camera();
    
    if (!dead) {
      scorePrint(score, Vec.of(64, 2), 2, newHighScore, true);
    }
    else {
      // background
      Graphics.rect(Vec.of(0, 32), Vec.of(128, 98), palette[1]);
      
      // game over
      int write = Math.min(60 - deadCooldown, 30) * 2;
      Graphics.map(palette[7], palette[0]);
      Graphics.easeSprite(
        sheet.slice(Vec.ofInt(1, 122), write, 5),
        Vec.of(64 - write, 32 + 8 + 2),
        write * 2, 10
      );
      Graphics.map(palette[7], palette[7]);
      Graphics.easeSprite(
        sheet.slice(Vec.ofInt(1, 122), write, 5),
        Vec.of(64 - write, 32 + 8),
        write * 2, 10
      );
      
      // boxEnds:/best:
      int[] box = new int[]{33, 116, 37, 5};
      int num = score;
      boolean exclaim = newHighScore;
      if (showingBest) {
        box = new int[]{1, 116, 30, 5};
        num = lastHighScore;
        exclaim = false;
      }
      String numStr = String.valueOf(num);
      int width = box[2] * 2 + 4 + numStr.length() * 7 * 2 + 2;
      if (exclaim) {
        width += 7 * 2;
      }
      write = Math.min(Math.max(30 - deadCooldown, 0), 30) * width / 30d;
      int y = 32 + 8 + 10 + 6;
      Graphics.map(palette[7], palette[0]);
      Graphics.easeSprite(
        sheet.slice(Vec.ofInt(box[0], box[1]), box[2], box[3]),
        Vec.of(
          64 - write * 0.5,
          y + 2
        ),
        box[2] * 2,
        10
      );
      Graphics.map(palette[7], palette[7]);
      Graphics.easeSprite(
        sheet.slice(Vec.ofInt(box[0], box[1]), box[2], box[3]),
        Vec.of(64 - write * 0.5, y),
        box[2] * 2, 10
      );
      scorePrint(num, Vec.of(64 - write * 0.5 + box[2] * 2 + 22, y), 2, exclaim, false);
      Graphics.rect(
        Vec.of(64 + write * 0.5, y),
        Vec.of(y + 128, y + 12),
        palette[1]
      ); // hiding rect
      String message = "DON'T TOUCH THE DISCS!";
      if (newHighScore) {
        message = "NEW HIGH SCORE!";
      }
      else if (score >= 10) {
        message = "ONLY " + (lastHighScore - score + 1) + " TO NEW HIGHSCORE!";
      }
      shadowPrintCentered(message, Vec.of(64, 32 + 12 + 20 + 12), palette[7]);
      shadowPrintCentered("PRESS 🅾️ OR ❎ TO RESTART", Vec.of(64 - 4, 32 + 12 + 20 + 12 + 8), palette[7]);
    }
  }
  
  void drawDiscs(boolean background) {
    for (Disc d : discs) {
      if ((d.cooldown > 0 && background) || (d.cooldown <= 0 && !background)) {
        if (d.cooldown > 0) {
          Graphics.map(palette[0], d.plus && frame % 24 < 18 ? palette[11] : palette[13]);
          Graphics.map(palette[1], palette[12]);
          Graphics.map(palette[7], palette[12]);
          Graphics.map(palette[6], palette[12]);
          Graphics.map(palette[5], palette[12]);
        }
        else if (d.blood || d.hitCooldown > 0) {
          Graphics.map(palette[7], palette[14]);
          Graphics.map(palette[6], palette[8]);
          Graphics.map(palette[5], palette[2]);
        }
        else if (d.plus && frame % 24 < 18) {
          Graphics.map(palette[7], palette[15]);
          Graphics.map(palette[6], palette[4]);
          Graphics.map(palette[5], palette[11]);
        }
        if (!d.seek) {
          if (d.radius > 10) {
            Graphics.sprite(
              spriteCache.get(discBigAnimation.get(d.frame / 2)),
              Vec.of(d.pos.x - 16, d.pos.y - 16),
              d.vel.x < 0
            );
          }
          else {
            Graphics.sprite(
              spriteCache.get(discSeekAnimation.get(d.frame / 2)),
              Vec.of(d.pos.x - 8, d.pos.y - 8),
              d.vel.x < 0
            );
          }
        }
        else {
          Graphics.sprite(
            spriteCache.get(discAnimation.get(d.frame / 2)),
            Vec.of(d.pos.x - 8, d.pos.y - 8),
            d.vel.x < 0
          );
        }
        Graphics.map(palette[0], palette[0]);
        Graphics.map(palette[1], palette[1]);
        Graphics.map(palette[7], palette[7]);
        Graphics.map(palette[6], palette[6]);
        Graphics.map(palette[5], palette[5]);
      }
    }
  }
  
  void prettyPrint(String str, Vec v, Color color) {
    Graphics.print(str, v.add(-1, -1), palette[0]);
    Graphics.print(str, v.add(+0, -1), palette[0]);
    Graphics.print(str, v.add(+1, -1), palette[0]);
    Graphics.print(str, v.add(-1, +0), palette[0]);
    Graphics.print(str, v.add(+1, +0), palette[0]);
    Graphics.print(str, v.add(-1, +1), palette[0]);
    Graphics.print(str, v.add(+0, +1), palette[0]);
    Graphics.print(str, v.add(+1, +1), palette[0]);
    Graphics.print(str, v, color);
  }
  
  void shadowPrint(String str, Vec v, Color color) {
    Graphics.print(str, v.add(0, 1), palette[0]);
    Graphics.print(str, v, color);
  }
  
  void shadowPrintCentered(String str, Vec v, Color color) {
    shadowPrint(str, v.add(str.length() * -2, 0), color);
  }
  
  void scorePrint(int num, Vec v, double scale, boolean exclaim, boolean center) {
    String chars = String.valueOf(num);
    if (exclaim) {
      chars += "!";
    }
    
    for (int i = 0; i < chars.length(); ++i) {
      char ch = chars.charAt(i);
      int sx = (
        ch == '!' 
          ? 10 
          : ch - '0'
      ) * 4 + 1;
      double startX = v.x + 7 * (i - 1) * scale;
      if (center) {
        startX = v.x - chars.length() * 3.5 * scale + 7 * i * scale;
      }
      Graphics.map(palette[7], palette[0]);
      Graphics.easeSprite(
        sheet.slice(Vec.ofInt(sx, 97), 3, 5),
        Vec.of(startX, v.y + 2),
        6 * scale, 5 * scale
      );
      Graphics.map(palette[7], palette[7]);
      Graphics.easeSprite(
        sheet.slice(Vec.ofInt(sx, 97), 3, 5),
        Vec.of(startX, v.y),
        6 * scale, 5 * scale
      );
    }
  }
  
  void knockback(double dx) {
    if (flipX) {
      pl.vel.x += dx;
    }
    else {
      pl.vel.x -= dx;
    }
  }
  
  Weapon getPistol() {
    boolean plus = weaponPlusByChance();
    
    return Weapon.builder()
      .plus(plus)
      .id(WeaponID.PISTOL)
      .name("PISTOL")
      .sprite(spriteCache.get(SpriteID.WEAPON_PISTOL))
      .spriteOffset(Vec.ofInt(-1, 0))
      .firePeriod(6)
      .fire(_ -> {
        Vec b = Vec.of(7, 0);
        Vec v = Vec.of(4, 0);
        if (flipX) {
          b.x *= -1;
          v.x *= -1;
        }
        bullets.add(makeBullet(pl.pos.add(b), v, 10));
        shake(2);
        knockback(1);
        Audios.emit(19);
      })
      .build();
  }
  
  Weapon getRevolver() {
    boolean plus = weaponPlusByChance();
    
    return Weapon.builder()
      .plus(plus)
      .id(WeaponID.REVOLVER)
      .name("REVOLVER")
      .sprite(spriteCache.get(SpriteID.WEAPON_REVOLVER))
      .firePeriod(plus ? 12 : 6)
      .autoFire(plus)
      .fire(_ -> {
        Vec b = Vec.of(7, 0);
        Vec v = Vec.of(4, 0);
        if (flipX) {
          b.x *= -1;
          v.x *= -1;
        }
        Bullet bullet = makeBullet(pl.pos.add(b), v, 50, plus);
        bullet.sprite = spriteCache.get(SpriteID.BULLET_YELLOW_BIG);
        bullets.add(bullet);
        shake(5);
        knockback(1);
        Audios.emit(20);
      })
      .build();
  }
  
  Weapon getMachineGun() {
    boolean plus = weaponPlusByChance();
    
    return Weapon.builder()
      .plus(plus)
      .id(WeaponID.MACHINE_GUN)
      .name("MACHINEGUN")
      .sprite(spriteCache.get(SpriteID.WEAPON_MACHINE_GUN))
      .spriteOffset(Vec.ofInt(-4, 0))
      .autoFire(true)
      .firePeriod(plus ? 3 : 4)
      .fire(_ -> {
        Vec b = Vec.of(7, 0);
        Vec v = Vec.of(4, 0);
        if (flipX) {
          b.x *= -1;
          v.x *= -1;
        }
        for (int py = (plus ? -5 : 0); py <= (plus ? 5 : 0); py += 5) {
          bullets.add(makeBullet(pl.pos.add(b).add(0, py), v, plus ? 20 : 10));
        }
        shake(2);
        knockback(1);
        Audios.emit(19);
      })
      .build();
  }
  
  Weapon getMiniGun() {
    boolean plus = weaponPlusByChance();
    
    return Weapon.builder()
      .plus(plus)
      .id(WeaponID.MINI_GUN)
      .name("MINIGUN")
      .sprite(spriteCache.get(SpriteID.WEAPON_MINI_GUN))
      .spriteOffset(Vec.ofInt(-3, -8))
      .autoFire(true)
      .firePeriod(plus ? 0 : 1)
      .fire(_ -> {
        for (int i = 0; i < (plus ? 6 : 3); ++i) {
          Vec b = Vec.of(11, 0);
          Vec v = Vec.of(
            8,
            (Math.random() * 2 - 1) * (plus ? 8 : 4)
          );
          if (flipX) {
            b.x *= -1;
            v.x *= -1;
          }
          bullets.add(makeBullet(pl.pos.add(b), v, plus ? 50 : 5));
        }
        shake(5);
        knockback(2);
        Audios.emit(19);
      })
      .build();
  }
  
  Weapon getDualPistol() {
    boolean plus = weaponPlusByChance();
    Sprite sprite = spriteCache.get(SpriteID.WEAPON_PISTOL);
    
    return Weapon.builder()
      .plus(plus)
      .id(WeaponID.DUAL_PISTOL)
      .name("DUAL PISTOL")
      .sprite(sprite)
      .spriteOffset(Vec.ofInt(-1, 0))
      .autoFire(plus)
      .firePeriod(6)
      .draw(_ -> {
        double y = pl.pos.y - 4;
        Graphics.sprite(sprite, Vec.of(pl.pos.x + 3, y), false);
        Graphics.sprite(sprite, Vec.of(pl.pos.x - 11, y), true);
      })
      .fire(_ -> {
        Vec b = Vec.of(7, 0);
        Vec v = Vec.of(4, 0);
        bullets.add(makeBullet(pl.pos.add(b), v, 10));
        bullets.add(makeBullet(pl.pos.add(b.mirror()), v, 10));
        if (plus) {
          bullets.add(makeBullet(pl.pos.add(b), Vec.of(v.y, v.x), 10));
          bullets.add(makeBullet(pl.pos.add(b), Vec.of(v.y, -v.x), 10));
          bullets.add(makeBullet(pl.pos.add(b.mirror()), Vec.of(-v.y, v.x), 10));
          bullets.add(makeBullet(pl.pos.add(b.mirror()), Vec.of(-v.y, -v.x), 10));
        }
        shake(2);
        Audios.emit(19);
      })
      .build();
  }
  
  Weapon getShotGun() {
    boolean plus = weaponPlusByChance();
    
    return Weapon.builder()
      .plus(plus)
      .id(WeaponID.SHOT_GUN)
      .name("SHOTGUN")
      .sprite(spriteCache.get(SpriteID.WEAPON_SHOT_GUN))
      .spriteOffset(Vec.ofInt(-5, 1))
      .autoFire(plus)
      .firePeriod(plus ? 10 : 30)
      .fire(_ -> {
        for (int i = 0; i < (plus ? 15 : 9); ++i) {
          Vec b = Vec.of(7, 0);
          Vec v = Vec.of(
            2 + Math.random() * (plus ? 48 : 12),
            (Math.random() * 2 - 1) * (plus ? 4 : 2)
          );
          if (flipX) {
            b.x *= -1;
            v.x *= -1;
          }
          Bullet bullet = makeBullet(pl.pos.add(b), v, 10);
          bullet.drag = 0.7;
          bullets.add(bullet);
        }
        shake(3);
        knockback(2);
        Audios.emit(21);
      })
      .build();
  }
  
  Weapon getDiscGun() {
    boolean plus = weaponPlusByChance();
    
    return Weapon.builder()
      .plus(plus)
      .id(WeaponID.DISC_GUN)
      .name("DISC GUN?!")
      .sprite(spriteCache.get(SpriteID.WEAPON_DISC_GUN))
      .spriteOffset(Vec.ofInt(-2, 0))
      .firePeriod(6)
      .fire(_ -> {
        Vec b = Vec.of(7, 0);
        Vec v = Vec.of(
          3,
          plus ? Math.random() * 2 - 1 : 0
        );
        if (flipX) {
          b.x *= -1;
          v.x *= -1;
        }
        makeDisc(
          7,
          20,
          false,
          0,
          pl.pos.add(b),
          v,
          plus
        );
        shake(2);
        knockback(5);
        Audios.emit(23);
      })
      .build();
  }
  
  Weapon getKatana() {
    boolean plus = weaponPlusByChance();
    
    return Weapon.builder()
      .plus(plus)
      .id(WeaponID.KATANA)
      .name("")
      .sprite(spriteCache.get(SpriteID.WEAPON_KATANA_0))
      .spriteOffset(Vec.ofInt(-4, -1))
      .firePeriod(plus ? 3 : 12)
      .tick(w -> {
        if (w.cooldown == 0) {
          w.sprite = spriteCache.get(SpriteID.WEAPON_KATANA_0);
          w.spriteOffset = Vec.ofInt(-4, -1);
        } else {
          w.sprite = spriteCache.get(SpriteID.WEAPON_KATANA_1);
          w.spriteOffset = Vec.ofInt(3, 0);
        }
      })
      .fire(_ -> {
        double bx = 16
        if (flipX) {
          bx = -bx;
        }
        for (Disc d : discs) {
          if (d.cooldown <= 0) {
            Vec delta = Vec.of(
              Math.max(Math.abs(pl.pos.x + bx - d.pos.x) - 16, 0),
              pl.pos.y - d.pos.y
            );
            if (delta.inRange((d.radius + 8) * (plus ? 2 : 1))) {
              // hit compressed space
              hitDisc(d, 50);
              
              if (flipX) {
                d.vel.x = -Math.abs(d.vel.x);
              } else {
                d.vel.x = Math.abs(d.vel.x);
              }
            }
          }
        }
        shake(2);
        Audios.emit(35);
      })
      .build();
  }
  
  Weapon getBazooka() {
    boolean plus = weaponPlusByChance();
    
    return Weapon.builder()
      .plus(plus)
      .id(WeaponID.BAZOOKA)
      .name("BAZOOKA")
      .sprite(spriteCache.get(SpriteID.WEAPON_BAZOOKA))
      .spriteOffset(Vec.ofInt(-6, 0))
      .autoFire(plus)
      .firePeriod(plus ? 20 : 60)
      .fire(_ -> {
        double bx = 9, vx = 1;
        if (flipX) {
          bx *= -1;
          vx *= -1;
        }
        bullets.add(makeShell(pl.pos.add(bx, 0), Vec.of(vx, 0), plus));
        shake(3);
        Audios.emit(36);
      })
      .build();
  }
  
  Weapon getGrenadeLauncher() {
    boolean plus = weaponPlusByChance();
    
    return Weapon.builder()
      .plus(plus)
      .id(WeaponID.GRENADE_LAUNCHER)
      .name("GRENADE LAUNCHER")
      .sprite(spriteCache.get(SpriteID.WEAPON_GRENADE_LAUNCHER))
      .spriteOffset(Vec.ofInt(-4, 0))
      .autoFire(plus)
      .firePeriod(plus ? 20 : 60)
      .fire(_ -> {
        double bx = 7, vx = 3;
        if (flipX) {
          bx *= -1;
          vx *= -1;
        }
        bullets.add(makeGrenade(pl.pos.add(bx, 0), Vec.of(vx, 0), plus));
        Audios.emit(37);
      })
      .build();
  }
  
  Weapon getMines() {
    boolean plus = weaponPlusByChance();
    
    return Weapon.builder()
      .plus(plus)
      .id(WeaponID.MINES)
      .name("MINES")
      .sprite(spriteCache.get(SpriteID.WEAPON_MINES))
      .firePeriod(plus ? 20 : 60)
      .draw(w -> {
        Graphics.map(palette[8], palette[0]);
        Graphics.sprite(w.sprite, pl.pos.add(-4, 0));
        Graphics.map(palette[8], palette[8]);
      })
      .fire(_ -> {
        bullets.add(makeMine(pl.pos.add(0, 4), plus));
        Audios.emit(38);
      })
      .build();
  }
  
  Weapon getFlameThrower() {
    boolean plus = weaponPlusByChance();
    
    return Weapon.builder()
      .plus(plus)
      .id(WeaponID.FLAME_THROWER)
      .name("FLAMETHROWER")
      .sprite(spriteCache.get(SpriteID.WEAPON_FLAME_THROWER))
      .spriteOffset(Vec.ofInt(-2, 0))
      .autoFire(true)
      .firePeriod(0)
      .fire(_ -> {
        Vec b = Vec.of(7, 0);
        Vec v = Vec.of(
          6 + Math.random() * 3 - 1.5,
          Math.random() * -2
        );
        if (flipX) {
          b.x *= -1;
          v.x *= -1;
        }
        bullets.add(makeFlame(pl.pos.add(b), v, plus));
      })
      .build();
  }
  
  Weapon getLaserRifle() {
    boolean plus = weaponPlusByChance();
    
    return Weapon.builder()
      .plus(plus)
      .id(WeaponID.LASER_RIFLE)
      .name("LASER RIFLE")
      .sprite(spriteCache.get(SpriteID.WEAPON_LASER_RIFLE))
      .spriteOffset(Vec.ofInt(-3, 0))
      .autoFire(plus)
      .firePeriod(plus ? 12 : 60)
      .tick(w -> {
        if (w.cooldown > (plus ? 4 : 30)) {
          // collide with discs
          double hitX = pl.pos.x;
          if (flipX) {
            hitX -= 8;
          }
          else {
            hitX += 8;
          }
          
          for (Disc d : discs) {
            if (d.cooldown <= 0) {
              Vec v = Vec.of(
                Math.max(Math.abs(d.pos.x - hitX) - 4, 0),
                pl.pos.y - d.pos.y
              );
              if (v.inRange(d.radius + 3)) {
                // hit compressed space
                hitDisc(d, 100);
              }
            }
          }
        }
      })
      .draw(w -> {
        if (w.cooldown > (w.plus ? 8 : 30)) {
          w.sprite = spriteCache.get(SpriteID.WEAPON_LASER_RIFLE_MUZZLE);
          w.spriteOffset.x = 5;
          w.baseDraw();
        }
        w.sprite = spriteCache.get(SpriteID.WEAPON_LASER_RIFLE);
        w.spriteOffset.x = -3;
        w.baseDraw();
      })
      .fire(w -> {
        Audios.emit(40);
        laserCooldown = w.plus ? 6 : 30;
      })
      .build();
  }
  
  Bullet makeBullet(Vec pos, Vec vel, int damage, boolean track) {
  
  }
  
  Bullet makeBullet(Vec pos, Vec vel, int damage) {
    makeBullet(pos, vel, damage, false);
  }
}
