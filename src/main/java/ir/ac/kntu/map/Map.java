package ir.ac.kntu.map;

import ir.ac.kntu.logic.Director;
import ir.ac.kntu.logic.PowerUp;
import javafx.scene.image.Image;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Map implements Serializable {
    private Director director;
    private String address;
    private String[][] elements;
    private ArrayList<Integer> playersCoordinates;
    private int size;
    public Map(Director director, String address, int size) {
        this.director = director;
        this.address = address;
        elements = new String[12][];
        this.size = size;
        playersCoordinates = new ArrayList<>();
    }
    public void load()  {
        try {
            Image block = new Image(new FileInputStream("src/main/resources/assets/map/normal.png"));
            loadBackGround(block);
            loadElements(true);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    public void loadElements(boolean addToPane){
        readMapFile();
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 15; j++) {
                if(addToPane) {
                    loadElement(elements[i][j], size * j, size * i);
                } else {
                    loadElementInArray(elements[i][j], size * j, size * i);
                }
            }
        }
    }
    private void loadElement(String element, int xCenter, int yCenter){
        switch (element){
            case "p":
                director.getPane().getChildren().add(new PowerUp(director.getPane(), xCenter, yCenter));
                break;
            case "b":
                director.getPane().getChildren().add(new Wall(xCenter, yCenter, Type.BRICK));
                break;
            case "w":
                director.getPane().getChildren().add(new Wall(xCenter, yCenter, Type.IRON));
                break;
            case "r":
                director.getPane().getChildren().add(new OneWay(xCenter, yCenter, Side.RIGHT));
                break;
            case "l":
                director.getPane().getChildren().add(new OneWay(xCenter, yCenter, Side.LEFT));
                break;
            case "d":
                director.getPane().getChildren().add(new OneWay(xCenter, yCenter, Side.DOWN));
                break;
            case "u":
                director.getPane().getChildren().add(new OneWay(xCenter, yCenter, Side.UP));
                break;
            case "":
                break;
            default:
                int place = Integer.parseInt(element);
                if(place>0&&place<5){
                    playersCoordinates.add(1000*xCenter+yCenter);
                }
                break;
        }
    }
    private void loadElementInArray(String element, int xCenter, int yCenter){
        switch (element){
            case "p":
            case "b":
            case "w":
            case "r":
            case "l":
            case "d":
            case "u":
            case "":
                break;
            default:
                int place = Integer.parseInt(element);
                if(place>0&&place<5){
                    playersCoordinates.add(1000*xCenter+yCenter);
                }
                break;
        }
    }
    public ArrayList<Integer> getPlayersCoordinates(boolean addToPane) {
        if(playersCoordinates.isEmpty()){
            loadElements(addToPane);
        }
        return new ArrayList<>(playersCoordinates);
    }

    private void loadBackGround(Image image){
        for(int i=0;i<15;i++){
            for(int j=0;j<11;j++){
                director.getPane().getChildren().add(new Block(size*i,size*j,image));
            }
        }
    }

    private void readMapFile(){
        String line;
        int row=0;
        try(
                FileReader fileReader = new FileReader(address);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
        ){
            while((line = bufferedReader.readLine())!=null&&!line.trim().equals("var board = [")) {
                System.out.print("");
            }
            while((line = bufferedReader.readLine())!=null&&!line.trim().equals("];")
                    &&!line.trim().equals("print_board(board);")) {
                parseALine(line.trim(), row++);
            }
        } catch (IOException e){
            System.out.println("A problem occurred while trying to read the map file");
        }
    }
    private void parseALine(String line, int row){
        int column=0;
        String regex = "'(\\w?)'";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        elements[row] = new String[16];
        while(matcher.find()){
            elements[row][column++] = matcher.group(0).replace("'","");
        }
    }
}
