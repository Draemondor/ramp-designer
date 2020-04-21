package List_Items;

public class TeamListItem {

    private int TID, privacyLevel, projects, members;
    private String teamName;

    public TeamListItem(int TID, String teamName) {
        this.TID = TID;
        this.teamName = teamName;
    }

    public TeamListItem(int TID, int privacyLevel, int projects, int members, String teamName) {
        this.TID = TID;
        this.privacyLevel = privacyLevel;
        this.projects = projects;
        this.members = members;
        this.teamName = teamName;
    }

    public int getTID() {
        return TID;
    }

    public void setTID(int TID) {
        this.TID = TID;
    }

    public int getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(int privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public int getProjects() {
        return projects;
    }

    public void setProjects(int projects) {
        this.projects = projects;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
