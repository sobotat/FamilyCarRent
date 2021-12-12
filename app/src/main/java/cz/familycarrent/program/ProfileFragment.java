package cz.familycarrent.program;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;
import cz.familycarrent.api.Family;
import cz.familycarrent.api.Member;
import cz.familycarrent.api.Vehicle;

public class ProfileFragment extends Fragment {

    ImageView iv_Account;
    TextView tv_AccountName, tv_AccountEmail;
    LinearLayout layoutMyFamily, layoutMyCars, layoutSettings, layoutSignOut;
    Context mContext;
    FirebaseFirestore db;
    static ProfileFragment profileFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = view.getContext();
        profileFragment = this;

        iv_Account = view.findViewById(R.id.iv_ProfileAccount);
        tv_AccountName = view.findViewById(R.id.tv_ProfileAccountName);
        tv_AccountEmail = view.findViewById(R.id.tv_ProfileAccountEmail);
        layoutMyFamily = view.findViewById(R.id.layout_ProfileMyFamily);
        layoutMyCars = view.findViewById(R.id.layout_ProfileMyCars);
        layoutSettings = view.findViewById(R.id.layout_ProfileSettings);
        layoutSignOut = view.findViewById(R.id.layout_ProfileSignOut);

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tv_AccountName.setText(user.getDisplayName());
            tv_AccountEmail.setText(user.getEmail());
            loadProfilePhoto(user.getPhotoUrl().toString());
        }

        layoutMyFamily.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), FamilyActivity.class));
        });

        layoutMyCars.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CarsActivity.class));
        });


        layoutSettings.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), SettingsActivity.class));
        });

        layoutSignOut.setOnClickListener(v -> {
            signOut(getActivity());
        });

        return view;
    }

    public static void signOut(Activity activity) {
        MainActivity.getFamilyLiveUpdate().remove();
        Log.i("Database - LiveUpdate", "Listening was turn off");

        MainActivity.setFamily(new Family());
        SharedPreferences preferences = activity.getSharedPreferences("Database", Context.MODE_PRIVATE);
        preferences.edit().putString("id", "").commit();

        FirebaseAuth.getInstance().signOut();
        activity.startActivity(new Intent(activity, LoginActivity.class));
        MainActivity.getMainActivity().finish();
    }

    public static ProfileFragment getProfileFragment() {
        return profileFragment;
    }

    public void loadProfilePhoto(String url) {
        ImageLoader imageLoader = Coil.imageLoader(getContext());
        ImageRequest request = new ImageRequest.Builder(getContext())
                .data(url)
                .crossfade(true)
                .target(iv_Account)
                .build();
        imageLoader.enqueue(request);
    }
}