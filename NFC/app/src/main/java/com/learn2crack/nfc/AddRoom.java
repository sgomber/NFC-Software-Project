/**
 * @unit_name        : AddRoom
 * @date_created     : 25 March, 2018
 * @author           : Shaurya Gomber
 * @last_update      : 29 March, 2018
 * @synopsis         : Option to add a new Room
 * @functions        : public void addRoom (View view)
                       protected void onCreate (Bundle savedInstanceState)
                       public void hideKeyboard()
                       public void addRoomToDatabase(String r,ArrayList<String> jl)
                       public static boolean isInteger(Object object) -> True/False depending on whether object is integer
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class AddRoom extends AppCompatActivity {
    private InputMethodManager imm;

    // Instantiates view of this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editText = (EditText) findViewById(R.id.editText5);
        editText.requestFocus();
    }

    //Hides keyboard after text input
    public void hideKeyboard(){
        if (null != this.getCurrentFocus())
            imm.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getApplicationWindowToken(), 0);

    }

    //Adds room details to database
    public void addRoomToDatabase(String r,ArrayList<String> jl){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();                  //get path where to add room
        if(Integer.parseInt(jl.get(0))<=0 | Integer.parseInt(jl.get(1))<=0 ) {                  // If row or column are negative
            Toast.makeText(getApplicationContext(), "Row and Column Number should be positive.", Toast.LENGTH_SHORT).show();
        }
        else {
            ref.child("Room").child(r).setValue(jl);;                                           //Add Room to Database
            Toast.makeText(getApplicationContext(), "Room Added Successfully!!", Toast.LENGTH_SHORT).show();
        }
    }

    //Takes room details and calls addRoomToDatabase function
    public void addRoom(View view) {
        EditText editText = (EditText) findViewById(R.id.editText5);
        String r = editText.getText().toString();                                               // r-> room number
        editText.setText("");
        EditText editText1 = (EditText) findViewById(R.id.editText6);
        String ro = editText1.getText().toString();                                             // ro -> number of rows in the room
        editText1.setText("");
        EditText editText2 = (EditText) findViewById(R.id.editText4);
        String col = editText2.getText().toString();                                            // col -> number of columns in the room
        editText2.setText("");
        hideKeyboard();
        if(r.equals("") | ro.equals("") | col.equals("") | !isInteger(ro) | !isInteger(col)) {  // If any entry is empty or row or column is not integer
            Toast.makeText(getApplicationContext(),"Invalid Details",Toast.LENGTH_SHORT).show();
        }
        else {
            ArrayList<String> jl = new ArrayList<String>();
            jl.add(ro);
            jl.add(col);
            addRoomToDatabase(r,jl);
        }
    }

    // Check if object is integer
    public static boolean isInteger(Object object) {
        if(object instanceof Integer) {
            return true;
        }
        else {
            String string = object.toString();
            try {
                Integer.parseInt(string);
            } catch(Exception e) {
                return false;
            }
        }
        return true;
    }
}
