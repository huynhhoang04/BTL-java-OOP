package view;

import javafx.util.Duration;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import controller.AddTowerToInventory;
import controller.BuyController;
import controller.CheckHaveChest;
import controller.DropItemAfterPull;
import controller.GoldLobbyUpdate;
import controller.LevelUp;
import controller.ListTower;
import controller.PullContrttoller;
import controller.ShowItemInfo;
import controller.ShowItemQty;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import util.GameFont;
import util.NeonButton;
import util.SoundManager;
import model.Item;
import model.Tower;
import model.TowerOnAccount;
import model.User;

public class GameLobbyView {

    private static final String bgUrl = "/tainguyen/pic/lobbybg1.png";
    public static Scene create(double w, double h, User user) {
        BorderPane root = new BorderPane();
        root.setPrefSize(w, h);

        ImageView bg = null;

        if (bgUrl != null && !bgUrl.isBlank()) {
            bg = new ImageView(new Image(bgUrl, true));
            bg.setPreserveRatio(false);
            root.getChildren().add(bg);
        }

        // ===== CENTER: khu trống để sau hiển thị map/preview =====
        StackPane center = new StackPane();
        center.setPadding(new Insets(10));
        root.setCenter(center);

        // ===== RIGHT: hộp gold + cột nút =====
        VBox right = new VBox(16);
        right.setPadding(new Insets(12, 16, 16, 12));
        right.setAlignment(Pos.CENTER_RIGHT);

        // Gold box
        HBox goldBox = new HBox(10);
        goldBox.setAlignment(Pos.CENTER_LEFT);
        goldBox.setPadding(new Insets(8, 14, 8, 14));
        goldBox.setStyle(
            "-fx-background-color: linear-gradient(#2b3137,#1e2328);"
          + "-fx-border-color: #2a1294ff;"
          + "-fx-border-width: 2;"
        );
        VBox accountBox = new VBox(4);
        accountBox.setAlignment(Pos.CENTER);
        accountBox.setPadding(new Insets(8, 12, 8, 12));
        accountBox.setStyle(
            "-fx-background-color: linear-gradient(#2b3137,#1e2328);"
          + "-fx-border-color: #2a1294ff;"
          + "-fx-border-width: 2;"
        );
        ImageView goldIcon = new ImageView("/tainguyen/pic/goldicon.png"); // để trống icon, bạn tự set sau
        goldIcon.setFitWidth(20); goldIcon.setFitHeight(20);
        Label goldLbl = new Label(String.valueOf(user.getGold()));
        Timeline refresher = new Timeline(
            new KeyFrame(Duration.seconds(5), e -> {
                long freshGold = new GoldLobbyUpdate().getGoldById(user.getId());
                goldLbl.setText(String.valueOf(freshGold));
            })
        );
        refresher.setCycleCount(Animation.INDEFINITE);
        refresher.play();
        goldLbl.setFont(GameFont.get(18));
        goldLbl.setTextFill(Color.web("#FFD94A"));
        goldBox.getChildren().addAll(goldIcon, goldLbl);

        // Cột nút: Settings / Map / Info
        VBox rightBtns = new VBox(16);
        rightBtns.setAlignment(Pos.CENTER);
        NeonButton btnSettings = new NeonButton("SETTINGS");
        NeonButton btnGuild = new NeonButton("GUILD");
        NeonButton btnInfo = new NeonButton("INFO");
        btnSettings.setPrefWidth(300);
        btnGuild.setPrefWidth(300);
        btnInfo.setPrefWidth(300);
        rightBtns.getChildren().addAll(btnSettings, btnGuild, btnInfo);

        right.getChildren().addAll(accountBox, goldBox, rightBtns);

        // ===== BOTTOM: thanh đáy =====
        VBox bottom = new VBox(16);
        bottom.setPadding(new Insets(16, 16, 16, 0));
        bottom.setAlignment(Pos.CENTER);
        // account (trái)
        Label accName = new Label(user.getUsername());
        accName.setFont(GameFont.get(16));
        accName.setTextFill(Color.web("#9ecbff"));
        accountBox.getChildren().add(accName);

        Region spacerL = new Region(); HBox.setHgrow(spacerL, Priority.ALWAYS);
        Region spacerR = new Region(); HBox.setHgrow(spacerR, Priority.ALWAYS);

        // To Battle (giữa)
        NeonButton toBattle = new NeonButton("TO BATTLE");
        toBattle.setPrefWidth(300);

        // nhóm nút phải: Shop / Pull / Buy
        VBox rightGroup = new VBox(16);
        rightGroup.setAlignment(Pos.CENTER);
        NeonButton btnInven = new NeonButton("INVENTORY");
        NeonButton btnShop = new NeonButton("SHOP");
        NeonButton btnPull = new NeonButton("PULL");
        NeonButton btnDonate = new NeonButton("DONATE");
        btnInven.setPrefWidth(300);
        btnShop.setPrefWidth(300);
        btnPull.setPrefWidth(300);
        btnDonate.setPrefWidth(300);
        rightGroup.getChildren().addAll(btnInven, btnShop, btnPull, btnDonate);

        bottom.getChildren().addAll(right, toBattle, rightGroup);
        root.setLeft(bottom);
        Scene scene = new Scene(root, w, h);

        // ===== handlers (stub) =====
        toBattle.setOnAction(e -> {
            showBattleOverlay(root, scene, user);
        });

        btnPull.setOnAction(e -> showPullOverlay(root, scene, user));
        btnShop.setOnAction(e -> showShopOverlay(root, scene, user));

        btnSettings.setOnAction(e -> showSettingsOverlay(root, scene, user));
        btnGuild.setOnAction(e -> showGuildOverlay(root, scene));

        btnInfo.setOnAction(e -> showInfoOverlay(root, scene));
        btnInven.setOnAction(e -> {
            ListTower listTower = new ListTower();
            showInventory(root, scene, listTower.listTowers(user));
        });
        btnDonate.setOnAction(e -> showDonate(root, scene));
        return scene;
    }

