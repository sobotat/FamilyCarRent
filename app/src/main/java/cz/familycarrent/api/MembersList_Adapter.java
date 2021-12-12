package cz.familycarrent.api;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cz.familycarrent.program.MainActivity;
import cz.familycarrent.program.R;

public class MembersList_Adapter extends RecyclerView.Adapter<MembersList_Adapter.viewHolder> {

    Context mContext;
    Activity mActivity;
    ArrayList<String> membersList;

    public MembersList_Adapter(Activity mActivity, ArrayList<String> membersList) {
        this.mActivity = mActivity;
        this.mContext = mActivity;
        this.membersList = membersList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View v = layoutInflater.inflate(R.layout.item_family, parent, false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(MembersList_Adapter.viewHolder holder, int position) {

        holder.tv_Email.setText(membersList.get(position));
        holder.iv_Delete.setOnClickListener(v -> {

            Family family = MainActivity.getFamily();
            ArrayList<String> newMembers = new ArrayList<>();
            for (int i = 0; i < family.getMembers().size(); i++) {
                if (i != position)
                    newMembers.add(family.getMembers().get(i));
            }
            family.setMembers(newMembers);
            MainActivity.getMainActivity().saveFamily(family);

            Toast.makeText(mContext, "You click deleted one member", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView iv_Delete;
        TextView tv_Email;

        public viewHolder(View itemView) {
            super(itemView);
            iv_Delete = itemView.findViewById(R.id.iv_FamilyItemDelete);
            tv_Email = itemView.findViewById(R.id.tv_FamilyItemEmail);

        }
    }
}
