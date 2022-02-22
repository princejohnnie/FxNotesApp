package com.example.crudapp.database;

import com.example.crudapp.model.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Optional;

public class NoteDao {
    private static final String DB_NAME = "notes_db";
    public static final String TABLE_NAME = "notes";
    public static final String COL_ID = "id";
    public static final String COL_NOTE_TITLE = "col_note_title";
    public static final String COL_NOTE_TEXT = "col_note_text";

    public static ObservableList<Note> observableNoteList = FXCollections.observableArrayList();

    public static DbConnection dbConnection = new DbConnection();

    private static void retrieveNotes(){

        CRUDHelper.readAll(TABLE_NAME);

    }
    public static ObservableList<Note> getNotes() {
        retrieveNotes();
        return FXCollections.unmodifiableObservableList(observableNoteList);
    }

    public static int insertNote(String noteTitle, String noteText){
        //update database
        int id = (int) CRUDHelper.create(
                TABLE_NAME,
                new String[]{"col_note_title", "col_note_text"},
                new Object[]{noteTitle, noteText},
                new int[]{Types.VARCHAR, Types.VARCHAR});

        //update cache
        observableNoteList.add(new Note(
                id,
                noteTitle,
                noteText
        ));
        return id;
    }

    public static int deleteNote(int id){
        // update db
        int deletedRows = CRUDHelper.delete(TABLE_NAME, id);

        // update cache
        Optional<Note> note = getNote(id);
        note.ifPresent(observableNoteList::remove);

        return deletedRows;
    }

    public static int updateNote(Note newNote){
        // update db
        int updatedRows = CRUDHelper.update(TABLE_NAME,
                        new String[]{COL_NOTE_TITLE, COL_NOTE_TEXT},
                        new Object[]{newNote.getNoteTitle(), newNote.getNoteText()},
                        new int[]{Types.VARCHAR, Types.VARCHAR},
                        COL_ID,
                        Types.INTEGER,
                        newNote.getId()
                        );

        if (updatedRows == 0)
            throw new IllegalStateException("Note to be updated with id " + newNote.getId() + " does not exist in database");

        return updatedRows;
    }

    public static Optional<Note> getNote(int id) {
        for (Note note : observableNoteList) {
            if (note.getId() == id) return Optional.of(note);
        }
        return Optional.empty();
    }
}
