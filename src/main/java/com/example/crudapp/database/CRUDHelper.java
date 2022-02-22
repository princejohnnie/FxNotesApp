package com.example.crudapp.database;

import com.example.crudapp.model.Note;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.crudapp.database.NoteDao.observableNoteList;

/**
 *  Helper class for CRUD operations
 */
public class CRUDHelper {
    public static DbConnection dbConnection = new DbConnection();

    public static Object read(String tableName, String fieldName, int fieldDataType,
                              String indexFieldName, int indexDataType, Object index) {
        StringBuilder queryBuilder = new StringBuilder("Select ");
        queryBuilder.append(fieldName);
        queryBuilder.append(" from ");
        queryBuilder.append(tableName);
        queryBuilder.append(" where ");
        queryBuilder.append(indexFieldName);
        queryBuilder.append(" = ");
        queryBuilder.append(convertObjectToSQLField(index, indexDataType));
        try {
            Connection connection = dbConnection.getDbConnection();
            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                switch (fieldDataType) {
                    case Types.INTEGER:
                        return rs.getInt(fieldName);
                    case Types.VARCHAR:
                        return rs.getString(fieldName);
                    default:
                        throw new IllegalArgumentException("Index type " + indexDataType + " from sql.Types is not yet supported.");
                }
            }
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not fetch from " + tableName + " by index " + index +
                            " and column " + fieldName);
            return null;
        }
    }

    public static void readAll(String tableName) {
        StringBuilder queryBuilder = new StringBuilder("Select ");
        queryBuilder.append("*");
        queryBuilder.append(" from ");
        queryBuilder.append(tableName);

        try {
            Connection connection = dbConnection.getDbConnection();
            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                int id = rs.getInt("id");
                String noteTitle = rs.getString("col_note_title");
                String noteText = rs.getString("col_note_text");

                observableNoteList.add(new Note(id, noteTitle, noteText));

                 System.out.println("Id: " + id + " Title: " + noteTitle + " Text: " + noteText);
            }

        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not fetch data from " + tableName);
        }
    }


    public static int update(String tableName, String[] columns, Object[] values, int[] types,
                             String indexFieldName, int indexDataType, Object index) {

        int number = Math.min(Math.min(columns.length, values.length), types.length);

        StringBuilder queryBuilder = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < number; i++) {
            queryBuilder.append(columns[i]);
            queryBuilder.append(" = ");
            queryBuilder.append(convertObjectToSQLField(values[i], types[i]));
            if (i < number - 1) queryBuilder.append(", ");
        }
        queryBuilder.append(" WHERE ");
        queryBuilder.append(indexFieldName);
        queryBuilder.append(" = ");
        queryBuilder.append(convertObjectToSQLField(index, indexDataType));

        try {
            Connection connection = dbConnection.getDbConnection();
            PreparedStatement pstmt = connection.prepareStatement(queryBuilder.toString());

            return pstmt.executeUpdate(); //number of affected rows
        } catch (Exception ex) {
            System.out.println(ex.toString());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not add note to database");
            return -1;
        }
    }

    public static long create(String tableName, String[] columns, Object[] values, int[] types) {
        int number = Math.min(Math.min(columns.length, values.length), types.length);

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + tableName + " (");
        for (int i = 0; i < number; i++) {
            queryBuilder.append(columns[i]);
            if (i < number - 1) queryBuilder.append(", ");
        }
        queryBuilder.append(") ");
        queryBuilder.append(" VALUES (");
        for (int i = 0; i < number; i++) {
            switch (types[i]) {
                case Types.VARCHAR:
                    queryBuilder.append("'");
                    queryBuilder.append((String) values[i]);
                    queryBuilder.append("'");
                    break;
                case Types.INTEGER:
                    queryBuilder.append((int) values[i]);
            }
            if (i < number - 1) queryBuilder.append(", ");
        }
        queryBuilder.append(");");

        try {
            Connection connection = dbConnection.getDbConnection();
            PreparedStatement pstmt = connection.prepareStatement(queryBuilder.toString(), Statement.RETURN_GENERATED_KEYS);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not add note to database");
            return -1;
        }
        return -1;
    }

    public static int delete(String tableName, int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try {
            Connection conn = dbConnection.getDbConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not delete from " + tableName + " by id " + id +
                            " because " + e.getCause());
            return -1;
        }
    }

    private static String convertObjectToSQLField(Object value, int type) {
        StringBuilder queryBuilder = new StringBuilder();
        switch (type) {
            case Types.VARCHAR:
                queryBuilder.append("'");
                queryBuilder.append(value);
                queryBuilder.append("'");
                break;
            case Types.INTEGER:
                queryBuilder.append(value);
                break;
            default:
                throw new IllegalArgumentException("Index type " + type + " from sql.Types is not yet supported.");
        }
        return queryBuilder.toString();
    }
}
