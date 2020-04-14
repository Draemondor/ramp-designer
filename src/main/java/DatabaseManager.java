import Entities.Customer;
import Entities.Project;
import Entities.Team;
import Entities.User;
import List_Items.MemberListItem;
import List_Items.ProjectListItem;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager manager = null;

    public DatabaseManager() {}

    public static DatabaseManager getInstance() {
        if (manager == null)
            manager = new DatabaseManager();
        return manager;
    }

    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    /********** CHECK EMAIL AVAILABILITY **************/

    public boolean isEmailTaken(String email) {
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
                return true;
            }
        }
        return true;
    }

    /********** ADD NEW USER **************/

    public User addUser(String fName, String lName, String mEmail, String hash, String phone) {
        Connection connection = getConnection();
        ResultSet resultSet;
        if (connection != null) {

            try {
                long time = System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(time);

                Statement statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO users (f_name, l_name, email, pswd, phone, accnt_created)"
                        + " values ('"
                        + fName
                        + "','" + lName
                        + "','" + mEmail
                        + "','" + hash
                        + "','" + phone
                        + "','" + date
                        + "')");

                resultSet = statement.getGeneratedKeys();
                if (resultSet != null && resultSet.next()) {
                    User user = new User(fName, lName, mEmail, hash, phone, time);
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

    public User loginCredentialsVerified(String email, String pswd) {
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

    public void createDefaultTables() {
        /********** USERS TABLE **************/

        String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS \"users\" (\n" +
                "\t\"user_id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"f_name\"\tVARCHAR(100) NOT NULL,\n" +
                "\t\"l_name\"\tVARCHAR(100) NOT NULL,\n" +
                "\t\"email\"\tVARCHAR(256) NOT NULL UNIQUE,\n" +
                "\t\"pswd\"\tVARCHAR(256) NOT NULL,\n" +
                "\t\"phone\"\tVARCHAR(12) NOT NULL,\n" +
                "\t\"role\"\tTINYINT DEFAULT 0,\n" +
                "\t\"accnt_created\"\tDATE NOT NULL\n" +
                ")";
        createTable(CREATE_USERS_TABLE);

        /********** TEAMS TABLE **************/

        String CREATE_TEAMS_TABLE = "CREATE TABLE IF NOT EXISTS \"teams\" (\n" +
                "\t\"team_id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"team_name\"\tVARCHAR(256) NOT NULL,\n" +
                "\t\"team_color\"\tTINYINT DEFAULT 0,\n" +
                "\t\"team_creator\"\tINTEGER NOT NULL,\n" +
                "\t\"date_created\"\tDATE NOT NULL,\n" +
                "\t\"privacy_level\"\tTINYINT DEFAULT 0\n" +
                ")";
        createTable(CREATE_TEAMS_TABLE);

        /********** CUSTOMERS TABLE **************/

        String CREATE_CUSTOMERS_TABLE = "CREATE TABLE IF NOT EXISTS \"customers\" (\n" +
                "\t\"customer_id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"f_name\"\tVARCHAR(100) NOT NULL,\n" +
                "\t\"l_name\"\tVARCHAR(100) NOT NULL,\n" +
                "\t\"email\"\tVARCHAR(256) NOT NULL,\n" +
                "\t\"primary_phone\"\tVARCHAR(12) NOT NULL,\n" +
                "\t\"secondary_phone\"\tVARCHAR(12),\n" +
                "\t\"st_address\"\tVARCHAR(256) NOT NULL,\n" +
                "\t\"state\"\tVARCHAR(60) NOT NULL,\n" +
                "\t\"city\"\tVARCHAR(256) NOT NULL,\n" +
                "\t\"zip\"\tTINYINT NOT NULL\n" +
                ")";
        createTable(CREATE_CUSTOMERS_TABLE);

        /********** PROJECTS TABLE **************/

        String CREATE_PROJECTS_TABLE = "CREATE TABLE IF NOT EXISTS \"projects\" (\n" +
                "\t\"project_id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"project_name\"\tVARCHAR(256) NOT NULL,\n" +
                "\t\"start_date\"\tDATE,\n" +
                "\t\"customer\"\tINTEGER NOT NULL,\n" +
                "\t\"status\"\tTINYINT DEFAULT 0,\n" +
                "\t\"team\"\tINTEGER NOT NULL,\n" +
                "\t\"notes\"\tTEXT,\n" +
                "\t\"priority\"\tTINYINT,\n" +
                "\t\"completion_date\"\tDATE,\n" +
                "\tFOREIGN KEY(\"customer\") REFERENCES \"customers\"(\"customer_id\"),\n" +
                "\tFOREIGN KEY(\"team\") REFERENCES \"teams\"(\"team_id\")\n" +
                ")";
        createTable(CREATE_PROJECTS_TABLE);

        /********** USER_TEAMS TABLE **************/

        String CREATE_USER_TEAMS_TABLE = "CREATE TABLE IF NOT EXISTS \"user_teams\" (\n" +
                "\t\"user_id\"\tINTEGER NOT NULL,\n" +
                "\t\"team_id\"\tINTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"user_id\",\"team_id\"),\n" +
                "\tFOREIGN KEY(\"team_id\") REFERENCES \"teams\"(\"team_id\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"user_id\") REFERENCES \"users\"(\"user_id\") ON DELETE CASCADE\n" +
                ")";
        createTable(CREATE_USER_TEAMS_TABLE);

        /********** USER_PROJECTS TABLE **************/

        String CREATE_USER_PROJECTS_TABLE = "CREATE TABLE IF NOT EXISTS \"user_projects\" (\n" +
                "\t\"user_id\"\tINTEGER NOT NULL,\n" +
                "\t\"project_id\"\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(\"user_id\") REFERENCES \"users\"(\"user_id\") ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"user_id\",\"project_id\"),\n" +
                "\tFOREIGN KEY(\"project_id\") REFERENCES \"projects\"(\"project_id\") ON DELETE CASCADE\n" +
                ")";
        createTable(CREATE_USER_PROJECTS_TABLE);

    }

    public void createTable(String query) {
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

    /********** Retrieve all projects for home screen list **************/

    public List<ProjectListItem> getAllProjects() {
        Connection connection = getConnection();
        List<ProjectListItem> projects = new ArrayList<>();
        String query = "SELECT project_id, project_name, start_date, f_name || ' ' || l_name AS customer, status, team_name, notes\n" +
                "FROM projects p JOIN customers c ON p.customer = c.customer_id\n" +
                "JOIN teams t ON P.team = t.team_id\n" +
                "ORDER BY status";
        if (connection != null) {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ProjectListItem item = new ProjectListItem(
                            rs.getInt("project_id"),
                            rs.getString("project_name"),
                            rs.getString("start_date"),
                            rs.getString("customer"),
                            rs.getInt("status"),
                            rs.getString("team_name"),
                            rs.getString("notes")
                    );
                    projects.add(item);
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return projects;
    }

    /********** Retrieve managers for combobox selection **************/

    public List<User> getManagers() {
        Connection connection = getConnection();
        List<User> managers = new ArrayList<>();
        String query = "SELECT user_id, f_name, l_name, role\n" +
                "FROM users\n" +
                "WHERE role = 2";
        if (connection != null) {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("user_id"),
                            rs.getString("f_name"),
                            rs.getString("l_name"),
                            rs.getInt("role")
                    );
                    managers.add(user);
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return managers;
    }

    /********** Retrieve managers for list item **************/

    public List<MemberListItem> getManagerListItems() {
        Connection connection = getConnection();
        List<MemberListItem> managers = new ArrayList<>();
        String query = "SELECT user_id, f_name || ' ' || l_name AS name, email, role\n" +
                "FROM users\n" +
                "WHERE role = 2";
        if (connection != null) {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    MemberListItem user = new MemberListItem(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getInt("role")
                    );
                    managers.add(user);
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return managers;
    }

    /********** Retrieve Team Leaders for list item **************/

    public List<MemberListItem> getTeamLeaderListItems() {
        Connection connection = getConnection();
        List<MemberListItem> leaders = new ArrayList<>();
        String query = "SELECT user_id, f_name || ' ' || l_name AS name, email, role\n" +
                "FROM users\n" +
                "WHERE role = 1";
        if (connection != null) {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    MemberListItem user = new MemberListItem(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getInt("role")
                    );
                    leaders.add(user);
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return leaders;
    }

    /********** Retrieve members for list item **************/

    public List<MemberListItem> getMemberListItems() {
        Connection connection = getConnection();
        List<MemberListItem> members = new ArrayList<>();
        String query = "SELECT user_id, f_name || ' ' || l_name AS name, email, role\n" +
                "FROM users\n" +
                "WHERE role = 0";
        if (connection != null) {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    MemberListItem user = new MemberListItem(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getInt("role")
                    );
                    members.add(user);
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return members;
    }

    /********** Retrieve teams for combobox selection **************/

    public List<Team> getTeams() {
        Connection connection = getConnection();
        List<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM teams";
        if (connection != null) {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Team team = new Team(
                            rs.getInt("team_id"),
                            rs.getInt("team_color"),
                            rs.getInt("team_creator"),
                            rs.getInt("privacy_level"),
                            rs.getString("team_name"),
                            rs.getString("date_created")
                    );
                    teams.add(team);
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return teams;
    }

    /********** From the new project form, create/add the new customer and project **************/

    public void addProject(Project project, Customer customer) {

    }
}
