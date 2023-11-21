package cs211.project.models.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User extends Account implements Comparable<User>{
    private String role;
    private int reportCount;

    public User(String userName) {
        super(userName);
    }

    public User(String firstName, String lastName, String userName, String password, String imgSrc, String role, String status, LocalDate date, int reportCount, LocalDateTime latestLogin){
        super(firstName, lastName, userName, password, imgSrc, status, date, latestLogin);
        this.role = role;
        this.reportCount = reportCount;
    }

    public String getRole() {
        return role;
    }

    public int getReportCount() {
        return reportCount;
    }
    public void addReportCount(){
        reportCount++;
    }

    public void setLatestLogin(LocalDateTime latestLogin) {
        this.latestLogin = latestLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", reportCount=" + reportCount +
                '}';
    }

    @Override
    public int compareTo(User o) {
        return 0;
    }
}
