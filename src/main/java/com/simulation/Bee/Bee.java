package com.simulation.Bee;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.UUID;

public abstract class Bee implements IBehaviour {
    private final UUID id;

    public UUID getId() {
        return id;
    }

    public Bee(UUID id, String imagePath) {
        this.id = id;
        this.imageView = new ImageView(new Image(imagePath));
    }


    final private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }


}
