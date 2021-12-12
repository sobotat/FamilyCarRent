package cz.familycarrent.api;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.familycarrent.program.MainActivity;
import cz.familycarrent.program.MainFragment;
import cz.familycarrent.program.R;

public class VehicleList_Adapter extends RecyclerView.Adapter<VehicleList_Adapter.viewHolder> {

    Context mContext;
    Activity mActivity;
    ArrayList<Vehicle> vehicleList;
    MainFragment mainFragment;

    public VehicleList_Adapter(Context mContext, Activity mActivity, MainFragment mainFragment, ArrayList<Vehicle> vehicleList) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mainFragment = mainFragment;
        this.vehicleList = vehicleList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.item_vehicle, parent, false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(VehicleList_Adapter.viewHolder holder, int position) {

        holder.tv_Name.setText(vehicleList.get(position).getName());

        //Date date = vehicleList.get(position).date;
        //holder.tv_Date.setText(date.getDate() + "." + date.getMonth() + "." + date.getYear());
        //holder.tv_Why.setText(vehicleList.get(position).why);

        holder.layout_Yes.setVisibility(View.GONE);
        holder.layout_No.setVisibility(View.GONE);
        holder.layout_Notification.setVisibility(View.VISIBLE);

        /*
        switch (vehicleList.get(position).status){
            case "yes":
                holder.layout_Yes.setVisibility(View.VISIBLE);
                holder.layout_No.setVisibility(View.GONE);
                holder.layout_Notification.setVisibility(View.GONE);
                break;
            case "no":
                holder.layout_Yes.setVisibility(View.GONE);
                holder.layout_No.setVisibility(View.VISIBLE);
                holder.layout_Notification.setVisibility(View.GONE);
                break;
            case "notification":
                holder.layout_Yes.setVisibility(View.GONE);
                holder.layout_No.setVisibility(View.GONE);
                holder.layout_Notification.setVisibility(View.VISIBLE);
                break;
            default:
                holder.layout_Yes.setVisibility(View.GONE);
                holder.layout_No.setVisibility(View.VISIBLE);
                holder.layout_Notification.setVisibility(View.GONE);
                break;
        }*/

        holder.btn_Rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.rentDialogLoad();
                Toast.makeText( mContext, "You click to rent a car", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btn_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( mContext, "You click Yes", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btn_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( mContext, "You click No", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        Button btn_Yes, btn_No, btn_Rent;
        TextView tv_Name, tv_Date, tv_Why;
        LinearLayout layout_Yes, layout_No, layout_Notification;

        public viewHolder(View itemView) {
            super(itemView);
            btn_Yes = itemView.findViewById(R.id.btn_VehicleYes);
            btn_No = itemView.findViewById(R.id.btn_VehicleNo);
            btn_Rent = itemView.findViewById(R.id.btn_VehicleRent);

            tv_Name = itemView.findViewById(R.id.tv_VehicleName);
            tv_Date = itemView.findViewById(R.id.tv_VehicleDate);
            tv_Why = itemView.findViewById(R.id.tv_VehicleWhy);

            layout_Yes = itemView.findViewById(R.id.layout_VehicleYes);
            layout_No = itemView.findViewById(R.id.layout_VehicleNo);
            layout_Notification = itemView.findViewById(R.id.layout_VehicleNotification);

        }
    }
}
