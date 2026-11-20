package com.example.sklep.db;

import com.example.sklep.model.Account;
import com.example.sklep.model.Item;
import com.example.sklep.model.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    private final Connection connection;
    private static final String tableName = "items";
    private static final String colId = "id";
    private static final String colName = "name";
    private static final String colPrecision = "precision";
    private static final String colCount = "count";
    private static final String colDescription = "description";

    public ItemRepository(Connection connection) {
        this.connection = connection;
    }

    public long create(Item item) throws SQLException {
        //INSERT INTO items VALUES (?, ?, ?, ?);

        var statement = connection.prepareStatement(
                "INSERT INTO " + tableName +
                        "(" +  colName + ", " +  colPrecision + ", " + colCount + ", " + colDescription +
                        ") VALUES (?, ?, ?, ?)"
        );
        //prepareStatement возвращает только результат
        statement.setString(1, item.getName());
        statement.setDouble(2, item.getPrecision());
        statement.setInt(3, item.getCount());
        statement.setString(4, item.getDescription());
        //сверяем тип данныз с бд (model.account)
        statement.executeUpdate();

        return getMaxId();
    }
    public void update(Item item) throws SQLException{
        var statement =  connection.prepareStatement(
                "UPDATE " + tableName +
                        "(" +  colName + ", " +  colPrecision + ", " + colCount + ", " + colDescription +
                        " WHERE " + colId + " = ?"
        );
        statement.setString(1, item.getName());
        statement.setDouble(2, item.getPrecision());
        statement.setLong(3, item.getCount());
        statement.setString(4, item.getDescription());
        statement.setLong(5, item.getId());
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
    public Item getById(long id) throws SQLException {
        //SELECT * FROM accounts WHERE colId = ?;
        var statement =  connection.prepareStatement(
                "SELECT * FROM " + tableName + " WHERE " + colId + " = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        statement.setLong(1, id);
        var result = statement.executeQuery();

        return convertToObj(result);
    }
    public List<Item> getAll() throws SQLException {
        // SELECT * FROM accounts;
        var statement =  connection.prepareStatement(
                "SELECT * FROM " + tableName ,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        var result = statement.executeQuery();

        return convertToList(result);
    }

    private long getMaxId() throws SQLException {
        //SELECT MAX(id) FROM items;
        var statement = connection.prepareStatement(
                "SELECT MAX (" + colId + ") FROM " + tableName,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
                //если данных много, то отображаются порционно (например только по 20)
        );
        var result = statement.executeQuery(); // только для чтения из бд (выполняет строку 62), result = set
        result.next();//стоим на 1 записи в resultSet

        return result.getLong(1); // получение данных из 1 колонки

    }

    private Item convert(ResultSet resultSet) throws SQLException {
        var obj = new Item();
        obj.setId(resultSet.getInt(colId));
        obj.setName(resultSet.getString(colName));
        obj.setPrecision(resultSet.getDouble(colPrecision));
        obj.setCount(resultSet.getInt(colCount));// from string to enum
        obj.setDescription(resultSet.getString(colDescription));

        return obj;
    }

    private Item convertToObj(ResultSet resultSet) throws SQLException {
        resultSet.beforeFirst();// итератора на значение -1
        if (!resultSet.next())
            return new Item();

        return convert(resultSet);
    }

    private List<Item> convertToList (ResultSet resulSet) throws SQLException {
        var list = new ArrayList<Item>();
        resulSet.beforeFirst();
        while (resulSet.next()) {
            list.add(convert(resulSet));
        }
        return list;
    }

}
