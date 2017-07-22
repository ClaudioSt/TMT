package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import de.uni_stuttgart.projektinf.tmt.R;

public class ChooseGoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_goal);
    }

    public void goToTMT(View view){
        Intent TMTIntent = new Intent(this, TMTActivity.class);
        startActivity(TMTIntent);
    }
}