package model;

public class PasswordChangeRequestModel {
    private String password;

    public PasswordChangeRequestModel() {
    }
    public PasswordChangeRequestModel(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordChangeRequestModel{" +
                "password='" + password + '\'' +
                '}';
    }
}
