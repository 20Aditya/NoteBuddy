package com.example.aditya.notebuddy;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aditya on 6/4/18.
 */

public class DetailsListAdapter extends ArrayAdapter<Details> {

    private static final String LOG_TAG = DetailsListAdapter.class.getSimpleName();



    public DetailsListAdapter(Activity context, ArrayList<Details> details){

        super(context,0,details);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View listitemview = convertView;
        if(listitemview == null){
            listitemview = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Details currentdetail = getItem(position);

        TextView nametextview = (TextView)listitemview.findViewById(R.id.name);

        nametextview.setText(currentdetail.getName());


        ImageView iconview = (ImageView)listitemview.findViewById(R.id.list_item_icon);


        iconview.setImageResource(currentdetail.getImageresourceid());


        return listitemview;

    }
}
