package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    String username;
    private int score = 0;
    private TextView scoreView;
    private int[] currentMoles = { 0, 0 };
    private int selectedLevel;
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;

    private void readyTimer(){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
        final Toast myToast = Toast.makeText(getApplicationContext(), "Get Ready In 10 seconds", Toast.LENGTH_SHORT);

        readyTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
                myToast.setText("Get Ready In " + millisUntilFinished/1000 + " seconds");
                myToast.show();
            }

            @Override
            public void onFinish() {
                Log.v(TAG, "Ready CountDown Complete!");
                Toast.makeText(getApplicationContext(), "GO!", Toast.LENGTH_SHORT).show();
                setNewMole();
                placeMoleTimer();
                readyTimer.cancel();
            }
        };
        readyTimer.start();
    }
    private void placeMoleTimer(){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        int millisToNewMole = (11 - selectedLevel) * 1000;

        newMolePlaceTimer = new CountDownTimer(millisToNewMole, millisToNewMole) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                setNewMole();
                Log.v(TAG, "New Mole Location!");
                newMolePlaceTimer.start();
            }
        };
        newMolePlaceTimer.start();
    }
    private static final int[] BUTTON_IDS = {
        /* HINT:
            Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
            You may use if you wish to change or remove to suit your codes.*/
        R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5,
            R.id.button6, R.id.button7, R.id.button8, R.id.button9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */
        Intent getData = getIntent();
        selectedLevel = getData.getIntExtra("level", 0);
        username = getData.getStringExtra("username");
        Log.v(TAG, "Starting up level " + selectedLevel);

        scoreView = findViewById(R.id.scoreView);
        scoreView.setText(Integer.toString(score));

        for(final int id : BUTTON_IDS){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            Button button = findViewById(id);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = (Button) view;
                    doCheck(button);
                }
            });
            button.setText("O");
        }

        Button buttonBack = findViewById(R.id.buttonBackToLevelSelect);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4Activity.this, Main3Activity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                updateUserScore();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        readyTimer();
    }
    private void doCheck(Button checkButton)
    {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */
        if (currentMoles[0] != 0){
            if (contains(currentMoles, checkButton.getId())){
                score += 1;
                Log.v(TAG, "Hit, score added!");
            }
            else{
                if (score > 0){
                    score -= 1;
                    Log.v(TAG, "Missed, point deducted!");
                }
                else{
                    Log.v(TAG, "Missed!");
                }
            }
            scoreView.setText(Integer.toString(score));
            newMolePlaceTimer.cancel();
            newMolePlaceTimer.start();
            setNewMole();
        }
    }

    public void setNewMole()
    {
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole. Adds additional mole if the level difficulty is from 6 to 10.
         */
        int[] tempButtonIds = Arrays.copyOf(BUTTON_IDS, BUTTON_IDS.length);

        int molesOffset = 0;
        if (selectedLevel <= 5){
            molesOffset = 1;
        }

        int[] tempCurrentMoles = { 0, 0 };
        for (int i = 0; i < currentMoles.length-molesOffset; i++) {
            if (currentMoles[i] != 0) {
                Button currentMoleButton = findViewById(currentMoles[i]);
                currentMoleButton.setText("O");
            }

            Random ran = new Random();
            int randomLocation = ran.nextInt(9-i);
            tempCurrentMoles[i] = tempButtonIds[randomLocation];
            int[] newTempButtonIdsI = Arrays.copyOfRange(tempButtonIds, 0, findIndex(tempButtonIds, tempCurrentMoles[i]));
            int[] newTempButtonIdsII = Arrays.copyOfRange(tempButtonIds, findIndex(tempButtonIds, tempCurrentMoles[i])+1, tempButtonIds.length);
            tempButtonIds = mergeIntArrays(newTempButtonIdsI, newTempButtonIdsII);

            if (selectedLevel <= 5){
                break;
            }
        }

        for (int i = 0; i < tempCurrentMoles.length; i++){
            currentMoles[i] = tempCurrentMoles[i];
            Button newMoleButton = findViewById(currentMoles[i]);
            newMoleButton.setText("*");

            if (selectedLevel <= 5){
                break;
            }
        }
    }

    private void updateUserScore()
    {

     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */
        newMolePlaceTimer.cancel();
        readyTimer.cancel();
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.updateScore(username, selectedLevel, score);
    }

    public boolean contains(int[] intArray, int intSearch)
    {
        for (int i : intArray){
            if (i == intSearch){
                return true;
            }
        }

        return false;
    }

    public int[] mergeIntArrays(int[] arr1, int[] arr2)
    {
        int fal = arr1.length;        //determines length of firstArray
        int sal = arr2.length;   //determines length of secondArray
        int[] result = new int[fal + sal];  //resultant array of size first array and second array
        System.arraycopy(arr1, 0, result, 0, fal);
        System.arraycopy(arr2, 0, result, fal, sal);
        return result;
    }

    // Linear-search function to find the index of an element
    public static int findIndex(int arr[], int t)
    {

        // if array is Null
        if (arr == null) {
            return -1;
        }

        // find length of array
        int len = arr.length;
        int i = 0;

        // traverse in the array
        while (i < len) {

            // if the i-th element is t
            // then return the index
            if (arr[i] == t) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }
}
