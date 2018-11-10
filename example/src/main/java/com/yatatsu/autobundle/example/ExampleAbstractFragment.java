package com.yatatsu.autobundle.example;

import androidx.fragment.app.Fragment;

import com.yatatsu.autobundle.AutoBundleField;
import com.yatatsu.autobundle.AutoBundleType;

@AutoBundleType(isAbstract = true)
public class ExampleAbstractFragment extends Fragment {

    @AutoBundleField
    String name;

}
