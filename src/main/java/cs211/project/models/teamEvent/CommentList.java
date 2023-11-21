package cs211.project.models.teamEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class CommentList {
    private ArrayList<Comment> commentList;

    public CommentList() {
        commentList = new ArrayList<>();
    }

    public void addComment(String eventName, String teamName, String username, String text) {
        Comment comment = new Comment(eventName, teamName, username, text);
        commentList.add(comment);
    }

    public void removeByEventName(String eventName) {
        Iterator<Comment> iterator = commentList.iterator();

        while (iterator.hasNext()) {
            Comment comment = iterator.next();
            if (comment.getEventName().equals(eventName)) {
                iterator.remove();
            }
        }
    }

    public void removeByTeamName(String teamName) {
        Iterator<Comment> iterator = commentList.iterator();

        while (iterator.hasNext()) {
            Comment comment = iterator.next();
            if (comment.getTeamName().equals(teamName)) {
                iterator.remove();
            }
        }
    }



    public ArrayList<Comment> getCommentList() {
        return commentList;
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }
}
