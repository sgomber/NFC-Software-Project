/**
 * @Module_name      : Classroom Visualization
 * @date_created     : 25 March, 2018
 * @author           : Shaurya Gomber
 * @last_update      : 29 March, 2018
 * @synopsis         : Option to add a new Admin
 * @functions        : protected void onCreate(Bundle savedInstanceState)
 *                      public void getRoom(View view)
 *                      public void readDatabase(String r)
 * @global_variables : String EXTRA_MESSAGE1
 */

package com.learn2crack.nfc;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE1 = "extra1";
    private EditText editText;

    // Instantiates view of this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);
        Intent intent = getIntent();
        String message = intent.getStringExtra(Login.EXTRA_MESSAGE);
        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(message);

    }

    // Read row and column corresponding to the room number if it exists
    public void readDatabase(String r) {
        DatabaseReference testRef = FirebaseDatabase.getInstance().getReference().child("Room").child(r);
        testRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {                                                            // If room exists
                    Intent intent = new Intent(ProfActivity.this, ClassView.class);
                    String message = dataSnapshot.child("0").getValue().toString() + " " + dataSnapshot.child("1").getValue().toString() +" " + r;
                    intent.putExtra(EXTRA_MESSAGE1, message);
                    startActivity(intent);
                } else {                                                                                //If room does not existss
                    editText.setText("");
                    Toast.makeText(getApplicationContext(), "Invalid Room Number", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                    // Database should do nothing on Cancelling the query
            }
        });

    }


    public void getRoom(View view) {
        editText = (EditText) findViewById(R.id.editText10);
        String r = editText.getText().toString();                                                       //r -> room number
        if (r.equals("")) {                                                                             // if room number is empty
            Toast.makeText(getApplicationContext(), "Invalid Room Number", Toast.LENGTH_SHORT).show();
            editText.setText("");
        } else {
            readDatabase(r);                                                                            // Read Database to get row and columns and thus initialize classroom matrix
        }
    }
}
