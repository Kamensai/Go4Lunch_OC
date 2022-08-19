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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by <Victor Khamvongsa> on <24/06/2022>
 */
public class DetailsRestaurantAdapter extends ListAdapter<UserStateItem, DetailsRestaurantAdapter.ViewHolder> {


    public DetailsRestaurantAdapter() {
        super(new ListWorkmatesItemCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmate, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView userNameTextView;
        private final ImageView avatarImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.item_workmate_name);
            avatarImageView= itemView.findViewById(R.id.item_workmate_avatar);
        }

        @SuppressLint("SetTextI18n")
        public void bind(UserStateItem item) {
            userNameTextView.setText(item.getUsername() + " is joining");
            Glide.with(avatarImageView)
                    .load(item.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImageView);
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
