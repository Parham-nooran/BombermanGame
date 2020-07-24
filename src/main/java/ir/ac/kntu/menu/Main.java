package ir.ac.kntu.menu;

import ir.ac.kntu.logic.Director;
import ir.ac.kntu.logic.SerializedPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class Main extends Menu {
    private Button start;
    private Spinner<Integer> spinner;
    private Button exit;
    private int numberOfPlayers = 2;
    private Director director;
    private Stage stage;
    private Scene scene;
    private Label label;
    public Main(SerializedPane pane, Stage stage, Scene scene){
        super(pane);
        this.stage = stage;
        this.start = new Button("Continue");
        setStartStatus();
        this.spinner = new Spinner<>();
        setSpinnerStatus();
        setLabelStatus();
        this.exit = new Button("Exit");
        setExitStatus();
        this.setOnAction(pane);
        this.scene = scene;
    }
    public void load(){
        getPane().getChildren().addAll(start, exit, spinner, label);
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
        spinner.setLayoutY(300);

    }
    private void setLabelStatus(){
        this.label = new Label("Number of Players");
        label.setLayoutX(150);
        label.setLayoutY(320);
    }
    private SpinnerValueFactory<Integer> getValueFactory(int initialValue){
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(2,4, initialValue);
    }
    private void setExitStatus(){
        exit.setMinSize(150,50);
        exit.setLayoutX(300);
        exit.setLayoutY(400);
    }

    public void setOnAction(SerializedPane pane){
        start.setOnAction( EventHandler ->{
            pane.getChildren().removeAll(pane.getChildren());
            this.director = new Director(pane, stage, scene,
                    "src/main/resources/map-builder/index.html");
            scene.setOnKeyPressed(this.director::actionOnKeyPress);
            new Players(director, numberOfPlayers, this).load();
        });
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> numberOfPlayers=newValue);
        exit.setOnAction(EventHandler ->{
            if(director!=null) {
                director.setFinished(true);
            }
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
