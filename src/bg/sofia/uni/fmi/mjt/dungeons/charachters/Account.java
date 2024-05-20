package bg.sofia.uni.fmi.mjt.dungeons.charachters;

public class Account {

    private String username;
    private String password;

    private Charachter charachter;

    public Account(String username, String password, Charachter charachter) {
        this.username = username;
        this.password = password;
        this.charachter = charachter;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Charachter getCharachter() {
        return charachter;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateUsername(String newUsername) {
        this.username = newUsername;
    }

}
