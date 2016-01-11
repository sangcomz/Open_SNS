package xyz.sangcomz.sangcomz_n_study.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sangc on 2015-12-28.
 */
public class Member {

    @SerializedName("member_srl")
    String memberSrl;
    @SerializedName("member_name")
    String memberName;
    @SerializedName("member_profile")
    String profilePath;
    @SerializedName("member_profile_bg")
    String memberProfileBg;
    @SerializedName("follow_yn")
    String followYN;
    @SerializedName("member_post_count")
    String memberPostCount;
    @SerializedName("member_follower_count")
    String memberFollowerCount;
    @SerializedName("member_following_count")
    String memberFollowingCount;
    @SerializedName("is_my_profile")
    String isMyProfile;


    public Member(String memberSrl, String memberName, String profilePath, String memberProfileBg, String followYN, String memberPostCount, String memberFollowerCount, String memberFollowingCount, String isMyProfile) {
        this.memberSrl = memberSrl;
        this.memberName = memberName;
        this.profilePath = profilePath;
        this.memberProfileBg = memberProfileBg;
        this.followYN = followYN;
        this.memberPostCount = memberPostCount;
        this.memberFollowerCount = memberFollowerCount;
        this.memberFollowingCount = memberFollowingCount;
        this.isMyProfile = isMyProfile;
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

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getMemberProfileBg() {
        return memberProfileBg;
    }

    public void setMemberProfileBg(String memberProfileBg) {
        this.memberProfileBg = memberProfileBg;
    }

    public String getFollowYN() {
        return followYN;
    }

    public void setFollowYN(String followYN) {
        this.followYN = followYN;
    }

    public String getMemberPostCount() {
        return memberPostCount;
    }

    public void setMemberPostCount(String memberPostCount) {
        this.memberPostCount = memberPostCount;
    }

    public String getMemberFollowerCount() {
        return memberFollowerCount;
    }

    public void setMemberFollowerCount(String memberFollowerCount) {
        this.memberFollowerCount = memberFollowerCount;
    }

    public String getMemberFollowingCount() {
        return memberFollowingCount;
    }

    public void setMemberFollowingCount(String memberFollowingCount) {
        this.memberFollowingCount = memberFollowingCount;
    }

    public String getIsMyProfile() {
        return isMyProfile;
    }

    public void setIsMyProfile(String isMyProfile) {
        this.isMyProfile = isMyProfile;
    }
}
