/**
 * @unit_name        : AddStudent
 * @date_created     : 25 March, 2018
 * @author           : Shaurya Gomber
 * @last_update      : 29 March, 2018
 * @synopsis         : Option to add a new Student
 * @functions        : public void addStudent (View view)
                       protected void onCreate (Bundle savedInstanceState)
                       public void hideKeyboard()
                       public void addStudToDatabase(String rn)
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


public class AddStudent extends AppCompatActivity {
    private InputMethodManager imm;
    private boolean b;
    private Long n;
    private Long counter;
    private Integer help;
    private String k;
    DatabaseReference testRef;
    String id;
    ArrayList<String> jl;

    // Instantiates view of this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.requestFocus();
    }

    //Hides keyboard after text input
    public void hideKeyboard(){
        if (null != this.getCurrentFocus())
            imm.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getApplicationWindowToken(), 0);

    }

    // Takes student details and adds/updates to databse
    public void addStudent(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String nam = editText.getText().toString();                                                     // nam -> name of the student
        editText.setText("");
        EditText editText1 = (EditText) findViewById(R.id.editText2);
        String rn = editText1.getText().toString();                                                     // rn -> roll no. of the student
        editText1.setText("");
        EditText editText2 = (EditText) findViewById(R.id.editText3);
        id = editText2.getText().toString();                                                            // id -> mobile id of the student
        editText2.setText("");
        hideKeyboard();
        if(nam.equals("") | rn.equals("") | id.equals("")) {                                            //If any of the entry is empty
            Toast.makeText(getApplicationContext(),"Invalid Details",Toast.LENGTH_SHORT).show();
        }
        else {
            jl = new ArrayList<String>();
            jl.add(nam);
            jl.add(rn);
            addStudToDatabase(rn);                                                                      //Add student to database
        }
    }

    public void addStudToDatabase(String rn){
        testRef = FirebaseDatabase.getInstance().getReference().child("Student");                       // Get path of student data in firebase
        k="";
        b=true;
        counter=0L;
        help=1;
        testRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                n=dataSnapshot.getChildrenCount();
                if (dataSnapshot.exists()) {
                    for(DataSnapshot snap : dataSnapshot.getChildren()){
                        counter += 1;
                        if(snap.getKey().equals(id))                                                   //Case if mobile id matches in the database
                            b=false;
                        if(snap.child("1").getValue().toString().equals(rn)) {                         //Case if roll no. matches in database
                            help=2;
                            k=snap.getKey();
                        }
                        if (counter==n)
                            performAdd();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Database should do nothing on Cancelling the query
            }
        });
    }

    // helper function to update database
    public void performAdd() {
        if(help==1) {                                                                                   // Case when roll no. does not match in database
            testRef.child(id).setValue(jl);
            if(!b)
            Toast.makeText(getApplicationContext(), "Info with this mobile id updated!!!", Toast.LENGTH_SHORT).show(); //Mobile id repeated
            else
                Toast.makeText(getApplicationContext(), "New Student Added!!!", Toast.LENGTH_SHORT).show(); //New Mobile id and roll number
        }
        else {                                                                                          // Case when roll no. matches in the database
            testRef.child(k).setValue(null);                                                            //delete prev. entry
            testRef.child(id).setValue(jl);                                                             // update with new
            Toast.makeText(getApplicationContext(), "Info with this roll no. updated!!!", Toast.LENGTH_SHORT).show();
        }
    }




}
