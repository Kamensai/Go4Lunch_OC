package com.khamvongsa.victor.go4lunch.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.khamvongsa.victor.go4lunch.BuildConfig;
import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.model.DetailRestaurantPOJO;
import com.khamvongsa.victor.go4lunch.model.NearbyRestaurantPOJO;
import com.khamvongsa.victor.go4lunch.model.Restaurant;
import com.khamvongsa.victor.go4lunch.model.RestaurantEatingItem;
import com.khamvongsa.victor.go4lunch.model.RestaurantLikedItem;
import com.khamvongsa.victor.go4lunch.model.RestaurantLocation;
import com.khamvongsa.victor.go4lunch.ui.RestaurantActivity;
import com.khamvongsa.victor.go4lunch.ui.RestaurantViewModel;
import com.khamvongsa.victor.go4lunch.ui.helper.LocaleHelper;
import com.khamvongsa.victor.go4lunch.ui.helper.NavigationHelper;
import com.khamvongsa.victor.go4lunch.ui.views.MapSearchViewAdapter;
import com.khamvongsa.victor.go4lunch.utils.MapAPIStream;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by <Victor Khamvongsa> on <18/11/2021>
 */
public class MapViewFragment extends Fragment{

    private static final String TAG = MapViewFragment.class.getSimpleName();

    private GoogleMap mMap;

    private String[] columns = new String[] { "_id", "text" };

    //Menu
    private MenuItem mMenuItem;
    private SearchView mSearchView;
    private MapSearchViewAdapter mMapSearchViewAdapter;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LatLng defaultLocation = new LatLng(45.1553, 5.3203);
    private static final int DEFAULT_ZOOM = 16;
    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private Location lastKnownLocation;
    private CameraPosition cameraPosition;

    // Keys for storing activity state.
    // [START maps_current_place_state_keys]
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_PLACE_ID = "placeId";

    // [END maps_current_place_state_keys]

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;

    //FOR DATA
    private Disposable mDisposableRestaurantLocation;

    private Disposable mDisposableRestaurantDetails;

    private RestaurantViewModel mRestaurantViewModel;

    private List<Restaurant> mRestaurantList;

    private List<RestaurantLikedItem> mRestaurantLikedList = new ArrayList<>();

    private List<RestaurantEatingItem> mRestaurantEatingList = new ArrayList<>();

    private List<RestaurantLocation> mRestaurantLocationList;

    private int mCountUsersLiking;

    private int mCountUsersEating;

    public MapViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        //Get Data from ViewModels
        mRestaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
        /*
        mRestaurantViewModel.getRestaurantsLikedList();
        getAllRestaurantsLikedList();
        mRestaurantViewModel.getRestaurantsEatingList();
        getAllRestaurantsEatingList();

         */

        View view = inflater.inflate(R.layout.fragment_map_view, container, false);

