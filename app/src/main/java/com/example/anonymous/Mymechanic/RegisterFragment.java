package com.example.anonymous.Mymechanic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private TextInputLayout emaillayout,pwdlayout,numberlayout,usernameLayout;
    private AppCompatEditText emailedit,pwdedit,editNumber,userNameedit;
    private TextView Login;
    private Button RegisterBtn;
    private ImageView userImageview;
    private FirebaseAuth auth;
    private Uri photopath
            ,downloadUrl;
    private DatabaseReference dbref;
    private StorageReference photoref;
    int PICK_PHOTO_REQUEST_CODE=57;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        photoref=FirebaseStorage.getInstance().getReference();
        dbref= FirebaseDatabase.getInstance().getReference("users");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_register, container, false);
        userNameedit=(AppCompatEditText) v.findViewById(R.id.signupuserNameedittxt);
        usernameLayout=(TextInputLayout) v.findViewById(R.id.signupusernamelayout);
        emaillayout=(TextInputLayout) v.findViewById(R.id.signupemaillayout);
        pwdlayout=(TextInputLayout) v.findViewById(R.id.signuppwdlayout);
        emailedit=(AppCompatEditText) v.findViewById(R.id.signupemailedittxt);
        pwdedit=(AppCompatEditText) v.findViewById(R.id.signuppwdlayoutpwdedittxt);
        numberlayout=(TextInputLayout) v.findViewById(R.id.numberlayout);
        editNumber=(AppCompatEditText) v.findViewById(R.id.numberedittxt);
        Login=(TextView) v.findViewById(R.id.logintxt);
        RegisterBtn=(Button) v.findViewById(R.id.registerBtn);
        userImageview=(ImageView) v.findViewById(R.id.userImageview);
userImageview.setOnClickListener(this);
        Login.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);
        return v;

    }
    public void onClick(View view){
        FragmentManager fm=getActivity().getSupportFragmentManager();
        int id=view.getId();
        switch(id) {

            case R.id.logintxt:
                fm.beginTransaction().replace(R.id.loginContainer, new LoginFragment(), null).commit();
                break;
            case R.id.registerBtn:
                String email=emailedit.getText().toString().trim();
                String pwd= pwdedit.getText().toString().trim();
                if (email.equalsIgnoreCase("")|| pwd.equalsIgnoreCase("")) {
                    Snackbar snackbar= Snackbar.make(getView(),"blanks not allowed ", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else{
                    auth.createUserWithEmailAndPassword(email,pwd);
               // onRegisterUser("daggyooks@gmail.com","kjiop");
                uploadPhoto();
                registerUser();}
                break;
            case R.id.userImageview:
choosePhoto();
                break;
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
                userImageview.setImageBitmap(bitmap);
               uploadPhoto();

            } catch (IOException e) {
                Toast.makeText(getActivity(),"Unable to load photo please try again..",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void uploadPhoto(){
        StorageReference riversRef = photoref.child("images/"+emailedit.getText().toString()+".jpg");
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



    public void registerUser(){
        String dbId=dbref.push().getKey();
        //String id=auth.getCurrentUser().getUid();
        String Email=emailedit.getText().toString();
        String pwd=pwdedit.getText().toString();
        String photoUrl=downloadUrl.toString();
        String userNumbr=editNumber.getText().toString();
        String userName=userNameedit.getText().toString();
        if(Email.equalsIgnoreCase("")||pwd.equalsIgnoreCase("")||userName.equalsIgnoreCase("" )||downloadUrl==null) {
            Snackbar snackbar= Snackbar.make(getView(),"Blanks not allowed", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else{
        UsersModel usersModel=new UsersModel(dbId,userName,Email,photoUrl,userName,pwd);
        dbref.child(dbId).setValue(usersModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
if(task.isSuccessful()){
    Snackbar snackbar= Snackbar.make(getView(),"Upload Succsesfully", Snackbar.LENGTH_LONG);
    snackbar.show();
}
            }
        });}


    }
}
