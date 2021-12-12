package cz.familycarrent.program;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import cz.familycarrent.api.Family;
import cz.familycarrent.api.MembersList_Adapter;
import cz.familycarrent.api.VehicleList_Adapter;

public class FamilyActivity extends AppCompatActivity {

    TextView tv_Name, tv_Message;
    EditText et_RenameText;
    ImageView iv_RenameCancel, iv_RenameChange;
    RecyclerView rv_Members;
    LinearLayout layout_Rename, layout_RenameEdit, layout_Invite, layout_Delete, layout_Create;

    static FamilyActivity familyActivity;
    Dialog dialog;

    MembersList_Adapter mL_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        familyActivity = this;

        rv_Members = findViewById(R.id.rv_FamilyMembers);
        tv_Name = findViewById(R.id.tv_FamilyName);
        tv_Message = findViewById(R.id.tv_FamilyMessage);
        layout_Rename = findViewById(R.id.layout_FamilyRename);
        layout_RenameEdit = findViewById(R.id.layout_FamilyRenameEdit);
        layout_Invite = findViewById(R.id.layout_FamilyInvite);
        layout_Delete = findViewById(R.id.layout_FamilyDelete);
        layout_Create = findViewById(R.id.layout_FamilyCreate);
        iv_RenameCancel = findViewById(R.id.iv_FamilyRenameCancel);
        iv_RenameChange = findViewById(R.id.iv_FamilyRenameChange);
        et_RenameText = findViewById(R.id.et_FamilyRenameText);
        dialog = new Dialog(this);

        updateUI(MainActivity.getFamily());

        layout_Rename.setOnClickListener(v -> {
            layout_Rename.setVisibility(View.GONE);
            layout_RenameEdit.setVisibility(View.VISIBLE);
        });
        iv_RenameCancel.setOnClickListener(v -> {
            layout_Rename.setVisibility(View.VISIBLE);
            layout_RenameEdit.setVisibility(View.GONE);
            et_RenameText.setText("");
        });
        iv_RenameChange.setOnClickListener(v -> {
            Family family = MainActivity.getFamily();
            family.setName(et_RenameText.getText().toString());
            MainActivity.mainActivity.saveFamily(family);

            layout_Rename.setVisibility(View.VISIBLE);
            layout_RenameEdit.setVisibility(View.GONE);
            et_RenameText.setText("");

            updateUI(MainActivity.getFamily());
        });
        layout_Delete.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            MainActivity.getFamilyLiveUpdate().remove();

            db.collection("Family").document(MainActivity.getFamily().getId())
                    .delete()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Family was deleted", Toast.LENGTH_LONG).show();
                        SharedPreferences preferences = getSharedPreferences("Database", MODE_PRIVATE);
                        preferences.edit().putString("id", "").commit();

                        MainActivity.setFamily(new Family());
                        updateUI(MainActivity.getFamily());
                    }).addOnFailureListener(e -> {
                Toast.makeText(this, "Family can't deleted because: " + e.getMessage(), Toast.LENGTH_LONG).show();
                MainActivity.getMainActivity().liveUpdateFamily(MainActivity.getFamily().getId());

            });
        });

        layout_Create.setOnClickListener(v -> {
            createFamilyDialog("create");
        });

        layout_Invite.setOnClickListener(v -> {
            createFamilyDialog("invite");
        });


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    public static FamilyActivity getFamilyActivity() {
        return familyActivity;
    }

    public void updateUI(Family familyObj) {
        tv_Name.setText(MainActivity.getFamily().getName());

        if (MainActivity.getFamily().getMembers().size() != 0) {
            tv_Message.setVisibility(View.GONE);
            rv_Members.setVisibility(View.VISIBLE);
            layout_Delete.setVisibility(View.VISIBLE);
            layout_Invite.setVisibility(View.VISIBLE);
            layout_Rename.setVisibility(View.VISIBLE);
            layout_Create.setVisibility(View.GONE);

            mL_adapter = new MembersList_Adapter(this, familyObj.getMembers());
            rv_Members.setAdapter(mL_adapter);
            rv_Members.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        }else {
            tv_Message.setVisibility(View.VISIBLE);
            rv_Members.setVisibility(View.GONE);
            layout_Delete.setVisibility(View.GONE);
            layout_Invite.setVisibility(View.GONE);
            layout_Rename.setVisibility(View.GONE);
            layout_RenameEdit.setVisibility(View.GONE);
            layout_Create.setVisibility(View.VISIBLE);
        }
    }

    public void createFamilyDialog(String type) {
        dialog.setContentView(R.layout.item_create);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_Create = dialog.findViewById(R.id.btn_CreateCreate);
        Button btn_Cancel = dialog.findViewById(R.id.btn_CreateCancel);
        EditText et_Name = dialog.findViewById(R.id.et_CreateName);
        TextView tv_HeadLine = dialog.findViewById(R.id.tv_CreateHeadLine);

        if (type == "create")
            tv_HeadLine.setText("Create Family");
        else if (type == "invite")
            tv_HeadLine.setText("Add Member");

        dialog.setCancelable(false);

        btn_Create.setOnClickListener(v -> {
            if(type == "create") {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                Family family = new Family();
                family.setName(et_Name.getText().toString());
                ArrayList<String> members = new ArrayList<>();
                members.add(mAuth.getCurrentUser().getEmail());
                family.setMembers(members);

                MainActivity.getMainActivity().saveFamily(family);
                MainActivity.getMainActivity().liveUpdateFamily(MainActivity.getFamily().getId());
                //finish();
            }else if (type == "invite"){
                Family family = MainActivity.getFamily();
                family.getMembers().add(et_Name.getText().toString());

                MainActivity.getMainActivity().saveFamily(family);
            }else
                Toast.makeText(this, "Dialog error", Toast.LENGTH_SHORT).show();

            dialog.dismiss();
        });
        btn_Cancel.setOnClickListener(v -> dialog.dismiss());


        dialog.show();
    }
}