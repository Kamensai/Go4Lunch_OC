package com.khamvongsa.victor.go4lunch.ui.views;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.model.UserStateItem;
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
public class WorkmatesAdapter extends ListAdapter<UserStateItem, WorkmatesAdapter.ViewHolder> {


    public WorkmatesAdapter() {
        super(new ListWorkmatesItemCallback());
    }

    @NonNull
    @Override
    public WorkmatesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkmatesAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmate, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mUserNameTextView;
        private final ImageView mAvatarImageView;
        private final View mItemWorkmate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserNameTextView = itemView.findViewById(R.id.item_workmate_name);
            mAvatarImageView = itemView.findViewById(R.id.item_workmate_avatar);
            mItemWorkmate = itemView.findViewById(R.id.item_workmate);
        }

        @SuppressLint("SetTextI18n")
        public void bind(UserStateItem item) {
            int notDecidedColor = ContextCompat.getColor(itemView.getContext(), R.color.dark_grey);
            if (item.getChosenRestaurantId() != null){
                mUserNameTextView.setText(item.getUsername() + " is eating at " + "("+ item.getChosenRestaurantName() +")");
            } else {
                mUserNameTextView.setText(item.getUsername() + " hasn't decided yet");
                mUserNameTextView.setTextColor(notDecidedColor);
            }

            Glide.with(mAvatarImageView)
                    .load(item.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(mAvatarImageView);

            mItemWorkmate.setOnClickListener(view -> {
                NavigationHelper.launchRestaurantActivity(mItemWorkmate.getContext(), RestaurantActivity.class, item.getChosenRestaurantId());
            });
        }
    }

    private static class ListWorkmatesItemCallback extends DiffUtil.ItemCallback<UserStateItem> {
        @Override
        public boolean areItemsTheSame(@NonNull UserStateItem oldItem, @NonNull UserStateItem newItem) {
            return oldItem.getUsername().equals(newItem.getUsername()) && oldItem.getUsername().equals(newItem.getUsername());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserStateItem oldItem, @NonNull UserStateItem newItem) {
            return oldItem.equals(newItem);
        }
    }
}
