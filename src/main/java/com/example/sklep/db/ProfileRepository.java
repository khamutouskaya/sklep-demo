package com.example.sklep.db;

import com.example.sklep.model.Account;
import com.example.sklep.model.Profile;
import com.example.sklep.model.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfileRepository {
    private final Connection connection;
    private static final String tableName = "profiles";
    private static final String colId = "id";
    private static final String colName = "name";
    private static final String colSurname = "surname";
    private static final String colAddress = "address";
    private static final String colAccount = "account";

    public ProfileRepository(Connection connection) {
        this.connection = connection;
    }

    public long create(Profile profile) throws SQLException {
        //INSERT INTO profiles VALUES (?, ?, ?, ?);
        var statement = connection.prepareStatement(
                "INSERT INTO " + tableName +
                        "(" + colName + ", " + colSurname + ", " +  colAddress + ", " + colAccount +
                        ") VALUES (?, ?, ?, ?)"
        );
        //ja
        statement.setString(1, profile.getName());
        statement.setString(2, profile.getSurname());
        statement.setString(3,profile.getAddress());
        statement.setString(4, profile.getAccount().toString());
        statement.executeUpdate();

        return getMaxId();
    }
    public void update(Profile profile) throws SQLException {
        //UPDATE profiles SET
        var statement = connection.prepareStatement(
                "UPDATE " + tableName + "SET" + colName + ", " + colSurname + ", " +  colAddress + ", " + colAccount +
                        " WHERE " + colId + " = ?"
        );
        //ja
        statement.setString(1, profile.getName());
        statement.setString(2, profile.getSurname());
        statement.setString(3,profile.getAddress());
        statement.setLong(4, profile.getAccount().getId());
        statement.setLong(5, profile.getId());
        statement.executeUpdate();

    }
    public void delete(long id) throws SQLException {
        //DELETE FROM profiles WHERE id = ?;
        var statement =  connection.prepareStatement(
                "DELETE FROM " + tableName + " WHERE " + colId + " = ?"
        );
        statement.setLong(1, id);
        statement.executeUpdate();
    }
    public Profile getById(long id) throws SQLException {
        //SELECT * FROM accounts WHERE id = ?;
        var statement =  connection.prepareStatement(
                "SELECT * FROM " + tableName + " WHERE " + colId + " = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        statement.setLong(1, id);
        var result = statement.executeQuery();

        return convertToObj(result);
    }
    public List<Profile> getAll() throws SQLException {
        // SELECT * FROM orders;
        var statement =  connection.prepareStatement(
                "SELECT * FROM " + tableName ,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        var result = statement.executeQuery();

        return convertToList(result);
    }
    private long getMaxId() throws SQLException {
        //SELECT MAX(id) FROM orders;
        var statement = connection.prepareStatement(
                "SELECT MAX (" + colId + ") FROM " + tableName,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
                //если данных много, то отображаются порционно (например только по 20)
        );
        var result = statement.executeQuery(); result.next();

        return result.getLong(1);
    }

    private Profile convert(ResultSet resultSet) throws SQLException {
        var obj = new Profile();
        obj.setId(resultSet.getInt(colId));
        obj.setName(resultSet.getString(colName));
        obj.setSurname(resultSet.getString(colSurname));
        obj.setAddress(resultSet.getString(colAddress));// from string to enum
        obj.setAddress(resultSet.getString(colAccount));

        return obj;
    }

    private Profile convertToObj(ResultSet resultSet) throws SQLException {
        resultSet.beforeFirst();
        if (!resultSet.next())
            return new Profile();

        return convert(resultSet);
    }

    private List<Profile> convertToList(ResultSet resultSet) throws SQLException {
        var list = new ArrayList<Profile>();
        resultSet.beforeFirst();
        while (resultSet.next()) {
            list.add(convert(resultSet));
        }

        return list;
    }
}
