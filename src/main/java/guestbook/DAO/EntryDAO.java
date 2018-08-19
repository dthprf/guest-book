package guestbook.DAO;

import guestbook.DBconnection.DBConnectionFactory;
import guestbook.Model.Entry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntryDAO {

    private static final String GET_ALL_POSTS =
            "SELECT id, name, message, date\n" +
                    "FROM comments;";

    private static final String ADD_POST =
            "INSERT INTO comments ( name, message, date ) VALUES ( ?, ?, ? );";

    public List<Entry> getAllEntrys() {
        List<Entry> allEntrys = new ArrayList<>();

        try {
            Connection connection = DBConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_POSTS);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                allEntrys.add(extractEntry(resultSet));
            }

            connection.close();
            preparedStatement.close();

        return allEntrys;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Entry extractEntry(ResultSet resultSet) throws SQLException {
        Entry entry = new Entry();

        entry.setId(resultSet.getInt("id"));
        entry.setName(resultSet.getString("name"));
        entry.setMessage(resultSet.getString("message"));
        entry.setDate(resultSet.getString("date"));

        return entry;
    }

    public boolean addEntry(Entry entry) {
        try {
            Connection connection = DBConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_POST);

            preparedStatement.setString(1, entry.getName());
            preparedStatement.setString(2, entry.getMessage());
            preparedStatement.setString(3, entry.getDate());

            int updateResult = preparedStatement.executeUpdate();

            System.out.println(updateResult);

            if(updateResult == 1) {
                return true;
            }

            preparedStatement.close();
            connection.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}


