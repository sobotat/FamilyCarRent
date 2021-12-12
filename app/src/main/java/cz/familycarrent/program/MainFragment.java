package cz.familycarrent.program;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;

import cz.familycarrent.api.Family;
import cz.familycarrent.api.VehicleList_Adapter;

public class MainFragment extends Fragment {

    //ArrayList<Vehicle> vehicleList = new ArrayList<>();
    VehicleList_Adapter vL_adapter;
    RecyclerView rv_VehicleList;
    GridLayoutManager manager;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        MainActivity.setMainFragment(this);
        dialog = new Dialog(view.getContext());
        rv_VehicleList = view.findViewById(R.id.rv_VehicleList);

        /*

        vL_adapter = new Database().getvL_adapter();
        rv_VehicleList.setAdapter(vL_adapter);

        //SharedPreferences preferences = getActivity().getSharedPreferences("Database", Context.MODE_PRIVATE);
        //loadVehicleList("alPiLyiNs1X93Sbd7JVJ", this);
        */

        loadVehicleView(MainActivity.getFamily());
        return view;
    }

    public void rentDialogLoad() {
        dialog.setContentView(R.layout.item_rent);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_Send = dialog.findViewById(R.id.btn_RentSend);
        Button btn_Close = dialog.findViewById(R.id.btn_RentClose);
        EditText et_Date = dialog.findViewById(R.id.tv_RentDate);
        EditText et_Why = dialog.findViewById(R.id.tv_RentWhy);
        dialog.setCancelable(false);

        btn_Send.setOnClickListener(v -> Toast.makeText(getContext(), "You clicked to send", Toast.LENGTH_SHORT).show());
        btn_Close.setOnClickListener(v -> dialog.dismiss());
        et_Date.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
                month1 = month1 + 1;
                String date = dayOfMonth + "/" + month1 + "/" + year1;
                et_Date.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });

        dialog.show();
    }

    public void loadVehicleView(Family familyObj) {
        if (MainActivity.getIsTablet())
            manager = new GridLayoutManager(getContext(), 2);
        else
            manager = new GridLayoutManager(getContext(), 1);

        vL_adapter = new VehicleList_Adapter(getContext(), getActivity(), this, familyObj.getVehicles());
        rv_VehicleList.setLayoutManager(manager);
        rv_VehicleList.setAdapter(vL_adapter);
        rv_VehicleList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void updateVehicleView(Family familyObj){
        vL_adapter = new VehicleList_Adapter(getContext(), getActivity(), this, familyObj.getVehicles());
        rv_VehicleList.setLayoutManager(manager);
        rv_VehicleList.setAdapter(vL_adapter);
        rv_VehicleList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}