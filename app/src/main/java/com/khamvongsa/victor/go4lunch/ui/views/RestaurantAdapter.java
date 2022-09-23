package com.khamvongsa.victor.go4lunch.ui.views;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.model.RestaurantStateItem;
import com.khamvongsa.victor.go4lunch.ui.RestaurantActivity;
import com.khamvongsa.victor.go4lunch.ui.helper.NavigationHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by <Victor Khamvongsa> on <24/06/2022>
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> implements Filterable {

    private static final String TAG = RestaurantAdapter.class.getSimpleName();

    private List<RestaurantStateItem> mRestaurantStateItemListSearchFiltered;

    private List<RestaurantStateItem> mRestaurantStateItemList;

    public RestaurantAdapter(List<RestaurantStateItem> restaurantStateItemList) {
        this.mRestaurantStateItemList = restaurantStateItemList;
        this.mRestaurantStateItemListSearchFiltered = restaurantStateItemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
        /*
        String name =  mRestaurantStateItemListSearchFiltered.get(position).getName();
        String address = mRestaurantStateItemListSearchFiltered.get(position).getAddress();

        int likingCount = mRestaurantStateItemListSearchFiltered.get(position).getUsersLikingCount();
        int eatingCount = mRestaurantStateItemListSearchFiltered.get(position).getUsersEatingCount();
        String eatingCountString = String.valueOf(eatingCount);
        String urlPicture = mRestaurantStateItemListSearchFiltered.get(position).getUrlPicture();
        Boolean openNow = mRestaurantStateItemListSearchFiltered.get(position).getOpenNow();
        String openUntil = mRestaurantStateItemListSearchFiltered.get(position).getOpenUntil();
        int distance = mRestaurantStateItemListSearchFiltered.get(position).getDistance();

        int nameColor = holder.mItemRestaurant.getResources().getColor(R.color.black);

        // Name
        holder.mRestaurantNameTextView.setText(name);
        holder.mRestaurantNameTextView.setTextColor(nameColor);

        //Address
            holder.mRestaurantAddressTextView.setText(address);

        //IS LIKED
        if (likingCount > 2 ){
            Log.e(TAG, holder.mRestaurantNameTextView.getText().toString());
            Log.e(TAG, name);
            System.out.println(TAG + likingCount);
            holder.mRestaurantFirstStarImageView.setImageResource(R.drawable.ic_like_yellow);
            holder.mRestaurantSecondStarImageView.setImageResource(R.drawable.ic_like_yellow);
            holder.mRestaurantThirdStarImageView.setImageResource(R.drawable.ic_like_yellow);
            holder.mRestaurantFirstStarImageView.setVisibility(View.VISIBLE);
            holder.mRestaurantSecondStarImageView.setVisibility(View.VISIBLE);
            holder.mRestaurantThirdStarImageView.setVisibility(View.VISIBLE);
        } else if (likingCount > 1 ){
            holder.mRestaurantFirstStarImageView.setImageResource(R.drawable.ic_like_yellow);
            holder.mRestaurantSecondStarImageView.setImageResource(R.drawable.ic_like_yellow);
            holder.mRestaurantFirstStarImageView.setVisibility(View.VISIBLE);
            holder.mRestaurantSecondStarImageView.setVisibility(View.VISIBLE);
            holder.mRestaurantThirdStarImageView.setVisibility(View.INVISIBLE);
        } else if (likingCount > 0 ) {
            holder.mRestaurantFirstStarImageView.setImageResource(R.drawable.ic_like_yellow);
            holder.mRestaurantFirstStarImageView.setVisibility(View.VISIBLE);
            holder.mRestaurantSecondStarImageView.setVisibility(View.INVISIBLE);
            holder.mRestaurantThirdStarImageView.setVisibility(View.INVISIBLE);
        } else {
            holder.mRestaurantFirstStarImageView.setVisibility(View.INVISIBLE);
            holder.mRestaurantSecondStarImageView.setVisibility(View.INVISIBLE);
            holder.mRestaurantThirdStarImageView.setVisibility(View.INVISIBLE);
        }

        //IS EATING
        if (eatingCount > 0){
            System.out.println(TAG + eatingCount);
            holder.mRestaurantUserIconImageView.setImageResource(R.drawable.ic_user);
            holder.mRestaurantUserIconImageView.setVisibility(View.VISIBLE);
            holder.mRestaurantNumberUserEatingTextView.setText(eatingCountString);
            holder.mRestaurantNumberUserEatingTextView.setVisibility(View.VISIBLE);
        } else {
            holder.mRestaurantUserIconImageView.setVisibility(View.INVISIBLE);
            holder.mRestaurantNumberUserEatingTextView.setVisibility(View.INVISIBLE);
        }
        if (distance > 0){
            String sDistance = String.valueOf(distance);
            holder.mRestaurantDistanceTextView.setText(sDistance + "m");
            holder.mRestaurantDistanceTextView.setVisibility(View.VISIBLE);
        } else {
            holder.mRestaurantDistanceTextView.setVisibility(View.INVISIBLE);
        }

        // IS OPEN
        if (openNow != null && openNow) {
            holder.mRestaurantOpeningHourTextView.setText("Open until " + openUntil);
        } else if (openNow != null && !openNow) {
            holder.mRestaurantOpeningHourTextView.setText("restaurant is closed");
        }

        //URLPICTURE
        if (urlPicture.equals("null")){
            Glide.with(holder.mRestaurantAvatarImageView.getContext())
                    .load(R.drawable.ic_no_photo)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.mRestaurantAvatarImageView);
            } else {
                Glide.with(holder.mRestaurantAvatarImageView.getContext())
                        .load(urlPicture)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.mRestaurantAvatarImageView);
            }
        holder.mItemRestaurant.setOnClickListener(view -> {
            NavigationHelper.launchRestaurantActivity(holder.mItemRestaurant.getContext(), RestaurantActivity.class,  mRestaurantStateItemListSearchFiltered.get(position).getRestaurantId());
        });

         */
    }


    @Override
    public int getItemCount() {
        return mRestaurantStateItemListSearchFiltered.size();
    }

    public RestaurantStateItem getItem(int position) {
        if (position < mRestaurantStateItemListSearchFiltered.size()) {
            return mRestaurantStateItemListSearchFiltered.get(position);
        } else {
            return null;
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String character = constraint.toString();
                if (character.isEmpty()){
                    mRestaurantStateItemListSearchFiltered = mRestaurantStateItemList;
                } else {
                    List<RestaurantStateItem> filterList = new ArrayList<>();
                    for (RestaurantStateItem restaurantStateItem : mRestaurantStateItemList ){
                        if (restaurantStateItem.getName().toLowerCase().contains(character.toLowerCase())){
                            filterList.add(restaurantStateItem);
                        }
                    }
                    mRestaurantStateItemListSearchFiltered = filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mRestaurantStateItemListSearchFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mRestaurantStateItemListSearchFiltered = (ArrayList<RestaurantStateItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    // TODO : Compléter les ItemView ( distance, ratings, openingHours, nombre d'utilisateurs qui y mangent)

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mRestaurantNameTextView;
        private final TextView mRestaurantAddressTextView;
        private final TextView mRestaurantDistanceTextView;
        private final TextView mRestaurantNumberUserEatingTextView;
        private final TextView mRestaurantOpeningHourTextView;
        private final ImageView mRestaurantAvatarImageView;
        private final ImageView mRestaurantUserIconImageView;
        private final ImageView mRestaurantFirstStarImageView;
        private final ImageView mRestaurantSecondStarImageView;
        private final ImageView mRestaurantThirdStarImageView;
        private final View mItemRestaurant;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRestaurantNameTextView = itemView.findViewById(R.id.item_restaurant_name);
            mRestaurantAddressTextView = itemView.findViewById(R.id.item_restaurant_address);
            mRestaurantDistanceTextView = itemView.findViewById(R.id.item_restaurant_distance);
            mRestaurantNumberUserEatingTextView = itemView.findViewById(R.id.item_restaurant_user_eating_number);
            mRestaurantOpeningHourTextView = itemView.findViewById(R.id.item_restaurant_opening);
            mRestaurantAvatarImageView = itemView.findViewById(R.id.item_restaurant_avatar);
            mRestaurantUserIconImageView = itemView.findViewById(R.id.item_restaurant_user_icon);
            mRestaurantFirstStarImageView = itemView.findViewById(R.id.item_restaurant_liked_1);
            mRestaurantSecondStarImageView = itemView.findViewById(R.id.item_restaurant_liked_2);
            mRestaurantThirdStarImageView = itemView.findViewById(R.id.item_restaurant_liked_3);

            mItemRestaurant = itemView.findViewById(R.id.item_restaurant);
        }

        @SuppressLint("SetTextI18n")
        public void bind(RestaurantStateItem item) {
            int likingCount = item.getUsersLikingCount();
            int eatingCount = item.getUsersEatingCount();
            String eatingCountString = String.valueOf(eatingCount);
            System.out.println(TAG + " UserEatingCount : " + eatingCount);
            int nameColor = ContextCompat.getColor(itemView.getContext(), R.color.black);
            mRestaurantNameTextView.setText(item.getName());
            mRestaurantNameTextView.setTextColor(nameColor);
            mRestaurantAddressTextView.setText(item.getAddress());

            //IS LIKED
            if (likingCount > 2 ){
                Log.e(TAG, mRestaurantNameTextView.getText().toString());
                Log.e(TAG, item.getName());
                System.out.println(TAG + item.getUsersLikingCount());
                mRestaurantFirstStarImageView.setImageResource(R.drawable.ic_like_yellow);
                mRestaurantSecondStarImageView.setImageResource(R.drawable.ic_like_yellow);
                mRestaurantThirdStarImageView.setImageResource(R.drawable.ic_like_yellow);
                mRestaurantFirstStarImageView.setVisibility(View.VISIBLE);
                mRestaurantSecondStarImageView.setVisibility(View.VISIBLE);
                mRestaurantThirdStarImageView.setVisibility(View.VISIBLE);
            } else if (likingCount > 1 ){
                mRestaurantFirstStarImageView.setImageResource(R.drawable.ic_like_yellow);
                mRestaurantSecondStarImageView.setImageResource(R.drawable.ic_like_yellow);
                mRestaurantFirstStarImageView.setVisibility(View.VISIBLE);
                mRestaurantSecondStarImageView.setVisibility(View.VISIBLE);
                mRestaurantThirdStarImageView.setVisibility(View.INVISIBLE);

            } else if (likingCount > 0 ) {
                mRestaurantFirstStarImageView.setImageResource(R.drawable.ic_like_yellow);
                mRestaurantFirstStarImageView.setVisibility(View.VISIBLE);
                mRestaurantSecondStarImageView.setVisibility(View.INVISIBLE);
                mRestaurantThirdStarImageView.setVisibility(View.INVISIBLE);

            } else {
                mRestaurantFirstStarImageView.setVisibility(View.INVISIBLE);
                mRestaurantSecondStarImageView.setVisibility(View.INVISIBLE);
                mRestaurantThirdStarImageView.setVisibility(View.INVISIBLE);
            }

            //IS EATING
            if (eatingCount > 0){
                System.out.println(TAG + eatingCount);
                mRestaurantUserIconImageView.setImageResource(R.drawable.ic_user);
                mRestaurantUserIconImageView.setVisibility(View.VISIBLE);
                mRestaurantNumberUserEatingTextView.setText(eatingCountString);
                mRestaurantNumberUserEatingTextView.setVisibility(View.VISIBLE);
            } else {
                mRestaurantUserIconImageView.setVisibility(View.INVISIBLE);
                mRestaurantNumberUserEatingTextView.setVisibility(View.INVISIBLE);
            }

            if (item.getDistance() > 0){
                String distance = String.valueOf(item.getDistance());
                mRestaurantDistanceTextView.setText(distance + "m");
                mRestaurantDistanceTextView.setVisibility(View.VISIBLE);
            } else {
                mRestaurantDistanceTextView.setVisibility(View.INVISIBLE);
            }

            // IS OPEN
            if (item.getOpenNow() != null && item.getOpenNow()) {
                mRestaurantOpeningHourTextView.setText("Open until " + item.getOpenUntil());
            } else if (item.getOpenNow() != null && !item.getOpenNow()) {
                mRestaurantOpeningHourTextView.setText("restaurant is closed");
            }

            //URLPICTURE
            if (item.getUrlPicture().equals("null")){
                Glide.with(mRestaurantAvatarImageView.getContext())
                        .load(R.drawable.ic_no_photo)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mRestaurantAvatarImageView);
            } else {
                Glide.with(mRestaurantAvatarImageView.getContext())
                        .load(item.getUrlPicture())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mRestaurantAvatarImageView);
            }

            mItemRestaurant.setOnClickListener(view -> {
                NavigationHelper.launchRestaurantActivity(mItemRestaurant.getContext(), RestaurantActivity.class, item.getRestaurantId());
            });
        }


    }

    private static class ListRestaurantItemCallback extends DiffUtil.ItemCallback<RestaurantStateItem> {
        @Override
        public boolean areItemsTheSame(@NonNull RestaurantStateItem oldItem, @NonNull RestaurantStateItem newItem) {
            return oldItem.getName().equals(newItem.getName()) && oldItem.getName().equals(newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull RestaurantStateItem oldItem, @NonNull RestaurantStateItem newItem) {
            return oldItem.equals(newItem);
        }
    }
}
