package com.example.carscontrols.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.carscontrols.R;

public class HistoryActivity extends Activity {
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;

    int [] img = {R.array.scan_serial_bg};
    String [] title = {"India", "China", "australia", "Portugle", "America", "NewZealand"};
    String [] subtitle = {"India", "China", "australia", "Portugle", "America", "NewZealand"};
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        //listView = findViewById(R.id.simpleListView);
        //listViewAdapter adapter = new listViewAdapter(getApplicationContext(), img, title, subtitle);
        //listView.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN,Color.YELLOW,Color.RED);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent i = new Intent(getApplicationContext(),
                        HistoryActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

/*    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
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
    }*/
}