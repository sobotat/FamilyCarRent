package cz.familycarrent.api;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.familycarrent.program.ProfileFragment;

public class Family {
    String id = "";
    String name = "";
    ArrayList<String> members = new ArrayList<>();
    ArrayList<Vehicle> vehicles = new ArrayList<>();

    public Family() {
    }

    public Family(String id, String name, ArrayList<String> members, ArrayList<Vehicle> vehicles, Boolean manual) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.vehicles = vehicles;
    }

    public Family(String id, String name, ArrayList<String> members, ArrayList<Map<String, Object>> vehicles) {
        this.id = id;
        this.name = name;
        this.members = members;

        ArrayList<Vehicle> pomV = new ArrayList<>();
        for (Map<String, Object> item:vehicles) {
            pomV.add(new Vehicle(item.get("name").toString(), item.get("owner").toString()));
        }
        this.vehicles = pomV;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    @Override
    public String toString() {
        return "Family{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", members=" + members +
                ", vehicles=" + vehicles +
                '}';
    }


}
