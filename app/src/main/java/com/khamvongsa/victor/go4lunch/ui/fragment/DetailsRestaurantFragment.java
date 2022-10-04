package com.khamvongsa.victor.go4lunch.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.manager.RestaurantManager;
import com.khamvongsa.victor.go4lunch.manager.UserManager;
import com.khamvongsa.victor.go4lunch.model.DetailRestaurantPOJO;
import com.khamvongsa.victor.go4lunch.model.UserStateItem;
import com.khamvongsa.victor.go4lunch.ui.FactoryViewModel;
import com.khamvongsa.victor.go4lunch.ui.RestaurantViewModel;
import com.khamvongsa.victor.go4lunch.ui.UserViewModel;
import com.khamvongsa.victor.go4lunch.ui.views.DetailsRestaurantAdapter;
import com.khamvongsa.victor.go4lunch.utils.MapAPIStream;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by <Victor Khamvongsa> on <24/06/2022>
 */
public class DetailsRestaurantFragment extends Fragment {

    private static final String TAG = DetailsRestaurantFragment.class.getSimpleName();

    private UserManager mUserManager = UserManager.getInstance();
    private RestaurantManager mRestaurantManager = RestaurantManager.getInstance();
    private RestaurantViewModel mRestaurantViewModel;
    private UserViewModel mUserViewModel;
    private DetailsRestaurantAdapter mDetailsRestaurantAdapter;

    private static final String USERS_LIKING_LIST_FIELD = "usersLikingList";
    private static final String KEY_PLACE_ID = "placeId";
    private String mPlaceId = null;
    private String mPlaceName = null;
    private String mPhotoReference;
    private String mUserId;
    private List<String> mUsersLikingList = new ArrayList<>();
    private List<UserStateItem> mUsersEatingList = new ArrayList<>();

    private Disposable disposable;

    TextView mEmptyIdRestaurant;
    ConstraintLayout mCompleteIdRestaurant;

    View mRootView;

    TextView mRestaurantName;
    TextView mRestaurantAddress;
    ImageView mRestaurantImage;
    TextView mRestaurantPhone;
    TextView mRestaurantLikes;
    TextView mRestaurantWebsite;
    ImageButton mRestaurantChosenButton;

