package application;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Gốc
        StackPane root = new StackPane();
        root.getStyleClass().add("root");

        // Layout chính
        BorderPane wrapper = new BorderPane();
        wrapper.getStyleClass().add("app-container");
        root.getChildren().add(wrapper);

        // Sidebar
        VBox sidebar = buildSidebar();
        wrapper.setRight(sidebar);

        // Content POS
        VBox content = new VBox(24);
        content.setAlignment(Pos.TOP_LEFT);
        content.getStyleClass().add("sales-container");

        content.getChildren().addAll(
            label("POS", "label-pos"),
            label("Sales", "label-sales"),
            buildProductGrid()
        );

        wrapper.setCenter(content);

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/UI.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("POS - Products");
        primaryStage.show();
    }

    // Sidebar builder
    private VBox buildSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setPrefWidth(160);
        sidebar.getStyleClass().add("sidebar");

        String[][] btnInfo = {
            {"Dashboard", "btn-dashboard"},
            {"Orders", "btn-orders"},
            {"Products", "btn-products"},
            {"Customers", "btn-customers"}
        };

        ToggleGroup navGroup = new ToggleGroup();

        for (String[] b : btnInfo) {
            ToggleButton btn = new ToggleButton(b[0]);
            btn.getStyleClass().addAll("toggle-button", b[1]);
            btn.setToggleGroup(navGroup);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setBackground(Background.EMPTY);
            btn.setBorder(Border.EMPTY);
            sidebar.getChildren().add(btn);
        }

        return sidebar;
    }

    // Label helper
    private Label label(String text, String cssClass) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add(cssClass);
        return lbl;
    }

    // Product Grid
    private GridPane buildProductGrid() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("products-grid");
        
        FlowPane fp = new FlowPane(15, 15);
        fp.setStyle("-fx-background-color: #FFFBF0;");
        
        for (int i = 1; i <= 100; i++) {
            Label n = new Label("Sản phẩm " + i);
            n.getStyleClass().add("product-name");
            Label pr = new Label("Giá: ...");
            pr.getStyleClass().add("product-price");
            ProductCard c = new ProductCard(n,pr);
            c.getStyleClass().addAll("product-card");
            c.setPadding(new Insets(10));
            c.getChildren().addAll(n, pr);
            fp.getChildren().add(c);
        }
        
        VBox cc = new VBox(fp);
        cc.setPadding(new Insets(15,15,15,15));
        cc.setStyle("-fx-background-color: #FFFBF0;");
        
        ScrollPane sp = new ScrollPane(cc);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: #FFFBF0;");
        GridPane.setHgrow(sp, Priority.ALWAYS);
        GridPane.setVgrow(sp, Priority.ALWAYS);
        
        grid.getChildren().add(sp);
        
        
        
        return grid;
    }

    static class ProductCard extends VBox {
        private final Button card;

        public ProductCard(Label nameText, Label priceText) {
            setAlignment(Pos.CENTER);

            card = new Button();
            card.setPrefSize(160, 100);
            card.setAlignment(Pos.CENTER);
            card.getStyleClass().add("product-card");

            DropShadow baseShadow = new DropShadow(8, Color.rgb(0, 0, 0, 0.08));
            DropShadow hoverShadow = new DropShadow(14, Color.rgb(0, 0, 0, 0.15));
            card.setEffect(baseShadow);

            VBox content = new VBox(4);
            content.setAlignment(Pos.CENTER);
            content.getChildren().addAll(nameText,priceText);
            card.setGraphic(content);


            Timeline hoverUp = new Timeline(
                new KeyFrame(Duration.millis(300),
                    new KeyValue(card.translateYProperty(), -6, Interpolator.EASE_BOTH),
                    new KeyValue(card.effectProperty(), hoverShadow))
            );
            Timeline hoverDown = new Timeline(
                new KeyFrame(Duration.millis(400),
                    new KeyValue(card.translateYProperty(), 0, Interpolator.EASE_BOTH),
                    new KeyValue(card.effectProperty(), baseShadow))
            );

            card.setOnMouseEntered(e -> {
                hoverDown.stop();
                hoverUp.playFromStart();
            });

            card.setOnMouseExited(e -> {
                hoverUp.stop();
                hoverDown.playFromStart();
            });

            getChildren().add(card);
        }

        private Label label(String text, String style) {
            Label lbl = new Label(text);
            lbl.getStyleClass().add(style);
            return lbl;
        }
    }
}
