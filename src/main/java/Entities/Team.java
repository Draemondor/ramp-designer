package Entities;

public class Team {
    private int TID, teamColor;
    private String teamName;
    private User team_manager;

    public Team(String teamName, User team_manager, int teamColor) {
        this.teamName = teamName;
        this.teamColor = teamColor;
        this.team_manager = team_manager;
    }

    public int getTID() {
        return TID;
    }

    public void setTID(int TID) {
        this.TID = TID;
    }

    public int getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(int teamColor) {
        this.teamColor = teamColor;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public User getTeam_manager() {
        return team_manager;
    }

    public void setTeam_manager(User team_manager) {
        this.team_manager = team_manager;
    }
}
