package cs211.project.models.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Account {
    protected String firstName;
    protected String lastName;
    protected String userName;
    protected String password;
    protected String imgSrc;
    protected String status;
    protected LocalDate date;
    protected LocalDateTime latestLogin;

    public Account(String userName) {
        this.userName = userName;
    }

    public Account(String firstName, String lastName, String userName, String password, String imgSrc, String status, LocalDate date, LocalDateTime latestLogin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.imgSrc = imgSrc;
        this.status = status;
        this.date = date;
        this.latestLogin = latestLogin;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getLatestLogin() {
        return latestLogin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isUsername(String userName) {
        return this.userName.equals(userName);
    }
}
