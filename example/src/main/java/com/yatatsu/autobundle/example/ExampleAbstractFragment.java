package com.yatatsu.autobundle.example;

import android.support.v4.app.Fragment;

import com.yatatsu.autobundle.AbstractAutoBundle;
import com.yatatsu.autobundle.AutoBundleField;

@AbstractAutoBundle
public class ExampleAbstractFragment extends Fragment {

    @AutoBundleField
    String name;

}
