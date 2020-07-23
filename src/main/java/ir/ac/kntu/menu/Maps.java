package ir.ac.kntu.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;


public class Maps {
    private ListView<String> listView;
    private ObservableList<String> list;
    private Players players;
    private Button start;
    private Button back;

    public Maps(Players players) {
        this.players = players;
        this.list = FXCollections.observableArrayList("Index", "Django");
        listView = new ListView<>();
        this.start = new Button("Start");
        this.back = new Button("Back");
        setListViewStatus();
        setStartStatus();
        setBackStatus();
    }

    public void load(){
        players.getDirector().getPane().getChildren().addAll(listView, start, back);
    }
    private void setStartStatus(){
        start.setMinSize(150,50);
        start.setLayoutX(300);
        start.setLayoutY(350);
        start.setDisable(true);
        setContinueAction();
    }
    private void setBackStatus(){
        back.setMinSize(150,50);
        back.setLayoutX(300);
        back.setLayoutY(450);
        setBackAction();
    }
    private void setContinueAction(){
        start.setOnAction(EventHandler ->{
            players.getDirector().getPane().getChildren().removeAll(players.getDirector().getPane().getChildren());
            players.getDirector().start();
        });
    }
    private void setBackAction(){
        back.setOnAction(EventHandler ->{
            players.getDirector().getPane().getChildren().removeAll(players.getDirector().getPane().getChildren());
            players.load();
        });
    }
    private void setListViewStatus(){
        listView.setItems(list);
        listView.relocate(250,10);
        listView.setMaxHeight(300);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setOnMouseClicked(EventHandler -> {
            players.getDirector().setMapFile("src/main/resources/map-builder/"+
                    listView.getSelectionModel().getSelectedItem().toLowerCase()+".html");
            start.setDisable(false);
        });
    }
}
