package List_Items;

public class MemberListItem {

    private int UID, role;
    private String full_name, email;
    private boolean isSelected;

    public MemberListItem(int UID, String full_name, String email, int role) {
        this.UID = UID;
        this.role = role;
        this.full_name = full_name;
        this.email = email;
        this.isSelected = false;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
