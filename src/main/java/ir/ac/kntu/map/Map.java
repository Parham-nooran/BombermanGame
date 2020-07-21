package ir.ac.kntu.map;

import ir.ac.kntu.logic.Director;
import ir.ac.kntu.logic.Element;
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
    public Map(Director director, String address) {
        this.director = director;
        this.address = address;
        elements = new String[12][];
        playersCoordinates = new ArrayList<>();
        director.setMap(this);
    }
    public void load()  {
        try {
            Image block = new Image(new FileInputStream("src/main/resources/assets/map/normal.png"));
            loadBackGround(block);
            /*loadIronWalls();
            loadRows();
            loadColumns();
            loadBrickWalls();*/
            loadElements();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    private void loadElements(){
        readMapFile();
        for(int i=0;i<11;i++){
            for(int j=0;j<15;j++) {
                loadElement(elements[i][j], 50*j, 50*i);
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

    public ArrayList<Integer> getPlayersCoordinates() {
        return new ArrayList<>(playersCoordinates);
    }

    private void loadRows(){
        loadARow(0,0,16);
        loadARow(0, 11*50, 16);
    }
    private void loadColumns(){
        loadAColumn(0, 50,10);
        loadAColumn(15*50, 50, 10);
    }
    private void loadARow(int xBegin, int yCenter, int number){
        for(int i=0;i<number;i++){
            director.getPane().getChildren().add(new Wall(50*i+xBegin, yCenter, Type.IRON));
        }
    }
    private void loadAColumn(int xCenter, int yBegin, int number){
        for(int i=0;i<number;i++){
            director.getPane().getChildren().add(new Wall(xCenter, 50*i+yBegin, Type.IRON));
        }
    }
    private void loadBackGround(Image image){
        for(int i=0;i<16;i++){
            for(int j=0;j<12;j++){
                director.getPane().getChildren().add(new Block(50*i,50*j,image));
            }
        }
    }
    private void loadBrickWalls(){
        for (int i = 0; i < 60;) {
            int xCenter = (int) ((16 * Math.random())) * 50, yCenter =(int) ((12 * Math.random())) * 50;
            if(director.getPane().getChildren().stream().filter(node -> node instanceof Element).
                    noneMatch(node -> ((Element)node).getXCenter()==xCenter&&
                            ((Element) node).getYCenter()==yCenter)) {
                director.getPane().getChildren().add(new Wall(xCenter, yCenter, Type.BRICK));
                i++;
            }
        }
    }
    private void loadIronWalls(){
        for (int i = 1; i < 12; i += 2) {
            for (int j = 1; j < 8; j += 2) {
                director.getPane().getChildren().
                        add(new Wall(50 * i + 50, 50 * j + 50, Type.IRON));
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
                System.out.println("");
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
