package com.example.sklep.db;

import com.example.sklep.model.Account;
import com.example.sklep.model.Category;
import com.example.sklep.model.Item;
import com.example.sklep.model.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CategoryRepository {
    private final Connection connection;
    private static final String tableName = "categories";
    private static final String colId = "id";
    private static final String colName = "name";
    public CategoryRepository(Connection connection) {
        this.connection = connection;
    }

    public long create(Category category) throws SQLException {
        //INSERT INTO categories VALUES (?);
        var statement = connection.prepareStatement(
                "INSERT INTO " + tableName +
                        "(" +  colName +
                        ") VALUES (?)"
        );
        //prepareStatement возвращает только результат
        statement.setString(1, category.getName());
        //сверяем тип данныз с бд (model.category)
        statement.executeUpdate();

        return getMaxId();
    }
    public void update(Category category) throws SQLException{
        //UPDATE accounts SET colUsername = ?, colPassword = ? WHERE colId = ? ;
        var statement =  connection.prepareStatement(
                "UPDATE " + tableName + "SET " + colName + " = ?" +
                        " WHERE " + colId + " = ?"
        );
        statement.setString(1, category.getName());
        statement.executeUpdate();
    }
    public void delete(long id) throws SQLException {
        //DELETE FROM accounts WHERE colId = ?;
        var statement =  connection.prepareStatement(
                "DELETE FROM " + tableName + " WHERE " + colId + " = ?"
        );
        statement.setLong(1, id);
        statement.executeUpdate();
    }
    public Category getById(long id) throws SQLException {
        //SELECT * FROM accounts WHERE colId = ?;
        var statement =  connection.prepareStatement(
                "SELECT * FROM " + tableName + " WHERE " + colId + " = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        statement.setLong(1, id);
        var result = statement.executeQuery();

        return convertToObj(result);
    }
    public List<Category> getAll() throws SQLException {
        // SELECT * FROM accounts;
        var statement =  connection.prepareStatement(
                "SELECT * FROM " + tableName ,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        var result = statement.executeQuery();

        return convertToList(result);
    }

    private long getMaxId() throws SQLException {
        //SELECT MAX(id) FROM categories;
        var statement = connection.prepareStatement(
                "SELECT MAX (" + colId + ") FROM " + tableName,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
                //если данных много, то отображаются порционно (например только по 20)
        );
        var result = statement.executeQuery();
        result.next();//стоим на 1 записи в resultSet

        return result.getLong(1); // получение данных из 1 колонки

    };

    private Category convert(ResultSet resultSet) throws SQLException {
        var obj = new Category();
        obj.setId(resultSet.getInt(colId));
        obj.setName(resultSet.getString(colName));

        return obj;
    }

    private Category convertToObj(ResultSet resultSet) throws SQLException {
        resultSet.beforeFirst();// итератора на значение -1
        if (!resultSet.next())
            return new Category();

        return convert(resultSet);
    }

    private List<Category> convertToList(ResultSet resultSet) throws SQLException {
        var list = new ArrayList<Category>();
        resultSet.beforeFirst();
        while (resultSet.next()) {
            list.add(convert(resultSet));
        }
        return list;
    }
}


