package ir.ac.kntu.file;

import ir.ac.kntu.logic.Player;

import java.io.*;
import java.util.ArrayList;

public class FileManager implements Serializable{
    public synchronized ArrayList<Player> loadPlayers(String address){
        ArrayList<Player> players = new ArrayList<>();
        try(
                FileInputStream fileInputStream = new FileInputStream(address);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        ){
            while(true){
                players.add((Player)objectInputStream.readObject());
            }
        } catch (EOFException e){
            System.out.println("All of the objects has been read");
        } catch(FileNotFoundException e){
            System.out.println("Indicated file does not exist");
        } catch(ClassNotFoundException e){
            System.out.println("The class has been changed");
        } catch (IOException e){
            System.out.println("Something went wrong while trying to read from file");
            e.printStackTrace();
        }
        return players;
    }
    public synchronized void storeInFile(ArrayList<Player> players, String address){
        ArrayList<Player> oldPlayers = loadPlayers("players.txt");
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(address);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {

            for(Player player :players){
                for(Player oldOne:oldPlayers){
                    player.setNumberOfGames(player.equals(oldOne)?oldOne.getNumberOfGames():player.getNumberOfGames());
                }
            }
            for(Player player:players){
                objectOutputStream.writeObject(player);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Indicated file does not exist");
        } catch (IOException e) {
            System.out.println("Something went wrong while trying to store in file");
        }
    }
}
