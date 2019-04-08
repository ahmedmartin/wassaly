package com.example.martin.waslny;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class sign_up extends AppCompatActivity {

    private Intent sign_in;
    private Intent main ;

    private TextView fname;
    private TextView lname;
    private TextView email;
    private TextView password;
    private TextView address;
    private TextView phone;

    private String Fname;
    private String Lname;
    private String Email;
    private String Password;
    private String Address;
    private String Phone;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private order w_order=new order();

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verification_callbacks;
    private PhoneAuthProvider.ForceResendingToken resend_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fname = findViewById(R.id.sign_up_fname);
        lname = findViewById(R.id.sign_up_lname);
        email = findViewById(R.id.sign_up_email);
        password =findViewById(R.id.sign_up_password);
        address = findViewById(R.id.sign_up_address);
        phone = findViewById(R.id.sign_up_phone);

        sign_in = new Intent(this,sign_in.class);
        main = new Intent(this,Main.class);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();


    }

    public void sign_in(View view) {
       startActivity(sign_in);
       finish();
    }

    ProgressBar bar;
    public void submit(View view) {
        if(entry_fill()){
            bar = findViewById(R.id.progressBar2);
            bar.setVisibility(View.VISIBLE);
            Verify_phone_number();

        }

    }



    private boolean entry_fill() {
        Fname = fname.getText().toString();
        Lname = lname.getText().toString();
        Email = email.getText().toString();
        Password=password.getText().toString();
        Address = address.getText().toString();
        Phone = phone.getText().toString();

         if(TextUtils.isEmpty(Fname)){
             Toast.makeText(this," First name Is Required",Toast.LENGTH_SHORT).show();
             return false;
         }else if(TextUtils.isEmpty(Lname)){
             Toast.makeText(this," Last name Is Required",Toast.LENGTH_SHORT).show();
             return false;
         }else if(TextUtils.isEmpty(Email)){
             Toast.makeText(this," Email Is Required",Toast.LENGTH_SHORT).show();
             return false;
         }else if(TextUtils.isEmpty(Password)){
             Toast.makeText(this," Password Is Required should be more than 6 ",Toast.LENGTH_SHORT).show();
             return false;
         }else if(TextUtils.isEmpty(Address)){
             Toast.makeText(this," Address Is Required",Toast.LENGTH_SHORT).show();
             return false;
         }else if(TextUtils.isEmpty(Phone)) {
             Toast.makeText(this, " Phone Is Required", Toast.LENGTH_SHORT).show();
             return false;
         }else if(!TextUtils.isEmpty(Phone)){
             if(Phone.length()!= 11){
                 Toast.makeText(sign_up.this," Please  phone contain 11 number",Toast.LENGTH_LONG).show();
                 return false;
             }

         }
         return true;
    }

    private void signup(){
        mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign up success, update UI with the signed-in user's information
                    mAuth=FirebaseAuth.getInstance();
                    FirebaseUser current = mAuth.getCurrentUser();
                    String user_id = current.getUid().toString();
                    user User = new user(Fname,Lname,Address,Phone);
                    myRef.child("user").child(user_id).child("personalinfo").setValue(User);
                    Intent main = new Intent(sign_up.this, Main.class);
                    main.putExtra("order",w_order);
                    startActivity(main);
                    finish();
                    Toast.makeText(sign_up.this, "We Are Happy For Sign Our App", Toast.LENGTH_SHORT).show();
                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(sign_up.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void Verify_phone_number() {
        String phone_number ="+2"+ phone.getText().toString();
        setupverificationcallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_number, 2, TimeUnit.MINUTES, this, verification_callbacks);
    }

    String phone_id;

    private void setupverificationcallbacks() {
        verification_callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential Credential) {

               signup();

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(sign_up.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(String Verification_Id, PhoneAuthProvider.ForceResendingToken Token) {
                resend_token = Token;
                phone_id=Verification_Id;
                bar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                Toast.makeText(sign_up.this, s, Toast.LENGTH_SHORT).show();
            }
        };
    }
}
