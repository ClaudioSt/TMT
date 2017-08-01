package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import de.uni_stuttgart.projektinf.tmt.R;

/**
 * The User Form Activity.
 * Offers the input for all the user data.
 * Then creates a user object with the User class containing all the data.
 *
 */

public class UserFormActivity extends AppCompatActivity {

    private RadioGroup radioGender;
    private RadioGroup radioSight;
    private RadioGroup radioHand;
    private EditText ageTxt;

    private Button btnOkUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        radioGender = (RadioGroup) findViewById(R.id.radioGender);
        radioSight = (RadioGroup) findViewById(R.id.radioSight);
        radioHand = (RadioGroup) findViewById(R.id.radioHand);
        ageTxt = (EditText) findViewById(R.id.editTextAge);
        btnOkUser = (Button) findViewById(R.id.buttonUser);
        addListenerOnButton();
    }


    public void addListenerOnButton() {

        btnOkUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radioButtons from radioGroups:
                int selectedIdGender = radioGender.getCheckedRadioButtonId();
                int selectedIdSight = radioSight.getCheckedRadioButtonId();
                int selectedIdHand = radioHand.getCheckedRadioButtonId();
                // get age:
                int age = Integer.parseInt(ageTxt.getText().toString());

            }

        });

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