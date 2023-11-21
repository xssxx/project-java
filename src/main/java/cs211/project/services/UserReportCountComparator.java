package cs211.project.services;

import cs211.project.models.user.User;

import java.util.Comparator;

public class UserReportCountComparator implements Comparator<User> {
    @Override
    public int compare(User o1, User o2) {
        return (int)(o2.getReportCount() - o1.getReportCount());
    }
}
