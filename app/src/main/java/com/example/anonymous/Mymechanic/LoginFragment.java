package com.example.anonymous.Mymechanic;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements  View.OnClickListener{
private TextInputLayout emaillayout,pwdlayout;
private AppCompatEditText emailedit,pwdedit;
private TextView forgotpwd,signUp;
private Button loginBtn;
private FirebaseAuth firebaseAuth;
  public   FirebaseUser user;
    FirebaseNetworkException networkException;
    ProgressDialog progressDialog;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v =inflater.inflate(R.layout.fragment_login, container, false);
         emaillayout=(TextInputLayout) v.findViewById(R.id.emaillayout);
         pwdlayout=(TextInputLayout) v.findViewById(R.id.pwdlayout);
         emailedit=(AppCompatEditText) v.findViewById(R.id.emailedittxt);
         pwdedit=(AppCompatEditText) v.findViewById(R.id.pwdedittxt);
         forgotpwd=(TextView) v.findViewById(R.id.loginforgotpass);
         signUp=(TextView) v.findViewById(R.id.loginsignuptxt);
         loginBtn=(Button) v.findViewById(R.id.loginBtn);
         forgotpwd.setOnClickListener(this);

         signUp.setOnClickListener(this);
         loginBtn.setOnClickListener(this);

        return v;

    }
    public void onClick(View view){
        FragmentManager fm=getActivity().getSupportFragmentManager();
int id=view.getId();
switch(id) {
    case R.id.loginforgotpass:
        fm.beginTransaction().replace(R.id.loginContainer, new ForgotPassFragment(), null).commit();
        break;
    case R.id.loginsignuptxt:
        fm.beginTransaction().replace(R.id.loginContainer, new RegisterFragment(), null).commit();
        break;
    case R.id.loginBtn:
        onLogin(emailedit.getText().toString(), pwdedit.getText().toString());
        break;
}}

    private void onLogin(final String email, String password) {
        if(emailedit.getText().toString().equalsIgnoreCase("")||pwdedit.getText().toString().equalsIgnoreCase("")) {
            Snackbar snackbar= Snackbar.make(getView(),"blanks not allowed ", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else{
            showProgressDialog(true,500);
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
if(!task.isSuccessful()){
    emaillayout.setErrorEnabled(true);
    emaillayout.setError("unable");
}
                        if(task.isSuccessful()){
                            Snackbar snackbar= Snackbar.make(getView(),"please wait as we log you in... ", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            startActivity(new Intent(getActivity(),HomeActivity.class));
                            user=firebaseAuth.getCurrentUser();
                        }
                        else if(FirebaseNetworkException.class!=null){
                            Snackbar snackbar= Snackbar.make(getView(),"no network please get connected then try again", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        else{
                            emaillayout.setErrorEnabled(true);
                            emaillayout.setError("Wrong password or email" +task.getException().toString());
                            emailedit.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
emaillayout.setErrorEnabled(false);
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });
                        }
                    }
                });
            }
    }






    private void showProgressDialog(boolean show, long time) {
        try {
            if (progressDialog != null) {
                if (show) {
                    progressDialog.setMessage("Cargando...");
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if(progressDialog!=null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Couldn't connect, please try again later.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, time);
                } else {
                    progressDialog.dismiss();
                }
            }
        }catch(IllegalArgumentException e){
        }catch(Exception e){
        }
    }

}


