package xyz.sangcomz.open_sns.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sangc on 2015-12-29.
 */
public class Post {

    @SerializedName("post_srl")
    String postSrl;

    @SerializedName("member_srl")
    String memberSrl;

    @SerializedName("member_name")
    String memberName;

    @SerializedName("member_profile")
    String memberProfile;

    @SerializedName("post_image")
    String postImage;

    @SerializedName("post_content")
    String postContent;

    @SerializedName("post_date")
    String postDate;

    @SerializedName("post_comment_count")
    String postCommentCount;

    public Post(String postSrl, String memberSrl, String memberName, String memberProfile, String postImage, String postContent, String postDate, String postCommentCount) {
        this.postSrl = postSrl;
        this.memberSrl = memberSrl;
        this.memberName = memberName;
        this.memberProfile = memberProfile;
        this.postImage = postImage;
        this.postContent = postContent;
        this.postDate = postDate;
        this.postCommentCount = postCommentCount;
    }

    public String getPostSrl() {
        return postSrl;
    }

    public void setPostSrl(String postSrl) {
        this.postSrl = postSrl;
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

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostCommentCount() {
        return postCommentCount;
    }

    public void setPostCommentCount(String postCommentCount) {
        this.postCommentCount = postCommentCount;
    }
}
