package cs211.project.services;

import cs211.project.models.user.User;
import java.util.Comparator;

public class UserDateComparator implements Comparator<User> {
    @Override
    public int compare(User o1, User o2) {
        return o2.getDate().compareTo(o1.getDate());
    }
}
