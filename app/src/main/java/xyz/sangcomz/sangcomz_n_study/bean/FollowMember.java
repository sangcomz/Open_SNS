package xyz.sangcomz.sangcomz_n_study.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sangc on 2015-12-28.
 */
public class FollowMember {

    @SerializedName("member_srl")
    String memberSrl;
    @SerializedName("member_profile")
    String profilePath;
    @SerializedName("member_name")
    String memberName;
    @SerializedName("follow_yn")
    String followYN;


    public FollowMember(String memberSrl, String profilePath, String memberName, String followYN) {
        this.memberSrl = memberSrl;
        this.profilePath = profilePath;
        this.memberName = memberName;
        this.followYN = followYN;
    }

    public String getMemberSrl() {
        return memberSrl;
    }

    public void setMemberSrl(String memberSrl) {
        this.memberSrl = memberSrl;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getFollowYN() {
        return followYN;
    }

    public void setFollowYN(String followYN) {
        this.followYN = followYN;
    }
}
