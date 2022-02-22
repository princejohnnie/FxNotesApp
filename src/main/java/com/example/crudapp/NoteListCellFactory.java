package com.example.crudapp;

import com.example.crudapp.model.Note;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.SplitPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NoteListCellFactory extends ListCell<Note> {

    @FXML
    public SplitPane splitPane;

    @FXML
    public Label title;

    @FXML
    public Label text;

    FXMLLoader mLLoader;

    public NoteListCellFactory(){
        if (mLLoader == null) {
            mLLoader = new FXMLLoader(NotesApplication.class.getResource("noteslistcell-view.fxml"));
            mLLoader.setController(this);

            try {
                mLLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void updateItem(Note item, boolean empty) {
        super.updateItem(item, empty);


        if(empty || item == null) {

            setText(null);
            setGraphic(null);

        } else {
            /*if (mLLoader == null) {
                mLLoader = new FXMLLoader(NotesApplication.class.getResource("noteslistcell-view.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }*/

            title.setText(item.getNoteTitle());
            text.setText(item.getNoteText());
        }

    }

}
