package com.example.ogulcan.eatit.Giris;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ogulcan.eatit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText input_email;
    private Button btnResetPass;
    ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        input_email = findViewById(R.id.edt_email);
        btnResetPass = findViewById(R.id.btn_sifirla);

        auth = FirebaseAuth.getInstance();

    }

    public void onClick(View view) {
        if(view.getId() == R.id.link_giris)
        {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
        else  if(view.getId() == R.id.btn_sifirla)
        {
            resetPassword(input_email.getText().toString());
        }
    }

    private void resetPassword(final String email) {
        progressDialog = new ProgressDialog(ForgotPassword.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Lütfen Bekelyin...");
        progressDialog.show();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(ForgotPassword.this, "E-postaya bir şifre gönderdik:" + email, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivityForResult(intent,0);
                            finish();
                            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(ForgotPassword.this, "Şifre gönderilemedi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void girisSayfası(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent,0);
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}