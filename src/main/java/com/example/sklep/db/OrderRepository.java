package com.example.sklep.db;

import com.example.sklep.model.Account;
import com.example.sklep.model.Order;
import com.example.sklep.model.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final Connection connection;
    private static final String tableName = "orders";
    private static final String colId = "id";
    private static final String colTimestamp = "timeStamp";
    private static final String colPaymentType = "paymentType";
    private static final String colState = "state";
    private static final String colComment = "comment";

    public OrderRepository(Connection connection) {
        this.connection = connection;
    }

    //ja
    public long create(Order order) throws SQLException {
        //INSERT INTO orders VALUES (?, ?, ?, ?, ?);

        var statement = connection.prepareStatement(
                "INSERT INTO " + tableName +
                        "(" +  colTimestamp + ", " +  colPaymentType + ", " + colState + ", " + colComment +
                        ") VALUES (?, ?, ?, ?)"
        );
        //prepareStatement возвращает только результат
        // в бд тип timestamp
        // в модели тип date
        // как понять когда (тип) - явное приведение (кастинг) работает с памятью, а когда .toType() - парсер ()
        // почему не пишем название таблицы
        /*String s = "";
        Integer ex = 0;
        s = ex.toString();
        String s1 = "123";
        ex = Integer.getInteger(s1);
        Integer x = 0;
        Double d = 0.;
        x = d.intValue();
        d = x.doubleValue();
        */

        statement.setDate(1, new java.sql.Date(order.getTimestamp().getTime()));
        statement.setString(2, order.getPaymentType());
        statement.setString(3, order.getState());
        statement.setString(4, order.getComment());
        //сверяем тип данныз с бд (model.account)
        statement.executeUpdate();

        return getMaxId();

    }
    public void update(Order order) throws SQLException {
        //UPDATE orders SET colTimestamp = ?, colPaymentType = ?, colState = ? WHERE id = ? ;
        var statement =  connection.prepareStatement(
                "UPDATE " + tableName + "SET " + colPaymentType + " = ?, " + colState + " = ?, " + colComment + " = ?" +
                        " WHERE " + colId + " = ?"
        );
        statement.setDate(1, new java.sql.Date(order.getTimestamp().getTime()));
        statement.setString(2, order.getPaymentType());
        statement.setString(3, order.getState());
        statement.setString(4, order.getComment());
        statement.setLong(5, order.getId());
        statement.executeUpdate();
    }

    public void delete(long id) throws SQLException {
        //DELETE FROM orders WHERE id = ?;
        var statement = connection.prepareStatement(
                "DELETE FROM " + tableName + " WHERE " + colId + " = ?"
        );
        statement.setLong(1, id);
        statement.executeUpdate();
    }
    public Order getById(long id) throws SQLException {
        //SELECT * FROM orders WHERE id = ?;
        var statement =  connection.prepareStatement(
                "SELECT * FROM " + tableName + " WHERE " + colId + " = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        statement.setLong(1, id);
        var result = statement.executeQuery();

        return convertToObj(result);
    }
    public List<Order> getAll() throws SQLException {
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
        var result = statement.executeQuery(); // только для чтения из бд (выполняет строку 62), result = set
        result.next();//стоим на 1 записи в resultSet

        return result.getLong(1); // получение данных из 1 колонки
    }

    private Order convert(ResultSet resultSet) throws SQLException {
        var obj = new Order();
        obj.setId(resultSet.getLong(colId));
        obj.setTimestamp( new java.util.Date(resultSet.getDate(colTimestamp).getTime()));
        obj.setPaymentType(resultSet.getString(colPaymentType));
        obj.setState(resultSet.getString(colState));// from string to enum
        obj.setComment(resultSet.getString(colComment));

        return obj;
    }

    private Order convertToObj(ResultSet resultSet) throws SQLException {
        resultSet.beforeFirst();// итератора на значение -1
        if (!resultSet.next())
            return new Order();

        return convert(resultSet);
    }

    private List<Order> convertToList (ResultSet resultSet) throws SQLException {
        var list = new ArrayList<Order>();
        resultSet.beforeFirst();
        while (resultSet.next()){
            list.add(convertToObj(resultSet));
        }

        return list;
    }
}
