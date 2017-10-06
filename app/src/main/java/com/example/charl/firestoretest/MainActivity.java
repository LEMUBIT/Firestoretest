package com.example.charl.firestoretest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DocumentReference documentReference = FirebaseFirestore.getInstance().document("sampledata/quote");
    TextView qtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button qetbtn = findViewById(R.id.getBtn);
        qtext = findViewById(R.id.gettxt);

        qetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetch();
            }
        });


        ///////////////
        Button qbtn = findViewById(R.id.qBtn);
        qbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveq();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        documentReference.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                String q = documentSnapshot.getString("author");
                String a = documentSnapshot.getString("quote");
                qtext.setText("Quote: " + q + "--author: " + a);
            }
        });
    }



    public void fetch() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String q = documentSnapshot.getString("author");
                String a = documentSnapshot.getString("quote");
                qtext.setText("Quote: " + q + "--author: " + a);

            }
        });
    }

    public void saveq() {
        EditText quote = findViewById(R.id.quotetxt);
        EditText name = findViewById(R.id.nameTxt);

        String quoteSt = quote.getText().toString();
        String nameSt = name.getText().toString();

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("quote", quoteSt);
        data.put("author", nameSt);
        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error", e.getMessage().toString());
            }
        });
    }
}
