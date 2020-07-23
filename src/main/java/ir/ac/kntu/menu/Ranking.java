package ir.ac.kntu.menu;

import ir.ac.kntu.logic.Director;
import ir.ac.kntu.logic.Player;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;

public class Ranking {
    private Button ok;
    private Director director;
    private ArrayList<Player> players;
    //private boolean on;
    public Ranking(Director director) {
        this.director = director;
        this.ok = new Button("Okay");
        this.players = director.getPlayers();
        //on = true;
        Collections.sort(players);
        players.get(0).setWins(players.get(0).getWins()+1);
        setOkStatus();
        setAction();
        loadPlayers();
    }
    private void setOkStatus(){
        ok.setMinSize(150,50);
        ok.setLayoutX(300);
        ok.setLayoutY(450);
    }
    public void setAction(){
        ok.setOnAction(EventHandler -> {
            director.getPane().getChildren().removeAll(director.getPane().getChildren());
            //on = false;
            director.getMain().load();
        });
    }
    public void load(){
        director.getPane().getChildren().add(ok);
        for(Player player:players){
            System.out.println(player);
        }
        //dance();
    }
    private void loadPlayers(){
        int rank = 1;
        Label ranking;
        Label name;
        for(int i=0;i<players.size();i++){
            imageSettings(i);
            director.getPane().getChildren().addAll((ranking = new Label(rank+"")),
                    (name = new Label(players.get(i).getName())));
            ranking.relocate(250,70+100*i);
            name.relocate(400, 70+100*i);
            setLabelStatus(name);
            setLabelStatus(ranking, rank);
            rank = i!=players.size()-1&&players.get(i).getTime()==(players.get(i+1).getTime())?rank:rank+1;
        }
    }
    private void setLabelStatus(Label label, int rank){
        label.setTextFill(rank<3?Color.GREEN:Color.RED);
        label.setScaleX(3);
        label.setScaleY(2);
    }
    private void setLabelStatus(Label label){
        label.setTextFill(Color.GREEN);
        label.setScaleX(3);
        label.setScaleY(2);
    }
    private void imageSettings(int i){
        players.get(i).setImage(players.get(i).getRankingImages()[0]);
        players.get(i).setFitHeight(50);
        players.get(i).setFitWidth(50);
        players.get(i).relocate(300, 50+100*i);
        director.getPane().getChildren().add(players.get(i));
    }
    /*private void dance(){
        new Thread(() -> {
            Executor executor = Executors.newSingleThreadExecutor();
            while (on) {
                executor.execute(() -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("Dancing interrupted");
                    }
                    players.get(0).setImage(players.get(0).getRankingImages()[1]);
                });
                executor.execute(() -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("Dancing interrupted");
                    }
                    players.get(0).setImage(players.get(0).getRankingImages()[0]);
                });
            }
        }).start();
    }*/
}
