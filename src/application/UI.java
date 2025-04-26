package application;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class UI extends Application {
    private VBox chatPane;
    private boolean chatVisible = true;
    private static final double CHAT_WIDTH = 300;
    private Scene mainScene;

    @Override
    public void start(Stage primaryStage) {
        String cssPath = new File("resource/styles/UI.css").toURI().toString();

        // Splash
        ImageView bgView = new ImageView(new Image(new File("resource/image/test.jpg").toURI().toString()));
        bgView.setFitWidth(800);
        bgView.setFitHeight(600);
        bgView.setPreserveRatio(true);

        StackPane splashRoot = new StackPane(bgView);
        splashRoot.getStyleClass().add("pane");
        Button startBtn = new Button("Bắt đầu");
        startBtn.getStyleClass().addAll("btn", "btn-primary");
        splashRoot.getChildren().add(startBtn);
        StackPane.setAlignment(startBtn, Pos.BOTTOM_CENTER);
        StackPane.setMargin(startBtn, new Insets(0,0,40,0));

        Scene splashScene = new Scene(splashRoot, 800, 600);
        splashScene.getStylesheets().add(cssPath);
        primaryStage.setScene(splashScene);
        primaryStage.setTitle("Chào mừng");
        primaryStage.show();

        startBtn.setOnAction(e -> {
            BorderPane root = buildMainUI();
            root.setPadding(new Insets(30));
            mainScene = new Scene(root, 1200, 800);
            mainScene.getStylesheets().add(cssPath);
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Skin Care Recommender");
        });
    }

    private BorderPane buildMainUI() {
        // Sidebar
        VBox sidebar = buildSidebar();

        // Chat pane
        chatPane = createChatPane();
        chatPane.setPrefWidth(CHAT_WIDTH);

        // Toggle small strip
        Button toggleBtn = new Button("<");
        toggleBtn.setPrefWidth(0);
        toggleBtn.setMaxHeight(Double.MAX_VALUE);
        // Match chat background and no rounding when visible
        toggleBtn.setStyle("-fx-background-color: #D0E8F2; -fx-background-radius: 0;");

        toggleBtn.setOnAction(ev -> {
            chatVisible = !chatVisible;
            chatPane.setVisible(chatVisible);
            chatPane.setManaged(chatVisible);
            if (chatVisible) {
                // show chat: no rounding
                toggleBtn.setText("<");
                toggleBtn.setStyle("-fx-background-color: #D0E8F2; -fx-background-radius: 0;");
            } else {
                // hide chat: round left corners
                toggleBtn.setText(">");
                toggleBtn.setStyle("-fx-background-color: #D0E8F2; -fx-background-radius: 30 0 0 30;");
            }
        });

        // Aggregated content
        VBox agg = createAggPane();
        HBox.setHgrow(agg, Priority.ALWAYS);

        // layout
        HBox contentBox = new HBox(chatPane, toggleBtn, agg);
        contentBox.setAlignment(Pos.TOP_LEFT);

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        root.setCenter(contentBox);
        root.setRight(sidebar);
        return root;
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setPrefWidth(160);
        sidebar.getStyleClass().add("sidebar");
        String[][] info = {{"Dashboard","btn-dashboard"},{"Orders","btn-orders"},{"Products","btn-products"},{"Customers","btn-customers"}};
        ToggleGroup tg = new ToggleGroup();
        for (String[] b: info) {
            ToggleButton btn = new ToggleButton(b[0]);
            btn.getStyleClass().addAll("toggle-button", b[1]);
            btn.setToggleGroup(tg);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setBackground(Background.EMPTY);
            btn.setBorder(Border.EMPTY);
            sidebar.getChildren().add(btn);
        }
        return sidebar;
    }

    private VBox createChatPane() {
        VBox pane = new VBox(10);
        pane.getStyleClass().addAll("pane","pane-card");
        pane.setPadding(new Insets(15));
        pane.setStyle("-fx-background-color: #D0E8F2; -fx-background-radius: 30 0 0 30;");

        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefHeight(200);
        chatArea.setStyle("-fx-background-color: #D0E8F2; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        VBox.setVgrow(chatArea, Priority.ALWAYS);

        TextField input = new TextField();
        input.setPromptText("Nhập tin nhắn...");
        input.setPrefHeight(40);
        input.setStyle("-fx-background-radius: 8px; -fx-border-radius: 8px;");

        Button sendBtn = new Button("Gửi");
        sendBtn.getStyleClass().addAll("btn","btn-primary");
        sendBtn.setStyle("-fx-background-radius: 8px; -fx-border-radius: 8px;");

        HBox inputBox = new HBox(10, input, sendBtn);
        inputBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(input, Priority.ALWAYS);

        pane.getChildren().addAll(chatArea, inputBox);
        return pane;
    }

    private VBox createAggPane() {
        VBox agg = new VBox(15);
        agg.setAlignment(Pos.TOP_LEFT);
        agg.getStyleClass().add("sales-container");
        agg.setPadding(new Insets(10));
        agg.setStyle("-fx-background-radius: 0;");

        TextField search = new TextField();
        search.setPromptText("Tìm kiếm sản phẩm...");
        search.getStyleClass().add("text-input");
        HBox.setHgrow(search, Priority.ALWAYS);
        Button sBtn = new Button("Tìm"); sBtn.getStyleClass().addAll("btn","btn-success");
        HBox searchBox = new HBox(10, search, sBtn);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> filter = new ComboBox<>(); filter.getItems().addAll("Tất cả","Serum","Kem dưỡng","Toner"); filter.setValue("Tất cả"); filter.getStyleClass().add("combo-box");
        ComboBox<String> sort = new ComboBox<>(); sort.getItems().addAll("Giá tăng dần","Giá giảm dần","Tên A-Z","Tên Z-A"); sort.setValue("Sắp xếp"); sort.getStyleClass().add("combo-box");
        HBox fsBox = new HBox(10, filter, sort); fsBox.setAlignment(Pos.CENTER_LEFT);

        VBox products = createProductPane();
        agg.getChildren().addAll(searchBox, fsBox, products);
        return agg;
    }

    private VBox createProductPane() {
        FlowPane flow = new FlowPane(15,15);
        flow.setPrefWrapLength(800);
        flow.setStyle("-fx-background-color:#FFFBF0;");
        for(int i=1;i<=100;i++){
            Label nm=new Label("Sản phẩm "+i);nm.getStyleClass().add("product-name");
            Label pr=new Label("Giá: ...");pr.getStyleClass().add("product-price");
            flow.getChildren().add(new ProductCard(nm, pr));
        }
        VBox wrap=new VBox(flow);wrap.setPadding(new Insets(15));wrap.setStyle("-fx-background-color:#FFFBF0; -fx-border-width:0;");
        ScrollPane sp=new ScrollPane(wrap);sp.setFitToWidth(true);sp.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");VBox.setVgrow(sp,Priority.ALWAYS);
        VBox p=new VBox(sp);p.setPadding(new Insets(15));p.setAlignment(Pos.TOP_LEFT);return p;
    }

    static class ProductCard extends VBox{
        public ProductCard(Label name, Label price){
            setAlignment(Pos.CENTER);setPadding(new Insets(10));getStyleClass().add("product-card");
            Button c=new Button();c.setPrefSize(160,100);c.setAlignment(Pos.CENTER);c.getStyleClass().add("product-card");
            DropShadow b=new DropShadow(8,Color.rgb(0,0,0,0.08));DropShadow h=new DropShadow(14,Color.rgb(0,0,0,0.15));c.setEffect(b);
            VBox cont=new VBox(4,name,price);cont.setAlignment(Pos.CENTER);c.setGraphic(cont);
            Timeline in=new Timeline(new KeyFrame(Duration.millis(300),new KeyValue(c.translateYProperty(),-6,Interpolator.EASE_BOTH),new KeyValue(c.effectProperty(),h)));
            Timeline out=new Timeline(new KeyFrame(Duration.millis(400),new KeyValue(c.translateYProperty(),0,Interpolator.EASE_BOTH),new KeyValue(c.effectProperty(),b)));
            c.setOnMouseEntered(e->{out.stop();in.playFromStart();});c.setOnMouseExited(e->{in.stop();out.playFromStart();});
            getChildren().add(c);
        }
    }

    public static void main(String[] args) {launch(args);}
}
