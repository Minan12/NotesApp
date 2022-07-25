package io.bonan.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NoteModelActivity {
    String Title;
    String Description;

    public NoteModelActivity(){

    }

    public NoteModelActivity(String title, String description){
        Title = title;
        Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}