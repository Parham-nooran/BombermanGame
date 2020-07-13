package ir.ac.kntu.logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class SerializedImageView extends ImageView implements Serializable {
    public SerializedImageView(Image image){
        super(image);
    }
}
