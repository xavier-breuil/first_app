package com.footbar.player_stat_addition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class Chip_list_Activity extends AppCompatActivity implements View.OnClickListener {

    // Attributes
    private Controller controller;
    private ArrayList<Spinner> meteor_spinner_list;
    private Button cancel_button;
    private Button load_player_button;
    private Button confirm_button;
    private String online_data_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chip_list_);
        this.controller = new Controller(this);
        this.online_data_url = "https://playerbackend.footbar.com/playerslist/";

        //add the spinners to the spinner list
        this.meteor_spinner_list = new ArrayList<Spinner>();
        this.meteor_spinner_list.add(0,(Spinner)findViewById(R.id.meteor_spinner_10));
        this.meteor_spinner_list.add(0,(Spinner)findViewById(R.id.meteor_spinner_9));
        this.meteor_spinner_list.add(0,(Spinner)findViewById(R.id.meteor_spinner_8));
        this.meteor_spinner_list.add(0,(Spinner)findViewById(R.id.meteor_spinner_7));
        this.meteor_spinner_list.add(0,(Spinner)findViewById(R.id.meteor_spinner_6));
        this.meteor_spinner_list.add(0,(Spinner)findViewById(R.id.meteor_spinner_5));
        this.meteor_spinner_list.add(0,(Spinner)findViewById(R.id.meteor_spinner_4));
        this.meteor_spinner_list.add(0,(Spinner)findViewById(R.id.meteor_spinner_3));
        this.meteor_spinner_list.add(0,(Spinner)findViewById(R.id.meteor_spinner_2));
        this.meteor_spinner_list.add(0,(Spinner)findViewById(R.id.meteor_spinner_1));

        // fill the spinners with the names of the players
        this.display_initial_player_list();

        this.cancel_button  = (Button) findViewById(R.id.cancel_button);
        this.cancel_button.setOnClickListener(this);
        this.load_player_button = (Button) findViewById(R.id.load_player_button);
        this.load_player_button.setOnClickListener(this);
        this.confirm_button = (Button) findViewById(R.id.confirm_button);
        this.confirm_button.setOnClickListener(this);

    }

    public void display_initial_player_list(){
        System.out.println("display init players");
        ArrayList<ArrayList<String>> list_of_list_of_meteors = new ArrayList<ArrayList<String>>();// = new ArrayList<String> (Arrays.asList(player_names));
        for (int i = 1; i < 11; i++) {
            list_of_list_of_meteors.add(0, new ArrayList<String>());
            list_of_list_of_meteors.get(0).add(0, "Meteor " + (11 - i));
        }
        // fill the spinners with the list of the players
        for (int i = 0; i < 10; i++) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_center_text_layout, list_of_list_of_meteors.get(i));//android.R.layout.simple_spinner_dropdown_item,
            adapter.setDropDownViewResource(R.layout.spinner_center_text_layout);
            this.meteor_spinner_list.get(i).setAdapter(adapter);
        }
    }
/*
    public void display_player_list() {
        // get the player names list
        ArrayList<String> player_names = this.controller.get_player_names();

        // create the list that contains the lists that will be written in the spinners
        // [(meteor 1, player 1, player 2,...), (meteor 2, player 1, player 2,...)]
        ArrayList<ArrayList<String>> list_of_list_of_names = new ArrayList<ArrayList<String>>();// = new ArrayList<String> (Arrays.asList(player_names));
        for (int i = 1; i < 11; i++) {
            list_of_list_of_names.add(0, new ArrayList<String>(player_names));
            list_of_list_of_names.get(0).add(0, "Meteor " + (11 - i));
        }

        // fill the spinners with the list of the players
        for (int i = 0; i < 10; i++) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_center_text_layout, list_of_list_of_names.get(i));//android.R.layout.simple_spinner_dropdown_item,
            adapter.setDropDownViewResource(R.layout.spinner_center_text_layout);
            this.meteor_spinner_list.get(i).setAdapter(adapter);
        }
    }*/

    public void update_player_list(ArrayList<String> player_names){

        // create the list that contains the lists that will be written in the spinners
        // [(meteor 1, player 1, player 2,...), (meteor 2, player 1, player 2,...)]
        ArrayList<ArrayList<String>> list_of_list_of_names=new ArrayList<ArrayList<String>>();// = new ArrayList<String> (Arrays.asList(player_names));
        for (int i=1; i<11; i++){
            list_of_list_of_names.add(0,new ArrayList<String>(player_names));
            list_of_list_of_names.get(0).add(0,"Meteor "+(11-i));
        }

        // fill the spinners with the list of the players
        for (int i=0;i<10;i++){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_center_text_layout, list_of_list_of_names.get(i));//android.R.layout.simple_spinner_dropdown_item,
            adapter.setDropDownViewResource(R.layout.spinner_center_text_layout);
            this.meteor_spinner_list.get(i).setAdapter(adapter);
        }

    }

    public void upload_data(){
        // get the names of the players and send them to the controller to upload the data
        ArrayList<String> player_names = new ArrayList<>();
        for (int player_number=1;player_number<11; player_number++){
            String player_name = this.meteor_spinner_list.get(10-player_number).getSelectedItem().toString();
            player_names.add(0,player_name);
        }

        this.controller.upload_data(player_names);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.confirm_button:
                this.upload_data();
                break;
            case R.id.load_player_button:
                // launch the process to get data from the internet page.
                // the listener is the model
                AsyncHttp asyncHttp = new AsyncHttp(this.controller.get_model());
                asyncHttp.execute(this.online_data_url);
                break;
        }
    }
}
