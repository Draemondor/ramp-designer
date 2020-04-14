package Entities;

public class Team {
    private int TID, teamColor, teamCreator, privacyLevel;
    private String teamName, dateCreated;

    public Team(int TID, int teamColor, int teamCreator, int privacyLevel, String teamName, String dateCreated) {
        this.TID = TID;
        this.teamColor = teamColor;
        this.teamCreator = teamCreator;
        this.privacyLevel = privacyLevel;
        this.teamName = teamName;
        this.dateCreated = dateCreated;
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

    public int getTeamCreator() {
        return teamCreator;
    }

    public void setTeamCreator(int teamCreator) {
        this.teamCreator = teamCreator;
    }

    public int getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(int privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return teamName;
    }
}
