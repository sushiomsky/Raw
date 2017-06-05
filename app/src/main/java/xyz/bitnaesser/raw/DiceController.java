package xyz.bitnaesser.raw;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import xyz.bitnaesser.raw.dice.client.web.BeginSessionResponse;
import xyz.bitnaesser.raw.dice.client.web.DiceWebAPI;
import xyz.bitnaesser.raw.dice.client.web.SessionInfo;

/**
 * Created by sushi on 03.06.17.
 */

public class DiceController extends AsyncTask<String, Void, String> {


    private Context mContext;
    private OnTaskDoneListener onTaskDoneListener;
    private BeginSessionResponse beginSessionResponse;
    private SessionInfo sessionInfo;
    private double balance;
    public DiceController(Context context,  OnTaskDoneListener onTaskDoneListener) {
        this.mContext = context;
        this.onTaskDoneListener = onTaskDoneListener;
    }

    private long getBalance(){
        return  DiceWebAPI.toSatoshis(beginSessionResponse.getSession().getBalance());
    }

    @Override
    protected String doInBackground(String... params) {
        beginSessionResponse = DiceWebAPI.BeginSession(mContext.getString(R.string.dice_api_key),"bitnaesser","misty12");
        sessionInfo = beginSessionResponse.getSession();
        balance = getBalance()-11;
        publishProgress();
        return String.valueOf(getBalance());
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (onTaskDoneListener != null && s != null) {
            onTaskDoneListener.onTaskDone(s);
        } else
            onTaskDoneListener.onError();
    }
}