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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.manager.RestaurantManager;
import com.khamvongsa.victor.go4lunch.manager.UserManager;
import com.khamvongsa.victor.go4lunch.model.DetailRestaurantPOJO;
import com.khamvongsa.victor.go4lunch.ui.FactoryViewModel;
import com.khamvongsa.victor.go4lunch.ui.RestaurantViewModel;
import com.khamvongsa.victor.go4lunch.utils.MapAPIStream;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

    private static final String USERS_LIKING_LIST_FIELD = "usersLikingList";
    private static final String KEY_PLACE_ID = "placeId";
    private String mPlaceId;
    private String mPhotoReference;
    private String mRestaurantWebsiteLink;
    private Boolean mRestaurantIsLiked;
    private String mUserId;
    private List<String> mUsersLikingList = new ArrayList<>();

    private Disposable disposable;

    TextView restaurantName;
    TextView restaurantAddress;
    ImageView restaurantImage;
    TextView restaurantPhone;
    TextView restaurantLikes;
    TextView restaurantWebsite;

    public DetailsRestaurantFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = requireActivity().getIntent().getExtras();
        mPlaceId = b.getString(KEY_PLACE_ID);
        mUserId = mUserManager.getCurrentUser().getUid();
        showPlaceId(mUserId);
        mRestaurantViewModel = new ViewModelProvider(this, FactoryViewModel.getInstance()).get(RestaurantViewModel.class);
        executeHttpRequestWithRetrofit(mPlaceId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_restaurant_details, container, false);
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

        restaurantName = view.findViewById(R.id.activity_restaurant_name);
        restaurantAddress = view.findViewById(R.id.activity_restaurant_address);
        restaurantImage = view.findViewById(R.id.activity_restaurant_image);
        restaurantPhone = view.findViewById(R.id.activity_restaurant_phone);
        restaurantLikes = view.findViewById(R.id.activity_restaurant_like);
        restaurantWebsite = view.findViewById(R.id.activity_restaurant_website);


        //lv.setAdapter(adapter);
    }

    private void executeHttpRequestWithRetrofit(String placeId){
        this.disposable = MapAPIStream.streamFetchDetailRestaurant(placeId).subscribeWith(new DisposableObserver<DetailRestaurantPOJO>() {
            @Override
            public void onNext(@NotNull DetailRestaurantPOJO restaurant) {
                restaurantName.setText(restaurant.getResult().getName());
                restaurantAddress.setText(restaurant.getResult().getFormattedAddress());
                setRestaurantPhoto(restaurant);
                //TODO aide https://stackoverflow.com/questions/9703447/phone-call-on-textview-click
                restaurantPhone.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_phone),null,null);
                restaurantPhone.setText(R.string.call);
                restaurantPhone.setOnClickListener(new View.OnClickListener() {
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

                restaurantWebsite.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_website),null,null);
                restaurantWebsite.setText(R.string.website);
                restaurantWebsite.setOnClickListener(new View.OnClickListener() {
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
                restaurantLikes.setText(R.string.like);
                restaurantLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateLikeButtonOnClick();
                        mRestaurantManager.createRestaurant(placeId,restaurant.getResult().getName());

                    }
                });
                mRestaurantViewModel.getUsersLikingListId(mPlaceId);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "error from executeHttp");
            }

            @Override
            public void onComplete() { }
        });
    }

    // TODO : Help from https://stackoverflow.com/questions/54604602/how-to-obtain-simple-listt-from-android-livedatalistt
    //app:drawableTopCompat="@drawable/ic_like"

    public void updateLikeButtonOnStart(){
        mRestaurantViewModel.getAllUsersIdLikeList().observe(requireActivity(), new Observer<List<String>>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onChanged(List<String> userIdList) {
                mUsersLikingList= userIdList;
                if(userLikeRestaurant(mUsersLikingList)){
                    Log.e(TAG, "user found at start");
                    restaurantLikes.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_like),null,null);
                }else{
                    Log.e(TAG, "user not found at start");
                    restaurantLikes.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_like_empty),null,null);
                }
            }
        });
    }


    public void updateLikeButtonOnClick(){
        if(userLikeRestaurant(mUsersLikingList)){
            Log.e(TAG, "List when user found" + mUsersLikingList.toString());
            mRestaurantManager.removeUsersLiking(mPlaceId);
            mRestaurantManager.decreaseUsersLikingCount(mPlaceId);
            restaurantLikes.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_like_empty),null,null);
            Log.e(TAG, "User deleted");
        }else{
            Log.e(TAG, "List when user not found" + mUsersLikingList.toString());
            mRestaurantManager.addUsersLiking(mPlaceId);
            mRestaurantManager.addUsersLikingCount(mPlaceId);
            restaurantLikes.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_like),null,null);
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

    private void setRestaurantPhoto (DetailRestaurantPOJO restaurant){
        if (restaurant.getResult().getPhotos() != null) {
            mPhotoReference = restaurant.getResult().getPhotos().get(0).getPhotoReference();
            Log.e(TAG, mPhotoReference);
            Glide.with(restaurantImage.getContext()).load(MapAPIStream.getImageUrl(mPhotoReference)).into(restaurantImage);
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
}