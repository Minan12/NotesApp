package io.bonan.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.bonan.notes.model.NoteModel;
import io.bonan.notes.util.DateUtil;

import java.util.Objects;

public class AddActivity extends AppCompatActivity {
    TextInputLayout titleTV, descriptionTV;
    Button SEND, btn_Back;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // hooks to all xml elements in activity-register.xml
        titleTV = findViewById(R.id.TITLE);
        descriptionTV = findViewById(R.id.DESC);
        SEND = findViewById(R.id.Send);
        btn_Back =findViewById(R.id.btnBack);

        // save data in firebase on button click
        SEND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance("https://bonan-notes-default-rtdb.asia-southeast1.firebasedatabase.app/");

                // get all the value
                String Title = Objects.requireNonNull(titleTV.getEditText()).getText().toString();
                String Description = Objects.requireNonNull(descriptionTV.getEditText()).getText().toString();
                String CreatedAt = DateUtil.dateNow();

                // get current logged-in user
                user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    // create db ref
                    reference = rootNode.getReference("users");
                    if(TextUtils.isEmpty(Title)){
                        titleTV.setError("FirstName cannot be empty");
                        titleTV.requestFocus();
                    }
                    else if(TextUtils.isEmpty(Description)){
                        descriptionTV.setError("LastName cannot be empty");
                        descriptionTV.requestFocus();
                    }
                    else {
                        NoteModel helperClass = new NoteModel(Title, Description, CreatedAt);
                        reference.child(user.getUid()).child("/notes").push().setValue(helperClass);
                        Toast.makeText(AddActivity.this, "Berhasil menambahkan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // back
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

}
