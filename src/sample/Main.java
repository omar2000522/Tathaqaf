package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
        VBox searchElement = new VBox(queryTextField,searchButton);
        HBox topBox = new HBox(searchElement);
        WebView center = new WebView();
        Scene scene = new Scene(root, windowWidth, windowHight);




        //--------Proprieties--------
        mainBox.setStyle("-fx-background-color : #101013");
        topBox.setStyle("-fx-background-color : #1A1A1D");
        topBox.setMinHeight(windowHight+100);
        queryTextField.setFont(new Font(20));
        logoImageView.setFitWidth(300);
        logoImageView.setFitHeight(300);
        queryTextField.setMinWidth(600);
        queryTextField.setMinHeight(50);
        searchButton.setMinWidth(160);
        searchButton.setMinHeight(50);
        searchElement.setSpacing(40);
        topBox.setPadding(new Insets(50));
        searchElement.setAlignment(Pos.CENTER);
        mainBox.setAlignment(Pos.CENTER);
        topBox.setAlignment(Pos.CENTER);
        root.setContent(rootBorderPane);
        rootBorderPane.setCenter(mainBox);
        rootBorderPane.setTop(topBox);
        root.setFitToWidth(true);
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        center.setMinHeight(windowHight-100);
        rootBorderPane.setCenter(center);
        scene.getStylesheets().add(getClass().getResource("styling.css").toString());


        //---------------Code----------------
        searchButton.setOnAction(value -> {
            if (!queryTextField.getText().equals("")) {
                String title = queryTextField.getText().toLowerCase().replace(" ","_");
                center.getEngine().load("https://en.wikipedia.org/wiki/" + title);
                root.setVvalue(1D);
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
