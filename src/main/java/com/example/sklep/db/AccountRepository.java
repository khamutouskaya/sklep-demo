package com.example.sklep.db;

import com.example.sklep.model.Account;
import com.example.sklep.model.Role;
import org.xml.sax.SAXException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//отвечает за взаимодействие с базой данных: сохранение, обновление,
// удаление и получение данных об аккаунтах
public class AccountRepository {
    private final Connection connection;//соединение с бд
    private static final String tableName = "accounts";
    private static final String colId = "id";
    private static final String colUsername = "username";
    private static final String colPassword = "password";
    private static final String colRole = "role";
    private static final String colIsActive = "isActive";

    public AccountRepository(Connection connection) {
        this.connection = connection;
    }

    public long create(Account account) throws SQLException {
        //INSERT INTO accounts (username, password, role, isActive ) VALUES ('zenya', '11111', 'user', false);
        var statement = connection.prepareStatement(
          "INSERT INTO " + tableName +
                  "(" +  colUsername + ", " +  colPassword + ", " + colRole + ", " + colIsActive +
                  ") VALUES (?, ?, ?, ?)"
        );
        //prepareStatement возвращает только результат и используется для создания подготовленного SQL-запроса
        statement.setString(1, account.getUsername());
        statement.setString(2, account.getPassword());
        statement.setString(3, account.getRole().toString());
        statement.setBoolean(4, account.isActive());
        //сверяем тип данныз с бд (model.account)
        statement.executeUpdate();

        return getMaxId();
    }
    public void update(Account account) throws SQLException{
        //UPDATE accounts SET colUsername = ?, colPassword = ? WHERE colId = ? ;
        var statement =  connection.prepareStatement(
                "UPDATE " + tableName + "SET " + colUsername + " = ?, " + colPassword + " = ?, " + colRole + " = ?," + colIsActive + " = ?" +
                        " WHERE " + colId + " = ?"
        );
        statement.setString(1, account.getUsername());
        statement.setString(2, account.getPassword());
        statement.setString(3, account.getRole().toString());
        statement.setBoolean(4, account.isActive());
        statement.setLong(5, account.getId());
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
    public Account getById(long id) throws SQLException {
        //SELECT * FROM accounts WHERE colId = ?;
        var statement =  connection.prepareStatement(
                "SELECT * FROM " + tableName + " WHERE " + colId + " = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        statement.setLong(1, id);
        //SQL-запрос, который возвращает результат в виде набора данных (например, SELECT ...).
        //executeQuery() — выполняет запрос к базе данных и возвращает результат.
        var result = statement.executeQuery();
        //возвращает объект типа ResultSet.

        return convertToObj(result);
    }
    public List<Account> getAll() throws SQLException {
        // SELECT * FROM accounts;
        var statement =  connection.prepareStatement(
                "SELECT * FROM " + tableName ,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        var result = statement.executeQuery();

        return convertToList(result);
    }
    public List<Account> getByRole (Role role) throws SQLException {
        // SELECT * FROM accounts  WHERE colRole = ?;
        var statement =  connection.prepareStatement(
                "SELECT * FROM " + tableName + " WHERE " + colRole + " = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        statement.setString(1, role.toString());
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
        //next -- переходит к следующей строке

        return result.getLong(1); // получение данных из 1 колонки
    }

    //преобразование строки бд resultSet в account
    private Account convert(ResultSet resultSet) throws SQLException {
        var obj = new Account();
        obj.setId(resultSet.getInt(colId));
        obj.setUsername(resultSet.getString(colUsername));
        obj.setPassword(resultSet.getString(colPassword));
        obj.setRole(Role.valueOf(resultSet.getString(colRole)));// from string to enum
        obj.setActive(resultSet.getBoolean(colIsActive));

        return obj;
    }

    //преобразование resultSet с одной  в account
    // Этот метод проверяет, есть ли в ResultSet хотя бы одна строка.
    // Если нет — возвращает пустой объект Account.
    private Account convertToObj(ResultSet resultSet) throws SQLException {
        resultSet.beforeFirst();// итератора на значение -1
        if (!resultSet.next())
            return new Account();

        return convert(resultSet);
    }

    //преобразование resultSet с несколькими строками в список account
    private List<Account> convertToList(ResultSet resultSet) throws SQLException {
        var list = new ArrayList<Account>();
        resultSet.beforeFirst();
        while (resultSet.next()) {
            list.add(convert(resultSet));
        }

        return list;
    }



}
