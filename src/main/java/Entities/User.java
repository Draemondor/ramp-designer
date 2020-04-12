package Entities;

public class User {

    private int UID, role;
    private String firstName, lastName, email, password, phone;
    private long dateCreated;

    public User() {  }

    /**** constructor for creating a new user ****/

    public User(String firstName, String lastName, String email, String password, String phone, long dateCreated) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.dateCreated = dateCreated;
    }

    /**** constructor for repute based list's ****/

    public User(int UID, String firstName, String lastName, int role) {
        this.UID = UID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }
}
