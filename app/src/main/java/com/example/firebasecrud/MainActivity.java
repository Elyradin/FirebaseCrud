package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText mNameEditText, mAddressEditText, mUpdateName, mUpdateAddress;

    DatabaseReference mDatabaseReference;
    Student student;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Student.class.getSimpleName());

        mNameEditText = findViewById(R.id.name_eddittext);
        mAddressEditText = findViewById(R.id.address_eddittext);
        mUpdateName = findViewById(R.id.update_name_eddittext);
        mUpdateAddress = findViewById(R.id.update_address_eddittext);

        findViewById(R.id.insert_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        findViewById(R.id.read_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });

        findViewById(R.id.update_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });
    }
    private void insertData(){
        Student newStudent = new Student();
        String name = mNameEditText.getText().toString();
        String address = mAddressEditText.getText().toString();
        if (name != "" && address!= ""){
            newStudent.setName(name);
            newStudent.setAddress(address);

            mDatabaseReference.push().setValue(newStudent);
            Toast.makeText(this, "Successfully insert data!", Toast.LENGTH_SHORT).show();
        }

    }
    private void readData(){
        student = new Student();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot currentData : snapshot.getChildren()){
                        key = currentData.getKey();
                        student.setName(currentData.child("name").getValue().toString());
                        student.setAddress(currentData.child("address").getValue().toString());
                    }
                }

                mUpdateName.setText(student.getName());
                mUpdateAddress.setText(student.getAddress());
                Toast.makeText(MainActivity.this, "Data has been shown!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateData(){
        Student updateData = new Student();
        updateData.setName(mUpdateName.getText().toString());
        updateData.setAddress(mUpdateAddress.getText().toString());

        mDatabaseReference.child(key).setValue(updateData);
        Toast.makeText(MainActivity.this, "Data has been updated!", Toast.LENGTH_SHORT).show();
    }
    private void deleteData(){
        Student updateData = new Student();
        updateData.setName(mUpdateName.getText().toString());
        updateData.setAddress(mUpdateAddress.getText().toString());

        mDatabaseReference.child(key).removeValue();
        Toast.makeText(MainActivity.this, "Data has been deleted!", Toast.LENGTH_SHORT).show();

    }
}