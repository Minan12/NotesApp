package io.bonan.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import io.bonan.notes.adapter.NoteAdapter;
import io.bonan.notes.model.NoteModel;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser user;
    NoteAdapter myNoteAdapter;
    ArrayList<String> keyList;
    ArrayList<NoteModel> valueList;
    Button btn_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        btn_Back = findViewById(R.id.btn_toBack);
        recyclerView = findViewById(R.id.noteList);

        // get current logged-in user
        user = FirebaseAuth.getInstance().getCurrentUser();
        // db ref
        databaseReference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/notes");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        keyList = new ArrayList<>();
        valueList = new ArrayList<>();

        myNoteAdapter = new NoteAdapter(this, keyList, valueList);
        recyclerView.setAdapter(myNoteAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NoteModel noteModel = dataSnapshot.getValue(NoteModel.class);
                    keyList.add(dataSnapshot.getKey());
                    valueList.add(noteModel);
                }
                myNoteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NoteListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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