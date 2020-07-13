package ir.ac.kntu.menu;

import ir.ac.kntu.logic.Director;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Menu {
    private Button start;
    private Button exit;
    public Main(Pane pane){
        super(pane);
        this.start = new Button("Start");
        start.setMinSize(100,50);
        start.setLayoutX(400);
        start.setLayoutY(200);
        this.exit = new Button("Exit");
        exit.setMinSize(100,50);
        exit.setLayoutX(400);
        exit.setLayoutY(250);
        pane.getChildren().addAll(start, exit);
    }
    public void setOnAction(Pane pane, Director director, Stage stage){
        start.setOnAction( EventHandler ->{
            pane.getChildren().removeAll(pane.getChildren());
            director.start();
        });
        exit.setOnAction(EventHandler ->{
            director.getTimer().setNotClosed(false);
            stage.close();
        });
    }
    public Button getStart() {
        return start;
    }

    public Button getExit() {
        return exit;
    }
}
