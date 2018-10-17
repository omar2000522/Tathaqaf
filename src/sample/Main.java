package sample;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javafx.util.Duration;
import org.json.*;



public class Main extends Application {
    private Double windowWidth = 1200.0;
    private Double windowHight = 800.0;
    private String currentOption = "";

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setResizable(false);
        screen1(primaryStage);
    }

    public void screen1(Stage window) throws IOException {
        BorderPane rootBorderPane = new BorderPane();
        TextField queryField = new TextField();
        Label currentOpt = new Label("Information");
        Button smallSearchButton = new Button("Search");
        Button upButton = new Button("×");
        TextField smallQueryField = new TextField();
        Button searchButton = new Button("Search");
        WebView center = new WebView();
        ToggleGroup options = new ToggleGroup();
        RadioButton definitionOpt = new RadioButton("Definition");
        RadioButton wikiOpt = new RadioButton("Information");
        RadioButton twitterOpt = new RadioButton("Opinions");

        //------Grouping-Elements
        HBox smallSearchElement = new HBox(smallQueryField,smallSearchButton);
        HBox topBox = new HBox(currentOpt,smallSearchElement,upButton);
        HBox topBoxBackground = new HBox(topBox);
        HBox radioOptsBox = new HBox(definitionOpt,wikiOpt,twitterOpt);
        VBox midBox = new VBox(queryField,radioOptsBox,searchButton);
        Scene scene = new Scene(rootBorderPane, windowWidth, windowHight);



        //--------Proprieties--------
        midBox.setStyle("-fx-background-color : #f0f3bd");
        topBoxBackground.setStyle("-fx-background-color : #f0f3bd");
        upButton.setId("upButton");
        currentOpt.setId("topLabel");
        smallSearchButton.setId("smallSearchButton");
        topBox.setId("topBox");
        topBox.setVisible(false);
        queryField.setFont(new Font(20));
        midBox.setMinHeight(windowHight+200);
        midBox.setMinWidth(windowWidth);
        center.setMinHeight(windowHight-100);
        topBox.setMinWidth(windowWidth);
        topBox.setMinHeight(100);
        queryField.setMinWidth(600);
        queryField.setMaxWidth(600);
        queryField.setMinHeight(50);
        smallSearchButton.setMaxHeight(30);
        smallQueryField.setMinWidth(200);
        smallQueryField.setMinHeight(30);
        searchButton.setMinWidth(160);
        searchButton.setMinHeight(50);
        definitionOpt.setScaleX(1.2);
        definitionOpt.setScaleY(1.2);
        wikiOpt.setScaleX(1.2);
        wikiOpt.setScaleY(1.2);
        twitterOpt.setScaleX(1.2);
        twitterOpt.setScaleY(1.2);
        topBox.setSpacing(100);
        midBox.setSpacing(20);
        smallSearchElement.setSpacing(10);
        radioOptsBox.setSpacing(40);
        midBox.setPadding(new Insets(0,50,windowHight/2-100,50));
        midBox.setAlignment(Pos.CENTER);
        radioOptsBox.setAlignment(Pos.CENTER);
        topBox.setAlignment(Pos.CENTER);
        smallSearchElement.setAlignment(Pos.CENTER);
        rootBorderPane.setCenter(midBox);
        rootBorderPane.setTop(topBoxBackground);
        definitionOpt.setToggleGroup(options);
        wikiOpt.setToggleGroup(options);
        twitterOpt.setToggleGroup(options);
        options.selectToggle(wikiOpt);
        scene.getStylesheets().add(getClass().getResource("styling.css").toString());




        //---------------Events----------------
        final boolean[] animate = {true};
        final boolean[] mainSender = {true};
        Runnable mainSearch = new Runnable() {
            @Override
            public void run() {
                String query;
                if (mainSender[0]){query = queryField.getText(); smallQueryField.setText("");}
                else query = smallQueryField.getText();
                RadioButton chosenOpt = (RadioButton) options.getSelectedToggle();
                currentOpt.setText(chosenOpt.getText());
                if(!query.equals("")) {
                    switch (chosenOpt.getText()) {
                        case "Definition":
                            try {
                                currentOption = "Definition";
                                defs(query,rootBorderPane,topBox,animate[0]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "Information":
                            currentOption = "Information";
                            wikiRun(query,rootBorderPane,center,topBox, animate[0]);
                            break;
                        case "Opinions":
                            System.out.println("Opinions");
                            break;
                        default:
                            System.out.println(chosenOpt);
                    }
                }
            }
        };
        queryField.setOnKeyReleased(value -> {
            if (value.getCode() == KeyCode.ENTER){
                animate[0] = true;
                mainSender[0] = true;
                mainSearch.run();
            }
        });
        searchButton.setOnAction(value -> {
            animate[0] = true;
            mainSender[0] = true;
            mainSearch.run();
        });
        smallSearchButton.setOnAction(value -> {
            animate[0] = false;
            mainSender[0] = false;
            mainSearch.run();
        });

        upButton.setOnAction(value -> {
            //------Reverse-the-Animations------
            Path path = new Path();
            path.getElements().add(new MoveTo(600,-windowHight+150));
            path.getElements().add (new LineTo(600, windowHight/2+100));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(1000));
            pathTransition.setNode(rootBorderPane.getBottom());
            pathTransition.setPath(path);
            pathTransition.play();
            FadeTransition ft = new FadeTransition(Duration.millis(1000), topBox);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.play();
        });

        window.setScene(scene);
        window.show();
    }


    public void wikiRun(String word, BorderPane root, WebView browser, HBox topBox,boolean animate){
        //Run the animation
        if (animate) {
            Path path = new Path();
            path.getElements().add(new MoveTo(600, windowHight / 2 + 100));
            path.getElements().add(new LineTo(600, -windowHight + 150));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(700));
            pathTransition.setNode(browser);
            pathTransition.setPath(path);
            pathTransition.play();
            FadeTransition ft = new FadeTransition(Duration.millis(700), topBox);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
            topBox.setVisible(true);
        }

        String title = word.toLowerCase().replace(" ", "_");
        browser.getEngine().load("https://www.wikipedia.org/wiki/" + title);
        root.setBottom(browser);
    }

    public void defs(String word, BorderPane root,HBox topBox,boolean animate) throws IOException {
        String id = "dd28d5f2";
        String key = "2ebf7763e1fb2354635b0ee9e3eed2c1";
        String line;
        word = word.substring(0,1).toUpperCase()+word.substring(1).toLowerCase();
        Label title = new Label(word);
        HBox titleBox = new HBox(title);
        VBox defnitionsBox = new VBox(titleBox);

        //------proprieties------
        title.setFont(new Font(50));
        defnitionsBox.setAlignment(Pos.TOP_CENTER);
        defnitionsBox.setPadding(new Insets(0,100,100,100));
        defnitionsBox.setSpacing(30);
        defnitionsBox.setId("defs-box");


        URL url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/entries/" + "en" + "/" + word.toLowerCase().replace(" ", "_"));
        HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
        urlConn.setRequestProperty("Accept", "application/json");
        urlConn.setRequestProperty("app_id", id);
        urlConn.setRequestProperty("app_key", key);

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
            VBox defAndEg = new VBox();
            String currentDef = (String) definitionsJSON.getJSONObject(i).getJSONArray("definitions").get(0);
            Text def = new Text(currentDef);
            try {
                String currentEg = (String) definitionsJSON.getJSONObject(i).getJSONArray("examples").getJSONObject(0).getString("text");
                Text eg = new Text("Example: " + currentEg);
                eg.setFont(new Font(18));
                defAndEg.getChildren().addAll(def, eg);
            }
            catch (Exception e){
                System.out.println("NO EXAMPLES");
                defAndEg.getChildren().add(def);
            }

            def.setFont(new Font("Roboto",24));
            def.setWrappingWidth(windowWidth-400);
            def.setWrappingWidth(windowWidth-400);
            defAndEg.setSpacing(20);
            defnitionsBox.getChildren().add(defAndEg);
        }

        if (animate) {
            root.setBottom(defnitionsBox);
            Path path = new Path();
            path.getElements().add(new MoveTo(windowWidth / 2, windowHight / 2 ));
            path.getElements().add(new LineTo(windowWidth / 2, -windowHight+150));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(700));
            pathTransition.setNode(defnitionsBox);
            pathTransition.setPath(path);
            pathTransition.play();
            FadeTransition ft = new FadeTransition(Duration.millis(700), topBox);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }
        else {
            root.setBottom(defnitionsBox);
            Path path = new Path();
            path.getElements().add(new MoveTo(windowWidth / 2, windowHight / 2 ));
            path.getElements().add(new LineTo(windowWidth / 2, -windowHight + 150));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(10));
            pathTransition.setNode(defnitionsBox);
            pathTransition.setPath(path);
            pathTransition.play();
            System.out.println("ANIMATED");
        }
        topBox.setVisible(true);

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
