package xyz.sangcomz.open_sns.event;

/**
 * Created by sangcomz on 2/2/16.
 */
public class DelPostEvent {
    int position;

    public DelPostEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
