package com.bruno.desafiomuxi.webrequest;

/**
 * Created by Bruno on 16/08/2017.
 */

public class Fruit {
    private final String name, image;
    private final double price;

    public Fruit(final String name, final String image, double price) {
        this.name = name;
        this.image = image;
        this.price = price;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Fruit) {
            Fruit otherFruit = (Fruit)other;

            return  price == otherFruit.price && name.compareTo(otherFruit.name) == 0 && image.compareTo(otherFruit.image) == 0;
        } else {
            return false;
        }
    }

    public final String getName() {
        return name;
    }

    public final String getImageUrl() {
        return image;
    }

    public final double getPrice() {
        return price;
    }
}
