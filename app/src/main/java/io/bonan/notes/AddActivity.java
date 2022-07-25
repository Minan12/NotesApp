package io.bonan.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {
    //Variable
    TextInputLayout title, description, email, phone, age;
    Button SEND, btn_Back;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //Hooks to all xml elements in activity_registrasi.xml
        title = findViewById(R.id.TITLE);
        description = findViewById(R.id.DESC);
        SEND = findViewById(R.id.Send);
        btn_Back =findViewById(R.id.btnBack);

        //Save data in firebase on button click
        SEND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance("https://bonan-notes-default-rtdb.asia-southeast1.firebasedatabase.app/");
                reference = rootNode.getReference("Notes");

                //get all the value
                String Title= title.getEditText().getText().toString();
                String Description = description.getEditText().getText().toString();

                if(TextUtils.isEmpty(Title)){
                    title.setError("FirstName cannot be empty");
                    title.requestFocus();
                }
                else if(TextUtils.isEmpty(Description)){
                    description.setError("LastName cannot be empty");
                    description.requestFocus();
                }
                else {
                    NoteModelActivity helperClass = new NoteModelActivity(Title, Description);
                    reference.child(Title).setValue(helperClass);
                    Toast.makeText(AddActivity.this, "Berhasil menambahkan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //back
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

}
