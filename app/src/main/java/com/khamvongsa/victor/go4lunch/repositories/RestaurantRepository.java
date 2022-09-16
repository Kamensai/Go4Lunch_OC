package com.khamvongsa.victor.go4lunch.repositories;


import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.khamvongsa.victor.go4lunch.manager.UserManager;
import com.khamvongsa.victor.go4lunch.model.Restaurant;
import com.khamvongsa.victor.go4lunch.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.annotations.Nullable;

/**
 * Created by <Victor Khamvongsa> on <24/05/2022>
 */
public final class RestaurantRepository {

    private static final String TAG = RestaurantRepository.class.getSimpleName();


    private static final String RESTAURANT_LIKED_COLLECTION = "restaurants_liked";
    private static final String RESTAURANT_TO_EAT_COLLECTION = "restaurants_to_eat";
    private static final String USERS_COLLECTION = "users";
    private static final String RESTAURANT_ID_FIELD = "restaurantId";
    private static final String USERS_EATING_LIST_FIELD = "usersEatingList";
    private static final String USERS_EATING_COUNT_FIELD = "usersEatingCount";
    private static final String USERS_LIKING_LIST_FIELD = "usersLikingList";
    private static final String USERS_LIKING_COUNT_FIELD = "usersLikingCount";

    private final MutableLiveData<List<User>> mListOfUsersEatingRestaurant = new MutableLiveData<>();

    private final MutableLiveData<List<String>> mListOfUsersLikingIdRestaurant = new MutableLiveData<>();

    private final MutableLiveData<List<Restaurant>> mListOfRestaurantLiked = new MutableLiveData<>();

    private final MutableLiveData<List<Restaurant>> mListOfRestaurantEating = new MutableLiveData<>();

    private final MutableLiveData<Number> mCountOfUsersLikingRestaurant = new MutableLiveData<>();

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

    // Get the Collection Reference
    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(USERS_COLLECTION);
    }

    public CollectionReference getRestaurantLikedCollection(){
        return FirebaseFirestore.getInstance().collection(RESTAURANT_LIKED_COLLECTION);
    }

    public CollectionReference getRestaurantToEatCollection(){
        return FirebaseFirestore.getInstance().collection(RESTAURANT_TO_EAT_COLLECTION);
    }

    public Query getAllRestaurantsLiked() {
        return this.getRestaurantLikedCollection();
    }


