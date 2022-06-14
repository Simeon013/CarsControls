package com.example.carscontrols.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.carscontrols.R;
import com.example.carscontrols.helper.listViewAdapter;

public class HistoryActivity extends AppCompatActivity {
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;

    int [] img = {R.array.scan_serial_bg};
    int [] title = {R.array.title_list};
    int [] subtitle = {R.array.subtitle_list};
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_history);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.activity_history, container, false);

        listView = v.findViewById(R.id.listView);
        listViewAdapter adapter = new listViewAdapter(v.getContext(), img, title, subtitle);
        listView.setAdapter(adapter);

        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN,Color.YELLOW,Color.RED);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        return v;
    }
}