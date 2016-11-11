package com.yatatsu.autobundle.example;

import android.support.v7.app.AppCompatActivity;

import com.yatatsu.autobundle.AbstractAutoBundle;
import com.yatatsu.autobundle.AutoBundleField;

@AbstractAutoBundle
public class ExampleAbstractActivity extends AppCompatActivity {
    @AutoBundleField
    String name;
}
