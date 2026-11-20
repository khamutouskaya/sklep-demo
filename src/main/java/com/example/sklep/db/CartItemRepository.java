package com.example.sklep.db;

import com.example.sklep.model.Account;
import com.example.sklep.model.CartItem;
import com.example.sklep.model.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartItemRepository {
    private final Connection connection;
    private static final String tableName = "cartItem";
    private static final String colId = "id";
    private static final String colProfileId = "profileId";
    private static final String colItemId = "itemId";
    private static final String colOrderId = "orderId";
    private static final String colCount = "count";

    public CartItemRepository(Connection connection) {
        this.connection = connection;
    }
    public long create(CartItem cartItem) throws SQLException {
        //INSERT INTO accounts (username, password, role, isActive ) VALUES ('zenya', '11111', 'user', false);
        var statement = connection.prepareStatement(
                "INSERT INTO " + tableName +
                        "(" +  colProfileId + ", " +  colItemId + ", " + colOrderId + ", " + colCount +
                        ") VALUES (?, ?, ?, ?)"
        );
        //prepareStatement возвращает только результат
        statement.setLong(1, cartItem.getProfile().getId());
        statement.setLong(2, cartItem.getItem().getId());
        statement.setLong(3, cartItem.getOrder().getId());
        statement.setInt(4, cartItem.getCount());
        statement.executeUpdate();

        return getMaxId();
    }
    //////////////ja
    public void update(CartItem cartItem) throws SQLException{
        //UPDATE cartItem SET colId = ?, colPassword = ? WHERE colId = ? ;
        var statement = connection.prepareStatement(
                "UPDATE " + tableName + "SET" + colProfileId + ", " +  colItemId + ", " + colOrderId + ", " + colCount +
                        " WHERE" + colId + " = ?"
        );
        statement.setLong(1, cartItem.getProfile().getId());
        statement.setLong(2, cartItem.getItem().getId());
        statement.setLong(3, cartItem.getOrder().getId());
        statement.setInt(4, cartItem.getCount());
        statement.setLong(5, cartItem.getId());
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
    public CartItem getById(long id) throws SQLException {
        //SELECT * FROM accounts WHERE colId = ?;
        var statement =  connection.prepareStatement(
                "SELECT * FROM " + tableName + " WHERE " + colId + " = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        statement.setLong(1, id);
        var result = statement.executeQuery();

        return convertToObj(result);
    }
    public List<CartItem> getAll() throws SQLException {
        // SELECT * FROM accounts;
        var statement =  connection.prepareStatement(
                "SELECT * FROM " + tableName ,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        var result = statement.executeQuery();

        return convertToList(result);
    }
    private long getMaxId() throws SQLException {
        //SELECT MAX(colId) FROM accounts;
        var statement = connection.prepareStatement(
                "SELECT MAX (" + colId + ") FROM " + tableName,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
                //если данных много, то отображаются порционно (например только по 20)
        );
        var result = statement.executeQuery(); // только для чтения из бд (выполняет строку 62), result = set
        result.next();//стоим на 1 записи в resultSet

        return result.getLong(1); // получение данных из 1 колонки
    }

    //преобразование строки бд resultSet в account
    private CartItem convert(ResultSet resultSet) throws SQLException {
        var obj = new CartItem();
        obj.setId(resultSet.getLong(colId));//!!!!!!!!
        obj.setCount(resultSet.getInt(colCount)); //независимые

        long profileId = resultSet.getLong(colProfileId);
        var profileRepository = new ProfileRepository(connection);
        obj.setProfile(profileRepository.getById(profileId));

        long itemId = resultSet.getLong(colItemId);
        var itemRepository = new ItemRepository(connection);
        obj.setItem(itemRepository.getById(itemId));

        long orderId = resultSet.getLong(colOrderId);
        var orderRepository = new OrderRepository(connection);
        obj.setOrder(orderRepository.getById(orderId));

        return obj;
    }

    private CartItem convertToObj(ResultSet resultSet) throws SQLException {
        resultSet.beforeFirst();// итератора на значение -1
        if (!resultSet.next())
            return new CartItem();

        return convert(resultSet);
    }

    //преобразование resultSet с несколькими строками в список account
    private List<CartItem> convertToList(ResultSet resultSet) throws SQLException {
        var list = new ArrayList<CartItem>();
        resultSet.beforeFirst();
        while (resultSet.next()) {
            list.add(convert(resultSet));
        }
        return list;
    }


}


