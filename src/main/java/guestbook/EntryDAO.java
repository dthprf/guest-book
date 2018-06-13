package guestbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntryDAO {

    private Entry extractEntry(ResultSet resultSet) throws SQLException {
        Entry entry = new Entry();

        entry.setId(resultSet.getInt("id"));
        entry.setName(resultSet.getString("name"));
        entry.setMessage(resultSet.getString("message"));
        entry.setDate(resultSet.getString("date"));

        return entry;
    }

    public List<Entry> getAllEntrys() {
        List<Entry> allEntrys = new ArrayList<>();

        try{
            String query = "SELECT * FROM comments;";
            Connection connection = DBConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

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


    public boolean addEntry(Entry entry) {
        String addQuery = "INSERT INTO comments ( name, message, date ) VALUES ( ?, ?, ? );";

        try {
            Connection connection = DBConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(addQuery);

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


