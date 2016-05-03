package com.footbar.player_stat_addition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;


public class HttpClient {
    // can send GET and POST request and handle the answer. Only the GET is used for the moment
    public final static String GET = "GET";
    public final static String POST = "POST";
    private final static String ENC_FORMAT = "UTF-8";
    private int readTimeOut = 10000; // Maximum allow time to read the answer (in ms)
    private int connectTimeOut = 15000; //Maximum allowed time to connect (in ms)
    private ArrayList<HttpClientObserver> obs = new ArrayList<HttpClientObserver>();// observers of the HttpClient

    public HttpClient() {

    }

    public String doHttpGET(String urlAsString) {
        // return the server answer as a string
        HttpURLConnection con = null;
        InputStream is        = null;
        StringBuffer buffer   = new StringBuffer();

        try {
            // create the connection using the parameters
            con = (HttpURLConnection) (new URL(urlAsString)).openConnection();
            con.setRequestMethod(GET);
            con.setReadTimeout(readTimeOut);
            con.setConnectTimeout(connectTimeOut);
            con.setDoInput(true);

            con.connect();

            // read the response
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\r\n");
            }
            is.close();
            con.disconnect();
        } catch (SocketTimeoutException e) {
            updateObservers("SocketTimeoutException", SeverityLevel.warn);
            e.printStackTrace();
        } catch (ConnectException e) {
            updateObservers("ConnectException", SeverityLevel.warn);
            updateObservers("Sorry, the server is not available, operation failed.", SeverityLevel.error);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            updateObservers("Cannot connect to server.", SeverityLevel.error);
            e.printStackTrace();
        } catch (IOException e) {
            updateObservers("IOException 1", SeverityLevel.error);
            e.printStackTrace();
        } finally {
            try {
                updateObservers("Closing InputStream...", SeverityLevel.verbose);
                is.close();
                updateObservers("Disconnecting HttpURLConnection...", SeverityLevel.verbose);
                con.disconnect();
            } catch (IOException e) {
                updateObservers("IOException 2", SeverityLevel.verbose);
                e.printStackTrace();
            } catch (NullPointerException e) {
                updateObservers("NullPointerException. If android phone: phone has no internet connection.", SeverityLevel.error);
                e.printStackTrace();
            }
        }
        return buffer.toString();// return the response as a string
    }

    /**
     * Send a HTTP POST request to a server designated by his url passed as a String. Returns the answer as a String.
     * @param urlAsString : the url of the server, as a String
     * @param urlParameters : the url parameters to send to the server
     * @return the answer of the server as a String
     */
    public String doHttpPOST(String urlAsString, String urlParameters) {
        HttpURLConnection con = null;
        InputStream is = null;
        StringBuffer buffer = new StringBuffer();

        try {
            con = (HttpURLConnection) (new URL(urlAsString)).openConnection();
            con.setRequestMethod(POST);
            con.setReadTimeout(readTimeOut);
            con.setConnectTimeout(connectTimeOut);
            con.setDoInput(true);
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, ENC_FORMAT));
            writer.write(urlParameters);
            writer.flush();
            writer.close();
            os.close();

            con.connect();

            // Let's read the response
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\r\n");
            }
            is.close();
            con.disconnect();
        } catch (SocketTimeoutException e) {
            updateObservers("SocketTimeoutException", SeverityLevel.warn);
            e.printStackTrace();
        } catch (ConnectException e) {
            updateObservers("ConnectException", SeverityLevel.warn);
            updateObservers("Sorry, the server is not available, operation failed.", SeverityLevel.error);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            updateObservers("Cannot connect to server.", SeverityLevel.error);
            e.printStackTrace();
        } catch (IOException e) {
            updateObservers("IOException 1", SeverityLevel.error);
            e.printStackTrace();
        } finally {
            try {
                updateObservers("Closing InputStream...", SeverityLevel.verbose);
                is.close();
                updateObservers("Disconnecting HttpURLConnection...", SeverityLevel.verbose);
                con.disconnect();
            } catch (IOException e) {
                updateObservers("IOException 2", SeverityLevel.verbose);
                e.printStackTrace();
            } catch (NullPointerException e) {
                updateObservers("NullPointerException. If android phone: phone has no internet connection.", SeverityLevel.error);
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public void addHttpClientObserver(HttpClientObserver newObs) {
        this.obs.add(newObs);
    }

    private void updateObservers(String message, SeverityLevel severityLevel) {
        for (HttpClientObserver o : obs) {
            o.updateMessage(message, severityLevel);
        }
    }

    public interface HttpClientObserver {
        // to display the error messages
        public void updateMessage(String message, SeverityLevel severity);
    }

    public enum SeverityLevel {
        // describe the errors and verbose
        verbose(1), debug(2), info(3), warn(4), error(5);
        private int value;

        SeverityLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
