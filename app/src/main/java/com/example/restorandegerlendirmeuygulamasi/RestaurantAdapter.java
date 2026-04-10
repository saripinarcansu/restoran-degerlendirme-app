package com.example.restorandegerlendirmeuygulamasi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RestaurantAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] names;
    private final String[] descriptions;
    private final int[] images;

    public RestaurantAdapter(@NonNull Context context, String[] names, String[] descriptions, int[] images) {
        super(context, R.layout.restaurant_item, names);
        this.context = context;
        this.names = names;
        this.descriptions = descriptions;
        this.images = images;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.restaurant_item, parent, false);
            holder = new ViewHolder();
            holder.nameText = convertView.findViewById(R.id.textViewName);
            holder.descText = convertView.findViewById(R.id.textViewDescription);
            holder.imageView = convertView.findViewById(R.id.imageViewRestaurant);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameText.setText(names[position]);
        holder.descText.setText(descriptions[position]);
        holder.imageView.setImageResource(images[position]);

        return convertView;
    }

    static class ViewHolder {
        TextView nameText;
        TextView descText;
        ImageView imageView;
    }
}
