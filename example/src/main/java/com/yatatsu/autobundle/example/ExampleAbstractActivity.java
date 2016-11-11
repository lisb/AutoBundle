package com.yatatsu.autobundle.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.yatatsu.autobundle.AbstractAutoBundle;
import com.yatatsu.autobundle.AutoBundle;
import com.yatatsu.autobundle.AutoBundleField;
import com.yatatsu.autobundle.AutoBundleGetter;
import com.yatatsu.autobundle.AutoBundleSetter;

import java.util.ArrayList;

@AbstractAutoBundle
public class ExampleAbstractActivity extends AppCompatActivity {
    @AutoBundleField
    String name;
}
