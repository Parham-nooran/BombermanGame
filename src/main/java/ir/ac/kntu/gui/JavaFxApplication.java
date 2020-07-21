package ir.ac.kntu.gui;

import ir.ac.kntu.logic.SerializedPane;
import ir.ac.kntu.menu.Main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class JavaFxApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage stage)  {
        SerializedPane root = new SerializedPane();
        //GridPane pane = new GridPane();
        //root.setStyle("-fx-border-width: 0 0 5 0;");
        checkPlayersFile();
        Scene scene = new Scene(root, 760, 560, Color.rgb(240, 240, 240));
        Main main = new Main(root, stage, scene);
        main.load();
        // Setting stage properties
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Faroborz Bobmerman");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            if(main.getDirector()!=null) {
                main.getDirector().setClosed(true);
            }
            stage.close();
        });
    }
    private void checkPlayersFile(){
        File file = new File("players.txt");
        file.delete();
    }
}
