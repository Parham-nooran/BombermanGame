package ir.ac.kntu.menu;

import ir.ac.kntu.logic.Director;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Menu {
    private Button start;
    private Spinner<Integer> spinner;
    private Button exit;
    private int numberOfPlayers = 2;
    private Director director;
    private Stage stage;
    private Scene scene;
    public Main(Pane pane, Stage stage, Scene scene){
        super(pane);
        this.stage = stage;
        this.start = new Button("Start");
        setStartStatus();
        this.spinner = new Spinner<>();
        setSpinnerStatus();
        setLabel();
        this.exit = new Button("Exit");
        setEndStatus();
        this.setOnAction(pane);
        this.scene = scene;

    }
    public void load(){
        getPane().getChildren().addAll(start, exit, spinner);
    }
    private void setStartStatus(){
        start.setMinSize(150,50);
        start.setLayoutX(300);
        start.setLayoutY(200);
    }
    private void setSpinnerStatus(){
        spinner.setMinSize(100,50);
        this.spinner.setValueFactory(getValueFactory(numberOfPlayers));
        spinner.setLayoutX(300);
        spinner.setLayoutY(250);

    }
    private void setLabel(){
        Label label = new Label("Number of Players");
        label.setLayoutX(150);
        label.setLayoutY(270);
        getPane().getChildren().add(label);
    }
    private SpinnerValueFactory<Integer> getValueFactory(int initialValue){
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(2,4, initialValue);
    }
    private void setEndStatus(){
        exit.setMinSize(150,50);
        exit.setLayoutX(300);
        exit.setLayoutY(300);
    }

    public void setOnAction(Pane pane){
        start.setOnAction( EventHandler ->{
            pane.getChildren().removeAll(pane.getChildren());
            this.director = new Director(pane, stage, scene, numberOfPlayers, "map.txt");
            scene.setOnKeyPressed(this.director::actionOnKeyPress);
            director.start();
        });
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> numberOfPlayers=newValue);
        exit.setOnAction(EventHandler ->{
            director.setFinished(true);
            stage.close();
        });
    }

    public Director getDirector() {
        return director;
    }

    public Scene getScene() {
        return scene;
    }
}
