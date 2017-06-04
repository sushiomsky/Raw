package xyz.bitnaesser.raw;

/**
 * Created by sushi on 04.06.17.
 */

public interface OnTaskDoneListener {
    void onTaskDone(String responseData);
    void onError();
}
