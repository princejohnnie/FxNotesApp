package com.example.crudapp;

import com.example.crudapp.database.NoteDao;
import com.example.crudapp.model.Note;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotesController implements Initializable {

    @FXML
    private Button deleteButton;

    @FXML
    private ListView<Note> notesList;

    @FXML
    private TextArea noteText;

    @FXML
    private Button saveButton;

    @FXML
    private TextField noteTitle;

    @FXML
    private Label infoText;

   // ObservableList<Note> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        notesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        notesList.setItems((NoteDao.getNotes()));

        notesList.setCellFactory(new Callback<ListView<Note>, ListCell<Note>>() {
            @Override
            public ListCell<Note> call(ListView<Note> param) {

               // return new NoteListCellFactory();

                return new ListCell<>(){
                    @Override
                    protected void updateItem(Note item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null){
                            setText(null);
                        }else {
                            setText(item.getNoteTitle());
                        }
                    }
                };
            }
        });
    }

    public void showNote(){

        if (notesList.getSelectionModel().getSelectedItem() == null ){
            return;
        }

        Note note = notesList.getSelectionModel().getSelectedItem();
        noteTitle.setText(note.getNoteTitle());
        noteText.setText(note.getNoteText());

    }

    public void saveNote(){
        String newNoteTitle = noteTitle.getText();
        String newNoteText = noteText.getText();

        if (newNoteTitle.isBlank() && newNoteText.isBlank()){
            infoText.setText("Cannot create empty note");
            return;
        }

        Note selectedNote = notesList.getSelectionModel().getSelectedItem();
        if (selectedNote == null){
            insertNote(newNoteTitle, newNoteText);
        }else {
            selectedNote.setNoteTitle(noteTitle.getText());
            selectedNote.setNoteText(noteText.getText());
            updateNote(selectedNote);
        }

    }

    public void deleteNote(){
        if (notesList.getSelectionModel().getSelectedItem() == null){
            infoText.setText("Select a note to delete");
            return;
        }
        Note note = notesList.getSelectionModel().getSelectedItem();
        int noteId = note.getId();
        int rows = NoteDao.deleteNote(noteId);
        infoText.setText("Note deleted");

        Logger.getAnonymousLogger().log(Level.INFO, "Deleted " + rows + " row(s)");

    }

    public void updateNote(Note selectedNote){

        int rows = NoteDao.updateNote(selectedNote);
        infoText.setText("Note updated");
        Logger.getAnonymousLogger().log(Level.INFO, "Updated " + rows + " row(s)");

    }

    private void insertNote(String newNoteTitle, String newNoteText){
        int noteId = NoteDao.insertNote(newNoteTitle, newNoteText);
        infoText.setText("Note saved");
        Logger.getAnonymousLogger().log(Level.INFO, "Note created with id " + noteId);
    }
}