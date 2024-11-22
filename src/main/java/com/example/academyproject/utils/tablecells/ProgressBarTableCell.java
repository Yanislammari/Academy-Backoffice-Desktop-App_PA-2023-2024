package com.example.academyproject.utils.tablecells;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;

public class ProgressBarTableCell<S> extends TableCell<S, Double> {
    private final ProgressBar progressBar;

    public ProgressBarTableCell() {
        this.progressBar = new ProgressBar();
        this.progressBar.setMaxWidth(Double.MAX_VALUE);
    }

    @Override
    protected void updateItem(Double progress, boolean empty) {
        super.updateItem(progress, empty);
        if(empty || progress == null) {
            setGraphic(null);
        }
        else {
            this.progressBar.setProgress(progress);
            setGraphic(this.progressBar);
        }
    }
}
