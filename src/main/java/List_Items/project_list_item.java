package List_Items;

public class project_list_item {

    private int PID;
    private String project_name, start_date, customer, team, notes;
    private int status;

    public project_list_item(int PID, String project_name, String start_date, String customer, int status, String team, String notes) {
        this.PID = PID;
        this.project_name = project_name;
        this.start_date = start_date;
        this.notes = notes;
        this.customer = customer;
        this.team = team;
        this.status = status;
    }

    public int getPID() {
        return PID;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getNotes() {
        return notes;
    }

    public String getCustomer() {
        return customer;
    }

    public int getStatus() {
        return status;
    }

    public String getTeam() {
        return team;
    }
}
