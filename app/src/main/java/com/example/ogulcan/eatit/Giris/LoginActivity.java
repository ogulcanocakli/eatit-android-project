package com.example.ogulcan.eatit.Giris;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ogulcan.eatit.FeedActivity;
import com.example.ogulcan.eatit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText emailText,sifreText;
    private Button girisBtn;
    private ProgressDialog progressDialog;
    private String username,password;
    public CheckBox beniHatirla;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.edt_email);
        sifreText = findViewById(R.id.edt_sifre);
        girisBtn = findViewById(R.id.btn_giris);
        beniHatirla = findViewById(R.id.chk_benihatirla);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            emailText.setText(loginPreferences.getString("username", ""));
            sifreText.setText(loginPreferences.getString("password", ""));
            beniHatirla.setChecked(true);
        }
    }


    public void girisYap(View view){

        username = emailText.getText().toString();
        password = sifreText.getText().toString();
        if (beniHatirla.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

        progressDialog = new ProgressDialog(LoginActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Kimlik doğrulanıyor...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(emailText.getText().toString(),sifreText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
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
            emailText.setError("Lütfen geçerli bir e-posta adresi giriniz.");
            progressDialog.dismiss();
            valid = false;
        } else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"E-posta adresiniz ya da şifreniz yanlış.",Toast.LENGTH_LONG).show();
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            progressDialog.dismiss();
            sifreText.setError("Şifren en az 6 karakter uzunluğunda olmalıdır.");
            valid = false;
        } else {
            sifreText.setError(null);
            Toast.makeText(getApplicationContext(),"E-posta adresiniz ya da şifreniz yanlış.",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
        return valid;
    }

    public void kayitSayfasi(View view) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
        finish();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void sifreSifirlama(View view) {
        Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
        finish();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onBackPressed() {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    LoginActivity.this);
            alertDialog.setTitle("Uygulamadan çıkılsın mı?");
            alertDialog.setMessage("Uygulamayı ayrılmak istediğinizden emin misiniz?");
            alertDialog.setPositiveButton("Evet",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
            alertDialog.setNegativeButton("Hayır",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
    }



