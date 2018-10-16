package sample;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javafx.util.Duration;
import org.json.*;



public class Main extends Application {
    private Double windowWidth = 1200.0;
    private Double windowHight = 800.0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        screen1(primaryStage);
    }

    public void screen1(Stage window) throws IOException {
        ScrollPane root = new ScrollPane();
        BorderPane rootBorderPane = new BorderPane();
        FileInputStream logoInputStream = new FileInputStream("src/images/logo.png");
        Image logo = new Image(logoInputStream);
        ImageView logoImageView = new ImageView(logo);
        VBox mainBox = new VBox(logoImageView);
        TextField queryTextField = new TextField();
        Button searchButton = new Button("Search");
        WebView center = new WebView();
        ToggleGroup options = new ToggleGroup();
        RadioButton definitionOpt = new RadioButton("Definition");
        RadioButton wikiOpt = new RadioButton("Information");
        RadioButton twitterOpt = new RadioButton("Opinions");
        HBox radioOptsBox = new HBox(definitionOpt,wikiOpt,twitterOpt);
        VBox searchElement = new VBox(queryTextField,radioOptsBox,searchButton);
        HBox topBox = new HBox(searchElement);
        Scene scene = new Scene(root, windowWidth, windowHight);




        //--------Proprieties--------
        mainBox.setStyle("-fx-background-color : #101013");
        topBox.setStyle("-fx-background-color : #f0f3bd");
        topBox.setMinHeight(windowHight+200);
        queryTextField.setFont(new Font(20));
        logoImageView.setFitWidth(300);
        logoImageView.setFitHeight(300);
        queryTextField.setMinWidth(600);
        queryTextField.setMinHeight(50);
        searchButton.setMinWidth(160);
        searchButton.setMinHeight(50);
        definitionOpt.setScaleX(1.2);
        definitionOpt.setScaleY(1.2);
        wikiOpt.setScaleX(1.2);
        wikiOpt.setScaleY(1.2);
        twitterOpt.setScaleX(1.2);
        twitterOpt.setScaleY(1.2);
        searchElement.setSpacing(20);
        radioOptsBox.setSpacing(40);
        topBox.setPadding(new Insets(50,50,windowHight/2-200,50));
        searchElement.setAlignment(Pos.CENTER);
        mainBox.setAlignment(Pos.CENTER);
        topBox.setAlignment(Pos.CENTER);
        radioOptsBox.setAlignment(Pos.CENTER);
        root.setContent(rootBorderPane);
        rootBorderPane.setCenter(mainBox);
        rootBorderPane.setTop(topBox);
        root.setFitToWidth(true);
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        center.setMinHeight(windowHight-100);
        definitionOpt.setToggleGroup(options);
        wikiOpt.setToggleGroup(options);
        twitterOpt.setToggleGroup(options);
        options.selectToggle(wikiOpt);
        scene.getStylesheets().add(getClass().getResource("styling.css").toString());


        //---------------Code----------------
        Path path = new Path();
        path.getElements().add (new MoveTo(searchButton.getTranslateX(), searchButton.getTranslateY()));
        path.getElements().add(new LineTo(searchButton.getTranslateX(),-windowHight));
        PathTransition pathTransition = new PathTransition();

        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setNode(searchButton);
        pathTransition.setPath(path);
        pathTransition.setCycleCount(4);
        pathTransition.setAutoReverse(true);

        pathTransition.play();


        searchButton.setOnAction(value -> {
            //String chosenOpt = options.getSelectedToggle().getProperties().toString();
            RadioButton chosenOpt = (RadioButton) options.getSelectedToggle();
            switch (chosenOpt.getText()){
                case "Definition":
                    System.out.println("Definition");
                    break;
                case "Information":
                    System.out.println("Information");
                    String title = queryTextField.getText().toLowerCase().replace(" ","_");
                    center.getEngine().load("https://en.wikipedia.org/wiki/" + title);
                    rootBorderPane.setCenter(center);
                    root.setVvalue(1D);
                    break;
                case "Opinions":
                    System.out.println("Opinions");
                    break;
                default:
                    System.out.println(chosenOpt);
            }

        });






        window.setScene(scene);
        window.show();
    }

    public List<String> defs(String word){
        String id = "dd28d5f2";
        String key = "2ebf7763e1fb2354635b0ee9e3eed2c1";
        List<String> definitions = new LinkedList<>();
        String line;

        try {
            URL url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/entries/"+"en"+"/"+word.toLowerCase());
            HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
            urlConn.setRequestProperty("Accept","application/json");
            urlConn.setRequestProperty("app_id",id);
            urlConn.setRequestProperty("app_key",key);

            BufferedReader output = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = output.readLine()) != null) {
                stringBuilder.append(line);
            }
            //System.out.println(stringBuilder);

            JSONObject obj = new JSONObject(stringBuilder.toString());
            JSONArray definitionsJSON = obj.
                    getJSONArray("results").
                    getJSONObject(0).
                    getJSONArray("lexicalEntries").
                    getJSONObject(0).
                    getJSONArray("entries").
                    getJSONObject(0).
                    getJSONArray("senses");

            for (int i = 0; i < definitionsJSON.length(); i++) {
                System.out.println(definitionsJSON.getJSONObject(i).getJSONArray("definitions").getString(0));
                definitions.add(definitionsJSON.getJSONObject(i).getJSONArray("definitions").getString(0));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return definitions;
    }

    public void twitterSearch(){


    }

    public String wikiSearch(String word) throws IOException {
        String line;
        try {
            URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&rvsection=0&format=json&titles=" + word.toLowerCase());
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

            BufferedReader output = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = output.readLine()) != null) {
                stringBuilder.append(line);
            }


            JSONObject mainJson = new JSONObject(stringBuilder.toString());
            String pageId = mainJson.getJSONObject("query").getJSONObject("pages").names().getString(0);
            String text = mainJson.getJSONObject("query").getJSONObject("pages").getJSONObject(pageId).getJSONArray("revisions").getJSONObject(0).getString("*");

            System.out.println(text+"\n\n\n\n\n\n"+mainJson);
            return text;
        }
        catch (Exception e){System.out.println(e);}
            return "There was an error.";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
