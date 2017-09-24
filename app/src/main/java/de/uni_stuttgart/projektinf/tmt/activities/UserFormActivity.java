package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import de.uni_stuttgart.projektinf.tmt.R;
import de.uni_stuttgart.projektinf.tmt.classes.User;

/**
 * The User Form Activity.
 * Offers the input for all the user data.
 * Then creates a user object with the User class containing all the data. After this activity the
 * real Test starts.
 *
 */

public class UserFormActivity extends AppCompatActivity {

    private RadioGroup radioGender;
    private RadioGroup radioSight;
    private RadioGroup radioHand;
    private EditText ageTxt;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        radioGender = (RadioGroup) findViewById(R.id.radioGender);
        radioSight = (RadioGroup) findViewById(R.id.radioSight);
        radioHand = (RadioGroup) findViewById(R.id.radioHand);
        ageTxt = (EditText) findViewById(R.id.editTextAge);
    }


    /**
     * Method UserClickedOk is called after the user presses the "Ok" button.
     * It takes the information about the user and creates a User object.
     * It then takes the user to the next activity, which is the choose sequence activity.
     *
     * @param view
     * @see ChooseSequenceActivity
     * @see de.uni_stuttgart.projektinf.tmt.classes.User
     */
    public void UserClickedOk(View view){
        int gender = 0;
        int sight = 0;
        int hand = 0;

        /*
        GENDER:
        1 = female
        2 = male
        3 = other
        SIGHT:
        1 = glasses
        2 = contacts
        3 = none
        HANDEDNESS:
        1 = left
        2 = right
         */

        switch (radioGender.getCheckedRadioButtonId()){
            case R.id.radioButtonGenderFemale:  gender = 1;
                                                break;
            case R.id.radioButtonGenderMale:    gender = 2;
                                                break;
            case R.id.radioButtonGenderOther:   gender = 3;
                                                break;
        }
        switch (radioSight.getCheckedRadioButtonId()){
            case R.id.radioButtonSightGlasses:  sight = 1;
                                                break;
            case R.id.radioButtonSightContacts: sight = 2;
                                                break;
            case R.id.radioButtonSightNone: sight = 3;
                                            break;
        }
        switch (radioHand.getCheckedRadioButtonId()){
            case R.id.radioButtonHandLeft:  hand = 1;
                                            break;
            case R.id.radioButtonHandRight: hand = 2;
                                            break;
        }

        String ageString = ageTxt.getText().toString();

        // check if everything is filled out properly:
        if (gender == 0 || sight == 0 || hand == 0 || TextUtils.isEmpty(ageString)){
            // if something is missing, show a small toast message:
            Context context = getApplicationContext();
            CharSequence text = "Please fill out all of the fields...";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else {
            // create the user object with the user class:
            user = new User(gender, Integer.parseInt(ageString), sight, hand);

            // go to the next activity:
            Intent chooseSequenceIntent = new Intent(this, ChooseSequenceActivity.class);
            startActivity(chooseSequenceIntent);
        }

    }
}