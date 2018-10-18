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
import javafx.scene.text.FontWeight;
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
        Label copyRight = new Label("© 2018 عمر الميموني");
        VBox copyRightBox = new VBox(copyRight);
        Button smallSearchButton = new Button("Search");
        Button upButton = new Button("×");
        TextField smallQueryField = new TextField();
        Button searchButton = new Button("Search");
        WebView center = new WebView();
        ToggleGroup options = new ToggleGroup();
        RadioButton definitionOpt = new RadioButton("Definitions");
        RadioButton wikiOpt = new RadioButton("Information");
        RadioButton twitterOpt = new RadioButton("News");

        //------Setting-up-the-logo--------
        Image logo = new Image(new FileInputStream("src/images/tth8f.png"));
        ImageView logoImageView = new ImageView(logo);
        logoImageView.setPreserveRatio(true);
        logoImageView.setFitWidth(400);

        //------Grouping-Elements----------
        HBox smallSearchElement = new HBox(smallQueryField,smallSearchButton);
        HBox topBox = new HBox(currentOpt,smallSearchElement,upButton);
        HBox topBoxBackground = new HBox(topBox);
        HBox radioOptsBox = new HBox(definitionOpt,wikiOpt,twitterOpt);
        VBox midBox = new VBox(logoImageView,queryField,radioOptsBox,searchButton,copyRightBox);
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
        topBox.setMinWidth(windowWidth+10);
        topBox.setMinHeight(100);
        queryField.setMinWidth(600);
        queryField.setMaxWidth(600);
        queryField.setMinHeight(50);
        smallSearchButton.setMaxHeight(30);
        smallQueryField.setMinWidth(200);
        smallQueryField.setMinHeight(30);
        searchButton.setMinWidth(160);
        searchButton.setMinHeight(50);
        copyRightBox.setMinHeight(180);
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
        midBox.setPadding(new Insets(0,50,windowHight/2-180,50));
        midBox.setAlignment(Pos.CENTER);
        radioOptsBox.setAlignment(Pos.CENTER);
        topBox.setAlignment(Pos.CENTER);
        smallSearchElement.setAlignment(Pos.CENTER);
        copyRightBox.setAlignment(Pos.BOTTOM_CENTER);
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
                topBox.setStyle("-fx-border-width: 0 0 1 0 ;");
                String query;
                if (mainSender[0]){query = queryField.getText(); smallQueryField.setText("");}
                else query = smallQueryField.getText();
                RadioButton chosenOpt = (RadioButton) options.getSelectedToggle();
                currentOpt.setText(chosenOpt.getText());
                if(!query.equals("")) {
                    switch (chosenOpt.getText()) {
                        case "Definitions":
                            try {
                                currentOption = "Definitions";
                                defs(query,rootBorderPane,topBox,animate[0]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;

                        case "Information":
                            currentOption = "Information";
                            //wikiRun(query,rootBorderPane,center,topBox, animate[0]);
                            try {
                                wikiSearch(query,rootBorderPane,center,topBox,animate[0]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;

                        case "News":
                            currentOption = "News";
                            try {
                                newsSearch(query,rootBorderPane,topBox,animate[0]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
        smallQueryField.setOnKeyReleased(value -> {
            if (value.getCode() == KeyCode.ENTER) {
                animate[0] = false;
                mainSender[0] = false;
                mainSearch.run();
            }
        });

        upButton.setOnAction(value -> {
            //------Reverse-the-Animations------
            topBox.setStyle("-fx-border-width: 0 0 0 0 ;");
            Path path = new Path();
            path.getElements().add(new MoveTo(601,-windowHight+150));
            path.getElements().add (new LineTo(601, windowHight/2+100));
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


    public void wikiRun(String word,WebView browser,ScrollPane textPane){
        //Run the animation
        FadeTransition ft = new FadeTransition(Duration.millis(700), browser);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();

        textPane.setMinWidth(windowWidth+20);
        browser.setMinSize(windowWidth+15,windowHight-80);
        String title = word.toLowerCase().replace(" ", "_");
        browser.getEngine().load("https://www.wikipedia.org/wiki/" + title);
        textPane.setContent(browser);
    }

    public void defs(String word, BorderPane root,HBox topBox,boolean animate) throws IOException {
        String id = "dd28d5f2";
        String key = "2ebf7763e1fb2354635b0ee9e3eed2c1";
        String line;
        word = word.substring(0,1).toUpperCase()+word.substring(1).toLowerCase();
        ScrollPane defsPane = new ScrollPane();
        Label title = new Label(word);
        HBox titleBox = new HBox(title);
        VBox defnitionsBox = new VBox(titleBox);

        //------proprieties------
        title.setFont(Font.font("Roboto",FontWeight.BOLD,70));
        defnitionsBox.setAlignment(Pos.TOP_CENTER);
        defnitionsBox.setPadding(new Insets(30,100,100,100));
        defnitionsBox.setSpacing(30);
        defnitionsBox.setMinSize(windowWidth+15,windowHight);
        defsPane.setMinSize(windowWidth+15,windowHight-80);
        defsPane.setMaxHeight(windowHight-80);
        defsPane.setFitToWidth(false);
        defsPane.setContent(defnitionsBox);
        defsPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        defsPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        defsPane.setId("defs-box");
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
            defAndEg.setId("def");
            defAndEg.setSpacing(10);
            defnitionsBox.getChildren().add(defAndEg);
        }

        if (animate) {
            root.setBottom(defsPane);
            Path path = new Path();
            path.getElements().add(new MoveTo(windowWidth / 2, windowHight / 2 ));
            path.getElements().add(new LineTo(windowWidth / 2, -windowHight+161));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(700));
            pathTransition.setNode(defsPane);
            pathTransition.setPath(path);
            pathTransition.play();
            FadeTransition ft = new FadeTransition(Duration.millis(700), topBox);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }
        else {
            root.setBottom(defsPane);
            Path path = new Path();
            path.getElements().add(new MoveTo(windowWidth / 2, windowHight / 2 ));
            path.getElements().add(new LineTo(windowWidth / 2, -windowHight + 161));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(10));
            pathTransition.setNode(defsPane);
            pathTransition.setPath(path);
            pathTransition.play();
            System.out.println("ANIMATED");
        }
        topBox.setVisible(true);

    }

    public void newsSearch(String word, BorderPane root, HBox topBox, boolean animate) throws IOException {
        String line;
        ScrollPane articlesPane = new ScrollPane();
        VBox articlesBox = new VBox();

        //---------Code-------
        URL url = new URL("https://newsapi.org/v2/everything?q="+word.toLowerCase().replace(" ","_"));
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setRequestProperty("Authorization","6d05881643864aa7899959bc15035a2f");

        BufferedReader output = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = output.readLine()) != null) {
            stringBuilder.append(line);
        }

        JSONObject mainJSON = new JSONObject(stringBuilder.toString());
        JSONArray articlesJSON = mainJSON.getJSONArray("articles");
        System.out.print(stringBuilder);
        for (int i = 0; i<articlesJSON.length() && i<20; i++) {
            VBox articleElement = new VBox();
            String currentTitle = articlesJSON.getJSONObject(i).getString("title");
            String currentAuth = "";
            try{currentAuth = articlesJSON.getJSONObject(i).getString("author");}catch (Exception e){}
            String currentSource = articlesJSON.getJSONObject(i).getJSONObject("source").getString("name");
            String currentContent = articlesJSON.getJSONObject(i).getString("content");
            String currentDesc = articlesJSON.getJSONObject(i).getString("description");

            Label authLabel;
            Label titleLabel = new Label(currentTitle);
            Text descText = new Text(currentDesc);
            Button fullArticle = new Button("View Full Article");

            if (!currentAuth.equals("")){
                authLabel = new Label("By "+currentAuth+" from "+currentSource);
            }else{
                authLabel = new Label("From "+currentSource);
            }

            titleLabel.setFont(new Font("Arial",30));
            authLabel.setFont(new Font("Arial",18));
            descText.setFont(new Font("Roboto",24));
            descText.setWrappingWidth(windowWidth-400);
            articleElement.setId("def");
            articleElement.setSpacing(20);

            fullArticle.setOnAction(value -> {});

            articleElement.getChildren().addAll(titleLabel,authLabel,descText,fullArticle);
            articlesBox.getChildren().add(articleElement);
        }
        articlesPane.setContent(articlesBox);

        if (animate) {
            root.setBottom(articlesPane);
            Path path = new Path();
            path.getElements().add(new MoveTo(windowWidth / 2, windowHight / 2 ));
            path.getElements().add(new LineTo(windowWidth / 2, -windowHight+161));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(700));
            pathTransition.setNode(articlesPane);
            pathTransition.setPath(path);
            pathTransition.play();
            FadeTransition ft = new FadeTransition(Duration.millis(700), topBox);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }
        else {
            root.setBottom(articlesPane);
            Path path = new Path();
            path.getElements().add(new MoveTo(windowWidth / 2, windowHight / 2 ));
            path.getElements().add(new LineTo(windowWidth / 2, -windowHight + 161));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(10));
            pathTransition.setNode(articlesPane);
            pathTransition.setPath(path);
            pathTransition.play();
            System.out.println("ANIMATED");
        }
        topBox.setVisible(true);

        //System.out.println(stringBuilder);
    }

    public void newsRun(String title, String auth, String content,BorderPane root){
        ScrollPane articlePane = new ScrollPane();
        Label titleLabel = new Label(title);
        Label authLabel = new Label(auth);
        HBox titleBox = new HBox(titleLabel,authLabel);
        Text contentText = new Text(content);
        VBox contentBox = new VBox(contentText);
        VBox article = new VBox(titleBox,contentBox);

        //Run the animation
        FadeTransition ft = new FadeTransition(Duration.millis(700), articlePane);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();

        articlePane.setMinWidth(windowWidth+20);
        articlePane.setContent(article);
    }

    public void wikiSearch(String word, BorderPane root ,WebView webView,HBox topBox, boolean animate) throws IOException {
        String line;
        JSONObject mainJson = null;
        try {
            URL url = new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=" + word.toLowerCase().replace(" ","_"));
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

            BufferedReader output = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = output.readLine()) != null) {
                stringBuilder.append(line);
            }

            mainJson = new JSONObject(stringBuilder.toString());
            //System.out.println(text+"\n\n\n\n\n\n"+mainJson);
        }
        catch (Exception e){System.out.println(e);}

        String pageId = mainJson.getJSONObject("query").getJSONObject("pages").names().getString(0);
        String title = mainJson.getJSONObject("query").getJSONObject("pages").getJSONObject(pageId).getString("title");
        String text = mainJson.getJSONObject("query").getJSONObject("pages").getJSONObject(pageId).getString("extract");


        //--------GUI-Elements--------------
        ScrollPane textPane = new ScrollPane();
        Label titleLabel = new Label(title);
        Text textText = new Text(text);
        Button wikiButton = new Button("Open Wikipedia Article");
        HBox titleBox = new HBox(titleLabel);
        HBox textBox = new HBox(textText);
        VBox mainBox = new VBox(titleBox,textBox,wikiButton);


        //------proprieties------
        titleLabel.setFont(Font.font("Roboto",FontWeight.BOLD,70));
        textText.setFont(Font.font("Ariel",FontWeight.SEMI_BOLD, 20));
        textText.setWrappingWidth(windowWidth-400);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setPadding(new Insets(30,100,300,100));
        mainBox.setSpacing(30);
        mainBox.setMinSize(windowWidth+20,windowHight);
        textPane.setMinSize(windowWidth+20,windowHight-80);
        textPane.setMaxHeight(windowHight-80);
        textPane.setFitToWidth(false);
        textPane.setContent(mainBox);
        textPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        textPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        textPane.setId("text-box");
        mainBox.setId("defs-box");
        textBox.setId("def");


        //---------Code----------
        wikiButton.setOnAction(value -> {
            wikiRun(title,webView,textPane);
        });
        int duration = 10;
        if (animate) {
            duration = 700;
            FadeTransition ft = new FadeTransition(Duration.millis(700), topBox);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }
        root.setBottom(textPane);
        Path path = new Path();
        path.getElements().add(new MoveTo(windowWidth / 2+1, windowHight / 2 ));
        path.getElements().add(new LineTo(windowWidth / 2+1, -windowHight + 160));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(duration));
        pathTransition.setNode(textPane);
        pathTransition.setPath(path);
        pathTransition.play();
        System.out.println("ANIMATED");

        topBox.setVisible(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
