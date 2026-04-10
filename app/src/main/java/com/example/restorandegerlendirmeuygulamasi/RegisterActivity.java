package com.example.restorandegerlendirmeuygulamasi;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText nameInput, emailInput, phoneInput, passwordInput;
    CheckBox checkBox;
    RadioGroup radioGroup;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.editTextName);
        emailInput = findViewById(R.id.editTextEmail);
        phoneInput = findViewById(R.id.editTextPhone);
        passwordInput = findViewById(R.id.editTextPassword);
        checkBox = findViewById(R.id.checkBoxAgree);
        radioGroup = findViewById(R.id.radioGroupUserType);
        registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(v -> {
            if (!checkBox.isChecked()) {
                Toast.makeText(this, "Lütfen KVKK kutucuğunu onaylayın", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Lütfen kullanıcı tipini seçin", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRadio = findViewById(selectedId);
            String userType = selectedRadio.getText().toString();

            String name = nameInput.getText().toString();
            String email = emailInput.getText().toString();
            String phone = phoneInput.getText().toString();
            String password = passwordInput.getText().toString();

            DatabaseHelper db = new DatabaseHelper(this);
            boolean isInserted = db.insertUser(name, email, phone, password, userType);

            if (isInserted) {
                new AlertDialog.Builder(this)
                        .setTitle("Kayıt Başarılı")
                        .setMessage("Kullanıcı başarıyla kaydedildi.")
                        .setPositiveButton("Tamam", null)
                        .show();
            } else {
                Toast.makeText(this, "Kayıt başarısız!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
