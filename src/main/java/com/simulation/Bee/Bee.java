package com.simulation.Bee;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.Serializable;
import java.util.UUID;

public abstract class Bee implements Serializable {
    private final UUID id;

    protected static int lifeTime;

    public static int getLifeTime() {
        return lifeTime;
    }

    public UUID getId() {
        return id;
    }

    public Bee(UUID id, String imagePath) {
        this.id = id;
        this.imageView = new ImageView(new Image(new File(imagePath).toURI().toString()));
    }


    transient private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }


    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }


    public void move() {

    }
}
