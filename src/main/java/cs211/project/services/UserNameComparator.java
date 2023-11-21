package cs211.project.services;

import cs211.project.models.user.User;

import java.util.Comparator;

public class UserNameComparator implements Comparator<User> {
    @Override
    public int compare(User o1,User o2) {
        return (o1.getUserName().toLowerCase().compareTo(o2.getUserName().toLowerCase()));
    }
}
