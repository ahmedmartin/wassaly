package com.example.martin.waslny;

import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class Main extends AppCompatActivity  {

    private EditText sender_phone ;
    private TextView sender_address;
    private EditText receiver_fname ;
    private EditText receiver_lname ;
    private EditText receiver_phone ;
    private TextView receiver_address;
    private TextView price ;
    private TextView direction;
    private TextView description;
    private LinearLayout update_layout,layout;
    private ScrollView info_scroll;

    private DrawerLayout dl ;
    private ActionBarDrawerToggle abdt;


    private DatabaseReference myref;

    private order w_order;

    private String uid;

    private double sum_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // remote config that get value at time
       final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        remoteConfig.fetch(60*30) // every 30 minut get value from server
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( Task<Void> task) {
                        if (task.isSuccessful()) {
                          //  Toast.makeText(Main.this, "success", Toast.LENGTH_SHORT).show();
                            remoteConfig.activateFetched();
                            try {
                                // get app version name
                                PackageInfo pInfo = Main.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                                String version = pInfo.versionName;
                                // check if app version name != update version in server pop up update required
                                if(!version.equals(remoteConfig.getString("version"))&&! TextUtils.isEmpty(remoteConfig.getString("version"))) {
                                   update_layout.setVisibility(View.VISIBLE);
                                    info_scroll.setVisibility(View.INVISIBLE);
                                    info_scroll.setEnabled(false);

                                }
                            } catch (PackageManager.NameNotFoundException e) {

                            }

                        }else
                        Toast.makeText(Main.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                });


        w_order=(order) getIntent().getSerializableExtra("order");

        sender_phone=findViewById(R.id.sender_phone);
        sender_address=findViewById(R.id.sender_address);
        receiver_fname=findViewById(R.id.receiver_fname);
        receiver_lname=findViewById(R.id.receiver_lname);
        receiver_phone=findViewById(R.id.receiver_phone);
        receiver_address=findViewById(R.id.receiver_address);
        price=findViewById(R.id.price);
        direction=findViewById(R.id.distance);

        update_layout =findViewById(R.id.update_layout);
        info_scroll=findViewById(R.id.info_scroll);
        description=findViewById(R.id.description);
        layout =findViewById(R.id.layout);

        myref= FirebaseDatabase.getInstance().getReference().child("orders").child("wait");
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();


        if(!TextUtils.isEmpty(w_order.getS_phone())) {
            sender_phone.setText(w_order.getS_phone());

        }else{
            final DatabaseReference user=FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("personalinfo");
            user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        user use = dataSnapshot.getValue(user.class);
                        sender_phone.setText(use.getPhone());
                        w_order.setS_phone(use.getPhone());
                    }
                    user.removeEventListener(this);
                }
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
        if(!TextUtils.isEmpty(w_order.getS_address())){
                sender_address.setText(w_order.getS_address());

        }
        if(!TextUtils.isEmpty(w_order.getR_first_name())){
            receiver_fname.setText(w_order.getR_first_name());

        }
        if(!TextUtils.isEmpty(w_order.getR_last_name())) {
            receiver_lname.setText(w_order.getR_last_name());

        }
        if(!TextUtils.isEmpty(w_order.getR_phone())) {
            receiver_phone.setText(w_order.getR_phone());

        }
        if(!TextUtils.isEmpty(w_order.getR_address())) {
            receiver_address.setText(w_order.getR_address());
        }
        if(!TextUtils.isEmpty(w_order.getDescription())){
            description.setText(w_order.getDescription());
        }

        if(!TextUtils.isEmpty(w_order.getR_address())&&!TextUtils.isEmpty(w_order.getS_address())){
           float result[]=new float[10];
            Location.distanceBetween(w_order.getS_lat(),w_order.getS_long(),w_order.getE_lat(),w_order.getE_long(),result);

            int distance = round_up(result[0]/1000);
            if (distance >= 5) {
                distance=distance-5;
                sum_price+=5*5;
                if(distance>=5){
                    distance=distance-5;
                    sum_price+=5*4;
                    if(distance>0)
                        sum_price+=distance*2.5;
                }else
                    sum_price+=distance*4;
            }else
                sum_price+=distance*5;

            price.setText("price : "+sum_price+" L.E");
            direction.setText("distance : "+round_up(result[0]/1000)+" K.M");
            layout.setVisibility(View.VISIBLE);

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(getResources().getColor(R.color.textcolor));

        dl =findViewById(R.id.draw_layout);
        abdt = new ActionBarDrawerToggle(this,dl,toolbar,R.string.open,R.string.close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nav = findViewById(R.id.navigation);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =item.getItemId();
                switch (id){


                    case R.id.person_info :
                        personal_info();
                        break;

                    case R.id.log_out :
                        log_out();
                        break;

                    case R.id.about_us :
                        about_us();
                        break;
                }

                return true;
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void personal_info() {
        if(connected_networt()) {
            Intent person = new Intent(Main.this, person_info.class);
            startActivity(person);
        }else
            Toast.makeText(this, "please check your network connection", Toast.LENGTH_SHORT).show();
    }

    private void about_us() {
        if (connected_networt())
         startActivity(new Intent(Main.this,about_us.class));
        else
            Toast.makeText(this, "please check your network connection", Toast.LENGTH_SHORT).show();
    }

    public int round_up(double number){
        int round =(int) number;
        if(number%round==0){
            return round;
        }else
            return round+1;
    }

    public void send(View view) {

        if(check_fields()&&connected_networt()){
            put_value();
            myref.push().setValue(w_order).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete( Task<Void> task) {
                    if(task.isSuccessful()){
                       /* sender_address.setText("");
                        receiver_fname.setText("");
                        receiver_lname.setText("");
                        receiver_phone.setText("");
                        receiver_address.setText("");
                        description.setText("");*/
                        Toast.makeText(Main.this,"you order is sent successful ",Toast.LENGTH_LONG).show();
                        Intent t = new Intent(Main.this,send_success.class);
                        t.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(t);
                        finish();
                        /*w_order=new order();
                        price.setText(" ");
                        direction.setText(" ");*/
                    }else
                        Toast.makeText(Main.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                }
            });

        }else
            Toast.makeText(this, "please check your network connection", Toast.LENGTH_SHORT).show();


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

    public void sender_address(View view) {
        fill_value();
        Intent map = new Intent(Main.this,MapsActivity.class);
        map.putExtra("order",w_order);
        map.putExtra("type","sender");
        startActivity(map);
    }

    public void receive_address(View view) {
        fill_value();
        Intent map = new Intent(Main.this,MapsActivity.class);
        map.putExtra("order",w_order);
        map.putExtra("type","receiver");
        startActivity(map);
    }



    public void log_out() {
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Intent sign_in=new Intent(Main.this,sign_in.class);
        finish();
        sign_in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(sign_in);

    }




    public void fill_value(){

        if(!TextUtils.isEmpty(sender_phone.getText().toString())) {
            w_order.setS_phone(sender_phone.getText().toString());

        }
        if(!TextUtils.isEmpty(sender_address.getText().toString())){
            w_order.setS_address(sender_address.getText().toString());

        }
        if(!TextUtils.isEmpty(receiver_fname.getText().toString())){
            w_order.setR_first_name(receiver_fname.getText().toString());

        }
        if(!TextUtils.isEmpty(receiver_lname.getText().toString())) {
            w_order.setR_last_name(receiver_lname.getText().toString());

        }
        if(!TextUtils.isEmpty(receiver_phone.getText().toString())) {
            w_order.setR_phone(receiver_phone.getText().toString());

        }
        if(!TextUtils.isEmpty(receiver_address.getText().toString())) {
            w_order.setR_address(receiver_address.getText().toString());
        }
        if(!TextUtils.isEmpty(description.getText().toString())){
            w_order.setDescription(description.getText().toString());
        }
    }

    public boolean check_fields(){
        boolean b = true;
        if(TextUtils.isEmpty(sender_phone.getText().toString())) {
            sender_phone.setError("please enter sender phone");
            b= false;
        }else{
            if(sender_phone.getText().toString().length()!= 11){
                sender_phone.setError("please sender phone contain 11 number");
                b= false;
            }
        }
        if(TextUtils.isEmpty(sender_address.getText().toString())){
            sender_address.setError("please enter sender address");
            b= false;

        }
        if(TextUtils.isEmpty(receiver_fname.getText().toString())){
            receiver_fname.setError("please enter receive first name");
            b= false;
        }
        if(TextUtils.isEmpty(receiver_lname.getText().toString())) {
            receiver_lname.setError("please enter receiver last name");
            b= false;
        }
        if(TextUtils.isEmpty(receiver_phone.getText().toString())) {
            receiver_phone.setError("please enter receiver phone");
            b= false;
        }else{
            if(receiver_phone.getText().toString().length()!= 11){
                receiver_phone.setError("please receiver phone contain 11 number");
                b= false;
            }
        }
        if(TextUtils.isEmpty(receiver_address.getText().toString())) {
            receiver_address.setError("please enter receiver address");
            b= false;
        }
        return b;
    }

    public void put_value(){
        w_order.setR_first_name(receiver_fname.getText().toString());
        w_order.setR_last_name(receiver_lname.getText().toString());
        w_order.setR_address(receiver_address.getText().toString());
        w_order.setR_phone(receiver_phone.getText().toString());
        w_order.setS_phone(sender_phone.getText().toString());
        w_order.setS_address(sender_address.getText().toString());
        w_order.setDescription(description.getText().toString());
        w_order.setPrice(sum_price);
        w_order.setUid(uid);
    }


    @Override
    public void onBackPressed() {
        Intent sign_in=new Intent(this,sign_in.class);
        sign_in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
    }


    public void update(View view) {
        try{
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="+getPackageName())));
            finish();
        }catch (ActivityNotFoundException ex){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName()))); //"com.android.chrome";
            finish();
        }
    }
}
