package ir.ac.kntu.logic;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.Serializable;

public class SerializedImage extends Image implements Serializable {
    public SerializedImage(FileInputStream inputStream){
        super(inputStream);
    }
}
