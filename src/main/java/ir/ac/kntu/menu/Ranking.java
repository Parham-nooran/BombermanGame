package ir.ac.kntu.menu;

import ir.ac.kntu.file.FileManager;
import ir.ac.kntu.logic.Director;
import ir.ac.kntu.logic.Player;
import javafx.scene.control.Button;

import java.beans.EventHandler;
import java.util.ArrayList;

public class Ranking {
    private Button ok;
    private Director director;
    public Ranking(Director director) {
        this.director = director;
        this.ok = new Button("Okay");
        setOkStatus();
        setAction();
    }
    private void setOkStatus(){
        ok.setMinSize(150,50);
        ok.setLayoutX(330);
        ok.setLayoutY(450);
    }
    public void setAction(){
        ok.setOnAction(EventHandler -> {
            director.getPane().getChildren().removeAll(ok);
            director.getMain().load();
        });
    }
    public void load(){
        director.getPane().getChildren().add(ok);
        ArrayList<Player> players = new FileManager().loadPlayers("players.txt");
        for(Player player:players){
            System.out.println(player);
        }
    }
}
