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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class PlayingAdminFragment extends Fragment {
    public static final String SERVICE_NUMBER = "";
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
        View view=inflater.inflate(R.layout.admin_fragment_playing,container,false);
        serviceName=(EditText) view.findViewById(R.id.serviceNamemechtxt);
mintxt=(EditText) view.findViewById(R.id.mintxt);
maxtxt=(EditText) view.findViewById(R.id.maxtxt);
        addService=(Button) view.findViewById(R.id.addService_Btn);
        addpicImageView=(ImageView) view.findViewById(R.id.addpicimageview);

        addpicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });
        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();

                addMovie();
            }
        });
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerMechanic);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() >=0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }
    private void addMovie() {
        String moviename=serviceName.getText().toString().trim();

String priceRange=mintxt.getText().toString().trim()+"/="+"-"+maxtxt.getText().toString().trim()+"/=";

        if(!TextUtils.isEmpty(moviename) || downloadUrl!=null){
           // String picurl= downloadUrl.toString();
            String id=dbreference.push().getKey();
            Service newmovie=new Service(id,moviename,"",priceRange);
            dbreference.child(id).setValue(newmovie);
            Toast.makeText(getActivity(),"added successfully",Toast.LENGTH_LONG).show();}

        else{
            Toast.makeText(getActivity(),"Opps!! something happend ,not added",Toast.LENGTH_LONG).show();
        }}

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
                    .inflate(R.layout.recycle_items, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Toast.makeText(getActivity(),listitems.get(view.getId()).getMovieName()+"",Toast.LENGTH_LONG).show();
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            serviceModel=listitems.get(position);

            holder.titleTextView.setText(serviceModel.getName());

            Glide.with(getActivity()).load(serviceModel.getPicUrl()).into(holder.coverImageView);


            String id=listitems.get(position).getId();

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

        public TextView titleTextView;
        public ImageView coverImageView,editImageview,deleteImageView;

        public Button chooseBtn;

        public MyViewHolder(View v) {
            super(v);

            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
editImageview=(ImageView) v.findViewById(R.id.editImageView);
deleteImageView=(ImageView) v.findViewById(R.id.delImageView);

            chooseBtn = (Button) v.findViewById(R.id.btnserviceChoose);
            editImageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),titleTextView.getText().toString()+"", Toast.LENGTH_LONG).show();
                }
            });



            chooseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),MechanicActivity.class);
                    intent.putExtra(SERVICE_NUMBER,"iuui");
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
                addpicImageView.setImageBitmap(bitmap);
                uploadPhoto();

            } catch (IOException e) {
                Toast.makeText(getActivity(),"Unable to load photo please try again..",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void uploadPhoto(){
        StorageReference riversRef = photoref.child("images/"+new Date().getTime()+".jpg");
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
        }}

}