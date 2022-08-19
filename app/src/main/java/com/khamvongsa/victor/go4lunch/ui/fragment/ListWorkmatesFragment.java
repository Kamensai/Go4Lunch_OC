package com.khamvongsa.victor.go4lunch.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.ui.FactoryViewModel;
import com.khamvongsa.victor.go4lunch.ui.UserViewModel;
import com.khamvongsa.victor.go4lunch.ui.views.WorkmatesAdapter;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by <Victor Khamvongsa> on <18/11/2021>
 */
public class ListWorkmatesFragment extends Fragment {

    private UserViewModel mUserViewModel;
    private WorkmatesAdapter mWorkmatesAdapter;
    private RecyclerView mRecyclerView;

    public ListWorkmatesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserViewModel = new ViewModelProvider(this, FactoryViewModel.getInstance()).get(UserViewModel.class);
        mWorkmatesAdapter = new WorkmatesAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_workmates, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setAdapter(mWorkmatesAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        getAllUsersSortedByChosenRestaurant();
        return view;
    }

    private void getAllUsersSortedByChosenRestaurant() {
        mUserViewModel.getAllUsersSortedByChosenRestaurant().observe(requireActivity(), mWorkmatesAdapter::submitList);
    }
}
