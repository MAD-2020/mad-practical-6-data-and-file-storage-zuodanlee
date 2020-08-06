package sg.edu.np.week_6_whackamole_3_0;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {
    /* Hint:
        1. This is the custom adaptor for the recyclerView list @ levels selection page

     */
    private UserData userData;
    ArrayList<Integer> level_list;
    ArrayList<Integer> score_list;
    private CustomScoreViewHolder.OnScoreListener mOnScoreListener;

    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    public CustomScoreAdaptor(UserData userdata, CustomScoreViewHolder.OnScoreListener onScoreListener){
        /* Hint:
        This method takes in the data and readies it for processing.
         */
        this.userData = userdata;
        this.level_list = userData.getLevels();
        this.score_list = userData.getScores();
        this.mOnScoreListener = onScoreListener;
    }

    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        /* Hint:
        This method dictates how the viewholder layuout is to be once the viewholder is created.
         */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_select, parent, false);
        return new CustomScoreViewHolder(view, mOnScoreListener);
    }

    public void onBindViewHolder(CustomScoreViewHolder holder, final int position){

        /* Hint:
        This method passes the data to the viewholder upon bounded to the viewholder.
        It may also be used to do an onclick listener here to activate upon user level selections.

        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + list_members.getMyUserName());
         */

        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        holder.level.setText("Level " + level_list.get(position).toString());
        holder.highscore.setText("Highest Score: " + score_list.get(position).toString());
    }

    public int getItemCount(){
        /* Hint:
        This method returns the the size of the overall data.
         */
        return userData.getLevels().size();
    }
}