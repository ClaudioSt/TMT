package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import de.uni_stuttgart.projektinf.tmt.R;

public class UserFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
    }

    public void goToSequenceChooser(View view){
        Intent chooseSequenceIntent = new Intent(this, ChooseSequenceActivity.class);
        startActivity(chooseSequenceIntent);
    }
}