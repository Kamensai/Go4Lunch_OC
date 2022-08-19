package com.khamvongsa.victor.go4lunch.repositories;

import android.content.Context;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.khamvongsa.victor.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
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
    private static final String CHOSEN_RESTAURANT_ID_FIELD = "chosenRestaurantId";
    private static final String CHOSEN_RESTAURANT_NAME_FIELD = "chosenRestaurantName";


    private final MutableLiveData<String> mChosenRestaurantId = new MutableLiveData<>();

    private final MutableLiveData<List<User>> mUsersList = new MutableLiveData<>();

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
                if (documentSnapshot.contains(CHOSEN_RESTAURANT_ID_FIELD)){
                    userToCreate.setChosenRestaurantId((String) documentSnapshot.get(CHOSEN_RESTAURANT_ID_FIELD));
                }
                if (documentSnapshot.contains(CHOSEN_RESTAURANT_NAME_FIELD)){
                    userToCreate.setChosenRestaurantName((String) documentSnapshot.get(CHOSEN_RESTAURANT_NAME_FIELD));
                }
                this.getUsersCollection().document(uid).set(userToCreate);
            });
        }
    }

    public MutableLiveData<String> getChosenRestaurantIdMutableLiveData() {
        return mChosenRestaurantId;
    }

    public Task<QuerySnapshot> getAllUsersSortedByChosenRestaurant(){
        return getUsersCollection().orderBy(CHOSEN_RESTAURANT_ID_FIELD).get();
    }

    public LiveData<List<User>> getAllUsersSortedByChosenRestaurantMutableLiveData() {
        getAllUsersSortedByChosenRestaurant().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        users.add(document.toObject(User.class));
                    }
                    mUsersList.postValue(users);
                } else {
                    Log.d("Error", "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //handle error
                mUsersList.postValue(null);
            }
        });
        return mUsersList;
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
                    String chosenRestaurantId = (String) snapshot.get(CHOSEN_RESTAURANT_ID_FIELD);
                    mChosenRestaurantId.postValue(chosenRestaurantId);
                    Log.d(TAG, "Current CHOSEN_RESTAURANT_ID_FIELD : " +chosenRestaurantId);
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
    public void updateChosenRestaurantIdAndName(String chosenRestaurantId, String chosenRestaurantName) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            this.getUsersCollection().document(uid).update(CHOSEN_RESTAURANT_ID_FIELD, chosenRestaurantId);
            this.getUsersCollection().document(uid).update(CHOSEN_RESTAURANT_NAME_FIELD, chosenRestaurantName);
        }
    }

    public void deleteChosenRestaurantIdAndName() {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            this.getUsersCollection().document(uid).update(CHOSEN_RESTAURANT_ID_FIELD, null);
            this.getUsersCollection().document(uid).update(CHOSEN_RESTAURANT_NAME_FIELD, null);
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
