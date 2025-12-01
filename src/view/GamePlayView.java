package view;

import controller.AddGold;
import controller.GamePlayController;
import controller.LevelUp;
import model.EnemyType;
import model.GameMap;
import model.Level;
import model.Tower;
import model.User;
import model.Wave;
import util.DBConnection;
import util.GameFont;
import util.NeonButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;



import javafx.animation.AnimationTimer;
import javafx.scene.effect.DropShadow;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class GamePlayView extends StackPane {

    public static final double WIDTH = 1280;
    public static final double HEIGHT = 720;

    private final User user;
    private final Runnable onReturn;

    // config lấy từ DB
    private Level levelConfig;
    private GameMap mapConfig;
    private List<Wave> wavesConfig = new ArrayList<>();
    private Map<Integer, EnemyType> enemyTypesConfig = new HashMap<>();


        // runtime path + enemy
    private List<double[]> pathPoints = new ArrayList<>();
    private final List<EnemyInstance> enemies = new ArrayList<>();

    private List<Tower> availableTowers;
    private Tower selectedTower = null;

    private List<Circle> towerSlots = new ArrayList<>();
    private final Map<Circle, TowerInstance> slotTowers = new HashMap<>();

    private final List<BulletInstance> bullets = new ArrayList<>();
    private final Map<Tower, Double> towerCooldowns = new HashMap<>();
    private final Map<Tower, StackPane> towerIconNodes = new HashMap<>();
    private static final double TOWER_COOLDOWN_SECONDS = 10.0;


    // wave runtime
    private final List<WaveRuntime> waveRuntimes = new ArrayList<>();
    private double elapsedTime = 0.0;     
    private AnimationTimer gameLoop;
    private long lastUpdateNanos = 0L;


    // runtime state 
    private int baseHpCurrent = 100;
    private boolean gameWon = false;
    private boolean gameLost = false;


    // UI
    private Pane mapLayer;
    private Label baseHpLabel;

    public GamePlayView(User user, Runnable onReturn) {
        this.user = user;
        this.onReturn = onReturn;

        loadConfigForUser();   // lấy Level + Map + Waves + EnemyTypes từ DB
        initGameState();  
        initWavesRuntime();    
        buildUI();
        startGameLoop();
    }

    private void loadConfigForUser() {
        int levelId = Math.max(1, user.getBaseLevel());

        try (Connection conn = DBConnection.getConnection()) {
            GamePlayController ctrl = new GamePlayController(conn);

            // Level
            levelConfig = ctrl.loadLevel(levelId);

            // Map
            mapConfig = ctrl.loadMap(levelConfig.getMapId());

            // Waves
            wavesConfig = ctrl.loadWaves(levelId);

            availableTowers = ctrl.loadTowers(user.getId());

            // Enemy types 
            var ids = new java.util.HashSet<Integer>();
            for (Wave w : wavesConfig) {
                ids.add(w.getEnemyTypeId());
            }
            enemyTypesConfig = ctrl.loadEnemyTypes(ids);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGameState() {
        int hp = 100;
        if (mapConfig != null) {
            hp = mapConfig.getBaseHpDefault();
        }
        if (levelConfig != null && levelConfig.getBaseHpOverride() != null) {
            hp = levelConfig.getBaseHpOverride();
        }
        baseHpCurrent = hp;
    }

    private void buildUI() {
        setPrefSize(WIDTH, HEIGHT);

        mapLayer = new Pane();
        mapLayer.setPrefSize(WIDTH, HEIGHT);
        mapLayer.setBackground(new Background(
                new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        // vẽ map
        if (mapConfig != null && mapConfig.getBackgroundUrl() != null) {
            try {
                Image bg = new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream(mapConfig.getBackgroundUrl())));
                ImageView bgView = new ImageView(bg);
                bgView.setFitWidth(WIDTH);
                bgView.setFitHeight(HEIGHT);
                bgView.setPreserveRatio(false);
                mapLayer.getChildren().add(bgView);
            } catch (Exception e) {
                e.printStackTrace(); 
            }
        }

        if (mapConfig != null && mapConfig.getPathPoints() != null) {
            pathPoints = parsePoints(mapConfig.getPathPoints());
        } else {
            pathPoints = new ArrayList<>();
        }

            // vẽ đường đi 
        if (!pathPoints.isEmpty()) {
            Polyline pathLine = createPathShape(pathPoints);
            mapLayer.getChildren().add(pathLine);
        }

        // vẽ các ô đặt tower
        if (mapConfig != null && mapConfig.getTowerSlots() != null) {
            towerSlots = createTowerSlots(mapConfig.getTowerSlots());
            for (Circle slot : towerSlots) {
                slot.setOnMouseClicked(e -> handleSlotClick(slot));
            }
            mapLayer.getChildren().addAll(towerSlots);
        }

        // thanh HP 
        HBox hpBox = new HBox();
        hpBox.setAlignment(Pos.CENTER_RIGHT);
        hpBox.setSpacing(8);
        hpBox.setPadding(new Insets(10, 20, 0, 10));
        hpBox.setStyle("-fx-background-color: linear-gradient(#2b3137,#1e2328);"
          + "-fx-border-color: #2a1294ff;"
          + "-fx-border-width: 2;");

        Label hpText = GameFont.makeTitle("Healh", 22);

        baseHpLabel = new Label(String.valueOf(baseHpCurrent));
        baseHpLabel.setTextFill(Color.LIGHTGREEN);

        hpBox.getChildren().addAll(hpText, baseHpLabel);

        BorderPane topPane = new BorderPane();
        BorderPane.setMargin(hpBox, new Insets(10, 0, 0, 10));
        topPane.setLeft(hpBox);
        topPane.setPickOnBounds(false);

        HBox towerBar = new HBox(12);
        towerBar.setPadding(new Insets(10));
        towerBar.setAlignment(Pos.CENTER);

        for (Tower t : availableTowers) {
            StackPane icon = createTowerButton(t);
            towerIconNodes.put(t, icon);  
            towerBar.getChildren().add(icon);
        }

        //  nút Return 
        NeonButton btnReturn = new NeonButton("Return");
        btnReturn.setOnAction(e -> {
            if (onReturn != null) onReturn.run();
        });
        BorderPane.setAlignment(btnReturn, Pos.TOP_LEFT);
        BorderPane.setMargin(btnReturn, new Insets(10, 0, 0, 10));

        BorderPane overlay = new BorderPane();
        overlay.setTop(topPane);
        overlay.setBottom(towerBar);
        overlay.setLeft(btnReturn);
        overlay.setPickOnBounds(false);

        getChildren().addAll(mapLayer, overlay);
        }


    private Polyline createPathShape(List<double[]> pts) {
            Polyline polyline = new Polyline();
            polyline.setStroke(Color.RED);
            polyline.setStrokeWidth(4);
            polyline.setOpacity(0.7);

            for (double[] p : pts) {
                polyline.getPoints().addAll(p[0], p[1]);
            }
            return polyline;
        }


    private List<Circle> createTowerSlots(String raw) {
        List<Circle> list = new ArrayList<>();
        for (double[] p : parsePoints(raw)) {
            Circle c = new Circle(p[0], p[1], 20);
            c.setStroke(Color.WHITE);
            c.setFill(Color.TRANSPARENT);
            list.add(c);
        }
        return list;
    }

    private List<double[]> parsePoints(String raw) {
        List<double[]> list = new ArrayList<>();
        if (raw == null || raw.isBlank()) return list;

        String[] pairs = raw.split(";");
        for (String s : pairs) {
            String trimmed = s.trim();
            if (trimmed.isEmpty()) continue;
            String[] xy = trimmed.split(",");
            if (xy.length != 2) continue;
            try {
                double x = Double.parseDouble(xy[0].trim());
                double y = Double.parseDouble(xy[1].trim());
                list.add(new double[]{x, y});
            } catch (NumberFormatException ignore) {
            }
        }
        return list;
    }

    private static class WaveRuntime {
            Wave config;
            int spawned = 0;
            double lastSpawnTime = 0;
            boolean completed = false;

            WaveRuntime(Wave config) {
                this.config = config;
            }
        }

    private class EnemyInstance {
            private final EnemyType type;
            private final Circle view;
            private int hp;
            private int segmentIndex = 0;
            private double distanceOnSegment = 0;

            EnemyInstance(EnemyType type) {
                this.type = type;
                this.hp = type.getMaxHp();
                this.view = new Circle(20);

                try {
                    String path = type.getSpriteUrl(); 
                    Image img;
                    InputStream is = getClass().getResourceAsStream(path);
                    if (is != null) {
                        img = new Image(is);
                    } else {
                        img = new Image("file:src/tainguyen/pic/ingameasset/basetower.png"); // fallback
                    }
                    view.setFill(new ImagePattern(img));
                } catch (Exception e) {
                    // nếu lỗi thì dùng màu đỏ 
                    view.setFill(Color.RED);
                }

                if (!pathPoints.isEmpty()) {
                    double[] start = pathPoints.get(0);
                    view.setCenterX(start[0]);
                    view.setCenterY(start[1]);
                }

                if (!pathPoints.isEmpty()) {
                    double[] start = pathPoints.get(0);
                    view.setCenterX(start[0]);
                    view.setCenterY(start[1]);
                }

                mapLayer.getChildren().add(view);
            }

            public EnemyType getType() {
                return type;
            }

            public double getX() { return view.getCenterX(); }
            public double getY() { return view.getCenterY(); }

            // trả true nếu enemy tới base
            public boolean update(double dt) {
                if (pathPoints.size() < 2) return false;

                double move = type.getMoveSpeed() * dt;

                while (move > 0 && segmentIndex < pathPoints.size() - 1) {
                    double[] A = pathPoints.get(segmentIndex);
                    double[] B = pathPoints.get(segmentIndex + 1);

                    double dx = B[0] - A[0];
                    double dy = B[1] - A[1];
                    double segLen = Math.hypot(dx, dy);

                    double remaining = segLen - distanceOnSegment;

                    if (move < remaining) {
                        distanceOnSegment += move;
                        move = 0;
                    } else {
                        move -= remaining;
                        segmentIndex++;
                        distanceOnSegment = 0;

                        if (segmentIndex >= pathPoints.size() - 1) {
                            updateView(B[0], B[1]);
                            return true;
                        }
                    }

                    double t = distanceOnSegment / segLen;
                    double x = A[0] + t * dx;
                    double y = A[1] + t * dy;
                    updateView(x, y);
                }
                return false;
            }

            private void updateView(double x, double y) {
                view.setCenterX(x);
                view.setCenterY(y);
            }

            public void destroy() {
                mapLayer.getChildren().remove(view);
            }
            
            public boolean isDead() {
                return hp <= 0;
            }

            public void takeDamage(int dmg) {
                hp -= dmg;
                if (hp <= 0) {
                    hp = 0;
                    destroy();
                    enemies.remove(this);  
                }
            }
        }

    private void initWavesRuntime() {
        waveRuntimes.clear();
        for (Wave w : wavesConfig) {
            waveRuntimes.add(new WaveRuntime(w));
        }
    }

    public Scene createScene() {
        return new Scene(this, WIDTH, HEIGHT);
    }

    public void decreaseBaseHp(int dmg) {
        if (gameWon || gameLost) return;
        baseHpCurrent = Math.max(0, baseHpCurrent - dmg);
        baseHpLabel.setText(String.valueOf(baseHpCurrent));
        if (baseHpCurrent <= 0) {
            handleLose();
        }
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdateNanos == 0L) {
                    lastUpdateNanos = now;
                    return;
                }
                double dt = (now - lastUpdateNanos) / 1_000_000_000.0;
                lastUpdateNanos = now;
                elapsedTime += dt;
                updateGame(dt);
            }
        };
        gameLoop.start();
    }

    private void updateGame(double dt) {
        updateWaves();        
        updateEnemies(dt);   
        updateTowers(dt);
        updateBullets(dt);
        updateTowerCooldowns(dt);
        checkWinCondition();
    }

    private void updateWaves() {
        for (WaveRuntime wr : waveRuntimes) {
            if (wr.completed) continue;

            Wave w = wr.config;

            if (elapsedTime < w.getStartTime()) continue;

            while (wr.spawned < w.getEnemyCount()
                    && (wr.spawned == 0 || elapsedTime - wr.lastSpawnTime >= w.getSpawnInterval())) {

                EnemyType type = enemyTypesConfig.get(w.getEnemyTypeId());
                if (type == null) break;

                EnemyInstance enemy = new EnemyInstance(type);
                enemies.add(enemy);

                wr.spawned++;
                wr.lastSpawnTime = elapsedTime;
            }

            if (wr.spawned >= w.getEnemyCount()) {
                wr.completed = true;
            }
        }
    }

    private void updateEnemies(double dt) {
        var it = enemies.iterator();
        while (it.hasNext()) {
            EnemyInstance e = it.next();
            boolean reachedBase = e.update(dt);
            if (reachedBase) {
                decreaseBaseHp(e.getType().getDamageToBase());
                e.destroy();
                it.remove();
            }
        }
    }

    private void updateTowers(double dt) {
        for (TowerInstance t : slotTowers.values()) {
            t.update(dt);
        }
    }

    private void updateBullets(double dt) {
        var it = bullets.iterator();
        while (it.hasNext()) {
            BulletInstance b = it.next();
            b.update(dt);
            if (!b.isActive()) {
                it.remove();
            }
        }
    }

    private StackPane createTowerButton(Tower t) {
        StackPane box = new StackPane();
        box.setPrefSize(60, 60);
        box.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-border-color: white;");

        // load image
        ImageView img = null;
        try {
            Image image = new Image(t.getTowerImgUrl(), 50, 50, true, true);
            img = new ImageView(image);
        } catch (Exception e) {
            img = new ImageView(new Image("file:src/pic/goldicon.png", 40, 40, true, true));
        }

        box.getChildren().add(img);

        // click
        box.setOnMouseClicked(e -> {
            if (isTowerOnCooldown(t)) {
                return;
            }
            selectedTower = t;
            highlightTower(box);
        });

        return box;
    }

    private void highlightTower(StackPane selected) {
    // clear highlight
            for (Node n : selected.getParent().getChildrenUnmodifiable()) {
                if (n instanceof StackPane sp) {
                    sp.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-border-color: white;");
                }
            }

            selected.setStyle("-fx-background-color: rgba(0,255,0,0.3); -fx-border-color: yellow; -fx-border-width: 2;");
        }

    private void handleSlotClick(Circle slot) {
        if (selectedTower == null) return;
        if (slotTowers.containsKey(slot)) return;

        // tower đang cooldown thi không cho đặt
        if (isTowerOnCooldown(selectedTower)) return;

        TowerInstance ti = new TowerInstance(selectedTower,
                slot.getCenterX(), slot.getCenterY());
        slotTowers.put(slot, ti);

        // bắt đầu cooldown
        startTowerCooldown(selectedTower);
    }

    private class TowerInstance {
        private final Tower type;
        private final ImageView view;
        private double range = 150;           
        private double attackInterval = 1.0; 
        private double timeSinceLastShot = 0; 
        private int damage;


        TowerInstance(Tower type, double x, double y) {
            this.type = type;
            this.damage = type.getBaseDmg();

            Image img;
            try {
                String path = type.getTowerImgUrl();
                InputStream is = getClass().getResourceAsStream(path);
                if (is != null) {
                    img = new Image(is);
                } else {
                    img = new Image("file:src/tainguyen/pic/ingameasset/default_tower.png");
                    System.out.println("Tower image NOT FOUND: " + path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            view = new ImageView(img);
            view.setFitWidth(48);
            view.setFitHeight(48);
            view.setPreserveRatio(true);
            view.setTranslateX(x - 24);
            view.setTranslateY(y - 24);

            mapLayer.getChildren().add(view);
        }

        public Tower getType() {
            return type;
        }

        public void update(double dt) {
            timeSinceLastShot += dt;

            // luôn tìm mục tiêu để xoay
            EnemyInstance target = findTarget();
            if (target == null) return;

            // xoay tower theo hướng enemy 
            double towerCx = view.getTranslateX() + 24;
            double towerCy = view.getTranslateY() + 24;
            double dx = target.getX() - towerCx;
            double dy = target.getY() - towerCy;

            double angle = Math.toDegrees(Math.atan2(dy, dx)); 
            view.setRotate(angle);

            // cooldown bắn
            if (timeSinceLastShot < attackInterval) return;

            // tạo bullet bay từ tower tới enemy
            bullets.add(new BulletInstance(
                    towerCx,
                    towerCy,
                    target,
                    damage
            ));
            animateShoot();
            timeSinceLastShot = 0;
        }

        private void animateShoot() {
            ScaleTransition st = new ScaleTransition(Duration.seconds(0.08), view);
            st.setFromX(1.0);
            st.setFromY(1.0);
            st.setToX(0.9); 
            st.setToY(0.9);
            st.setAutoReverse(true);
            st.setCycleCount(2); 
            st.play();
        }

        private EnemyInstance findTarget() {
            EnemyInstance best = null;
            double bestDist = Double.MAX_VALUE;

            for (EnemyInstance e : enemies) {
                if (e.isDead()) continue;

                double dx = e.getX() - (view.getTranslateX() + 24);
                double dy = e.getY() - (view.getTranslateY() + 24);
                double dist = Math.hypot(dx, dy);

                if (dist <= range && dist < bestDist) {
                    bestDist = dist;
                    best = e;
                }
            }
            return best;
        }

    }

    private class BulletInstance {
        private final Circle view;
        private EnemyInstance target;
        private final int damage;
        private boolean active = true;
        private double x, y;
        private double speed = 400;

        BulletInstance(double startX, double startY,
                    EnemyInstance target, int damage) {
            this.x = startX;
            this.y = startY;
            this.target = target;
            this.damage = damage;

            view = new Circle(5);
            view.setFill(Color.GOLD);
            view.setStroke(Color.WHITE);
            view.setCenterX(x);
            view.setCenterY(y);

            DropShadow glow = new DropShadow();
            glow.setRadius(10);
            glow.setSpread(0.6);
            glow.setColor(Color.WHITE);

            view.setEffect(glow);

            mapLayer.getChildren().add(view);
        }

        public void update(double dt) {
            if (!active) return;
            if (target == null || target.isDead()) {
                destroy();
                return;
            }

            double tx = target.getX();
            double ty = target.getY();
            double dx = tx - x;
            double dy = ty - y;
            double dist = Math.hypot(dx, dy);

            if (dist < 1e-6) dist = 1e-6;

            if (dist < 10) {
                target.takeDamage(damage);
                destroy();
                return;
            }

            double step = speed * dt;
            if (step > dist) step = dist;

            x += dx / dist * step;
            y += dy / dist * step;

            view.setCenterX(x);
            view.setCenterY(y);
        }

        public void destroy() {
            if (!active) return;
            active = false;
            mapLayer.getChildren().remove(view);
        }

        public boolean isActive() {
            return active;
        }
    }

    private void checkWinCondition() {
        if (gameWon) return;           
        if (baseHpCurrent <= 0) return; // thua rồi thì không xử lý thắng

        boolean allCompleted = true;
        for (WaveRuntime wr : waveRuntimes) {
            if (!wr.completed) {
                allCompleted = false;
                break;
            }
        }
        if (!allCompleted) return;

        if (!enemies.isEmpty()) return;

        // đủ điều kiện thắng
        handleWin();
    }

    private void handleWin() {
        AddGold ag = new AddGold();
        ag.addgold(1000, user.getId());
        LevelUp lu = new LevelUp();
        lu.levelup(user);
        gameWon = true;
        if (gameLoop != null) {
            gameLoop.stop();
        }

        // overlay thắng game
        StackPane overlay = new StackPane();
        overlay.setPickOnBounds(true);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");

        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);

        Label title = new Label("VICTORY!");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");

        Label sub = new Label("Bạn đã phòng thủ thành công.");
        sub.setTextFill(Color.WHITE);
        sub.setStyle("-fx-font-size: 18px;");

        NeonButton btn = new NeonButton("Return");
        btn.setOnAction(e -> {
            if (onReturn != null) {
                onReturn.run();   // quay lại màn trước 
            }
        });

        box.getChildren().addAll(title, sub, btn);
        overlay.getChildren().add(box);

        getChildren().add(overlay); // vẽ đè lên trên map
    }

    private void handleLose() {
        AddGold ag = new AddGold();
        ag.addgold(100, user.getId());
        if (gameLost || gameWon) return;
        gameLost = true;

        if (gameLoop != null) {
            gameLoop.stop();
        }

        StackPane overlay = new StackPane();
        overlay.setPickOnBounds(true);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");

        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);

        Label title = new Label("DEFEAT");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");

        Label sub = new Label("Căn cứ đã bị phá hủy.");
        sub.setTextFill(Color.WHITE);
        sub.setStyle("-fx-font-size: 18px;");

        NeonButton btn = new NeonButton("Return");
        btn.setOnAction(e -> {
            if (onReturn != null) {
                onReturn.run();  // quay lại lobby 
            }
        });

        box.getChildren().addAll(title, sub, btn);
        overlay.getChildren().add(box);

        getChildren().add(overlay);
}

    private boolean isTowerOnCooldown(Tower t) {
        Double remain = towerCooldowns.get(t);
        return remain != null && remain > 0;
    }

    private void startTowerCooldown(Tower t) {
        towerCooldowns.put(t, TOWER_COOLDOWN_SECONDS);

        StackPane icon = towerIconNodes.get(t);
        if (icon != null) {
            icon.setOpacity(0.4);   // làm mờ
            icon.setDisable(true);  // không click được
        }
    }

    private void updateTowerCooldowns(double dt) {
        if (towerCooldowns.isEmpty()) return;

        List<Tower> finished = new ArrayList<>();
        for (Map.Entry<Tower, Double> e : towerCooldowns.entrySet()) {
            double remain = e.getValue() - dt;
            if (remain <= 0) {
                finished.add(e.getKey());
            } else {
                e.setValue(remain);
            }
        }

        // reset icon khi hết cooldown
        for (Tower t : finished) {
            towerCooldowns.remove(t);
            StackPane icon = towerIconNodes.get(t);
            if (icon != null) {
                icon.setOpacity(1.0);
                icon.setDisable(false);
            }
        }
    }


    public Level getLevelConfig() { return levelConfig; }
    public GameMap getMapConfig() { return mapConfig; }
    public java.util.List<Wave> getWavesConfig() { return wavesConfig; }
    public java.util.Map<Integer, EnemyType> getEnemyTypesConfig() { return enemyTypesConfig; }
}


