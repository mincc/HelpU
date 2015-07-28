package com.example.chungmin.helpu;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Chung Min on 7/22/2015.
 */
public class ServiceSpinAdapter extends ArrayAdapter<Service>{

    // Your sent context
    private Context context;
    // Your custom values for the spinner (Service)
    private Service[] services;

    public ServiceSpinAdapter(Context context, int textViewResourceId, Service[] services) {
        super(context, textViewResourceId, services);
        this.context = context;
        this.services = services;
    }

    public int getCount(){
        return services.length;
    }

    public Service getItem(int position){
        return services[position];
    }

    public long getItemId(int position){
        return position;
    }

    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(services[position].getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(services[position].getName());

        return label;
    }

}
