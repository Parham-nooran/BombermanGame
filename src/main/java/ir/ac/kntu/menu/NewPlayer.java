package ir.ac.kntu.menu;

import ir.ac.kntu.logic.Player;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

import java.util.ArrayList;
import java.util.List;

public class NewPlayer {
    private TextField name;
    private ChoiceBox<String> color;
    private List<String> colors;
    private Button add;
    private Button cancel;
    private Players players;
    public NewPlayer(Players players){
        this.name = new TextField();
        this.add = new Button("Add");
        this.cancel = new Button("Cancel");
        this.color = new ChoiceBox<>();
        this.colors = new ArrayList<>();
        this.players = players;
        addColors();
        setAddStatus();
        setCancelStatues();
        setTextStatus();
        setChoiceBoxStatus();
        setColorStatus();
    }
    private void addColors(){
        colors.add("Red");
        colors.add("Black");
        colors.add("Yellow");
        colors.add("White");
    }
    private void setAddStatus(){
        add.setMinSize(150,50);
        add.setLayoutX(300);
        add.setLayoutY(400);
        setOnAddAction();
    }
    private void setCancelStatues(){
        cancel.setMinSize(150,50);
        cancel.setLayoutX(300);
        cancel.setLayoutY(450);
        setOnCancelAction();
    }
    private void setOnCancelAction(){
        cancel.setOnAction(EventHandler ->{
            continueTo();
        });
    }
    private void setColorStatus(){
        color.relocate(300, 150);
        color.setTooltip(new Tooltip("Color"));
    }
    private void setOnAddAction(){
        add.setOnAction(EventHandler ->{
            if(checkText()&&!color.getSelectionModel().isEmpty()) {
                players.addPlayer(new Player(name.getText(), 0, 0,
                        players.getDirector().getPane(), players.getDirector().getPlayerImages(getColor()),
                        players.getDirector().getTimer()));
                continueTo();
            } else{
                System.out.println("This player already exists");
            }
        });
    }
    private void continueTo(){
        players.getDirector().getPane().getChildren().removeAll(players.getDirector().getPane().getChildren());
        players.load();
    }
    public String getColor(){
        if(!color.getSelectionModel().getSelectedItem().equals("White")) {
            return "_" + color.getSelectionModel().getSelectedItem().toLowerCase();
        }
        return "";
    }
    private void setChoiceBoxStatus(){
        color.setItems(FXCollections.observableList(colors));
    }
    private boolean checkText(){
        return players.getDirector().getPlayers().stream().noneMatch(player -> player.getName().equals(name.getText()));
    }
    public void setTextStatus(){
        name.relocate(300, 100);
        name.setPromptText("Name");
    }
    public void load(){
        players.getDirector().getPane().getChildren().addAll(name, color, add, cancel);
    }
}
