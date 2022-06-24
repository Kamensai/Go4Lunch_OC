package com.khamvongsa.victor.go4lunch.repositories;


import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.khamvongsa.victor.go4lunch.manager.UserManager;
import com.khamvongsa.victor.go4lunch.model.Restaurant;

import java.util.ArrayList;

import androidx.annotation.NonNull;

/**
 * Created by <Victor Khamvongsa> on <24/05/2022>
 */
public final class RestaurantRepository {

    private static final String TAG = RestaurantRepository.class.getSimpleName();


    private static final String RESTAURANT_COLLECTION = "restaurants";
    private static final String RESTAURANT_ID_FIELD = "restaurantId";
    private static final String USERS_EATING_LIST_FIELD = "usersEatingList";
    private static final String USERS_EATING_COUNT_FIELD = "usersEatingCount";
    private static final String USERS_LIKING_LIST_FIELD = "usersLikingList";
    private static final String USERS_LIKING_COUNT_FIELD = "usersLikingCount";


    private static volatile RestaurantRepository instance;

    private UserManager userManager;

    private RestaurantRepository() { this.userManager = UserManager.getInstance();
    }

    public static RestaurantRepository getInstance() {
        RestaurantRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(RestaurantRepository.class) {
            if (instance == null) {
                instance = new RestaurantRepository();
            }
            return instance;
        }
    }

    public CollectionReference getRestaurantCollection(){
        return FirebaseFirestore.getInstance().collection(RESTAURANT_COLLECTION);
    }

    public Query getAllRestaurants() {
        return this.getRestaurantCollection();
    }



// TODO : Ajouter restaurant et updates

    public void createRestaurant(String restaurantId, String name){
        if (restaurantId != null)  {
        //Task<DocumentSnapshot> mRestaurantId = getRestaurantData(restaurantId);
        Restaurant restaurantToCreate = new Restaurant(restaurantId, name);
        /*
            mRestaurantId.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains()) {

                    // Store Message to Firestore
                    this.getRestaurantCollection().set(restaurantToCreate);
                }
            });
         */
            this.getRestaurantCollection().document(restaurantId).set(restaurantToCreate);
        }
    }

    public void createChosenRestaurant(String restaurantId, String name, String type){
        if (restaurantId != null)  {
            //Task<DocumentSnapshot> mRestaurantId = getRestaurantData(restaurantId);
            Restaurant restaurantToCreate = new Restaurant(restaurantId, name, type);
        /*
            mRestaurantId.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains()) {

                    // Store Message to Firestore
                    this.getRestaurantCollection().set(restaurantToCreate);
                }
            });
         */
            this.getRestaurantCollection().add(restaurantToCreate);
        }
    }

    public Task<DocumentSnapshot> getRestaurantData(String restaurantId) {
        if(restaurantId != null){
            return this.getRestaurantCollection().document(restaurantId).get();
        }else{
            return null;
        }
    }
    // Help StackOverflow : https://stackoverflow.com/questions/52537965/how-can-i-get-data-from-array-data-field-from-cloud-firestore
    // https://stackoverflow.com/questions/46757614/how-to-update-an-array-of-objects-with-firestore
    public Task<DocumentSnapshot> getUsersEatingList(String restaurantId) {
        return this.getRestaurantCollection().document(restaurantId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> list = (ArrayList<String>) document.get(USERS_EATING_LIST_FIELD);
                        assert list != null;
                        Log.d(TAG, list.toString());
                    }
                }
            }
        });
    }

    // Help from StackOverflow : https://stackoverflow.com/questions/68160074/how-do-i-insert-data-in-firestore-in-array-of-string
    // Add UsersEatingToArray
    public Task<Void> addUsersEating(String restaurantId) {
        String uid = userManager.getCurrentUser().getUid();
        if(restaurantId != null){
            return this.getRestaurantCollection().document(restaurantId).update(USERS_EATING_LIST_FIELD, FieldValue.arrayUnion(uid));
        }else{
            return null;
        }
    }

    // Remove UsersEatingFromArray
    public Task<Void> removeUsersEating(String restaurantId) {
        String uid = userManager.getCurrentUser().getUid();
        if(restaurantId != null){
            return this.getRestaurantCollection().document(restaurantId).update(USERS_EATING_LIST_FIELD, FieldValue.arrayRemove(uid));
        }else{
            return null;
        }
    }

    // Add UsersEatingCount
    public Task<Void> addUsersEatingCount(String restaurantId) {
        if(restaurantId != null){
            return this.getRestaurantCollection().document(restaurantId).update(USERS_EATING_COUNT_FIELD, FieldValue.increment(1));
        }else{
            return null;
        }
    }

    // Decrease UsersEatingCount
    public Task<Void> decreaseUsersEatingCount(String restaurantId) {
        if(restaurantId != null){
            return this.getRestaurantCollection().document(restaurantId).update(USERS_EATING_COUNT_FIELD, FieldValue.increment(-1));
        }else{
            return null;
        }
    }

    // Add UsersLiking
    public Task<Void> addUsersLiking(String restaurantId) {
        String uid = userManager.getCurrentUser().getUid();
        if(restaurantId != null){
            return this.getRestaurantCollection().document(restaurantId).update(USERS_LIKING_LIST_FIELD, FieldValue.arrayUnion(uid));
        }else{
            return null;
        }
    }

    // Add UsersEatingCount
    public Task<Void> addUsersLikingCount(String restaurantId) {
        if(restaurantId != null){
            return this.getRestaurantCollection().document(restaurantId).update(USERS_LIKING_COUNT_FIELD, FieldValue.increment(1));
        }else{
            return null;
        }
    }

    // Decrease UsersEatingCount
    public Task<Void> decreaseUsersLikingCount(String restaurantId) {
        if(restaurantId != null){
            return this.getRestaurantCollection().document(restaurantId).update(USERS_LIKING_COUNT_FIELD, FieldValue.increment(-1));
        }else{
            return null;
        }
    }

}