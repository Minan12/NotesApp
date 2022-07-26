package io.bonan.notes.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import io.bonan.notes.NoteListActivity;
import io.bonan.notes.R;
import io.bonan.notes.model.NoteModel;
import io.bonan.notes.util.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> keyList;
    ArrayList<NoteModel> valueList;
    FirebaseUser user;

    public NoteAdapter(Context context, ArrayList<String> keyList, ArrayList<NoteModel> valueList) {
        this.context = context;
        this.keyList = keyList;
        this.valueList = valueList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String keyModel = keyList.get(position);
        NoteModel noteModel = valueList.get(position);

        holder.title.setText(noteModel.getTitle());
        holder.description.setText(noteModel.getDescription());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.title.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1200)
                        .create();
                View view = dialogPlus.getHolderView();

                TextInputEditText title = view.findViewById(R.id.ed_Title);
                TextInputEditText description = view.findViewById(R.id.ed_Description);

                Button btnEdit = view.findViewById(R.id.btn_Update);

                title.setText(noteModel.getTitle());
                description.setText(noteModel.getDescription());

                dialogPlus.show();

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("title", Objects.requireNonNull(title.getText()).toString());
                        map.put("description", Objects.requireNonNull(description.getText()).toString());
                        map.put("createdAt", DateUtil.dateNow());

                        // get current logged-in user
                        user = FirebaseAuth.getInstance().getCurrentUser();

                        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid())
                                .child("notes").child(keyModel).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.title.getContext(), "Data Telah DiUpdate", Toast.LENGTH_SHORT).show();
                                        context.startActivity(new Intent(holder.title.getContext(), NoteListActivity.class));
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.title.getContext(), "Data Error DiUpdate", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.title.getContext());
                builder.setTitle("Yakin Menghapus?");
                builder.setMessage("Menghapus tidak bisa di undo.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get current logged-in user
                        user = FirebaseAuth.getInstance().getCurrentUser();

                        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid())
                                .child("notes").child(keyModel).removeValue();

                        Intent intent = new Intent(holder.title.getContext(), NoteListActivity.class);
                        context.startActivity(intent);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Toast.makeText(holder.title.getContext(), "Note Berhasil di Delete", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.title.getContext(), "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return valueList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, description;
        Button btnEdit, btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);

            btnEdit = itemView.findViewById(R.id.btn_Update);
            btnDelete = itemView.findViewById(R.id.btn_Delete);
        }
    }

}