    private static void showSettingsOverlay(BorderPane root, Scene scene, User user) {
    // 1) Nếu scene root KHÔNG phải StackPane thì bọc vào StackPane
    if (!(scene.getRoot() instanceof StackPane)) {
        BorderPane old = (BorderPane) scene.getRoot();
        StackPane stack = new StackPane(old);
        scene.setRoot(stack);
    }
    StackPane stackRoot = (StackPane) scene.getRoot();

    // 2) Overlay full màn
    StackPane overlay = new StackPane();
    overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
    overlay.setPickOnBounds(true); // chặn click xuyên
    // bind kích thước để phủ kín
    overlay.prefWidthProperty().bind(stackRoot.widthProperty());
    overlay.prefHeightProperty().bind(stackRoot.heightProperty());

    // 3) Panel settings ở GIỮA
    VBox panel = new VBox(12);
    panel.setAlignment(Pos.CENTER);
    panel.setPadding(new Insets(18));
    panel.setMaxSize(420, 240);
    panel.setStyle(
        "-fx-background-color: linear-gradient(#2b3137,#1e2328);"
          + "-fx-border-color: #2a1294ff;"
          + "-fx-border-width: 2;"
    );

    Label title = GameFont.makeTitle("SETTINGS", 28);

    NeonButton musicBtn = new NeonButton(SoundManager.isMuted() ? "MUSIC: OFF" : "MUSIC: ON");
    musicBtn.setPrefWidth(220);
    musicBtn.setOnAction(ev -> {
        SoundManager.toggleMute();
        musicBtn.setText(SoundManager.isMuted() ? "MUSIC: OFF" : "MUSIC: ON");
    });

    NeonButton logoutBtn = new NeonButton("LOG OUT");
    logoutBtn.setPrefWidth(220);
    logoutBtn.setOnAction(ev -> {
        SoundManager.stopBgm();
        Stage st = (Stage) scene.getWindow();
        st.setScene(StartScreen.create(scene.getWidth(), scene.getHeight()));
    });

    NeonButton closeBtn = new NeonButton("CLOSE");
    closeBtn.setPrefWidth(220);
    closeBtn.setOnAction(ev -> stackRoot.getChildren().remove(overlay));

    panel.getChildren().addAll(title, musicBtn, logoutBtn, closeBtn);
    overlay.getChildren().add(panel);

    // 4) Thêm overlay lên trên cùng, căn giữa panel
    stackRoot.getChildren().add(overlay);
    StackPane.setAlignment(panel, Pos.CENTER);
    }


