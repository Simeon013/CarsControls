package com.example.carscontrols.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.carscontrols.R;

import java.util.ArrayList;

public class listViewAdapter extends BaseAdapter {
    Activity context;
    ArrayList<Model>arrayList;

    public listViewAdapter(Activity context, ArrayList<Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public listViewAdapter(Context context, int[] img, int[] title, int[] subtitle) {
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, @NonNull ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_row, viewGroup, false);
        }
        TextView titleText = view.findViewById(R.id.title);
        TextView subTitleText = view.findViewById(R.id.subtitle);
        ImageView imageView = view.findViewById(R.id.img);
        
        titleText.setText(arrayList.get(position).getName());
        subTitleText.setText(arrayList.get(position).getSubtitle());
        imageView.setImageResource(arrayList.get(position).getFlag());
        
        return view;
    }

    //    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return null;
//    }
}
