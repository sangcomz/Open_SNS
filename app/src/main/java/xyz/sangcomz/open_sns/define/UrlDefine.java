package xyz.sangcomz.open_sns.define;

/**
 * Created by sangc on 2015-12-23.
 */
public class UrlDefine {

    ///////////////
    public static final String HTTP = "http://";

    public static final String SERVER_HOST = "sangcomz.xyz/open_sns";

    public static final String URL_ACCOUNT_JOIN = HTTP + SERVER_HOST + "/account/join.php";
    public static final String URL_ACCOUNT_LOGIN = HTTP + SERVER_HOST + "/account/login.php";
    public static final String URL_ACCOUNT_PHONE_SETTING = HTTP + SERVER_HOST + "/account/phone_setting.php";
    public static final String URL_ACCOUNT_GET_APP_VERSION = HTTP + SERVER_HOST + "/account/get_app_version.php";
    public static final String URL_ACCOUNT_GET_ACCOUNT = HTTP + SERVER_HOST + "/account/get_account.php";
    public static final String URL_ACCOUNT_SET_PROFILE_BG = HTTP + SERVER_HOST + "/account/set_profile_bg.php";
    public static final String URL_ACCOUNT_SET_PROFILE = HTTP + SERVER_HOST + "/account/set_profile.php";
    public static final String URL_GET_MY_POSTS = HTTP + SERVER_HOST + "/account/get_my_posts.php";
    public static final String URL_PHONE_INFO = HTTP + SERVER_HOST + "/account/phone_info.php";

    public static final String URL_POST_CREATE = HTTP + SERVER_HOST + "/post/create.php";
    public static final String URL_POST_DELETE = HTTP + SERVER_HOST + "/post/delete.php";
    public static final String URL_GET_POSTS = HTTP + SERVER_HOST + "/post/get_posts.php";
    public static final String URL_GET_POST = HTTP + SERVER_HOST + "/post/get_post.php";

    public static final String URL_SEARCH = HTTP + SERVER_HOST + "/search/search.php";

    public static final String URL_FOLLOW = HTTP + SERVER_HOST + "/follow/add.php";
    public static final String URL_UNFOLLOW = HTTP + SERVER_HOST + "/follow/delete.php";
    public static final String URL_GET_FOLLOW = HTTP + SERVER_HOST + "/follow/get_list.php";


    public static final String URL_COMMENT_CREATE = HTTP + SERVER_HOST + "/post/comment/create.php";
    public static final String URL_COMMENT_DELETE = HTTP + SERVER_HOST + "/post/comment/delete.php";
    public static final String URL_COMMENT_GET = HTTP + SERVER_HOST + "/post/comment/get_comment.php";

}
