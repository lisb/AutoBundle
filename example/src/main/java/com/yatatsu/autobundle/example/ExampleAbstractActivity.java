package com.yatatsu.autobundle.example;

import android.support.v7.app.AppCompatActivity;

import com.yatatsu.autobundle.AutoBundleField;
import com.yatatsu.autobundle.AutoBundleType;

@AutoBundleType(isAbstract = true)
public class ExampleAbstractActivity extends AppCompatActivity {
    @AutoBundleField
    String name;
}
