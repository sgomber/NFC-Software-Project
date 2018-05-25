/**
 * @unit_name        : Login_Admin
 * @date_created     : 25 March, 2018
 * @author           : Shaurya Gomber
 * @last_update      : 29 March, 2018
 * @synopsis         : To login the professor
 * @functions        : public void hideKeyboard()
 *                     public void Login(View view)
 *                     public void success(Datasnapshot d)
 *                     public void failure()
 *                     public void checkDatabase(String user,String pass)
                       protected void onCreate (Bundle savedInstanceState)

 * @global_variables : NONE
 */

package com.learn2crack.nfc;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




public class Login_Admin extends AppCompatActivity {
    private String y;
    public static final String EXTRA_MESSAGE = "extra";
    private ProgressBar spinner;
    private InputMethodManager imm;
    private EditText editText;
    private EditText editText1;


    // Instantiates view of this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        editText = (EditText) findViewById(R.id.editText9);
        editText.requestFocus();
        spinner = (ProgressBar)findViewById(R.id.progressBar2);
        spinner.setVisibility(View.GONE);
    }


    //Hides keyboard after text input
    public void hideKeyboard(){
        if (null != this.getCurrentFocus())
            imm.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getApplicationWindowToken(), 0);

    }

    // if username and password match
    public void success(DataSnapshot d){
        Intent intent = new Intent(Login_Admin.this, AdminActivity.class);
        String message = d.child("0").getValue().toString();
        message = "Hello!! "+message;
        intent.putExtra(EXTRA_MESSAGE, message);
        spinner.setVisibility(View.GONE);
        startActivity(intent);

    }

    //Unsuccessful try to login
    public void failure(){
        editText1.setText("");
        editText.setText("");
        spinner.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(),"Invalid Username or Password",Toast.LENGTH_SHORT).show();
    }


    // Checking database
    public void checkDatabase(String user,String pass){
        DatabaseReference testRef = FirebaseDatabase.getInstance().getReference().child("Admin").child(user); //Path in database where to check
        testRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    y = dataSnapshot.child("1").getValue().toString();
                    if (y.equals(pass)) {                                                                     //If username equals password
                        success(dataSnapshot);
                    }
                    else{                                                                                     //If username is not equal to password
                        failure();
                    }
                }
                else{                                                                                         //If username not in database
                    failure();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Database should do nothing on Cancelling the query
            }
        });
    }

    //Main login function
    public void login(View view) {
        editText = (EditText) findViewById(R.id.editText9);
        String user = editText.getText().toString();                                                        //user -> entered username
        editText1 = (EditText) findViewById(R.id.editText7);
        String pass = editText1.getText().toString();                                                       //pass ->entered password
        hideKeyboard();
        if(user.equals("")) {                                                                               // if username is empty
            Toast.makeText(getApplicationContext(),"Invalid Username or Password", Toast.LENGTH_SHORT).show();
            editText1.setText("");
            editText.setText("");
        }
        else {
            spinner.setVisibility(View.VISIBLE);
            checkDatabase(user,pass);                                                                       //Check if username and password exists in database
        }
    }
}
