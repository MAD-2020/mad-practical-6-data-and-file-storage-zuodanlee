package sg.edu.np.week_6_whackamole_3_0;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView level;
    TextView highscore;
    OnScoreListener onScoreListener;
    /* Hint:
        1. This is a customised view holder for the recyclerView list @ levels selection page
     */
    private static final String FILENAME = "CustomScoreViewHolder.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    public CustomScoreViewHolder(final View itemView, OnScoreListener onScoreListener){
        super(itemView);

        /* Hint:
        This method dictates the viewholder contents and links the widget to the objects for manipulation.
         */
        level = itemView.findViewById(R.id.tvLevel);
        highscore = itemView.findViewById(R.id.tvHighscore);
        this.onScoreListener = onScoreListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onScoreListener.selectLevel(getAdapterPosition());
    }

    public interface OnScoreListener {
        void selectLevel(int position);
    }
}
