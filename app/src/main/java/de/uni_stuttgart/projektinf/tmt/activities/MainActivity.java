package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import de.uni_stuttgart.projektinf.tmt.R;

/**
 * The Main Activity.
 * This is the activity the app starts with. It offers the possibility to start a new Trail Making
 * Test or look at the history of already done TMT's.
 *
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * Method goToUserForm takes user to the next activity,
     * which is the user form activity. Pressing this button "starts" a new TMT.
     *
     * @param view
     * @see UserFormActivity
     */
    public void goToUserForm(View view){
        Intent fillOutUserFormIntent = new Intent(this, UserFormActivity.class);
        startActivity(fillOutUserFormIntent);
    }

    //TODO: Möglichkeit die bisherige Historie zu exportieren...

}