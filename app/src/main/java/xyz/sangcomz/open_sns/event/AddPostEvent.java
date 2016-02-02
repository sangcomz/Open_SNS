package xyz.sangcomz.open_sns.event;

import xyz.sangcomz.open_sns.bean.Post;

/**
 * Created by sangcomz on 2/2/16.
 */
public class AddPostEvent {
    Post post;

    public AddPostEvent(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
