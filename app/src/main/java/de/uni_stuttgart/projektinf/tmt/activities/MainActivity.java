package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import de.uni_stuttgart.projektinf.tmt.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void goToUserForm(View view){
        Intent fillOutUserFormIntent = new Intent(this, UserFormActivity.class);
        startActivity(fillOutUserFormIntent);
    }
}