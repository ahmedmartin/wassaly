package com.example.martin.waslny;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class person_info extends AppCompatActivity {

    private String uid;
    private String uemail;

    private DatabaseReference myref;

    private EditText fname;
    private EditText lname;
    private TextView email;
    private EditText address;
    private EditText phone;

    private user User ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        fname=findViewById(R.id.edit_fname);
        lname=findViewById(R.id.edit_lname);
        email=findViewById(R.id.edit_email);
        address=findViewById(R.id.edit_address);
        phone=findViewById(R.id.edit_phone);

        uid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        uemail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        myref= FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("personalinfo");

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue(user.class);
                user use = dataSnapshot.getValue(user.class);
                fname.setText(use.getFname());
                lname.setText(use.getLname());
                address.setText(use.getAddress());
                phone.setText(use.getPhone());
                email.setText(uemail);
            }
            public void onCancelled(DatabaseError databaseError) {}
        });

    }



    public void edit(View view) {
        if(check_fildes()&&connected_networt()){
            User=new user(fname.getText().toString(),lname.getText().toString(),address.getText().toString(),phone.getText().toString());
            myref.setValue(User);
            Toast.makeText(person_info.this," updated successful",Toast.LENGTH_LONG).show();
        }else
            Toast.makeText(this, " Please Check Your Network Connection ", Toast.LENGTH_SHORT).show();
    }


    boolean connected_networt(){
        ConnectivityManager mang = (ConnectivityManager) this.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(mang!=null){
            NetworkInfo inf = mang.getActiveNetworkInfo();
            if(inf!=null){
                if(inf.isConnected())
                    return true;
            }
        }
        return false;
    }

    public boolean check_fildes(){
        if(TextUtils.isEmpty(fname.getText().toString())){
            Toast.makeText(person_info.this," Please enter first name",Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(lname.getText().toString())){
            Toast.makeText(person_info.this," please enter last name",Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(address.getText().toString())){
            Toast.makeText(person_info.this," please enter address",Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(phone.getText().toString())){
            Toast.makeText(person_info.this," please enter phone",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void change_password(View view) {

        final EditText text = new EditText(person_info.this);
        text.setHint("new Password ..");
        text.setGravity(Gravity.CENTER);
        text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AlertDialog.Builder alart = new AlertDialog.Builder(person_info.this);
        alart.setMessage("Please Enter your new Password");
        alart.setView(text);
        alart.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(TextUtils.isEmpty(text.getText().toString())){
                    Toast.makeText(person_info.this, "no thing happened ", Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(text.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete( Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(person_info.this, "your Password changed Successfully", Toast.LENGTH_LONG).show();
                                FirebaseAuth mauth=FirebaseAuth.getInstance();
                                mauth.signOut();
                                Intent sign_in=new Intent(person_info.this,sign_in.class);
                                sign_in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                                startActivity(sign_in);
                            }else
                                Toast.makeText(person_info.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        alart.show();

    }
}
