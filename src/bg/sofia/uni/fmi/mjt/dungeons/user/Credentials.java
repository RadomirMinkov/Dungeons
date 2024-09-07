package bg.sofia.uni.fmi.mjt.dungeons.user;

public class Credentials implements Comparable<Credentials> {

    private String username;
    private String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void updateUsername(String username) {
        if (password == null || password.isEmpty() || password.isBlank()) {
            throw new IllegalArgumentException("The given username is invalid!");
        }
        this.username = username;
    }

    public void updatePassword(String password) {
        if (password == null || password.isEmpty() || password.isBlank()) {
            throw new IllegalArgumentException("The given password is invalid!");
        }
        this.password = password;
    }

    @Override
    public int compareTo(Credentials c) {
        return username.compareTo(c.username);
    }
}
