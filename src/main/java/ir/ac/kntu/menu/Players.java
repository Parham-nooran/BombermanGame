package ir.ac.kntu.menu;

import ir.ac.kntu.file.FileManager;
import ir.ac.kntu.logic.Director;
import ir.ac.kntu.logic.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Players extends Menu{
    private ArrayList<Player> oldPlayers;
    private ListView<String> listView;
    private ObservableList<String> list;
    private Button newPlayer;
    private Button button;
    private Button back;
    private Director director;
    private Main main;
    private int numberOfPlayers;
    private ArrayList<Integer> coordinates;
    private ArrayList<Integer> availableCoordinates;
    public Players(Director director, int numberOfPlayers, Main main) {
        super(director.getPane());
        oldPlayers = new FileManager().loadPlayers("players.txt");
        this.director = director;
        this. main = main;
        this.back = new Button("Back");
        this.numberOfPlayers = numberOfPlayers;
        this.button = new Button("Continue");
        this.newPlayer = new Button("Add Player");
        this.list = FXCollections.observableArrayList(oldPlayers.stream().parallel()
                .map(player -> player.getName()+"   Games : "+player.getNumberOfGames()+"   Wins : "+player.getWins())
                .collect(Collectors.toList()));
        listView = new ListView<>();
        this.coordinates = director.getMap().getPlayersCoordinates(false);
        this.availableCoordinates = availableCoordinates();
        setListViewStatus();
        setNewPlayersStatus();
        setButtonStatus();
        setBackStatus();
    }
    private void setListViewStatus(){
        listView.setItems(list);
        listView.relocate(250,10);
        listView.setMaxHeight(300);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setOnMouseClicked(EventHandler ->{
            ObservableList<String> item =  listView.getSelectionModel().getSelectedItems();
            checkSelectedItems(item);
            checkSizeOfPlayers();
        });
    }
    private void checkSelectedItems(ObservableList<String> item){
        director.addAll(oldPlayers.stream().filter(player -> item.contains(player.getName()+
                "   Games : "+player.getNumberOfGames()+"   Wins : "+player.getWins()))
                .collect(Collectors.toCollection(ArrayList::new)));
        director.removeAll(oldPlayers.stream().filter(player -> !item.contains(player.getName()+
                "   Games : "+player.getNumberOfGames()+"   Wins : "+player.getWins()))
                .collect(Collectors.toCollection(ArrayList::new)));
    }
    private void setBackStatus(){
        back.setMinSize(150,50);
        back.setLayoutX(300);
        back.setLayoutY(450);
        setBackAction();
    }
    private void setBackAction(){
        back.setOnAction(EventHandler ->{
            director.getPane().getChildren().removeAll(director.getPane().getChildren());
            main.load();
        });
    }
    public void load(){
        checkSizeOfPlayers();
        getPane().getChildren().addAll(listView, button, newPlayer, back);
    }
    private void setNewPlayersStatus(){
        newPlayer.setMinSize(150,50);
        newPlayer.setLayoutX(300);
        newPlayer.setLayoutY(350);
        setOnNewPlayersAction();
    }
    private void setButtonStatus(){
        button.setMinSize(150,50);
        button.setLayoutX(300);
        button.setLayoutY(400);
        button.setDisable(true);
        setButtonAction();
    }
    private void checkSizeOfPlayers(){
        this.newPlayer.setDisable(director.getPlayers().size()>=numberOfPlayers);
        button.setDisable(!(director.getPlayers().size()==numberOfPlayers));
    }
    private void setOnNewPlayersAction(){
        newPlayer.setOnAction(EventHandler ->{
            getPane().getChildren().removeAll(director.getPane().getChildren());
            new NewPlayer(this).load();
        });
    }
    public ArrayList<Integer> availableCoordinates(){
        director.getPlayers().iterator().forEachRemaining(player ->
                coordinates.remove(Integer.valueOf(1000 * player.getXCenter() + player.getYCenter())));
        return coordinates;
    }

    public ArrayList<Integer> getAvailableCoordinates() {
        return availableCoordinates;
    }

    public void setButtonAction(){
        button.setOnAction(EventHandler -> {
            getPane().getChildren().removeAll(director.getPane().getChildren());
            new Maps(this).load();
        });
    }
    public void addPlayer(Player player){
        director.addPlayer(player);
    }

    public Director getDirector() {
        return director;
    }
}
