/**
 * @unit_name        : AddAdmin
 * @date_created     : 25 March, 2018
 * @author           : Shaurya Gomber
 * @last_update      : 29 March, 2018
 * @synopsis         : Option to add a new Admin
 * @functions        : public void addAdmin (View view)
                       protected void onCreate (Bundle savedInstanceState)
                       public void hideKeyboard()
                       public void addAdminToDatabase(String us,ArrayList<String> jl)
 * @global_variables : NONE
 */

package com.learn2crack.nfc;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class AddAdmin extends AppCompatActivity {
    private InputMethodManager imm;


    // Instantiates view of this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editText = (EditText) findViewById(R.id.editText11);
        editText.requestFocus();
    }

    //Hides keyboard after text input
    public void hideKeyboard(){
        if (null != this.getCurrentFocus())
            imm.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getApplicationWindowToken(), 0);

    }

    // Adds admin data to database
    public void addAdminToDatabase(String us,ArrayList<String> jl){
        DatabaseReference testRef =  FirebaseDatabase.getInstance().getReference().child("Admin").child(us); //get location where data is to be added
        testRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {                                           // Function to read data to Firebase
                if (dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(),"Admin with same username exists",Toast.LENGTH_SHORT).show();
                }
                else {
                    testRef.setValue(jl);                                                                   //Add to database
                    Toast.makeText(getApplicationContext(),"Admin Added Successfully!!",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Database should do nothing on Cancelling the query
            }
        });
    }

    public void addAdmin(View view) {
        EditText editText = (EditText) findViewById(R.id.editText11);
        String na = editText.getText().toString();                                                          // na --> Name of the Admin to be added
        editText.setText("");
        EditText editText1 = (EditText) findViewById(R.id.editText12);
        String us = editText1.getText().toString();                                                         // us --> Username of the admin to be added
        editText1.setText("");
        EditText editText2 = (EditText) findViewById(R.id.editText13);
        String pas = editText2.getText().toString();                                                        // pas --> Password entered by the admin
        editText2.setText("");
        hideKeyboard();
        if (na.equals("") | us.equals("") | pas.equals("")){                                                // Check if any field is empty
            Toast.makeText(getApplicationContext(),"Invalid Details",Toast.LENGTH_SHORT).show();
        }
        else {
            ArrayList<String> jl = new ArrayList<String>();
            jl.add(na);
            jl.add(pas);
            addAdminToDatabase(us,jl);                                                                      //Add to Database

        }
    }
}

