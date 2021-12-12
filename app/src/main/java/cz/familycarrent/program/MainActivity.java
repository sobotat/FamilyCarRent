package cz.familycarrent.program;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cz.familycarrent.api.Family;

public class MainActivity extends AppCompatActivity {

    static Boolean isTablet = false;
    static FragmentManager fragmentManager;

    private FirebaseAuth mAuth;
    static ListenerRegistration familyLiveUpdate;
    static Family family = new Family();
    static MainFragment mainFragment;
    static MainActivity mainActivity;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            //Toast.makeText(this, "Login is successful", Toast.LENGTH_SHORT).show();

        } else {
            Intent intant = new Intent(this, LoginActivity.class);
            startActivity(intant);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mainActivity = this;

        database();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
        NavController navController = Navigation.findNavController(findViewById(R.id.fragmentNavigationView));
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        fragmentManager = getSupportFragmentManager();
        isTablet = isTablet();
        if (isNetworkAvailable()) {
            //TODO: Check internet Connection
        }

        forceDarkMode();
    }

    private void database() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            SharedPreferences preferences = getSharedPreferences("Database", MODE_PRIVATE);
            String id = preferences.getString("id", "");//alPiLyiNs1X93Sbd7JVJ
            if (id != "") {
                liveUpdateFamily(id);
            } else {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Family")
                        .whereArrayContains("members", user.getEmail().toString())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                Log.i("Database-Query", "Success find the data");
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        Log.i("Database-Query-Read", document.get("id").toString());
                                        preferences.edit().putString("id", document.get("id").toString()).apply();
                                    }
                                    liveUpdateFamily(preferences.getString("id", ""));
                                } else
                                    Log.i("Database-Query-Read", "Not in the database");
                            }
                        });
            }
        }
    }

    public static Boolean getIsTablet() {
        return isTablet;
    }

    public static ListenerRegistration getFamilyLiveUpdate() {
        return familyLiveUpdate;
    }

    public static Family getFamily() {
        return family;
    }

    public static void setMainFragment(MainFragment mainFragment) {
        MainActivity.mainFragment = mainFragment;
    }

    public static void setFamily(Family family) {
        MainActivity.family = family;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }


    public void forceDarkMode() {
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        Boolean forceDarkMode = preferences.getBoolean("forceDarkMode", false);

        if (forceDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;

            if (connectivityManager != null) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.isConnected();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public Boolean isTablet() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

        if (diagonalInches >= 6.5) {
            return true;
        } else {
            return false;
        }
    }

    public void liveUpdateFamily(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String TAG = "Database-Family-LiveUpdate";

        DocumentReference docRef = db.collection("Family").document(id);
        familyLiveUpdate = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    ProfileFragment.signOut(mainActivity);
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, source + " data: " + snapshot.getData());
                    family = new Family(snapshot.getId(), snapshot.get("name").toString(), (ArrayList<String>) snapshot.get("members"), (ArrayList<Map<String, Object>>) snapshot.get("vehicles"));
                    if (mainFragment != null)
                        mainFragment.updateVehicleView(family);
                    if (FamilyActivity.getFamilyActivity() != null)
                        FamilyActivity.getFamilyActivity().updateUI(family);
                    if (CarsActivity.getCarsActivity() != null)
                        CarsActivity.getCarsActivity().updateUI(family);

                } else {
                    Log.d(TAG, source + " data: null");
                }
            }
        });
    }

    public void saveFamily(Family familyObj) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef;
        if (familyObj.getId() != "")
            docRef = db.collection("Family").document(familyObj.getId());
        else {
            docRef = db.collection("Family").document();
            familyObj.setId(docRef.getId());
        }

        docRef.set(familyObj).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Log.d("Database-Family", "DocumentSnapshot saved");
                family = familyObj;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Database-Family", "Error adding document", e);
            }
        });
    }
}