package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.uni_stuttgart.projektinf.tmt.R;

/**
 * The Choose Sequence Activity.
 * Enables the user to choose which sequence he/she wants for the TMT.
 *
 */
public class ChooseSequenceActivity extends AppCompatActivity {

    public static int sequence;
    private Button btnNumberSequence;
    private Button btnLetterSequence;
    private Button btnMixedSequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sequence);
    }

    /**
     * Method goToGoalChooser first sets the chosen sequence order and then takes user to the next
     * activity, which is the choose goal activity.
     *
     * @param view
     * @see ChooseGoalActivity
     */
    public void goToGoalChooser(View view){
        // check which button was pressed to get the correct sequence order:
        switch (view.getId()){
            case R.id.buttonNumberSequence: sequence = 1;
                                            break;
            case R.id.buttonLetterSequence: sequence = 2;
                                            break;
            case R.id.buttonMixedSequence:  sequence = 3;
                                            break;
        }
        // go to the next activity:
        Intent chooseGoalIntent = new Intent(this, ChooseGoalActivity.class);
        startActivity(chooseGoalIntent);
    }
}