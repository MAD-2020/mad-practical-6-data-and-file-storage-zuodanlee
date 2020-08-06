package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Hint:
            This method creates the necessary login inputs and the new user creation ontouch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");

        */
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        Button bLogin = (Button) findViewById(R.id.buttonLogin);
        TextView etNewUser = (TextView) findViewById(R.id.tvNewUser);

        etNewUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                Log.v(TAG, FILENAME + ": Create new user!");
                startActivity(intent);
                return false;
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidUser(etUsername.getText().toString(), etPassword.getText().toString())) {
                    Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                    intent.putExtra("username", etUsername.getText().toString());
                    startActivity(intent);
                }
            }
        });

    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public void deleteAccount(String username) { // for development purposes
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.deleteAccount(username);
    }

    public boolean isValidUser(String userName, String password){

        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */
        boolean result = false;
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        UserData dbData = dbHandler.findUser(userName);
        if (dbData != null) {
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + ": " + password);
            if (dbData.getMyPassword().equals(password)) {
                result = true;
            }
            else {
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Log.v(TAG, FILENAME + ": No data found!");
            Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

}
