package ir.ac.kntu.menu;

import ir.ac.kntu.file.FileManager;
import ir.ac.kntu.logic.Director;
import ir.ac.kntu.logic.Player;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Collections;

public class Ranking {
    private Button ok;
    private Director director;
    private ArrayList<Player> players;
    public Ranking(Director director) {
        this.director = director;
        this.ok = new Button("Okay");
        this.players = new FileManager().loadPlayers("players.txt");
        Collections.sort(players);
        setOkStatus();
        setAction();
        loadPlayers();
    }
    private void setOkStatus(){
        ok.setMinSize(150,50);
        ok.setLayoutX(330);
        ok.setLayoutY(450);
    }
    public void setAction(){
        ok.setOnAction(EventHandler -> {
            director.getPane().getChildren().removeAll(director.getPane().getChildren());
            director.getMain().load();
        });
    }
    public void load(){
        director.getPane().getChildren().add(ok);
        for(Player player:players){
            System.out.println(player);
        }
    }
    private void loadPlayers(){
        int rank = 1;
        for(int i=0;i<players.size();i++){
            Label label;
            imageSettings(i);
            director.getPane().getChildren().add((label = new Label(rank+"")));
            label.relocate(250,70+100*i);
            rank = i!=players.size()-1&&players.get(i).getTime()==(players.get(i+1).getTime())?rank:rank+1;
        }
    }
    private void imageSettings(int i){
        players.get(i).setImage(players.get(i).getRankingImages()[0]);
        players.get(i).setFitHeight(50);
        players.get(i).setFitWidth(50);
        players.get(i).relocate(300, 50+100*i);
        director.getPane().getChildren().add(players.get(i));
    }
}