// TODO : Ajouter restaurant et updates
    @SuppressWarnings("unchecked")
    public void createRestaurantLiked(String restaurantId, String name){
        if (restaurantId != null)  {
        //Task<DocumentSnapshot> restaurantAlreadyExist = getRestaurantData(restaurantId);
            Restaurant restaurantToCreate = new Restaurant(restaurantId, name);
            Task<DocumentSnapshot> restaurantData = getRestaurantLikedData(restaurantId);
            // If the restaurant already exist in Firestore, we get his data.
            restaurantData.addOnSuccessListener(documentSnapshot -> {
                if (!documentSnapshot.exists()){
                    restaurantToCreate.setUsersLikingCount(1);
                    restaurantToCreate.setUsersLikingList(Collections.singletonList(userManager.getCurrentUser().getUid()));
                    this.getRestaurantLikedCollection().document(restaurantId).set(restaurantToCreate);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    public void createReferenceToRestaurantChosen(String restaurantId, String name ){
        if (restaurantId != null) {
            String uid = userManager.getCurrentUser().getUid();
            DocumentReference userDocRef = getUsersCollection().document(uid);
            Restaurant restaurantToCreate = new Restaurant(restaurantId, name);
            Task<DocumentSnapshot> restaurantData = getRestaurantToEatData(restaurantId);
            // If the restaurant already exist in Firestore, we get his data.
            restaurantData.addOnSuccessListener(documentSnapshot -> {
                if (!documentSnapshot.exists()) {
                    Log.d(TAG, documentSnapshot.toString());

                    restaurantToCreate.setUsersEatingCount(1);
                    restaurantToCreate.setUsersEatingList(Collections.singletonList(userDocRef));
                    this.getRestaurantToEatCollection().document(restaurantId).set(restaurantToCreate);
                }
            });
        }
    }

    public Task<QuerySnapshot> getRestaurantUserIsEatingAt(){
        String uid = userManager.getCurrentUser().getUid();
        DocumentReference userDocRef = getUsersCollection().document(uid);
        return getRestaurantToEatCollection().whereArrayContains(USERS_EATING_LIST_FIELD, userDocRef).get();
    }

    public Task<DocumentSnapshot> getRestaurantLikedData(String restaurantId) {
        if(restaurantId != null){
            return this.getRestaurantLikedCollection().document(restaurantId).get();
        }else{
            return null;
        }
    }

    public Task<DocumentSnapshot> getRestaurantToEatData(String restaurantId) {
        if(restaurantId != null){
            return this.getRestaurantToEatCollection().document(restaurantId).get();
        }else{
            return null;
        }
    }

    public Task<QuerySnapshot> getRestaurantLikedList(){
            return this.getRestaurantLikedCollection().get();
    }

    public Task<QuerySnapshot> getRestaurantEatingList(){
            return this.getRestaurantToEatCollection().get();
    }

    public DocumentReference getRestaurantLikedDoc(String restaurantId){
        if(restaurantId != null){
            return this.getRestaurantLikedCollection().document(restaurantId);
        }else{
            return null;
        }
    }

    public DocumentReference getRestaurantEatingDoc(String restaurantId){
        if(restaurantId != null){
            return this.getRestaurantToEatCollection().document(restaurantId);
        }else{
            return null;
        }
    }

    public MutableLiveData<List<String>> getAllUsersLikingIdListMutableLiveData() {
        return mListOfUsersLikingIdRestaurant;
    }

    public MutableLiveData<List<User>> getAllUsersEatingListMutableLiveData() {
        return mListOfUsersEatingRestaurant;
    }

    public MutableLiveData<List<Restaurant>> getAllRestaurantLikedListMutableLiveData() {
        return mListOfRestaurantLiked;
    }

    public MutableLiveData<List<Restaurant>> getAllRestaurantEatingListMutableLiveData() {
        return mListOfRestaurantEating;
    }

    public void deleteUserEatingAtOtherRestaurant(String restaurantIdClickedOn){
        this.getRestaurantUserIsEatingAt().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String restaurantIdOfOtherRestaurant = document.getId();
                        if (!restaurantIdClickedOn.equals(restaurantIdOfOtherRestaurant)){
                            decreaseUsersEatingCount(restaurantIdOfOtherRestaurant);
                            removeUsersEating(restaurantIdOfOtherRestaurant);
                        }
                    }
                } else {
                    Log.d("Error", "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //handle error
                Log.d(TAG, "Error getting documents: RestaurantUserIsEatingAt ");
            }
        });
    }

    // GET RESTAURANTLIKED
    public void getRestaurantsLikedList() {
        this.getRestaurantLikedList().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Restaurant> restaurantLikedList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Restaurant restaurant = ((DocumentSnapshot) document).toObject(Restaurant.class);
                        Log.d(TAG, restaurant.getName());
                        restaurantLikedList.add(restaurant);
                    }
                    mListOfRestaurantLiked.postValue(restaurantLikedList);
                    Log.d(TAG, "Current RESTAURANT_COLLECTION : " + restaurantLikedList);
                } else {
                    mListOfRestaurantLiked.postValue(null);
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    // GET RESTAURANTEATING
    public void getRestaurantsEatingList() {
        this.getRestaurantEatingList().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Restaurant> restaurantEatingList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Restaurant restaurant = ((DocumentSnapshot) document).toObject(Restaurant.class);
                        Log.d(TAG, restaurant.getName());
                        restaurantEatingList.add(restaurant);
                    }
                    mListOfRestaurantEating.postValue(restaurantEatingList);
                    Log.d(TAG, "Current RESTAURANT_EATING_COLLECTION : " + restaurantEatingList);
                } else {
                    mListOfRestaurantEating.postValue(null);
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


        // TODO : help from https://firebase.google.com/docs/firestore/query-data/listen
    @SuppressWarnings("unchecked")
    public void getUsersLikingListId(String restaurantId) {
        this.getRestaurantLikedDoc(restaurantId).addSnapshotListener(new EventListener<DocumentSnapshot>(){
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                    @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    List<String> usersLikingRestaurantListId = (List<String>) snapshot.get(USERS_LIKING_LIST_FIELD);
                    if (usersLikingRestaurantListId != null) {
                        mListOfUsersLikingIdRestaurant.postValue(usersLikingRestaurantListId);
                        Log.d(TAG, "Current USERS_LIKING_LIST_FIELD : " + usersLikingRestaurantListId);
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                    mListOfUsersLikingIdRestaurant.postValue(null);
                }
            }
        });
    }


    @SuppressWarnings("unchecked")
    public void getUsersLikingListIdBis(String restaurantId) {
        this.getRestaurantLikedList().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        List<String> usersLikingRestaurantListId = (List<String>) document.get(USERS_LIKING_LIST_FIELD);
                        assert usersLikingRestaurantListId != null;
                        mListOfUsersLikingIdRestaurant.setValue(usersLikingRestaurantListId);
                    }
                } else {
                    Log.d("Error", "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //handle error
                mListOfUsersLikingIdRestaurant.setValue(null);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void getCountRestaurantsLikedBis(String restaurantId) {
        this.getRestaurantLikedDoc(restaurantId).addSnapshotListener(new EventListener<DocumentSnapshot>(){
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Number usersLikingRestaurantCount = (Number) snapshot.get(USERS_LIKING_COUNT_FIELD);
                        mCountOfUsersLikingRestaurant.setValue(usersLikingRestaurantCount);
                        Log.d(TAG, "Current USERS_LIKING_COUNT_FIELD : " + usersLikingRestaurantCount);
                    Log.d(TAG, "Current USERS_LIKING_COUNT_FIELD : " + mCountOfUsersLikingRestaurant.getValue());
                } else {

                    mCountOfUsersLikingRestaurant.setValue(0);
                    Log.d(TAG, "Current data: " + mCountOfUsersLikingRestaurant.getValue());
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void getCountRestaurantsLiked(String restaurantId) {
        this.getRestaurantLikedDoc(restaurantId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Number usersLikingRestaurantCount = (Number) document.get(USERS_LIKING_COUNT_FIELD);
                        mCountOfUsersLikingRestaurant.postValue(usersLikingRestaurantCount);
                        Log.d(TAG, "DocumentSnapshot data: " + usersLikingRestaurantCount);

                    } else {
                        mCountOfUsersLikingRestaurant.postValue(0);
                        Log.d(TAG, "No such document" + mCountOfUsersLikingRestaurant);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }



        // TODO : Vérifier Help StackOverflow : https://stackoverflow.com/questions/51892766/android-firestore-convert-array-of-document-references-to-listpojo
    //  https://stackoverflow.com/questions/52537965/how-can-i-get-data-from-array-data-field-from-cloud-firestore
    //  https://stackoverflow.com/questions/46757614/how-to-update-an-array-of-objects-with-firestore

    // TODO : Faire comme dans l'application FreelanceFinder pour récupérer l'objet à partir de la référence.
    // TODO : Help from https://stackoverflow.com/questions/68919182/how-to-access-array-data-from-firebase-firestore

    @SuppressWarnings("unchecked")
    public void getUsersEatingList(String restaurantId) {
        this.getRestaurantEatingDoc(restaurantId).addSnapshotListener(new EventListener<DocumentSnapshot>(){
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    List<DocumentReference> list = (List<DocumentReference>) snapshot.get(USERS_EATING_LIST_FIELD);
                    List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                    if (list != null) {
                        for (DocumentReference documentReference : list) {
                            Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
                            tasks.add(documentSnapshotTask);
                        }
                        Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                            @Override
                            public void onSuccess(List<Object> list) {
                                List<User> userEatingList = new ArrayList<>();
                                //Do what you need to do with your list
                                for (Object object : list) {
                                    User user = ((DocumentSnapshot) object).toObject(User.class);
                                    assert user != null;
                                    Log.d(TAG, user.getUsername());
                                    userEatingList.add(user);
                                }
                                mListOfUsersEatingRestaurant.postValue(userEatingList);
                                Log.d(TAG, "Current USERS_EATING_LIST_FIELD : " + userEatingList);
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                    mListOfUsersEatingRestaurant.postValue(null);
                }
            }
        });
    }

    // TODO : Vérifier Help from StackOverflow : https://stackoverflow.com/questions/51292378/how-do-you-insert-a-reference-value-into-firestore
    //  https://stackoverflow.com/questions/50722942/firestore-how-create-documentreference-using-path-string
    //  https://stackoverflow.com/questions/68160074/how-do-i-insert-data-in-firestore-in-array-of-string
    // Add UsersEatingToArray
    public Task<Void> addUsersEating(String restaurantId) {
        String uid = userManager.getCurrentUser().getUid();
        DocumentReference userDocRef = getUsersCollection().document(uid);
        if(restaurantId != null){
            Log.e(TAG, "Adding userEating");
            return this.getRestaurantToEatCollection().document(restaurantId).update(USERS_EATING_LIST_FIELD, FieldValue.arrayUnion(userDocRef));
        }else{
            return null;
        }
    }

    // Remove UsersEatingFromArray
    public Task<Void> removeUsersEating(String restaurantId) {
        String uid = userManager.getCurrentUser().getUid();
        DocumentReference userDocRef = getUsersCollection().document(uid);
        if(restaurantId != null){
            Log.e(TAG, "UserEating deleted");
            return this.getRestaurantToEatCollection().document(restaurantId).update(USERS_EATING_LIST_FIELD, FieldValue.arrayRemove(userDocRef));
        }else{
            return null;
        }
    }

    // Add UsersEatingCount
    public Task<Void> addUsersEatingCount(String restaurantId) {
        if(restaurantId != null){
            return this.getRestaurantToEatCollection().document(restaurantId).update(USERS_EATING_COUNT_FIELD, FieldValue.increment(1));
        }else{
            return null;
        }
    }

    // Decrease UsersEatingCount
    public Task<Void> decreaseUsersEatingCount(String restaurantId) {
        if(restaurantId != null){
            return this.getRestaurantToEatCollection().document(restaurantId).update(USERS_EATING_COUNT_FIELD, FieldValue.increment(-1));
        }else{
            return null;
        }
    }

    // Add UsersLiking
    public Task<Void> addUsersLiking(String restaurantId) {
        String uid = userManager.getCurrentUser().getUid();
        if(restaurantId != null){
            return this.getRestaurantLikedCollection().document(restaurantId).update(USERS_LIKING_LIST_FIELD, FieldValue.arrayUnion(uid));
        }else{
            return null;
        }
    }

    // Remove UsersLikingFromArray
    public Task<Void> removeUsersLiking(String restaurantId) {
        String uid = userManager.getCurrentUser().getUid();
        if(restaurantId != null){
            return this.getRestaurantLikedCollection().document(restaurantId).update(USERS_LIKING_LIST_FIELD, FieldValue.arrayRemove(uid));
        }else{
            return null;
        }
    }

    // Add UsersEatingCount
    public Task<Void> addUsersLikingCount(String restaurantId) {
        if(restaurantId != null){
            return this.getRestaurantLikedCollection().document(restaurantId).update(USERS_LIKING_COUNT_FIELD, FieldValue.increment(1));
        }else{
            return null;
        }
    }

    // Decrease UsersEatingCount
    public Task<Void> decreaseUsersLikingCount(String restaurantId) {
        if(restaurantId != null){
            return this.getRestaurantLikedCollection().document(restaurantId).update(USERS_LIKING_COUNT_FIELD, FieldValue.increment(-1));
        }else{
            return null;
        }
    }

}