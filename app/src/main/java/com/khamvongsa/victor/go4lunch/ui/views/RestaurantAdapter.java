package com.khamvongsa.victor.go4lunch.ui.views;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.model.RestaurantStateItem;
import com.khamvongsa.victor.go4lunch.ui.RestaurantActivity;
import com.khamvongsa.victor.go4lunch.ui.helper.NavigationHelper;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by <Victor Khamvongsa> on <24/06/2022>
 */
public class RestaurantAdapter extends ListAdapter<RestaurantStateItem, RestaurantAdapter.ViewHolder> {

    private static final String TAG = RestaurantAdapter.class.getSimpleName();


    public RestaurantAdapter() {
        super(new ListRestaurantItemCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
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
            } else if (likingCount > 0 ) {
                mRestaurantFirstStarImageView.setImageResource(R.drawable.ic_like_yellow);
                mRestaurantFirstStarImageView.setVisibility(View.VISIBLE);
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
    /*
    void updateProjects(@NonNull final List<Project> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }

     */

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