    private  static void showGuildOverlay(BorderPane root, Scene scene){
        if (!(scene.getRoot() instanceof StackPane)) {
        BorderPane old = (BorderPane) scene.getRoot();
        StackPane stack = new StackPane(old);
        scene.setRoot(stack);
        }



        StackPane stackRoot = (StackPane) scene.getRoot();

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
        overlay.setPickOnBounds(true); // chặn click xuyên
        // bind kích thước để phủ kín
        overlay.prefWidthProperty().bind(stackRoot.widthProperty());
        overlay.prefHeightProperty().bind(stackRoot.heightProperty());


        VBox panel = new VBox(12);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(18));
        panel.setMaxSize(420, 240);
        panel.setStyle(
            "-fx-background-color: linear-gradient(#2b3137,#1e2328);"
          + "-fx-border-color: #2a1294ff;"
          + "-fx-border-width: 2;"
        );

        Label lbGuild = GameFont.makeTitle("Đéo có gì đâu mà xem", 26);

        NeonButton closeBtn = new NeonButton("CLOSE");
        closeBtn.setPrefWidth(220);
        closeBtn.setOnAction(ev -> stackRoot.getChildren().remove(overlay));

        panel.getChildren().addAll(lbGuild ,closeBtn);
        overlay.getChildren().add(panel);


