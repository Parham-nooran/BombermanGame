package ir.ac.kntu.file;

import ir.ac.kntu.logic.Player;

import java.io.*;
import java.util.ArrayList;

public class FileManager implements Serializable{
    public synchronized void storePlayers(ArrayList<Player> players, String address){
        for(Player player:players){
            storeInFile(player, address);
        }
    }
    public synchronized ArrayList<Player> loadPlayers(String address){
        ArrayList<Player> players = new ArrayList<>();
        Player player;
        try(
                FileInputStream fileInputStream = new FileInputStream(address);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        ){
            while((player = (Player)objectInputStream.readObject())!=null){
                players.add((Player)objectInputStream.readObject());
            }
        } catch(FileNotFoundException e){
            System.out.println("Indicated file does not exist");
        }catch(IOException | ClassNotFoundException e){
            System.out.println("Something went wrong while trying to read from file");
        }
        return players;
    }
    public synchronized <T extends Serializable>T loadFromFile(String address){
        try(
                FileInputStream fileInputStream = new FileInputStream(address);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        ){
            return (T)objectInputStream.readObject();
        } catch(FileNotFoundException e){
            System.out.println("Indicated file does not exist");
            e.printStackTrace();
        }catch(IOException | ClassNotFoundException e){
            System.out.println("Something went wrong while trying to read from file");
            e.printStackTrace();
        }
        return null;
    }

    public synchronized <T>void storeInFile(T t, String address){
        checkFile(address);
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(address);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(t);
        } catch (FileNotFoundException e) {
            System.out.println("Indicated file does not exist");
        } catch (IOException e) {
            System.out.println("Something went wrong while trying to store in file");
        }
    }
    private void checkFile(String adress){
        File file = new File(adress);
        if(file.exists()){
            file.delete();
        }
    }
}
