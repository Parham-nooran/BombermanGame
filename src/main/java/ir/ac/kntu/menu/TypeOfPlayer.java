package ir.ac.kntu.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.util.ArrayList;

public class TypeOfPlayer {
    private Button button;
    private ArrayList<Spinner<String>> types;
    private Button back;
    private Players players;
    private ObservableList<String> observableList;
    public TypeOfPlayer(Players players) {
        this.button = new Button("Continue");
        this.back = new Button("Back");
        this.types = new ArrayList<>();
        this.players = players;
        this.observableList = FXCollections.observableArrayList("User", "AI");
        initialize();
    }
    private void initialize(){
        setButtonStatus();
        setBackStatus();
        setSpinnersStatus();
    }

    private void setSpinnersStatus(){
        for(int i=0;i<players.getDirector().getPlayers().size();i++) {
            Spinner<String> spinner = new Spinner<>();
            setALabel(i);
            setSpinnerStatus(spinner, i);
            setOnSpinnerAction(spinner, i);
            types.add(spinner);
        }
    }
    private void setOnSpinnerAction(Spinner<String> spinner, int i){
        spinner.valueProperty().addListener((observableValue, oldValue, newValue) ->
                players.getDirector().getPlayers().get(i).setControl(Control.valueOf(newValue.toUpperCase())));
    }
    private void setALabel(int i){
        Label label = new Label(players.getDirector().getPlayers().get(i).getName());
        label.relocate(200, 60+50*i);
        players.getDirector().getPane().getChildren().add(label);
    }

    private void setSpinnerStatus(Spinner<String> spinner, int i){
        spinner.setMinSize(100, 50);
        spinner.setLayoutX(300);
        spinner.setLayoutY(40+50*i);
        spinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(observableList));
        spinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
    }
    private void setButtonStatus(){
        button.setMinSize(150,50);
        button.setLayoutX(300);
        button.setLayoutY(400);
        setBackStatus();
        setButtonAction();
        setBackStatus();
        setBackAction();
    }
    public void setButtonAction(){
        button.setOnAction(EventHandler -> {
            players.getDirector().getPane().getChildren().removeAll(players.getDirector().getPane().getChildren());
            new Maps(players,this).load();
        });
    }
    public void load(){
        players.getDirector().getPane().getChildren().addAll(back, button);
        for(Spinner spinner:types){
            players.getDirector().getPane().getChildren().add(spinner);
        }
    }
    private void setBackStatus(){
        back.setMinSize(150,50);
        back.setLayoutX(300);
        back.setLayoutY(450);
        setBackAction();
    }
    private void setBackAction(){
        back.setOnAction(EventHandler ->{
            players.getDirector().getPane().getChildren().removeAll(players.getDirector().getPane().getChildren());
            players.load();
        });
    }
}
