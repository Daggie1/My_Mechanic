package com.example.anonymous.Mymechanic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassFragment extends Fragment {
    private TextInputLayout emaillayout;
    private AppCompatEditText emailedit;
    private Button submit;
    private FirebaseAuth auth;

    public ForgotPassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_forgotpass, container, false);
        emaillayout=(TextInputLayout) v.findViewById(R.id.forgotpassemaillayout);

        emailedit=(AppCompatEditText) v.findViewById(R.id.forgotpassEmailedit);
        submit=(Button) v.findViewById(R.id.submitforgotpassBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
auth.sendPasswordResetEmail(emailedit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {

    }
});
            }
        });
        return v;


    }
}
