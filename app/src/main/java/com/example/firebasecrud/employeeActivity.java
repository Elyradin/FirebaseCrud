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

public class employeeActivity extends AppCompatActivity {
    EditText mName, mJabatan, mGaji, mUpdateGaji, mUpdateNama, mUpdateJabatan;

    DatabaseReference databaseReference;
    Employee employee;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        databaseReference = FirebaseDatabase.getInstance().getReference(Employee.class.getSimpleName());

        mName = findViewById(R.id.nameEmployee);
        mJabatan = findViewById(R.id.jobName);
        mGaji = findViewById(R.id.salary);
        mUpdateNama = findViewById(R.id.updateName);
        mUpdateJabatan = findViewById(R.id.updateJabatan);
        mUpdateGaji = findViewById(R.id.updateGaji);

        findViewById(R.id.insert_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDataPegawai();
            }
        });
        findViewById(R.id.read_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readDataPegawai();
            }
        });
        findViewById(R.id.update_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDataPegawai();
            }
        });
        findViewById(R.id.delete_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDataPegawai();
            }
        });
    }
    public void insertDataPegawai() {
        Employee newEmployee = new Employee();
        String Name = mName.getText().toString();
        String jobTitle = mJabatan.getText().toString();
        Integer gaji = Integer.valueOf(mGaji.getText().toString());
        if (Name != "" && jobTitle != ""){
            newEmployee.setName(Name);
            newEmployee.setJobTitle(jobTitle);
            newEmployee.setGaji(String.valueOf(gaji));

            databaseReference.push().setValue(newEmployee);
            Toast.makeText(this, "Data pegawai berhasil tersimpan!", Toast.LENGTH_SHORT).show();
        }
    }
    public void readDataPegawai(){
        employee = new Employee();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot currentData : snapshot.getChildren()){
                        key = currentData.getKey();
                        employee.setName(currentData.child("name").getValue().toString());
                        employee.setJobTitle(currentData.child("jobTitle").getValue().toString());
                        employee.setGaji(currentData.child("gaji").getValue().toString());
                    }
                }

                mUpdateNama.setText(employee.getName());
                mUpdateJabatan.setText(employee.getJobTitle());
                mUpdateGaji.setText(employee.getGaji());
                Toast.makeText(employeeActivity.this, "data berhasil ditampilkan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void updateDataPegawai(){
        Employee updateDataPegawai = new Employee();
        updateDataPegawai.setName(mUpdateNama.getText().toString());
        updateDataPegawai.setJobTitle(mUpdateJabatan.getText().toString());
        updateDataPegawai.setGaji(mUpdateGaji.getText().toString());

        databaseReference.child(key).setValue(updateDataPegawai);
        Toast.makeText(this, "Data Pegawai berhasil diubah!", Toast.LENGTH_SHORT).show();
    }
    public void deleteDataPegawai(){
        Employee updateDataPegawai = new Employee();
        updateDataPegawai.setName(mUpdateNama.getText().toString());
        updateDataPegawai.setJobTitle(mUpdateJabatan.getText().toString());
        updateDataPegawai.setGaji(mUpdateGaji.getText().toString());

        databaseReference.child(key).removeValue();
        Toast.makeText(this, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show();
    }
}