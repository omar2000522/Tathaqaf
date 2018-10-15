package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.json.*;



public class Main extends Application {
    private Double windowWidth = 800.0;
    private Double windowHight = 600.0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        screen1(primaryStage);
    }

    public void screen1(Stage window) throws IOException {
        BorderPane rootBorderPane = new BorderPane();
        FileInputStream logoInputStream = new FileInputStream("src/images/logo.png");
        Image logo = new Image(logoInputStream);
        ImageView logoImageView = new ImageView(logo);
        VBox mainBox = new VBox(logoImageView);

        //--------Proprieties--------
        mainBox.setStyle("-fx-background-color : #1A1A1D");
        logoImageView.setFitWidth(300);
        logoImageView.setFitHeight(300);
        mainBox.setAlignment(Pos.CENTER);
        rootBorderPane.setCenter(mainBox);
        Scene scene = new Scene(rootBorderPane, windowWidth, windowHight);


        //---------------Code----------------

        //defs("human");
        twitterSearch();
        wikiSearch("water");






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

    public void wikiSearch(String word) throws IOException {
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

            System.out.println(text+"\n\n\n\n\n\n"+pageId);
        }
        catch (Exception e){System.out.println(e);}
    }

    public static void main(String[] args) {
        launch(args);
    }
}
