package xyz.sangcomz.sangcomz_n_study.bean;

/**
 * Created by sangc on 2015-12-30.
 */
public class Comment {
    String commentSrl;
    String memberSrl;
    String commentContent;
    String commentDate;
    String postSrl;

    public Comment(String commentSrl, String memberSrl, String commentContent, String commentDate, String postSrl) {
        this.commentSrl = commentSrl;
        this.memberSrl = memberSrl;
        this.commentContent = commentContent;
        this.commentDate = commentDate;
        this.postSrl = postSrl;
    }

    public String getCommentSrl() {
        return commentSrl;
    }

    public void setCommentSrl(String commentSrl) {
        this.commentSrl = commentSrl;
    }

    public String getMemberSrl() {
        return memberSrl;
    }

    public void setMemberSrl(String memberSrl) {
        this.memberSrl = memberSrl;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getPostSrl() {
        return postSrl;
    }

    public void setPostSrl(String postSrl) {
        this.postSrl = postSrl;
    }
}
