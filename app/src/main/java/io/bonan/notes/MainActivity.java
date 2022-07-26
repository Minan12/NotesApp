package io.bonan.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button btn_to_add, btn_to_List, btn_logout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_to_add = findViewById(R.id.button_to_add);
        btn_to_List = findViewById(R.id.button_to_list);
        btn_logout = findViewById(R.id.btn_logout);

        mAuth = FirebaseAuth.getInstance();

        // adding on click to add note
        btn_to_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a login activity on clicking login text
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivity(i);
            }
        });

        // adding on click to list note
        btn_to_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a login activity on clicking login text
                Intent i = new Intent(MainActivity.this, NoteListActivity.class);
                startActivity(i);
            }
        });

        btn_logout.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

    }
}