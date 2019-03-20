package com.example.anonymous.Mymechanic;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import static android.app.Activity.RESULT_OK;


public class NonAdminPlaying extends Fragment {
    public static final String SERVICE_NAME = "";
    private static final String MOVIE_NAME_CONST ="movie_Name_contatnt";
    private  Uri photopath;
    private static final int PICK_PHOTO_REQUEST_CODE = 0700;
    Button addService;
    private Uri downloadUrl;
    Service serviceModel;
    EditText serviceName,mintxt,maxtxt;
    RadioGroup availabilityRadiogroup;
    RadioButton availableradio,comingSoonRadio;
    ImageView addpicImageView;

    //Query dbref;
    StorageReference photoref;
    DatabaseReference dbreference;
    ArrayList<Service> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // initializeList();
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Now playing Movies");

        photoref= FirebaseStorage.getInstance().getReference();
        dbreference=FirebaseDatabase.getInstance().getReference("Services");
        //dbref= FirebaseDatabase.getInstance().getReference("Movies").orderByChild("availability").equalTo(true);
        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listitems.clear();
                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {

                    Service serviceList = serviceSnapshot.getValue(Service.class);
                    listitems.add(serviceList);


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
        View view=inflater.inflate(R.layout.playing_non_admin,container,false);



        MyRecyclerView = (RecyclerView) view.findViewById(R.id.mechcardView2);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() >=0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<Service> list;

        public MyAdapter(ArrayList<Service> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_items_non_admin, parent, false);
            MyViewHolder holder = new MyViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            serviceModel=listitems.get(position);

            holder.titleTextView.setText(serviceModel.getName());

            Glide.with(getActivity()).load(serviceModel.getPicUrl()).into(holder.coverImageView);
            holder.pricetxt.setText(serviceModel.getPriceRange());




            //holder.coverImageView.setImageResource(list.get(position).getImageResourceId());
            //holder.coverImageView.setTag(list.get(position).getImageResourceId());
            //holder.likeImageView.setTag(R.drawable.trailerplay);


        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView,pricetxt;
        public ImageView coverImageView;

        public Button chooseBtn;

        public MyViewHolder(View v) {
            super(v);

            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            pricetxt=(TextView) v.findViewById(R.id.pricetxtv);

            chooseBtn = (Button) v.findViewById(R.id.btnserviceChoose);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(),MechanicActivity.class);
                    intent.putExtra(SERVICE_NAME,titleTextView.getText().toString());
                    startActivity(intent);
                }
            });



            chooseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),MechanicActivity.class);
                    intent.putExtra(SERVICE_NAME,titleTextView.getText().toString());
                    startActivity(intent);

//new BookFragment().bookMovie(getActivity(),titleTextView.getText().toString());


                    //

                    /*Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(coverImageView.getId())
                            + '/' + "drawable" + '/' + getResources().getResourceEntryName((int)coverImageView.getTag()));


                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));*/



                }
            });



        }
    }










    }











