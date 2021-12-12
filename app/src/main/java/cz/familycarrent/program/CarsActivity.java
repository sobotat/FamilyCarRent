package cz.familycarrent.program;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import cz.familycarrent.api.CarsList_Adapter;
import cz.familycarrent.api.Family;
import cz.familycarrent.api.Vehicle;

public class CarsActivity extends AppCompatActivity {

    TextView tv_Name, tv_Message;
    RecyclerView rv_Vehicles;
    LinearLayout layout_Add;

    static CarsActivity carsActivity;
    Dialog dialog;

    CarsList_Adapter cl_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);

        carsActivity = this;
        dialog = new Dialog(this);

        rv_Vehicles = findViewById(R.id.rv_CarsVehicles);
        tv_Name = findViewById(R.id.tv_CarsFamilyName);
        tv_Message = findViewById(R.id.tv_CarsMessage);
        layout_Add = findViewById(R.id.layout_CarsAdd);

        updateUI(MainActivity.getFamily());

        layout_Add.setOnClickListener(v -> {
            createFamilyDialog();
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    public static CarsActivity getCarsActivity() {
        return carsActivity;
    }

    public void updateUI(Family familyObj) {
        tv_Name.setText(MainActivity.getFamily().getName());

        if (MainActivity.getFamily().getVehicles().size() != 0) {
            tv_Message.setVisibility(View.GONE);
            rv_Vehicles.setVisibility(View.VISIBLE);

            cl_adapter = new CarsList_Adapter(this, familyObj.getVehicles());
            rv_Vehicles.setAdapter(cl_adapter);
            rv_Vehicles.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        }else {
            tv_Message.setVisibility(View.VISIBLE);
            rv_Vehicles.setVisibility(View.GONE);
        }
    }

    public void createFamilyDialog() {
        dialog.setContentView(R.layout.item_car_create);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_Create = dialog.findViewById(R.id.btn_CarCreateCreate);
        Button btn_Cancel = dialog.findViewById(R.id.btn_CarCreateCancel);
        EditText et_Name = dialog.findViewById(R.id.et_CarCreateName);

        dialog.setCancelable(false);

        btn_Create.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            Family family = MainActivity.getFamily();
            Vehicle vehicle = new Vehicle(et_Name.getText().toString(), mAuth.getCurrentUser().getEmail());
            family.getVehicles().add(vehicle);

            MainActivity.getMainActivity().saveFamily(family);
            //finish();
            dialog.dismiss();
        });
        btn_Cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}