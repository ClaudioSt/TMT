package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import de.uni_stuttgart.projektinf.tmt.R;

/**
 * The User Form Activity.
 * Offers the input for all the user data.
 * Then creates a user object with the User class containing all the data.
 *
 */

public class UserFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
    }

    /**
     * Method goToSequenceChooser takes user to the next activity,
     * which is the choose sequence activity.
     *
     * @param view
     * @see ChooseSequenceActivity
     */
    public void goToSequenceChooser(View view){
        Intent chooseSequenceIntent = new Intent(this, ChooseSequenceActivity.class);
        startActivity(chooseSequenceIntent);
    }
}