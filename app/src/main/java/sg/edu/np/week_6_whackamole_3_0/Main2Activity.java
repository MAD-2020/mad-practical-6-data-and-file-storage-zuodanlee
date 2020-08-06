package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    /* Hint:
        1. This is the create new user page for user to log in
        2. The user can enter - Username and Password
        3. The user create is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user already exists.
        4. For the purpose the practical, successful creation of new account will send the user
           back to the login page and display the "User account created successfully".
           the page remains if the user already exists and "User already exist" toastbox message will appear.
        5. There is an option to cancel. This loads the login user page.
     */


    private static final String FILENAME = "Main2Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /* Hint:
            This prepares the create and cancel account buttons and interacts with the database to determine
            if the new user created exists already or is new.
            If it exists, information is displayed to notify the user.
            If it does not exist, the user is created in the DB with default data "0" for all levels
            and the login page is loaded.

            Log.v(TAG, FILENAME + ": New user created successfully!");
            Log.v(TAG, FILENAME + ": User already exist during new user creation!");

         */
        final EditText etUsername = (EditText) findViewById(R.id.etCreateUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etCreatePassword);
        Button bCancel = (Button) findViewById(R.id.buttonCancel);
        Button bCreate = (Button) findViewById(R.id.buttonCreate);

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (username.isEmpty() == false && password.isEmpty() == false) {
                    if (createNewUser(username, password)) {
                        Toast.makeText(getApplicationContext(), "User Created Successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter a username and password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    protected void onStop() {
        super.onStop();
        finish();
    }

    public boolean createNewUser(String username, String password) {
        boolean result = false;

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        UserData newUserData = dbHandler.findUser(username);

        if (newUserData == null) {
            ArrayList<Integer> userLevels = new ArrayList<>();
            ArrayList<Integer> userScores = new ArrayList<>();

            newUserData = new UserData();
            newUserData.setMyUserName(username);
            newUserData.setMyPassword(password);
            for (int i = 1; i < 11; i++) {
                userLevels.add(i);
                userScores.add(0);
            }
            newUserData.setLevels(userLevels);
            newUserData.setScores(userScores);
            dbHandler.addUser(newUserData);
            Log.v(TAG, FILENAME + ": New user created successfully!");
            result = true;
        }
        else {
            Log.v(TAG, FILENAME + ": User already exist during new user creation!");
            Toast.makeText(this, "User Already Exists. Please Try Again.", Toast.LENGTH_SHORT).show();
        }

        return result;
    }
}
