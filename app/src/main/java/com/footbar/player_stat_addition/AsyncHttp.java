package com.footbar.player_stat_addition;

import android.os.AsyncTask;

import com.footbar.player_stat_addition.HttpClient.HttpClientObserver;
import com.footbar.player_stat_addition.HttpClient.SeverityLevel;


public class AsyncHttp extends AsyncTask<String, Integer, String> {
    // Class to create a thread that will get the data from the internet page
    private HttpClient httpClient = new HttpClient(); // HttpClient will communicate with the internet page
    private AsyncHttpObserver obs; // the observer will treat the result once the internet page sent them

    public AsyncHttp(AsyncHttpObserver obs) {
        this.obs = obs;

        // HttpClientObserver will display error messages in case there is an issue getting the data
        this.httpClient.addHttpClientObserver(new HttpClientObserver() {

            @Override
            public void updateMessage(String message, SeverityLevel severity) {
                System.out.println(message);
            }
        });
    }

    @Override
    protected String doInBackground(String... arg0) {
        // ask the content of the internet page
        return httpClient.doHttpGET(arg0[0]);
    }

    protected void onProgressUpdate(Integer... progress) {
        // If needed, you can show progress
        obs.onProgressUpdate(progress[0]);
    }

    protected void onPostExecute(String result) {
        // the observer deals with the data from the page
        obs.onPostExecute(result);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setAsyncHttpObserver(AsyncHttpObserver obs) {
        this.obs = obs;
    }


    public interface AsyncHttpObserver {
        //Interface to implement to deal with the result of the page
        public void onProgressUpdate(int progress);

        public void onPostExecute(String result);
    }

}
