package com.yatatsu.autobundle.example;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.yatatsu.autobundle.AutoBundle;
import com.yatatsu.autobundle.AutoBundleField;
import com.yatatsu.autobundle.AutoBundleGetter;
import com.yatatsu.autobundle.AutoBundleSetter;

import java.util.ArrayList;
import java.util.HashMap;


public class ExampleActivity extends AppCompatActivity {

    @AutoBundleField
    String name;

    @AutoBundleField(required = false)
    String optionalName;

    @AutoBundleField(required = false)
    ArrayList<CharSequence> fooList;

    @AutoBundleField(required = false, converter = ParcelableConverter.class)
    ExampleData exampleData;

    @AutoBundleField(required = false)
    ArrayList<Person> persons;

    @AutoBundleField(required = false, converter = ParcelableConverter.class)
    ExampleData exampleData2;

    @AutoBundleField(generateBuilderParam = false)
    String state;

    @AutoBundleField(generateGetter = true, generateBuilderParam = false)
    String name2;

    @AutoBundleField(generateGetter = true, generateBuilderParam = false)
    ArrayList<Person> person2;

    @AutoBundleField(generateGetter = true, required = false,
            converter = ParcelableConverter.class)
    ExampleData exampleData3;

    @AutoBundleField(required = false)
    HashMap<HashMap<String, String>, String> map;

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    @AutoBundleSetter(key = "optionalName")
    protected void setAltName(String name) {
        this.optionalName = name;
    }

    @AutoBundleGetter(key = "optionalName")
    public String getAltName() {
        return optionalName;
    }

    public void setExampleData2(ExampleData exampleData2) {
        this.exampleData2 = exampleData2;
    }

    public ExampleData getExampleData2() {
        return exampleData2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        setContentView(view);
        if (savedInstanceState != null) {
            AutoBundle.bind(this, savedInstanceState);
            Log.d("ExampleActivity", "savedInstanceState: " + savedInstanceState.toString());
        } else {
            AutoBundle.bind(this, getIntent());
            Log.d("ExampleActivity", "intent: " + getIntent().getExtras());
        }
        setTitle(name);
        StringBuilder sb = new StringBuilder("bundle\n");
        if (!TextUtils.isEmpty(optionalName)) {
            sb.append("optionalName: ").append(optionalName).append("\n");
        }
        if (fooList != null) {
            sb.append("fooList: ");
            for (CharSequence c : fooList) {
                sb.append(c).append(" ");
            }
            sb.append("\n");
        }
        if (exampleData != null) {
            sb.append("exampleData: ");
            sb.append("id: ").append(exampleData.id).append(" ");
            sb.append("message: ").append(exampleData.message);
            sb.append("\n");
        }
        if (persons != null) {
            sb.append("persons: ").append("\n");
            for (Person p : persons) {
                sb.append(p.name).append(",").append(p.age).append("\n");
            }
        }
        String text = sb.toString();
        view.setText(text);
        Log.d("ExampleActivity", text);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        AutoBundle.pack(this, outState);
        Log.d("ExampleActivity", "outState: " + outState);
    }
}
