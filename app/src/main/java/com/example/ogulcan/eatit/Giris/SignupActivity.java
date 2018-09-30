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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mRef;
    FirebaseDatabase firebaseDatabase;
    private EditText emailText,sifreText;
    private Button kayitOl;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailText = findViewById(R.id.edt_email);
        sifreText = findViewById(R.id.edt_sifre);
        kayitOl = findViewById(R.id.btn_kaydol);

        firebaseDatabase = firebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference();


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    public void kayitOl(View view){
        progressDialog = new ProgressDialog(SignupActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Hesap oluşturuluyor...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emailText.getText().toString(),sifreText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(getApplicationContext(),"Başarılı, Artık giriş yapabilirsiniz.",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!kontrol()) {
                    return;
                }
            }
        });
    }

    public boolean kontrol() {
        boolean valid = true;
        String email = emailText.getText().toString();
        String password = sifreText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            progressDialog.dismiss();
            emailText.setError("Lütfen geçerli bir e-posta adresi giriniz.");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            progressDialog.dismiss();
            sifreText.setError("Şifren en az 6 karakter uzunluğunda olmalıdır.");
            valid = false;
        } else {
            sifreText.setError(null);
        }
        return valid;
    }

    public void girisSayfasi(View view) {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}