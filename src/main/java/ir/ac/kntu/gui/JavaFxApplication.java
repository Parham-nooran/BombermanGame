package ir.ac.kntu.gui;

import ir.ac.kntu.logic.Director;
import ir.ac.kntu.logic.Player;
import ir.ac.kntu.logic.SerializedPane;
import ir.ac.kntu.menu.Main;
import ir.ac.kntu.status.Timer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class JavaFxApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage stage)  {
        SerializedPane root = new SerializedPane();
        //GridPane pane = new GridPane();
        //root.setStyle("-fx-border-width: 0 0 5 0;");
        Scene scene = new Scene(root, 800, 600, Color.rgb(240, 240, 240));
        Timer timer = new Timer(root, 0, 3,0,true);
        ArrayList<Player> players = new ArrayList<>();
        try{
            makePlayers(players);
        } catch (FileNotFoundException e){
            System.out.println("Indicated file does not exist");
        }
        Director director = new Director(root, players, timer, "players.txt", "map.txt");
        Main main = new Main(root);
        main.setOnAction(root, director, stage);
        scene.setOnKeyPressed(director::actionOnKeyPress);
        // Setting stage properties
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Faroborz Bobmerman");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> director.setClosed(true));
    }
    public void makePlayers(ArrayList<Player> players) throws FileNotFoundException {
        Image[] images = new Image[1];
        images[0] = new Image(new FileInputStream(
                "src/main/resources/assets/player/player_down_standing.png"));
        //Player player1 = new FileManager().loadFromFile("players.txt");
        Player player2 = new Player("Masih", 300, 2500, images);
        Player player3 = new Player("Parvane", 250, 100, images);
        Player player4 = new Player("Ali", 250, 150, images);
        //players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
    }
}
