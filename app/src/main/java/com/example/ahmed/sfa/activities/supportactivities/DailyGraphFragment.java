package com.example.ahmed.sfa.activities.supportactivities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.sfa.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;

/**
 * Created by Ahmed on 4/3/2017.
 */

public class DailyGraphFragment extends Fragment {
    GraphView gv;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        View view =layoutInflater.inflate(R.layout.graph,container,false);
        gv = (GraphView)view.findViewById(R.id.weeklytarget);

        Viewport viewport = gv.getViewport();
        viewport
    }
}
