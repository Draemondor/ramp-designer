package Entities;

public class Customer {
    private int CID, zip;
    private String first_name, last_name, str_address, state, city, pPhone, sPhone, email;

    public Customer() {}

    public Customer(String first_name, String last_name, String email, String pPhone, String sPhone, String str_address, String state, String city, int zip) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.str_address = str_address;
        this.state = state;
        this.city = city;
        this.zip = zip;
        this.pPhone = pPhone;
        this.sPhone = sPhone;
        this.email = email;
    }

    public int getCID() {
        return CID;
    }

    public void setCID(int CID) {
        this.CID = CID;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getStr_address() {
        return str_address;
    }

    public void setStr_address(String str_address) {
        this.str_address = str_address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getpPhone() {
        return pPhone;
    }

    public void setpPhone(String pPhone) {
        this.pPhone = pPhone;
    }

    public String getsPhone() {
        return sPhone;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
