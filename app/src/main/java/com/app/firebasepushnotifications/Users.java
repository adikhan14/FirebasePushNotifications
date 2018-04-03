package com.app.firebasepushnotifications;

/**
 * Created by Adil khan on 2/12/2018.
 */

public class Users extends UserId {

    String name;
    String image;

    public Users(String name, String image) {
        this.name = name;
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public Users() {
    }

    @Override
    public String toString() {
        return "Users{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
