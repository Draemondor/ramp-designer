import Entities.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class DatabaseManager {

    public DatabaseManager() {}

    public static DatabaseManager getInstance() {
        return new DatabaseManager();
    }

    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    /********** CHECK EMAIL AVAILABILITY **************/

    public static boolean isEmailTaken(String email) {
        Connection connection = getConnection();
        if (connection != null) {
            String emailCheck = "SELECT email FROM users WHERE email = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(emailCheck);
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next())
                    return true;
                else return false;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /********** ADD NEW USER **************/

    public static User addUser(User user) {
        Connection connection = getConnection();
        ResultSet resultSet;
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO users (f_name, l_name, email, pswd, phone)" + " values ('"
                    + user.getFirstName()
                    + "','" + user.getLastName()
                    + "','" + user.getEmail()
                    + "','" + user.getPassword()
                    + "','" + user.getPhone() + "')");

                resultSet = statement.getGeneratedKeys();
                if (resultSet != null && resultSet.next()) {
                    user.setUID(resultSet.getInt(1));
                    connection.close();
                    return user;
                } else return null;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /********** VERIFY THE USERS LOGIN CREDENTIALS **************/

    public static User loginCredentialsVerified(String email, String pswd) {
        Connection connection = getConnection();
        if (connection != null) {
            String query = "SELECT * FROM users WHERE email = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, email);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    if (BCrypt.checkpw(pswd, resultSet.getString("pswd"))) {
                        User currentUser = new User();
                        currentUser.setUID(resultSet.getInt("user_id"));
                        currentUser.setFirstName(resultSet.getString("f_name"));
                        currentUser.setLastName(resultSet.getString("l_name"));
                        currentUser.setEmail(resultSet.getString("email"));
                        currentUser.setPhone(resultSet.getString("phone"));
                        connection.close();
                        return currentUser;
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /********** Create a method that, itself creates a 'session' for the logged in user. once the user
     *          provides their login information and is confirmed through the database, a session key and
     *          time stamp is created. store these values along with the user email in the sessions table.
     *          when the user returns, check the sessions table for an existing session. if one exists, check
     *          the time stamp against the current time. if the timestamp is so far behind the current time,
     *          invalidate the user session, delete it from the database and make them log in again.
     *          If not, log the user in as normal.
     ***************/

    public static void createDefaultTables() {

        /********** USERS TABLE **************/

        String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS \"users\" (\n" +
                "\t\"user_id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"f_name\"\tVARCHAR(60) NOT NULL,\n" +
                "\t\"l_name\"\tVARCHAR(60) NOT NULL,\n" +
                "\t\"email\"\tVARCHAR(256) NOT NULL UNIQUE,\n" +
                "\t\"pswd\"\tVARCHAR(60) NOT NULL,\n" +
                "\t\"phone\"\tVARCHAR(10) NOT NULL\n" +
                ")";
        createTable(CREATE_USERS_TABLE);

        /********** TEAMS TABLE **************/

        String CREATE_TEAMS_TABLE = "CREATE TABLE IF NOT EXISTS \"teams\" (\n" +
                "\t\"team_id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"team_name\"\tVARCHAR(256) NOT NULL,\n" +
                "\t\"team_color\"\tTINYINT DEFAULT 0,\n" +
                "\t\"team_leader\"\tINTEGER NOT NULL\n" +
                ")";
        createTable(CREATE_TEAMS_TABLE);

        /********** CUSTOMERS TABLE **************/

        String CREATE_CUSTOMERS_TABLE = "CREATE TABLE IF NOT EXISTS \"customers\" (\n" +
                "\t\"customer_id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"f_name\"\tVARCHAR(60) NOT NULL,\n" +
                "\t\"l_name\"\tVARCHAR(60) NOT NULL,\n" +
                "\t\"email\"\tVARCHAR(256) NOT NULL,\n" +
                "\t\"primary_phone\"\tVARCHAR(10) NOT NULL,\n" +
                "\t\"secondary_phone\"\tVARCHAR(10) DEFAULT null,\n" +
                "\t\"st_address\"\tVARCHAR(256) NOT NULL,\n" +
                "\t\"state\"\tVARCHAR(60) NOT NULL,\n" +
                "\t\"city\"\tVARCHAR(256) NOT NULL,\n" +
                "\t\"zip\"\tTINYINT NOT NULL\n" +
                ")";
        createTable(CREATE_CUSTOMERS_TABLE);

        /********** PROJECTS TABLE **************/

        String CREATE_PROJECTS_TABLE = "CREATE TABLE IF NOT EXISTS \"projects\" (\n" +
                "\t\"project_id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"start_date\"\tDATE,\n" +
                "\t\"project_manager\"\tINTEGER NOT NULL,\n" +
                "\t\"customer\"\tINTEGER NOT NULL,\n" +
                "\t\"status\"\tTINYINT DEFAULT 0,\n" +
                "\t\"team\"\tINTEGER NOT NULL,\n" +
                "\t\"notes\"\tTEXT,\n" +
                "\tFOREIGN KEY(\"project_manager\") REFERENCES \"users\"(\"user_id\"),\n" +
                "\tFOREIGN KEY(\"customer\") REFERENCES \"customers\"(\"customer_id\"),\n" +
                "\tFOREIGN KEY(\"team\") REFERENCES \"teams\"(\"team_id\")\n" +
                ")";
        createTable(CREATE_PROJECTS_TABLE);

    }

    public static void createTable(String query) {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                statement.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
