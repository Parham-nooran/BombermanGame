package ir.ac.kntu.logic;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.io.Serializable;

public class SerializedImage extends Image implements Serializable {
    public SerializedImage(){
        super("");
    }
    public SerializedImage(String s) {
        super(s);
    }

    public SerializedImage(String s, boolean b) {
        super(s, b);
    }

    public SerializedImage(String s, double v, double v1, boolean b, boolean b1) {
        super(s, v, v1, b, b1);
    }

    public SerializedImage(String s, double v, double v1, boolean b, boolean b1, boolean b2) {
        super(s, v, v1, b, b1, b2);
    }

    public SerializedImage(InputStream inputStream) {
        super(inputStream);
    }

    public SerializedImage(InputStream inputStream, double v, double v1, boolean b, boolean b1) {
        super(inputStream, v, v1, b, b1);
    }
}