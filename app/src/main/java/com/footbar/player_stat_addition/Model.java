package com.footbar.player_stat_addition;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.footbar.player_stat_addition.AsyncHttp.AsyncHttpObserver;

public class Model implements AsyncHttpObserver {

    // Attributes
    private Controller controller;
    private ArrayList<String> player_names;
    private String string_from_url;

    public Model(Controller controller){
        this.controller = controller;
        System.out.println("model");
    }

    @Override
    public void onProgressUpdate(int progress) {
        // to indicate progress
    }

    @Override
    public void onPostExecute(String result) {
        // get the data from the page and process them
        this.string_from_url = result;
        this.process_player_names();
        this.controller.update_player_list();
    }

    public void process_player_names(){

        // create a list with data relative to players
        ArrayList<ArrayList<String>> data_list = new ArrayList<ArrayList<String>>();
        try {
            JSONArray jsonArray = new JSONArray (this.string_from_url);
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    String string_list = jsonArray.get(i).toString();
                    ArrayList<String> myList = new ArrayList<String>(Arrays.asList(string_list.split(",")));
                    data_list.add(myList);
                }

            }
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
        }

        // create the list of the player names sorted by alphabetical order
        ArrayList<String> name_array_list = new ArrayList<String>();

        for (int player_number=0;player_number<data_list.size();player_number++){
            String first_name = data_list.get(player_number).get(1).substring(8);
            int end_index = first_name.indexOf("\"");
            String name = first_name.substring(0,end_index);
            name_array_list.add(name);
        }
        Collections.sort(name_array_list, String.CASE_INSENSITIVE_ORDER);

        this.player_names = name_array_list;
    }

    public void upload_data(ArrayList<String> player_names){
        // upload the data (for the moment just display the player selected in the spinners)
        System.out.println(player_names);
    }

    public void set_controller(Controller controller){
        this.controller = controller;
    }

    public Controller get_controller(){
        return this.controller;
    }

    public void set_player_names(ArrayList<String> player_names){
        this.player_names = player_names;
    }

    public ArrayList<String> get_player_names(){
        return this.player_names;
    }

    public void set_string_from_url(String string_from_url){
        this.string_from_url = string_from_url;
    }

    public String get_string_from_url(){
        return this.string_from_url;
    }
}
