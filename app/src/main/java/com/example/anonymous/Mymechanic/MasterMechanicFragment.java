package com.example.anonymous.Mymechanic;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MasterMechanicFragment extends Fragment {
   ImageView messageimageView,callImageview;
   CircleImageView accountImage;
   Query querydb;
    private static final String SMS_SENT_INTENT_FILTER = "com.yourapp.sms_send";
    private static final String SMS_DELIVERED_INTENT_FILTER = "com.yourapp.sms_delivered";
   String pirealUrl;
     String accountNumber;
   TextView Accountxtt;
    public String accountname;
    String intentId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentId=getActivity().getIntent().getStringExtra(MechanicFragment.USER_ID);
        querydb= FirebaseDatabase.getInstance().getReference("Mechanics").orderByChild("mechname").equalTo(intentId);
        querydb.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot all:dataSnapshot.getChildren()){
                    MechanicModel model=all.getValue(MechanicModel.class);
                    pirealUrl=model.getPickUrl();
                    accountname=model.getMechname();
                    //Toast.makeText(getActivity(),pirealUrl,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mechanic_master_detail_flow_,container,false);
        querydb.addListenerForSingleValueEvent(new ValueEventListener() {




            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot all:dataSnapshot.getChildren()){
                    MechanicModel model=all.getValue(MechanicModel.class);
                    pirealUrl=model.getPickUrl();
                    accountname=model.getMechname();
                    accountNumber=model.getMechnumber();
                   // Toast.makeText(getActivity(),accountname,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Accountxtt=(TextView) view.findViewById(R.id.accountnametxt);
        messageimageView=(ImageView) view.findViewById(R.id.messageImageView);
        callImageview=(ImageView) view.findViewById(R.id.callImageView);
        accountImage=(CircleImageView) view.findViewById(R.id.accountCircularImage);
        Accountxtt.setText(intentId);

       //Glide.with(getActivity()).load(pirealUrl).into(accountImage);

        messageimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Uri uri = Uri.parse("smsto:" +accountNumber);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "hey please come and repair my car");
                startActivity(intent);
            }
        });
        callImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+accountNumber));
                startActivity(callIntent);
            }
        });
        return view;
    }
}
