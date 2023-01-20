package es.upm.etsiinf.dam.coinapp.modelos;

public class User {
    private String username;
    private String password;
    private String email;
    private byte[] profileImage;

    public User(String username, String password, String email, byte[] profileImage) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileImage = profileImage;
    }
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String email, byte[] profileImage) {
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getProfileImage () {
        return profileImage;
    }

    public void setProfileImage (byte[] profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString () {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

