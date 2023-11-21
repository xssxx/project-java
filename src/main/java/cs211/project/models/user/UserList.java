package cs211.project.models.user;

import cs211.project.services.ImageReader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserList {
    private ArrayList<User> users;

    public UserList() {
        users = new ArrayList<>();
    }

    public void addUser(String firstName, String lastName, String userName, String password, String imgSrc, String role, String status, LocalDate date, int reportCount, LocalDateTime latestLogin) {
        if (!firstName.isEmpty() && !lastName.isEmpty() && !userName.isEmpty() && !password.isEmpty() && !imgSrc.isEmpty() && !role.isEmpty() && !status.isEmpty()) {
            String img = ImageReader.getImagePath("data/images/profiles/", imgSrc);
            users.add(new User(firstName, lastName, userName, password, img, role, status, date, reportCount, latestLogin));
        }
    }
    public ArrayList<User> getUsers() {
        return users;
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUserName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void sort(Comparator<User> cmp){
        Collections.sort(users, cmp);
    }

    @Override
    public String toString() {
        return "UserList{" +
                "users=" + users +
                '}';
    }
}

