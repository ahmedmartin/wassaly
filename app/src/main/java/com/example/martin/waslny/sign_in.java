package com.example.martin.waslny;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class sign_in extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView password;
    private TextView email;
    private Intent sign_up;
    private Intent main;

    private order w_order=new order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        password=findViewById(R.id.sign_in_password);
        email = findViewById(R.id.sign_in_email);
        sign_up= new Intent(this,sign_up.class);
        main= new Intent(this,Main.class);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null ){
            Intent main = new Intent(sign_in.this, Main.class);
            main.putExtra("order",w_order);
            startActivity(main);
            finish();
        }

    }

    public void sign_up(View view) {
        startActivity(sign_up);
        finish();

    }

    public void sign_in(View view) {
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){
            Toast.makeText(this,"please enter all entry ! ",Toast.LENGTH_LONG).show();
        }else {
            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent main = new Intent(sign_in.this, Main.class);
                                main.putExtra("order",w_order);
                                startActivity(main);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(sign_in.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
    public void forget_password(View view) {
        final EditText text = new EditText(sign_in.this);
        text.setHint("your Email ...");
        text.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        text.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
        text.setGravity(Gravity.CENTER);
        AlertDialog.Builder alart = new AlertDialog.Builder(sign_in.this);
        alart.setMessage(" Please Enter your Email and check your Email Box ");
        alart.setView(text);
        alart.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(TextUtils.isEmpty(text.getText().toString())){
                    Toast.makeText(sign_in.this, "Please Enter Email to Continue"+"\n process failed ", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseAuth mAuth =FirebaseAuth.getInstance();
                    mAuth.sendPasswordResetEmail(text.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete( Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(sign_in.this, "Please Check your Email Box ", Toast.LENGTH_SHORT).show();
                            }else
                                Toast.makeText(sign_in.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        alart.show();
    }
}
