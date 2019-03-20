package com.example.anonymous.Mymechanic;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by anonymous on 10/30/15.
 */
public class MechanicFragment extends Fragment {

RatingBar ratingBar;
    private DatabaseReference dbreference,forSpinner;
    StorageReference storageReference;
    private final  String TAG = "ListScientistsFragment";
    private  ArrayList<MechanicModel> mScientists;
    private RecyclerView mScientistRecyclerView;
    private ScientistAdapter mAdapter;
    Spinner serviceIdSpinner;
    private int PICK_PHOTO_REQUEST_CODE=90;
    Uri photopath,downloadUrl;
    public static final String USER_ID = "";
    float rating;

EditText nameedittxt,emailtxt,numbertxt,servicetxt,uNametxtt;
Button addMechanic;
ImageView addmechPic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    storageReference= FirebaseStorage.getInstance().getReference();
    forSpinner=FirebaseDatabase.getInstance().getReference("Services");
        dbreference= FirebaseDatabase.getInstance().getReference("Mechanics");
        mScientists = new ArrayList<>();
dbreference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mScientists.clear();
        for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {

            MechanicModel serviceList = serviceSnapshot.getValue(MechanicModel.class);
            mScientists.add(serviceList);
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mechanic, container, false);
        serviceIdSpinner=(Spinner) view.findViewById(R.id.serviceSpinner);

        emailtxt=(EditText) view.findViewById(R.id.mechanicEmailtxt) ;
        numbertxt=(EditText) view.findViewById(R.id.mechanicNumbertxt);
        uNametxtt=(EditText) view.findViewById(R.id.MechanicNamtxt);
        ratingBar=(RatingBar) view.findViewById(R.id.mechanicRating);
        addmechPic=(ImageView) view.findViewById(R.id.addmechpicimageview);
        addMechanic=(Button) view.findViewById(R.id.addMechanic_Btn);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating=ratingBar.getRating();
            }
        });
        mScientistRecyclerView = (RecyclerView) view
                .findViewById(R.id.mechanic_fragment_recyclerview);
        mScientistRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        mScientistRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addmechPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });
        addMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
                addMovie();
            }
        });
        populateSpinner();
        updateUI();
        return view;
    }
    private void updateUI(){
        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mScientists.clear();
                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {

                    MechanicModel serviceList = serviceSnapshot.getValue(MechanicModel.class);
                    mScientists.add(serviceList);
                    mAdapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAdapter = new ScientistAdapter(mScientists);
        mScientistRecyclerView.setAdapter(mAdapter);
    }
    private class ScientistHolder extends RecyclerView.ViewHolder{
        private MechanicModel mScientist;
        public ImageView mImageView;
        public TextView mNameTextView;
        public RatingBar mBirthDeathTextView;

        public ScientistHolder(View itemView){
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
            mNameTextView = (TextView) itemView.findViewById(R.id.textview_name);
            mBirthDeathTextView = (RatingBar) itemView.findViewById(R.id.textview_birth_death);
            itemView.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                   // Toast.makeText(getActivity(),
                            //mNameTextView.getText().toString() + " clicked!", Toast.LENGTH_SHORT)
                            //.show();
                    Intent intent=new Intent(getActivity(),MasterMechanicActivi.class);
                    intent.putExtra(USER_ID, mNameTextView.getText().toString().trim());
startActivity(intent);
                }
            });

        }

    }
    private class ScientistAdapter extends RecyclerView.Adapter<ScientistHolder>{
        private ArrayList<MechanicModel> mScientists;
        public ScientistAdapter(ArrayList<MechanicModel> Scientists){
            mScientists = Scientists;
        }
        @Override
        public ScientistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.mechanic_list_items,parent,false);
            return new ScientistHolder(view);
        }
        @Override
        public void onBindViewHolder(ScientistHolder holder, int position) {
            MechanicModel s = mScientists.get(position);
            // mImageView.setImageResource(s.getImageId());
            Glide.with(getActivity()).load(s.getPickUrl()).into(holder.mImageView);
            holder.mNameTextView.setText(s.getMechname());
            holder.mBirthDeathTextView.setRating(s.getRating());
        }
        @Override
        public int getItemCount() {
            return mScientists.size();
        }
    }
    private void addMovie() {
        String name=uNametxtt.getText().toString().trim();
String email=emailtxt.getText().toString().trim();
String number=numbertxt.getText().toString().trim();
String service=serviceIdSpinner.getSelectedItem().toString();

float nowrate=rating;

        if(!TextUtils.isEmpty(email) || downloadUrl!=null ){
            String picurl=downloadUrl.toString();
            String id=dbreference.push().getKey();
            MechanicModel newmovie=new MechanicModel(id,name,email,number,picurl,service,nowrate);
            dbreference.child(id).setValue(newmovie);
            Toast.makeText(getActivity(),"added successfully",Toast.LENGTH_LONG).show();}

        else{
            Toast.makeText(getActivity(),"Opps!! something happend ,not added",Toast.LENGTH_LONG).show();
        }}
    public void choosePhoto() {
        Intent photointent=new Intent();
        photointent.setType("image/*");
        photointent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(photointent,"Choose a Service Photo"),PICK_PHOTO_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PICK_PHOTO_REQUEST_CODE && resultCode==RESULT_OK && data!=null&& data.getData()!=null){
            photopath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),photopath);
                addmechPic.setImageBitmap(bitmap);
                uploadPhoto();

            } catch (IOException e) {
                Toast.makeText(getActivity(),"Unable to load photo please try again..",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void uploadPhoto(){
        StorageReference riversRef = storageReference.child("images/"+new Date().getTime()+".jpg");
        if(photopath!=null){
            riversRef.putFile(photopath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            //downloadUrl= taskSnapshot.getDownloadUrl();
                            downloadUrl=taskSnapshot.getMetadata().getDownloadUrl();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Snackbar snackbar= Snackbar.make(getView(),"Cannt upload to Firebase...try another photo", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
        }else{
            Toast.makeText(getActivity(),"EmptyFilepath please select a photo..",Toast.LENGTH_LONG).show();
        }
}
    public void populateSpinner(){
        forSpinner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("name").getValue(String.class);
                    areas.add(areaName);
                }


                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                serviceIdSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}