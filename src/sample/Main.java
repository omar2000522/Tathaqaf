package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.*;



public class Main extends Application {
    private Double windowWidth = 800.0;
    private Double windowHight = 600.0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        screen1(primaryStage);
    }

    public void screen1(Stage window) throws FileNotFoundException {
        BorderPane rootBorderPane = new BorderPane();
        FileInputStream logoInputStream = new FileInputStream("src/images/logo.png");
        Image logo = new Image(logoInputStream);
        ImageView logoImageView = new ImageView(logo);
        VBox mainBox = new VBox(logoImageView);

        //--------Proprieties--------
        logoImageView.setFitWidth(300);
        logoImageView.setFitHeight(300);
        mainBox.setAlignment(Pos.CENTER);
        rootBorderPane.setCenter(mainBox);
        Scene scene = new Scene(rootBorderPane, windowWidth, windowHight);


        //---------------Code----------------

        defs("human");
        twitterSearch();







        window.setScene(scene);
        window.show();
    }

    public List<String> defs(String word){
        final String app_id = "dd28d5f2";
        final String app_key = "2ebf7763e1fb2354635b0ee9e3eed2c1";
        List<String> definitions = null;
        try {
            URL url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/entries/"+"en"+"/"+word.toLowerCase());
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject obj = new JSONObject(stringBuilder.toString());
            JSONArray definitionsJSON = obj.getJSONArray("results").getJSONObject(0).
                    getJSONArray("lexicalEntries").
                    getJSONObject(0).getJSONArray("entries").getJSONObject(0).getJSONArray("senses");
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

    public void wikiSearch() throws MalformedURLException {
        URL url = new URL("https://en.wikipedia.org/w/api.php");

    }

    public static void main(String[] args) {
        launch(args);
    }
}
