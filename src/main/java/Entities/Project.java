package Entities;

public class Project {
    private int PID, customer, status, team, priority;
    private String project_name, start_date, notes;

    public Project() {  }

    public Project(String name) { this.project_name = name; }

    /****** constructor for new project form ***********/

    public Project(String name, String startDate, int tid) {
        this.project_name = name;
        this.start_date = startDate;
        this.team = tid;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        notes = notes;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