        stackRoot.getChildren().add(overlay);
        StackPane.setAlignment(panel, Pos.CENTER);
    }

    private static void showInfoOverlay(BorderPane root, Scene scene){
        if (!(scene.getRoot() instanceof StackPane)) {
        BorderPane old = (BorderPane) scene.getRoot();
        StackPane stack = new StackPane(old);
        scene.setRoot(stack);
        }



        StackPane stackRoot = (StackPane) scene.getRoot();

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
        overlay.setPickOnBounds(true); // chặn click xuyên
        // bind kích thước để phủ kín
        overlay.prefWidthProperty().bind(stackRoot.widthProperty());
        overlay.prefHeightProperty().bind(stackRoot.heightProperty());


        VBox panel = new VBox(12);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(18));
        panel.setMaxSize(900, 400);
        panel.setStyle(
            "-fx-background-color: linear-gradient(#2b3137,#1e2328);"
          + "-fx-border-color: #2a1294ff;"
          + "-fx-border-width: 2;"
        );

        Label lbInfo = GameFont.makeTitle("Project của em huynh, hãy donate để em huynh có tiền đi ăn hàu", 26);

        NeonButton closeBtn = new NeonButton("CLOSE");
        closeBtn.setPrefWidth(220);
        closeBtn.setOnAction(ev -> stackRoot.getChildren().remove(overlay));

        panel.getChildren().addAll(lbInfo,closeBtn);
        overlay.getChildren().add(panel);


        stackRoot.getChildren().add(overlay);
        StackPane.setAlignment(panel, Pos.CENTER);

    }

    public static void showPullOverlay(BorderPane root, Scene scene, User user) {
    if (!(scene.getRoot() instanceof StackPane)) {
        BorderPane old = (BorderPane) scene.getRoot();
        StackPane stack = new StackPane(old);
        scene.setRoot(stack);
    }
    StackPane stackRoot = (StackPane) scene.getRoot();

    // Overlay phủ mờ
    StackPane overlay = new StackPane();
    overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
    overlay.setPickOnBounds(true);
    overlay.prefWidthProperty().bind(stackRoot.widthProperty());
    overlay.prefHeightProperty().bind(stackRoot.heightProperty());

    // Panel chính
    VBox panel = new VBox(16);
    panel.setAlignment(Pos.CENTER);
    panel.setPadding(new Insets(18));
    panel.setMaxSize(900, 420);
    panel.setStyle(
        "-fx-background-color: linear-gradient(#2b3137,#1e2328);"
          + "-fx-border-color: #2a1294ff;"
          + "-fx-border-width: 2;"
    );

    // Title
    Label title = GameFont.makeTitle("PULL", 26);

    // 4 ô LỰA CHỌN CHEST (Bronze / Silver / Gold / Legend)
    HBox choiceRow = new HBox(20);
    choiceRow.setAlignment(Pos.CENTER);
    choiceRow.setPadding(new Insets(8, 0, 6, 0));

    Label note = new Label();
    note.setTextFill(Color.web("#7cff00"));
    note.setVisible(false);

    ToggleGroup chestGroup = new ToggleGroup(); // để chỉ chọn 1 loại

    ToggleButton bronze = makeChestToggle("BRONZE", 1);  bronze.setToggleGroup(chestGroup);
    ToggleButton silver  = makeChestToggle("SILVER", 2);  silver.setToggleGroup(chestGroup);
    ToggleButton gold    = makeChestToggle("GOLD", 3);    gold.setToggleGroup(chestGroup);
    ToggleButton legend  = makeChestToggle("LEGEND", 4);  legend.setToggleGroup(chestGroup);

    choiceRow.getChildren().addAll(bronze, silver, gold, legend);

    // Nút PULL (để trống xử lý – bạn tự code logic sau)
    NeonButton btnPull = new NeonButton("PULL");
    btnPull.setPrefWidth(220);
    // ví dụ nếu muốn disable khi chưa chọn: (bạn có thể bỏ nếu không cần)
    btnPull.disableProperty().bind(chestGroup.selectedToggleProperty().isNull());

    btnPull.setOnAction(e ->{
       Random rd = new Random();
       int min = 1;
       int max = 5;
       int rdtier = rd.nextInt((max - min) + 1) + min;
        System.out.println(rdtier);
        ToggleButton sel = (ToggleButton) chestGroup.getSelectedToggle();
        int tier = (int) sel.getUserData();
        System.out.println(tier);
        CheckHaveChest check = new CheckHaveChest();
        if(check.checkchest(user, tier)){
           PullContrttoller pull = new PullContrttoller();
           Tower tower = pull.HandlePull(rdtier);
           AddTowerToInventory aToInventory = new AddTowerToInventory();
           System.out.println(tower.getId());
           System.out.println(user.getId());
           showReciveTower(root, scene, tower, aToInventory.add(user.getId(), tower.getId()));
           DropItemAfterPull afterPull = new DropItemAfterPull();
           afterPull.dropitem(user, tier);
        }
        else{
            note.setText("Nap tien di em");
            note.setTextFill(Color.web("#ff5252"));
            note.setVisible(true);
        }
    });

    // Nút CLOSE
    NeonButton btnClose = new NeonButton("CLOSE");
    btnClose.setPrefWidth(220);
    btnClose.setOnAction(e -> stackRoot.getChildren().remove(overlay));

    // Footer: PULL trên, CLOSE dưới (đúng phác thảo)
    VBox footer = new VBox(10, note,  btnPull, btnClose);
    footer.setAlignment(Pos.CENTER);

    // Lắp vào panel & overlay
    panel.getChildren().addAll(title, choiceRow, footer);
    overlay.getChildren().add(panel);
    stackRoot.getChildren().add(overlay);
    StackPane.setAlignment(panel, Pos.CENTER);
}

    private static ToggleButton makeChestToggle(String text, int tier) {
        ToggleButton t = new ToggleButton(text);
        t.setPrefSize(150, 120);
        t.setFocusTraversable(false);
        t.setUserData(Integer.valueOf(tier));
        t.setStyle(
            "-fx-background-color:#222a30; -fx-text-fill:#ffd94a; -fx-font-size:16;" +
            "-fx-background-radius:12; -fx-border-radius:12;" +
            "-fx-border-color:#4682B4; -fx-border-width:2;"
        );
        // đổi border khi selected
        t.selectedProperty().addListener((obs, a, b) -> {
            if (b) {
                t.setStyle(
                    "-fx-background-color:#28323a; -fx-text-fill:#ffd94a; -fx-font-size:16;" +
                    "-fx-background-radius:12; -fx-border-radius:12;" +
                    "-fx-border-color:#87CEEB; -fx-border-width:3;"
                );
            } else {
                t.setStyle(
                    "-fx-background-color:#222a30; -fx-text-fill:#ffd94a; -fx-font-size:16;" +
                    "-fx-background-radius:12; -fx-border-radius:12;" +
                    "-fx-border-color:#4682B4; -fx-border-width:2;"
                );
            }
        });
        return t;
    }


    public static void showShopOverlay(BorderPane root, Scene scene, User user){
        if (!(scene.getRoot() instanceof StackPane)) {
        BorderPane old = (BorderPane) scene.getRoot();
        StackPane stack = new StackPane(old);
        scene.setRoot(stack);
        }



        StackPane stackRoot = (StackPane) scene.getRoot();

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
        overlay.setPickOnBounds(true); // chặn click xuyên
        // bind kích thước để phủ kín
        overlay.prefWidthProperty().bind(stackRoot.widthProperty());
        overlay.prefHeightProperty().bind(stackRoot.heightProperty());


        VBox panel = new VBox(12);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(18));
        panel.setMaxSize(900, 400);
        panel.setStyle(
             "-fx-background-color: linear-gradient(#2b3137,#1e2328);"
            + "-fx-border-color: #2a1294ff;"
            + "-fx-border-width: 2;"
        );


        Label lbInfo = GameFont.makeTitle("WELCOME TO SHOP", 26);

        HBox itemsRow = new HBox(20);
        itemsRow.setAlignment(Pos.CENTER);
        itemsRow.setPadding(new Insets(8, 0, 6, 0));
        ShowItemInfo showinfo = new ShowItemInfo();
        ShowItemQty showqty = new ShowItemQty();
        System.out.println(user.getId());
        itemsRow.getChildren().addAll(
            makeShopCard(showinfo.HandleDisplay(1).getItemName(), showinfo.HandleDisplay(1).getPrice(), showqty.HandleTakeQty(user.getId(), 1).getQty(), user, showinfo.HandleDisplay(1)),
            makeShopCard(showinfo.HandleDisplay(2).getItemName(), showinfo.HandleDisplay(2).getPrice(), showqty.HandleTakeQty(user.getId(), 2).getQty(), user, showinfo.HandleDisplay(2)),
            makeShopCard(showinfo.HandleDisplay(3).getItemName(), showinfo.HandleDisplay(2).getPrice(), showqty.HandleTakeQty(user.getId(), 3).getQty(), user, showinfo.HandleDisplay(3)),
            makeShopCard(showinfo.HandleDisplay(4).getItemName(), showinfo.HandleDisplay(4).getPrice(), showqty.HandleTakeQty(user.getId(), 4).getQty(), user, showinfo.HandleDisplay(4))
        );

        NeonButton closeBtn = new NeonButton("CLOSE");
        closeBtn.setPrefWidth(220);
        closeBtn.setOnAction(ev -> stackRoot.getChildren().remove(overlay));

        panel.getChildren().addAll(lbInfo, itemsRow ,closeBtn);
        overlay.getChildren().add(panel);


        stackRoot.getChildren().add(overlay);
        StackPane.setAlignment(panel, Pos.CENTER);
    }

    private static VBox makeShopCard(String name, String priceText, String qtyText, User user, Item item) {
    // Khung thumbnail (placeholder)
        StackPane thumb = new StackPane();
        thumb.setPrefSize(110, 90);
        thumb.setStyle(
            "-fx-background-color:#222a30; -fx-background-radius:12;" +
            "-fx-border-color:#4682B4; -fx-border-radius:12; -fx-border-width:2;"
        );

        if (item.getItemImgUrl() != null && !item.getItemImgUrl().isBlank()) {
            ImageView iv = new ImageView(new Image(item.getItemImgUrl(), true));
            iv.setPreserveRatio(true);
            iv.setFitWidth(300);
            iv.setFitHeight(240);
            thumb.getChildren().add(iv);
        }

        Label lbName  = GameFont.makeTitle(name, 18);
        Label lbPrice = new Label(priceText);
        lbPrice.setStyle("-fx-text-fill:#cfd8dc; -fx-font-size:14;");

        Label lbQty   = new Label(qtyText);
        lbQty.setStyle("-fx-text-fill:#9ccc65; -fx-font-size:13;");

        Label note = new Label();
        note.setTextFill(Color.web("#7cff00"));
        note.setVisible(false);

        NeonButton btnBuy = new NeonButton("BUY");
        btnBuy.setPrefWidth(110);

        btnBuy.setOnAction(e -> {
        BuyController buy = new BuyController();
        long currrentgold = user.getGold();
        int currentprice = item.getIntPrice();
    
        if(currrentgold >= currentprice){
            buy.HandleBuy(user, item);
            note.setText("Thành công");
            note.setTextFill(Color.web("#7cff00"));
            note.setVisible(true);
        }
        else{
            note.setText("Nap tien di em");
            note.setTextFill(Color.web("#ff5252"));
            note.setVisible(true);
        }
        });

        HBox qtyRow = new HBox(6, new Label(""), lbQty);
        qtyRow.setAlignment(Pos.CENTER);

        VBox card = new VBox(8, thumb, lbName, lbPrice, qtyRow, note,  btnBuy);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(160);
        return card;
    }

    private static void showReciveTower(BorderPane root, Scene scene, Tower tower, TowerOnAccount towerOnAccount){
        if (!(scene.getRoot() instanceof StackPane)) {
            BorderPane old = (BorderPane) scene.getRoot();
            StackPane stack = new StackPane(old);
            scene.setRoot(stack);
        }
        StackPane stackRoot = (StackPane) scene.getRoot();

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
        overlay.setPickOnBounds(true);
        overlay.prefWidthProperty().bind(stackRoot.widthProperty());
        overlay.prefHeightProperty().bind(stackRoot.heightProperty());

        VBox panel = new VBox(14);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(18));
        panel.setMaxSize(700, 520);
        panel.setStyle(
            "-fx-background-color: linear-gradient(#2b3137,#1e2328);" +
            "-fx-background-radius:16; -fx-border-radius:16;" +
            "-fx-border-color:#a7ff00; -fx-border-width:2;"
        );

        // khung ảnh
        StackPane thumbWrap = new StackPane();
        thumbWrap.setPrefSize(320, 260);
        thumbWrap.setStyle(
            "-fx-background-color:#222a30; -fx-background-radius:14;" +
            "-fx-border-color:#a7ff00; -fx-border-width:2; -fx-border-radius:14;"
        );

        if (tower.getTowerImgUrl() != null && !tower.getTowerImgUrl().isBlank()) {
            ImageView iv = new ImageView(new Image(tower.getTowerImgUrl(), true));
            iv.setPreserveRatio(true);
            iv.setFitWidth(300);
            iv.setFitHeight(240);
            thumbWrap.getChildren().add(iv);
        }

        String name = tower.getTowerName();
        System.out.println(name);

        Label nameLbl = GameFont.makeTitle(name, 24);

        NeonButton closeBtn = new NeonButton("ADD TO INVENTORY");
        closeBtn.setPrefWidth(220);
        closeBtn.setOnAction(e -> stackRoot.getChildren().remove(overlay));

        panel.getChildren().addAll(thumbWrap, nameLbl, closeBtn);
        overlay.getChildren().add(panel);

        stackRoot.getChildren().add(overlay);
        StackPane.setAlignment(panel, Pos.CENTER);
    }


    private static void showInventory(BorderPane root, Scene scene, List<Tower> towers){
    if (!(scene.getRoot() instanceof StackPane)) {
        scene.setRoot(new StackPane(scene.getRoot()));
    }
    StackPane stackRoot = (StackPane) scene.getRoot();

    // --- Overlay tối phủ toàn màn hình ---
    StackPane overlay = new StackPane();
    overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
    overlay.setPickOnBounds(true);
    overlay.prefWidthProperty().bind(stackRoot.widthProperty());
    overlay.prefHeightProperty().bind(stackRoot.heightProperty());

    // --- Panel chính ---
    VBox panel = new VBox(16);
    panel.setAlignment(Pos.CENTER);
    panel.setPadding(new Insets(20));
    panel.setMaxSize(900, 480);
    panel.setStyle(
        "-fx-background-color: linear-gradient(#2b3137,#1e2328);"
          + "-fx-border-color: #2a1294ff;"
          + "-fx-border-width: 2;"
    );

    Label title = GameFont.makeTitle("INVENTORY", 28);

    // --- Card center: ảnh + name + dmg ---
    VBox card = new VBox(8);
    card.setAlignment(Pos.CENTER);
    card.setMinSize(260, 260);
    card.setStyle(
        "-fx-background-color:#222a30; -fx-background-radius:14;" +
        "-fx-border-color:#87CEEB; -fx-border-radius:14; -fx-border-width:2;"
    );

    ImageView img = new ImageView();
    img.setFitWidth(160);
    img.setFitHeight(140);
    img.setPreserveRatio(true);

    Label lbName = GameFont.makeTitle("Name", 20);
    Label lbDmg  = new Label("DMG: 0");
    lbDmg.setStyle("-fx-text-fill:#cfd8dc; -fx-font-size:16;");
    card.getChildren().addAll(img, lbName, lbDmg);

    // --- 2 nút mũi tên trái/phải ---
    NeonButton btnPrev = new NeonButton("<");
    NeonButton btnNext = new NeonButton(">");
    btnPrev.setPrefWidth(60);
    btnNext.setPrefWidth(60);

    HBox middle = new HBox(16, btnPrev, card, btnNext);
    middle.setAlignment(Pos.CENTER);

    // --- Nút Close ---
    NeonButton btnClose = new NeonButton("CLOSE");
    btnClose.setPrefWidth(220);
    btnClose.setOnAction(e -> stackRoot.getChildren().remove(overlay));

    // --- Logic duyệt danh sách ---
    final int[] idx = {0};

    Runnable refresh = () -> {
        if (towers == null || towers.isEmpty()) {
            lbName.setText("Empty");
            lbDmg.setText("DMG: --");
            img.setImage(null);
            return;
        }
        if (idx[0] < 0) idx[0] = towers.size() - 1;
        if (idx[0] >= towers.size()) idx[0] = 0;

        Tower t = towers.get(idx[0]);
        lbName.setText(t.getTowerName());
        lbDmg.setText("ATK: " + t.getBaseDmg());
        
        Image image = new Image(t.getTowerImgUrl());
        img.setImage(image);
    };

    btnPrev.setOnAction(e -> { idx[0]--; refresh.run(); });
    btnNext.setOnAction(e -> { idx[0]++; refresh.run(); });

    // lần đầu
    refresh.run();

    panel.getChildren().addAll(title, middle, btnClose);
    overlay.getChildren().add(panel);
    stackRoot.getChildren().add(overlay);
    StackPane.setAlignment(panel, Pos.CENTER);
    }

    private static void showBattleOverlay(BorderPane root, Scene scene, User user) {
        // 1) Nếu scene root KHÔNG phải StackPane thì bọc vào StackPane
        if (!(scene.getRoot() instanceof StackPane)) {
            BorderPane old = (BorderPane) scene.getRoot();
            StackPane stack = new StackPane(old);
            scene.setRoot(stack);
        }
        StackPane stackRoot = (StackPane) scene.getRoot();

        // 2) Overlay che toàn màn
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
        overlay.setPickOnBounds(true);
        overlay.prefWidthProperty().bind(stackRoot.widthProperty());
        overlay.prefHeightProperty().bind(stackRoot.heightProperty());

        // 3) Panel ở giữa
        VBox panel = new VBox(18);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(24));
        panel.setMaxSize(420, 260);
        panel.setStyle(
               "-fx-background-color: linear-gradient(#2b3137,#1e2328);"
          + "-fx-border-color: #2a1294ff;"
          + "-fx-border-width: 2;"
        );

        LevelUp lu = new LevelUp();
        int level = lu.getCurrentLevel(user);
        Label title = GameFont.makeTitle("LEVEL " + level, 28);

        NeonButton btnPlay = new NeonButton("PLAY");
        btnPlay.setPrefWidth(220);

        NeonButton btnReturn = new NeonButton("RETURN");
        btnReturn.setPrefWidth(220);

        // RETURN: đóng overlay, về lại Lobby (không đổi scene)
        btnReturn.setOnAction(ev -> stackRoot.getChildren().remove(overlay));

        // PLAY: sang GamePlayView (map tương ứng base_level)
        btnPlay.setOnAction(ev -> {
            Stage stage = (Stage) scene.getWindow();
            Scene backScene = scene; // scene Lobby để quay lại
            GamePlayView gp = new GamePlayView(user, () -> stage.setScene(backScene));
            stage.setScene(gp.createScene());
            stackRoot.getChildren().remove(overlay);
        });

        panel.getChildren().addAll(title, btnPlay, btnReturn);
        overlay.getChildren().add(panel);

        stackRoot.getChildren().add(overlay);
        StackPane.setAlignment(panel, Pos.CENTER);
    }

    private static void showDonate(BorderPane root, Scene scene){
            if (!(scene.getRoot() instanceof StackPane)) {
        BorderPane old = (BorderPane) scene.getRoot();
        StackPane stack = new StackPane(old);
        scene.setRoot(stack);
        }



        StackPane stackRoot = (StackPane) scene.getRoot();

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
        overlay.setPickOnBounds(true); // chặn click xuyên
        // bind kích thước để phủ kín
        overlay.prefWidthProperty().bind(stackRoot.widthProperty());
        overlay.prefHeightProperty().bind(stackRoot.heightProperty());


        VBox panel = new VBox(12);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(18));
        panel.setMaxSize(420, 240);
        panel.setStyle(
            "-fx-background-color: linear-gradient(#2b3137,#1e2328);"
          + "-fx-border-color: #2a1294ff;"
          + "-fx-border-width: 2;"
        );

        Label lbGuild = GameFont.makeTitle("", 26);
        ImageView iv = new ImageView("/tainguyen/pic/donate.jpg");

        NeonButton closeBtn = new NeonButton("CLOSE");
        closeBtn.setPrefWidth(220);
        closeBtn.setOnAction(ev -> stackRoot.getChildren().remove(overlay));

        panel.getChildren().addAll(lbGuild, iv ,closeBtn);
        overlay.getChildren().add(panel);


        stackRoot.getChildren().add(overlay);
        StackPane.setAlignment(panel, Pos.CENTER);
    }
}