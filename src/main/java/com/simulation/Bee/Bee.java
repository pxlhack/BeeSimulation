package com.simulation.Bee;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Bee implements IBehaviour {

    Bee(String imagePath) {
        this.imagePath = imagePath;
        this.imageView = new ImageView(new Image(this.imagePath));
    }


    final private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }

    protected String imagePath;

}
