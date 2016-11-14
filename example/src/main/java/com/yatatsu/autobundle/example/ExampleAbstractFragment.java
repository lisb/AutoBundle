package com.yatatsu.autobundle.example;

import android.support.v4.app.Fragment;

import com.yatatsu.autobundle.AutoBundleField;
import com.yatatsu.autobundle.AutoBundleType;

@AutoBundleType(isAbstract = true)
public class ExampleAbstractFragment extends Fragment {

    @AutoBundleField
    String name;

}
