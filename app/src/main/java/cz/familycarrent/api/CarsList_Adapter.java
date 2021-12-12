package cz.familycarrent.api;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cz.familycarrent.program.MainActivity;
import cz.familycarrent.program.R;

public class CarsList_Adapter extends RecyclerView.Adapter<CarsList_Adapter.viewHolder> {

    Context mContext;
    Activity mActivity;
    ArrayList<Vehicle> vehicleList;

    public CarsList_Adapter(Activity mActivity, ArrayList<Vehicle> vehicleList) {
        this.mActivity = mActivity;
        this.mContext = mActivity;
        this.vehicleList = vehicleList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View v = layoutInflater.inflate(R.layout.item_family, parent, false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {

        holder.tv_Name.setText(vehicleList.get(position).getName());
        holder.iv_Delete.setOnClickListener(v -> {

            Family family = MainActivity.getFamily();
            ArrayList<Vehicle> newVehicles = new ArrayList<>();
            for (int i = 0; i < family.getMembers().size(); i++) {
                if (i != position)
                    newVehicles.add(family.getVehicles().get(i));
            }
            family.setVehicles(newVehicles);
            MainActivity.getMainActivity().saveFamily(family);

            Toast.makeText(mContext, "Car was deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView iv_Delete;
        TextView tv_Name;

        public viewHolder(View itemView) {
            super(itemView);
            iv_Delete = itemView.findViewById(R.id.iv_FamilyItemDelete);
            tv_Name = itemView.findViewById(R.id.tv_FamilyItemEmail);

        }
    }
}
