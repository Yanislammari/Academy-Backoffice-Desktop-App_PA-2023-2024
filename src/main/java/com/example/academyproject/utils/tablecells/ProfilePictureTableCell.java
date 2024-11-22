package com.example.academyproject.utils.tablecells;

import com.example.academyproject.models.User;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProfilePictureTableCell extends TableCell<User, String> {
    private final ImageView imageView = new ImageView();

    @Override
    protected void updateItem(String imageUrl, boolean empty) {
        super.updateItem(imageUrl, empty);
        if(empty || imageUrl == null) {
            setGraphic(null);
        }
        else {
            this.imageView.setImage(new Image(imageUrl));
            setGraphic(this.imageView);
        }
    }
}
