package com.khamvongsa.victor.go4lunch.repositories;

import android.content.Context;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.khamvongsa.victor.go4lunch.model.User;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by <Victor Khamvongsa> on <24/05/2022>
 */
public final class UserRepository {

    private static final String TAG = UserRepository.class.getSimpleName();

    private static final String USERS_COLLECTION = "users";
    private static final String USERNAME_FIELD = "username";
    private static final String MAIL_FIELD = "mail";
    private static final String URL_PICTURE_FIELD = "urlPicture";
    private static final String CHOSEN_RESTAURANT_FIELD = "chosenRestaurant";

    private final MutableLiveData<String> mChosenRestaurantId = new MutableLiveData<>();

    private static volatile UserRepository instance;

    private UserRepository() { }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }

    @Nullable
    public FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    // TODO : Demander à François si c'est du kotlin
    @Nullable
    public String getCurrentUserUID(){
        FirebaseUser user = getCurrentUser();
        return (user != null)? user.getUid() : null;
    }

    public Task<Void> signOut(Context context){
        return AuthUI.getInstance().signOut(context);
    }

    public Task<Void> deleteUser(Context context){
        return AuthUI.getInstance().delete(context);
    }

    // Get the Collection Reference
    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(USERS_COLLECTION);
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if(user != null){
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String uid = user.getUid();
            String mail = user.getEmail();

            User userToCreate = new User(uid, username, mail, urlPicture);

            Task<DocumentSnapshot> userData = getUserData();
            // If the user already exist in Firestore, we get his data (isMentor)
            userData.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains(CHOSEN_RESTAURANT_FIELD)){
                    userToCreate.setChosenRestaurant((String) documentSnapshot.get(CHOSEN_RESTAURANT_FIELD));
                }
                this.getUsersCollection().document(uid).set(userToCreate);
            });
        }
    }

    public MutableLiveData<String> getChosenRestaurantIdMutableLiveData() {
        return mChosenRestaurantId;
    }

    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData(){
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).get();
        }else{
            return null;
        }
    }

    public DocumentReference getCurrentUserDoc(){
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid);
        }else{
            return null;
        }
    }

    public void getUserChosenRestaurant() {
        this.getCurrentUserDoc().addSnapshotListener(new EventListener<DocumentSnapshot>(){
            @Override
            public void onEvent(@io.reactivex.annotations.Nullable DocumentSnapshot snapshot,
                                @io.reactivex.annotations.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if ( snapshot != null && snapshot.exists()) {
                    String chosenRestaurantId = (String) snapshot.get(CHOSEN_RESTAURANT_FIELD);
                    mChosenRestaurantId.postValue(chosenRestaurantId);
                    Log.d(TAG, "Current CHOSEN_RESTAURANT_FIELD : " +chosenRestaurantId);
                } else {
                    Log.d(TAG, "Current data: null");
                    mChosenRestaurantId.postValue(null);
                }
            }
        });
    }

    // Update User Username
    public Task<Void> updateUsername(String username) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
        }else{
            return null;
        }
    }

    // TODO : récupérer l'id du restaurant
    // Update chosenRestaurant
    public void updateChosenRestaurant(String chosenRestaurant) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            this.getUsersCollection().document(uid).update(CHOSEN_RESTAURANT_FIELD, chosenRestaurant);
        }
    }

    public void deleteChosenRestaurant() {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            this.getUsersCollection().document(uid).update(CHOSEN_RESTAURANT_FIELD, null);
        }
    }

    // Delete the User from Firestore
    public void deleteUserFromFirestore() {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            this.getUsersCollection().document(uid).delete();
        }
    }
}
