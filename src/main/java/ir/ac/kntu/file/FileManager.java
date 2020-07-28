package ir.ac.kntu.file;

import ir.ac.kntu.logic.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
        }
        return players;
    }

    public synchronized void storeInFile(ArrayList<Player> players, String address){
        ArrayList<Player> oldPlayers = loadPlayers("players.txt");
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(address, false);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            for(Player player :players){
                for(Player oldOne:oldPlayers){
                    oldOne.setNumberOfGames(player.equals(oldOne)?player.getNumberOfGames():oldOne.getNumberOfGames());
                    oldOne.setWins(player.equals(oldOne)?player.getWins():oldOne.getWins());
                }
            }
            mergePlayers(oldPlayers, players);
            for(Player player:oldPlayers){
                objectOutputStream.writeObject(player);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Indicated file does not exist");
        } catch (IOException e) {
            System.out.println("Something went wrong while trying to store in file");
        }
    }
    private void mergePlayers(ArrayList<Player> oldPlayer, ArrayList<Player> players){
        oldPlayer.addAll(players.stream().filter(player -> !oldPlayer.contains(player)).collect(Collectors.toList()));
    }
}
