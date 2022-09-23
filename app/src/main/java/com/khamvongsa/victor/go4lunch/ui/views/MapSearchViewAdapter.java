package com.khamvongsa.victor.go4lunch.ui.views;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.model.RestaurantLocation;

import java.util.ArrayList;
import java.util.List;

import androidx.cursoradapter.widget.CursorAdapter;

/**
 * Created by <Victor Khamvongsa> on <21/09/2022>
 */
public class MapSearchViewAdapter extends CursorAdapter implements Filterable {

    private List<RestaurantLocation> mRestaurantLocationSearchFiltered;

    private List<RestaurantLocation> mRestaurantLocationList;

    public MapSearchViewAdapter(Context context, Cursor cursor, List<RestaurantLocation> restaurantLocations) {
        super(context, cursor, false);
        this.mRestaurantLocationList = restaurantLocations;
        this.mRestaurantLocationSearchFiltered = restaurantLocations;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.item_search_view, parent, false);
        holder.textView = (TextView) v.findViewById(R.id.item_search_name);

        v.setTag(holder);
        return v;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.textView.setText(mRestaurantLocationSearchFiltered.get(cursor.getPosition()).getName());
    }

    public class ViewHolder {
        public TextView textView;
    }

    @Override
    public int getCount() {
        return mRestaurantLocationSearchFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < mRestaurantLocationSearchFiltered.size()) {
            return mRestaurantLocationSearchFiltered.get(position);
        } else {
            return null;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null){
                    String character = constraint.toString();
                    if (character.isEmpty()){
                        mRestaurantLocationSearchFiltered = mRestaurantLocationList;
                    } else {
                        List<RestaurantLocation> filterList = new ArrayList<>();
                        for (RestaurantLocation restaurantLocation : mRestaurantLocationList ){
                            if (restaurantLocation.getName().toLowerCase().contains(character.toLowerCase())){
                                filterList.add(restaurantLocation);
                            }
                        }
                        mRestaurantLocationSearchFiltered = filterList;
                    }
                } else {
                    mRestaurantLocationSearchFiltered = mRestaurantLocationList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mRestaurantLocationSearchFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mRestaurantLocationSearchFiltered = (ArrayList<RestaurantLocation>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
