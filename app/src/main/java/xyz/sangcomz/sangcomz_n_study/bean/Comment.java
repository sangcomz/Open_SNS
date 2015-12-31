package xyz.sangcomz.sangcomz_n_study.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sangc on 2015-12-30.
 */
public class Comment {
    @SerializedName("comment_srl")
    String commentSrl;
    @SerializedName("member_srl")
    String memberSrl;
    @SerializedName("member_name")
    String memberName;
    @SerializedName("member_profile")
    String memberProfile;
    @SerializedName("comment_content")
    String commentContent;
    @SerializedName("comment_date")
    String commentDate;
    @SerializedName("post_srl")
    String postSrl;
    @SerializedName("is_my_comment")
    String isMyComment;

    public Comment(String commentSrl, String memberSrl, String memberName, String memberProfile, String commentContent, String commentDate, String postSrl, String isMyComment) {
        this.commentSrl = commentSrl;
        this.memberSrl = memberSrl;
        this.memberName = memberName;
        this.memberProfile = memberProfile;
        this.commentContent = commentContent;
        this.commentDate = commentDate;
        this.postSrl = postSrl;
        this.isMyComment = isMyComment;
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

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberProfile() {
        return memberProfile;
    }

    public void setMemberProfile(String memberProfile) {
        this.memberProfile = memberProfile;
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

    public String getIsMyComment() {
        return isMyComment;
    }

    public void setIsMyComment(String isMyComment) {
        this.isMyComment = isMyComment;
    }
}
