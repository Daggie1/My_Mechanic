package com.example.anonymous.Mymechanic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ServicesFragment extends Fragment {

private Uri photopath;
    private static final int PICK_PHOTO_REQUEST_CODE = 0700;
    Button addServiceBtn;
    Service service;
    EditText servicenameedittxt;
    ImageView addpicImageView;
    Uri downloadUrl;
    StorageReference photoref;
    DatabaseReference dbreference;
    ArrayList<Service> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
       // initializeList();
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Our Services");
        photoref= FirebaseStorage.getInstance().getReference();
        dbreference= FirebaseDatabase.getInstance().getReference("Services");
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
       View view=inflater.inflate(R.layout.service_fragment,container,false);
        servicenameedittxt=(EditText) view.findViewById(R.id.servicenametxt);
        addServiceBtn=(Button) view.findViewById(R.id.addserviceBtn);
        addpicImageView=(ImageView) view.findViewById(R.id.addpicimageview);

        addpicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });
        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addService();
            }
        });
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() >=0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);


        return view;
    }
    private void addService() {
        String servicename=servicenameedittxt.getText().toString().trim();
        String picurl=downloadUrl.toString();
        if(!TextUtils.isEmpty(servicename)){
            String id=dbreference.push().getKey();
            Service newService=new Service(id,servicename,picurl);
            dbreference.child(id).setValue(newService);
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
Toast.makeText(getActivity(),service.getId(),Toast.LENGTH_LONG).show();
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
service=listitems.get(position);
            holder.priceRangeTxt.setText("797");
            holder.titleTextView.setText(service.getName());
            Glide.with(getActivity()).load(service.getPicUrl()).into(holder.coverImageView);

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

        public TextView titleTextView,priceRangeTxt;
        public ImageView coverImageView;

        public Button btnChoose;

        public MyViewHolder(View v) {
            super(v);
            //priceRangeTxt=(TextView) v.findViewById(R.id.priceRangetxt);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);

          btnChoose = (Button) v.findViewById(R.id.btnchoose);
            /*likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int id = (int)likeImageView.getTag();
                    if( id == R.drawable.trailerplay){

                        likeImageView.setTag(R.drawable.trailerplay1);

                        likeImageView.setImageResource(R.drawable.trailerplay1);

                        Toast.makeText(getActivity(),titleTextView.getText()+" trailer Unavailable",Toast.LENGTH_SHORT).show();

                    }else{

                        likeImageView.setTag(R.drawable.trailerplay);
                        likeImageView.setImageResource(R.drawable.trailerplay);
                        Toast.makeText(getActivity(),titleTextView.getText()+" trailer Unavailable",Toast.LENGTH_SHORT).show();


                    }

                }
            });
*/


            btnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




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
               addServiceBtn.setClickable(true);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }
    public void uploadPhoto(){
        StorageReference riversRef = photoref.child("images/rivers.jpg");

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
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }
}

