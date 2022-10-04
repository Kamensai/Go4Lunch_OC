package com.khamvongsa.victor.go4lunch.ui.fragment;

import android.content.Context;
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

import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.model.RestaurantStateItem;
import com.khamvongsa.victor.go4lunch.ui.RestaurantViewModel;
import com.khamvongsa.victor.go4lunch.ui.views.RestaurantAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by <Victor Khamvongsa> on <18/11/2021>
 */
public class ListViewRestaurantFragment extends Fragment {

    private static final String TAG = ListViewRestaurantFragment.class.getSimpleName();

    private List<RestaurantStateItem> mRestaurantStateItemList;

    private RestaurantViewModel mRestaurantViewModel;

    private RestaurantAdapter mRestaurantAdapter;

    private Location mLastKnownLocation;

    //Menu
    private MenuItem mMenuItem;
    private SearchView mSearchView;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private RecyclerView mRecyclerView;


    public ListViewRestaurantFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param location Parameter 1.
     * @return A new instance of fragment list_view_restaurant.
     */
    // TODO: Rename and change types and number of parameters
    public static ListViewRestaurantFragment newInstance(Location location) {
        ListViewRestaurantFragment fragment = new ListViewRestaurantFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        mRestaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
        //getAllRestaurantList();
        mRestaurantAdapter = new RestaurantAdapter(mRestaurantStateItemList);

        if (getArguments() != null) {
            mLastKnownLocation = getArguments().getParcelable(KEY_LOCATION);
            Log.e(TAG, mLastKnownLocation.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_view_restaurants, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mRestaurantAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getAllRestaurantList();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
        super.onSaveInstanceState(outState);
    }

    private void getAllRestaurantList() {
        //mRestaurantViewModel.getAllRestaurant().observe(requireActivity(), mRestaurantAdapter::submitList);
        mRestaurantViewModel.getAllRestaurant().observe(requireActivity(), new Observer<List<RestaurantStateItem>>() {
            @Override
            public void onChanged(List<RestaurantStateItem> restaurantStateItemList) {
                mRestaurantStateItemList = restaurantStateItemList;
            }
        });
    }

    // Help from : https://www.youtube.com/watch?v=GvVz4KO4D54&ab_channel=TheTechBrothers and https://www.youtube.com/watch?v=MrZNoSrcJUs&ab_channel=FoysalTech
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        mMenuItem = menu.findItem(R.id.search_bar);
        mSearchView = (SearchView) mMenuItem.getActionView();
        mSearchView.setIconified(true);

        EditText editText = (EditText) mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.white));
        editText.setHint(R.string.restaurant_search_hint);
        editText.setHintTextColor(getResources().getColor(R.color.grey));
        ImageView searchClose  = (ImageView) mSearchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchClose.setColorFilter(getResources().getColor(R.color.white));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mRestaurantAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mRestaurantAdapter.getFilter().filter(query);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

   /*
    @Override
    public void onStop() {
        super.onStop();
        getAllRestaurantList();
        mRestaurantAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllRestaurantList();
        mRestaurantAdapter.notifyDataSetChanged();
    }

 */
}