        // Construct a PlacesClient
        //Places.initialize(this.requireContext(), BuildConfig.MAPS_API_KEY);
        //placesClient = Places.createClient(this.requireActivity());

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);
        assert mapFragment != null;
        /*
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                getLocationPermission();
                getDeviceLocation();
                updateLocationUI();

                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        getDeviceLocation();
                        return true;
                    }
                });

                // TODO : A récupérer les données et les ransférer à Restaurant Activity
                //  https://stackoverflow.com/questions/42950845/sending-data-through-infowindow-to-another-activity-android
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(@NotNull Marker marker) {
                        String placeId = (String) marker.getTag();
                        NavigationHelper.launchRestaurantActivity(getContext(), RestaurantActivity.class, placeId);
                    }
                });
            }
        });

         */
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }


    // Googlemaps
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult..
         */

        if (ContextCompat.checkSelfPermission(this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }

    @SuppressLint("MissingPermission")
    public void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                @SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener( this.requireActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            // TODO : Afficher les restaurants proches. Help : https://stackoverflow.com/questions/50090135/pass-data-fragment-to-fragment-in-the-same-activity
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                //executeHttpRequestWithRetrofit(lastKnownLocation);
                            }

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /*
    private void showNearPlaces() {
        if (mMap == null) {
            return;
        }

        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener (new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        likelyPlaceNames = new String[count];
                        likelyPlaceAddresses = new String[count];
                        likelyPlaceAttributions = new List[count];
                        likelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            LatLng markerLatLng = likelyPlaceLatLngs[i];
                            String markerSnippet = likelyPlaceAddresses[i];
                            if (likelyPlaceAttributions[i] != null) {
                                markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[i];
                            }

                            mMap.addMarker(new MarkerOptions()
                                    .title(likelyPlaceNames[i])
                                    .position(markerLatLng)
                                    .snippet(markerSnippet));

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }
                    }
                    else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,16));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

     */

    // -------------------
    // HTTP (RxJAVA)
    // -------------------

    // https://stackoverflow.com/questions/42365658/custom-marker-in-google-maps-in-android-with-vector-asset-icon/45564994#45564994
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorResId) {
        Drawable background = ContextCompat.getDrawable(context, vectorResId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_dining_place);
        vectorDrawable.setBounds(20, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    // ToResize an IconFromBitmap
    public Bitmap resizeBitmap(String drawableName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(drawableName, "drawable", getActivity().getPackageName()));
        Canvas canvas = new Canvas(imageBitmap);
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }
    //         Icon by <a href="https://freeicons.io/profile/3">freeicons</a> on <a href="https://freeicons.io">freeicons.io</a>
    private void executeHttpRequestWithRetrofit(Location lastKnownLocation){
        String location = lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude();
        this.mDisposableRestaurantLocation = MapAPIStream.streamFetchNearbyRestaurant(location).subscribeWith(new DisposableObserver<NearbyRestaurantPOJO>() {
            @Override
            public void onNext(@NotNull NearbyRestaurantPOJO restaurants) {
                // 6 - Update RecyclerView after getting results from Googlemap API
                mMap.clear();
                mRestaurantList = new ArrayList<>();
                mRestaurantLocationList = new ArrayList<>();
                for (NearbyRestaurantPOJO.PlaceResults r : restaurants.getPlaceResults()) {
                    String restaurantId = r.getPlaceId();
                    getRestaurantDetails(restaurantId);
                    double lat = r.getGeometry().getLocation().getLat();
                    double lng = r.getGeometry().getLocation().getLng();
                    LatLng latLng = new LatLng(lat, lng);
                    String name = r.getName();

                    restaurantIsEatingAt(mRestaurantEatingList, restaurantId);
                    Marker marker;
                    if (mCountUsersEating > 0){
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(name)
                                .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_dining_location_with_users )));
                        // TODO : changer le visuel
                    } else {
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(name)
                                .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_dining_location_without_users )));
                    }
                    assert marker != null;
                    marker.setTag(restaurantId);
                    RestaurantLocation restaurantLocation = new RestaurantLocation(restaurantId, name, latLng);
                    mRestaurantLocationList.add(restaurantLocation);
                }
                mRestaurantViewModel.setRestaurantList(mRestaurantList);
                setHasOptionsMenu(true);
            }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onComplete() { }
        });
    }


    // TODO go search in MapAPIStream to fetch data precisely
    // TODO : Demander à François comment afficher les heures correctement dans l'adapter
    // TODO : Rajouter CountOfUsersEatingAtRestaurant dans les modelsRestaurant pour récupérer les données.
    private void getRestaurantDetails(String restaurantId){
        this.mDisposableRestaurantDetails = MapAPIStream.streamFetchOpenRestaurant(restaurantId).subscribeWith(new DisposableObserver<DetailRestaurantPOJO>() {
            @Override
            public void onNext(@NotNull DetailRestaurantPOJO restaurant) {
                restaurantIsLiked(mRestaurantLikedList, restaurantId);
                restaurantIsEatingAt(mRestaurantEatingList, restaurantId);

                //Name and Address
                String name = restaurant.getResult().getName();
                String address = restaurant.getResult().getFormattedAddress();

                //Picture
                String urlPicture;
                if (restaurant.getResult().getPhotos() != null && !restaurant.getResult().getPhotos().isEmpty()) {
                    urlPicture = MapAPIStream.getImageUrl(restaurant.getResult().getPhotos().get(0).getPhotoReference());
                } else {
                    urlPicture = "null";
                }
                Log.e(TAG, urlPicture);

                // Opening Hours
                Calendar date = Calendar.getInstance(Locale.FRANCE);
                int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
                System.out.println("Day Of the Week : " + dayOfWeek);
                Boolean openNow = false;
                String openUntil = "null";

                if (restaurant.getResult().getOpeningHours() != null) {
                    openNow = restaurant.getResult().getOpeningHours().getOpenNow();
                    if (openNow){
                        List<DetailRestaurantPOJO.Period> listOpenPeriods = restaurant.getResult().getOpeningHours().getPeriods();
                        if (listOpenPeriods.size() > 0 ){
                            int i = 0;
                            while ( i < listOpenPeriods.size()){
                                int realDayOfWeek = dayOfWeek - 1;
                                System.out.println("Day Of the Week : " + realDayOfWeek + " == restaurantDay " + listOpenPeriods.get(i).getOpen().getDay());
                                if ((realDayOfWeek) == listOpenPeriods.get(i).getOpen().getDay()){
                                    openUntil = listOpenPeriods.get(i).getClose().getTime();
                                    break;
                                }
                                i++;
                            }
                        }
                    }
                }

                // Distance
                float[] resultDistance = new float[2];
                Location.distanceBetween(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude(),
                        restaurant.getResult().getGeometry().getLocation().getLat(), restaurant.getResult().getGeometry().getLocation().getLng(), resultDistance);
                int distance = (int)Math.round(resultDistance[0]);

                Restaurant createRestaurant = new Restaurant(restaurantId, name, address, urlPicture, openNow, openUntil, mCountUsersLiking, mCountUsersEating, distance);
                mRestaurantList.add(createRestaurant);
            }

            @Override
            public void onError(@NotNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getAllRestaurantsLikedList(){
        mRestaurantViewModel.getAllRestaurantLikedList().observe(requireActivity(), new Observer<List<RestaurantLikedItem>>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onChanged(List<RestaurantLikedItem> restaurantLikedItemList) {
                mRestaurantLikedList = restaurantLikedItemList;
            }
        });
    }

    public void restaurantIsLiked(List<RestaurantLikedItem> restaurantLikedItemList, String restaurantId){
        if (restaurantLikedItemList != null && restaurantLikedItemList.size() > 0){
            for (RestaurantLikedItem rId : restaurantLikedItemList){
                if (rId.getRestaurantId().equals(restaurantId)){
                    mCountUsersLiking = rId.getUsersLikingCount();
                    break;
                }
                else {
                    mCountUsersLiking = 0;
                }
            }
        }
    }



    public void getAllRestaurantsEatingList(){
        mRestaurantViewModel.getAllRestaurantEatingList().observe(requireActivity(), new Observer<List<RestaurantEatingItem>>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onChanged(List<RestaurantEatingItem> restaurantToEatItemList) {
                mRestaurantEatingList = restaurantToEatItemList;
            }
        });
    }

    public void restaurantIsEatingAt(List<RestaurantEatingItem> restaurantEatingItemList, String restaurantId){
        if (restaurantEatingItemList != null && restaurantEatingItemList.size() > 0){
            for (RestaurantEatingItem rId : restaurantEatingItemList){
                if (rId.getRestaurantId().equals(restaurantId)){
                    mCountUsersEating = rId.getUsersEatingCount();
                    System.out.println(TAG + " UserEatingCount : " + mCountUsersEating);
                    break;
                }
                else {
                    mCountUsersEating = 0;
                }
            }
        }
    }

    // Help from https://stackoverflow.com/questions/26786179/searchview-filtering-and-set-suggestions and
    // https://stackoverflow.com/questions/28335935/set-text-in-searchview-after-selecting-item-from-searchable-suggestion-window
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        mMenuItem = menu.findItem(R.id.search_bar);
        mSearchView = (SearchView) mMenuItem.getActionView();
        EditText editText = (EditText) mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.white));
        editText.setHint("Choose a Restaurant");
        editText.setHintTextColor(getResources().getColor(R.color.grey));
        ImageView searchClose  = (ImageView) mSearchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchClose.setColorFilter(getResources().getColor(R.color.white));

        Object[] temp = new Object[] { 0, "default" };

        final MatrixCursor cursor = new MatrixCursor(columns);
        for (int i = 0; i < mRestaurantLocationList.size(); i++) {
            temp[0] = i;
            temp[1] = mRestaurantLocationList.get(i);
            cursor.addRow(temp);
        }
        Log.e(TAG, "RestaurantLocationListSize inOption : " + mRestaurantLocationList.size());

        mMapSearchViewAdapter = new MapSearchViewAdapter(getContext(), cursor, mRestaurantLocationList);
        mSearchView.setSuggestionsAdapter(mMapSearchViewAdapter);
        mSearchView.setIconified(true);

        //SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        //mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null){
                    mMapSearchViewAdapter.getFilter().filter(query);
                }
                showRestaurant(query);
                return true;
                //Don't forget to return "true" in the onSuggestionClick callback,
                //if you return true it makes sure that the event is not returned to the parent, but if you return false,
                //android assumes you want even the parent to handle the event and the parent event handler gets called.
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query != null){
                    mMapSearchViewAdapter.getFilter().filter(query);
                }
                return true;
            }
        });

        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                String suggestion = getSuggestion(position).getName();
                mSearchView.setQuery(suggestion, false);
                showRestaurant(suggestion);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private RestaurantLocation getSuggestion(int position) {
        RestaurantLocation suggest1 = (RestaurantLocation) mSearchView.getSuggestionsAdapter().getItem(position);
        return suggest1;
    }

    private void showRestaurant(String location){

        // checking if the entered location is null or not.
        if (location != null || location.equals("")) {
            List<RestaurantLocation> filterList = new ArrayList<>();
            for (RestaurantLocation restaurantLocation : mRestaurantLocationList ){
                if (restaurantLocation.getName().toLowerCase().contains(location.toLowerCase())){
                    filterList.add(restaurantLocation);
                }
            }
            if (filterList.size() > 0) {
                // on below line we are getting the location
                // from our list a first position.
                RestaurantLocation restaurantLocation = filterList.get(0);

                // below line is to animate camera to that position.
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocation.getLatLng(), 20));
            }
        }
    }

    private void disposeWhenDestroy(){
        if (this.mDisposableRestaurantLocation != null && !this.mDisposableRestaurantLocation.isDisposed()) this.mDisposableRestaurantLocation.dispose();
        if (this.mDisposableRestaurantDetails != null && !this.mDisposableRestaurantDetails.isDisposed()) this.mDisposableRestaurantDetails.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    /*
    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, lastKnownLocation.toString());
        getParentFragmentManager().beginTransaction().add(R.id.navHostFragment, ListViewRestaurantFragment.newInstance(lastKnownLocation)).commit();
    }
 */

}