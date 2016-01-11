package com.msi.nearbyplacefinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by MSi on 11/1/2015.
 */
public class AppSettings extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private Toolbar toolbar;

    private static final String Shared_Name = "DistanceRaius";
    private static final int Radius = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appsettings);

        toolbar = (Toolbar) findViewById(R.id.toobar_withoutmenu);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        sharedPreferences = getSharedPreferences("DistanceRaius", Context.MODE_PRIVATE);

        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
        numberPicker.setWrapSelectorWheel(true);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                editor = sharedPreferences.edit();

                if (sharedPreferences.getInt("radius", 0) == 0) {
                    editor.putInt("radius", 2000);
                } else {
                    editor.putInt("radius", newVal * 1000);
                    editor.putInt("numPicker_radius",newVal);
                }
                editor.commit();
            }
        });

        numberPicker.setValue(sharedPreferences.getInt("radius",0)/1000);
    }
}
