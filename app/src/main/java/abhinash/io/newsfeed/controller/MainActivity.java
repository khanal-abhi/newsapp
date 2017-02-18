package abhinash.io.newsfeed.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import abhinash.io.newsfeed.R;

/**
 * Created by khanal on 2/17/17.
 * This is the main Activity.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_header);
    }
}