    public DetailsRestaurantFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mUserId = mUserManager.getCurrentUser().getUid();
        Bundle b = requireActivity().getIntent().getExtras();
        if (b != null && b.getString(KEY_PLACE_ID) != null) {
            mPlaceId = b.getString(KEY_PLACE_ID);
            //executeHttpRequestWithRetrofit(mPlaceId);
        }
        mUserViewModel = new ViewModelProvider(this, FactoryViewModel.getInstance()).get(UserViewModel.class);
        mRestaurantViewModel = new ViewModelProvider(this, FactoryViewModel.getInstance()).get(RestaurantViewModel.class);
        mDetailsRestaurantAdapter = new DetailsRestaurantAdapter();
    }

    private void updateViewFragment(View view,String placeId) {
        if (placeId == null) {
            mEmptyIdRestaurant =  view.findViewById(R.id.activity_restaurant_empty_id_restaurant);
            mEmptyIdRestaurant.setVisibility(View.VISIBLE);
            mCompleteIdRestaurant.setVisibility(View.GONE);
            mCompleteIdRestaurant.requestLayout();
        }else {
            mCompleteIdRestaurant.setVisibility(View.VISIBLE);
            showPlaceId(placeId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_restaurant_details, container, false);
        RecyclerView recyclerView = mRootView.findViewById(R.id.activity_restaurant_list_workmates);
        recyclerView.setAdapter(mDetailsRestaurantAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //getUsersEatingList();
        return mRootView;
    }

    public void showPlaceId (String placeId){
        Toast.makeText(getContext(), placeId + " Is  your placeId!", Toast.LENGTH_SHORT).show();
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    // TODO : Mettre tous les éléments du layout et le recyclerview
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCompleteIdRestaurant = view.findViewById(R.id.activity_restaurant_complete_id_restaurant);
        updateViewFragment(view, mPlaceId);

        mRestaurantName = view.findViewById(R.id.activity_restaurant_name);
        mRestaurantAddress = view.findViewById(R.id.activity_restaurant_address);
        mRestaurantImage = view.findViewById(R.id.activity_restaurant_image);
        mRestaurantPhone = view.findViewById(R.id.activity_restaurant_phone);
        mRestaurantLikes = view.findViewById(R.id.activity_restaurant_like);
        mRestaurantWebsite = view.findViewById(R.id.activity_restaurant_website);
        mRestaurantChosenButton = view.findViewById(R.id.activity_restaurant_chosen_ActionButton);

    }

    private void getUsersEatingList() {
        mRestaurantViewModel.getAllUsersEatingList().observe(requireActivity(), mDetailsRestaurantAdapter::submitList);
    }

    private void executeHttpRequestWithRetrofit(String placeId){
        this.disposable = MapAPIStream.streamFetchDetailRestaurant(placeId).subscribeWith(new DisposableObserver<DetailRestaurantPOJO>() {
            @Override
            public void onNext(@NotNull DetailRestaurantPOJO restaurant) {
                mPlaceName = restaurant.getResult().getName();
                mRestaurantName.setText(restaurant.getResult().getName());
                mRestaurantAddress.setText(restaurant.getResult().getFormattedAddress());
                setRestaurantPhoto(restaurant);
                //TODO aide https://stackoverflow.com/questions/9703447/phone-call-on-textview-click
                mRestaurantPhone.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_phone),null,null);
                mRestaurantPhone.setText(R.string.call);
                mRestaurantPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (restaurant.getResult().getFormattedPhoneNumber() != null) {
                            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) ==
                                    PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + restaurant.getResult().getFormattedPhoneNumber()));
                                startActivity(callIntent);
                                Log.i("Finished making a call", "");
                            } else {
                                Toast.makeText(requireContext(), "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
                                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);
                            }
                        }
                        else {
                            Toast.makeText(requireContext(), "This place has no phone number. Sorry.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mRestaurantWebsite.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_website),null,null);
                mRestaurantWebsite.setText(R.string.website);
                mRestaurantWebsite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (restaurant.getResult().getWebsite() != null){
                            String url = restaurant.getResult().getWebsite();
                            Intent web = new Intent(Intent.ACTION_VIEW);
                            web.setData(Uri.parse(url));
                            startActivity(web);
                        }
                        else {
                            Toast.makeText(requireContext(), "This place has no website. Sorry.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                updateLikeButtonOnStart();
                mRestaurantLikes.setText(R.string.like);
                mRestaurantLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateLikeButtonOnClick();
                        mRestaurantManager.createRestaurant(mPlaceId,restaurant.getResult().getName());
                    }
                });
                mRestaurantViewModel.getUsersLikingIdList(mPlaceId);

                updateEatButtonOnStart();
                mRestaurantChosenButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateEatButtonOnClick();
                        mRestaurantManager.createChosenRestaurant(mPlaceId, restaurant.getResult().getName());
                    }
                });
                mRestaurantViewModel.getUsersEatingList(mPlaceId);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "error from executeHttp");
            }

            @Override
            public void onComplete() { }
        });
    }



    // USER LIKE RESTAURANT

    // TODO : Help from https://stackoverflow.com/questions/54604602/how-to-obtain-simple-listt-from-android-livedatalistt

    public void updateLikeButtonOnStart(){
        mRestaurantViewModel.getAllUsersLikeIdList().observe(requireActivity(), new Observer<List<String>>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onChanged(List<String> userIdList) {
                mUsersLikingList= userIdList;
                if(userLikeRestaurant(mUsersLikingList)){
                    Log.e(TAG, "user found at start");
                    mRestaurantLikes.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_like),null,null);
                }else{
                    Log.e(TAG, "user not found at start");
                    mRestaurantLikes.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_like_empty),null,null);
                }
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void updateLikeButtonOnClick(){
        if(userLikeRestaurant(mUsersLikingList)){
            Log.e(TAG, "List when user found" + mUsersLikingList.toString());
            mRestaurantManager.removeUsersLiking(mPlaceId);
            mRestaurantManager.decreaseUsersLikingCount(mPlaceId);
            mRestaurantLikes.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_like_empty),null,null);
            Log.e(TAG, "User deleted");
        }else{
            Log.e(TAG, "List when user not found" + mUsersLikingList.toString());
            mRestaurantManager.addUsersLiking(mPlaceId);
            mRestaurantManager.addUsersLikingCount(mPlaceId);
            mRestaurantLikes.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_like),null,null);
            Log.e(TAG, "Adding user");
        }
    }

    public Boolean userLikeRestaurant(List<String> listUsersLikingRestaurant){
        boolean userLike = false;
        if (listUsersLikingRestaurant != null && listUsersLikingRestaurant.size() > 0){
            for (String userId : listUsersLikingRestaurant){
                if (userId.trim().contains(mUserId)){
                    userLike = true;
                    break;
                }
            }
        }
        return userLike;
    }

    // USER EAT TO RESTAURANT

    public void updateEatButtonOnStart(){
        mRestaurantViewModel.getAllUsersEatingList().observe(requireActivity(), new Observer<List<UserStateItem>>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onChanged(List<UserStateItem> userEatingList) {
                mUsersEatingList= userEatingList;
                if(userEatAtRestaurant(mUsersEatingList)){
                    Log.e(TAG, "userEating found at start " + mUsersEatingList.get(0).getUsername());
                    Log.e(TAG, "List when userEating found at start " + mUsersEatingList.toString());
                    mRestaurantChosenButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                }else{
                    Log.e(TAG, "userEating not found at start");
                    Log.e(TAG, "List when userEating not found at start " + mUsersEatingList.toString());
                    mRestaurantChosenButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_empty));
                }
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void updateEatButtonOnClick(){
        if(userEatAtRestaurant(mUsersEatingList)){
            Log.e(TAG, "List when userEating found OnClick " + mUsersEatingList.toString());
            mRestaurantManager.removeUsersEating(mPlaceId);
            mRestaurantManager.decreaseUsersEatingCount(mPlaceId);
            mUserManager.deleteChosenRestaurant();
            mRestaurantChosenButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_empty));
        }else{
            Log.e(TAG, "List when userEating not found OnClick " + mUsersEatingList.toString());
            mRestaurantManager.addUsersEating(mPlaceId);
            mRestaurantManager.addUsersEatingCount(mPlaceId);
            mUserManager.updateChosenRestaurantIdAndName(mPlaceId,mPlaceName);
            mRestaurantChosenButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
        }
    }

    public void updateOtherRestaurantEatingList(){
        if(userEatAtRestaurant(mUsersEatingList)){
            mRestaurantManager.deleteUserEatingAtOtherRestaurant(mPlaceId);
        }
    }

    public Boolean userEatAtRestaurant(List<UserStateItem> listUsersEatingRestaurant){
        boolean userLike = false;
        if (listUsersEatingRestaurant != null && listUsersEatingRestaurant.size() > 0){
            for (UserStateItem userEating : listUsersEatingRestaurant){
                if (userEating.getUid().trim().contains(mUserId)){
                    userLike = true;
                    break;
                }
            }
        }
        return userLike;
    }

    // PHOTO
    private void setRestaurantPhoto (DetailRestaurantPOJO restaurant){
        if (restaurant.getResult().getPhotos() != null) {
            mPhotoReference = restaurant.getResult().getPhotos().get(0).getPhotoReference();
            Log.e(TAG, mPhotoReference);
            Glide.with(mRestaurantImage.getContext()).load(MapAPIStream.getImageUrl(mPhotoReference)).into(mRestaurantImage);
        }
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        updateOtherRestaurantEatingList();
        mDetailsRestaurantAdapter.notifyDataSetChanged();
    }
}