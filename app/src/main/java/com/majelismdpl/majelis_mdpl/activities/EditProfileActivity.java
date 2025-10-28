package com.majelismdpl.majelis_mdpl.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.majelismdpl.majelis_mdpl.R;
import com.majelismdpl.majelis_mdpl.utils.SharedPrefManager;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etEmail, etWhatsapp, etAlamat, etPassword;
    private MaterialButton btnSave, btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etWhatsapp = findViewById(R.id.etWhatsapp);
        etAlamat = findViewById(R.id.etAlamat);
        etPassword = findViewById(R.id.etPassword);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        populateFields();

        btnSave.setOnClickListener(v -> saveProfile());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void populateFields() {
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        if (etUsername != null) etUsername.setText(pref.getUsername());
        if (etEmail != null) etEmail.setText(pref.getEmail());
        if (etWhatsapp != null) etWhatsapp.setText(pref.getWhatsapp());
        if (etAlamat != null) etAlamat.setText(pref.getAddress());
        if (etPassword != null) etPassword.setText(pref.getPassword());
    }

    private void saveProfile() {
        String username = textOf(etUsername);
        String email = textOf(etEmail);
        String whatsapp = textOf(etWhatsapp);
        String address = textOf(etAlamat);
        String password = textOf(etPassword);

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPrefManager.getInstance(this)
                .saveProfile(username, email, whatsapp, address, password);

        setResult(RESULT_OK);
        finish();
    }

    private String textOf(TextInputEditText editText) {
        return editText == null || editText.getText() == null ? null : editText.getText().toString().trim();
    }
}