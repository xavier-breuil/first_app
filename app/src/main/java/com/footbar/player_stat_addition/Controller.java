package com.footbar.player_stat_addition;

import java.util.ArrayList;

public class Controller {

    // Attribute
    private Chip_list_Activity chip_list_activity;
    private Model model;

    public Controller(Chip_list_Activity chip_list_activity){
        this.chip_list_activity = chip_list_activity;
        this.model              = new Model(this);
    }

    public void set_chip_list_activity(Chip_list_Activity chip_list_activity){
        this.chip_list_activity = chip_list_activity;
    }

    public Chip_list_Activity get_chip_list_activity(){
        return this.chip_list_activity;
    }

    public void set_model(Model model){
        this.model = model;
    }

    public Model get_model(){
        return this.model;
    }

    public ArrayList<String> get_player_names(){
        // get the player names from the model and return them
        return this.model.get_player_names();
    }

    public void update_player_list(){
        this.chip_list_activity.update_player_list(model.get_player_names());
    }

    public void upload_data(ArrayList<String> player_names){
        // send the matches between players and trackers to the model so it can upload the data
        this.model.upload_data(player_names);
    }
}
