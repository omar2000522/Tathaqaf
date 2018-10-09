package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main extends Application {
    Double windowWidth = 800.0;
    Double windowHight = 600.0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        screen1(primaryStage);
    }

    public void screen1(Stage window) throws FileNotFoundException {
        BorderPane rootBorderPane = new BorderPane();
        Label countdownLabel = new Label();
        HBox topBox = new HBox();
        HBox bottomBox = new HBox(countdownLabel);
        FileInputStream logoInputStream = new FileInputStream("src/images/logo.png");
        Image logo = new Image(logoInputStream);
        ImageView logoImageView = new ImageView(logo);
        VBox mainBox = new VBox(logoImageView);

        //--------Proprieties--------
        topBox.setStyle("-fx-background-color: #336699;");
        bottomBox.setStyle("-fx-background-color: #336699;");
        logoImageView.setFitWidth(300);
        logoImageView.setFitHeight(300);
        bottomBox.setPadding(new Insets(15));
        topBox.setPadding(new Insets(20));
        topBox.setSpacing(20);
        bottomBox.setAlignment(Pos.CENTER);
        mainBox.setAlignment(Pos.CENTER);
        rootBorderPane.setTop(topBox);
        rootBorderPane.setBottom(bottomBox);
        rootBorderPane.setCenter(mainBox);

        window.setScene(new Scene(rootBorderPane, windowWidth, windowHight));
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
