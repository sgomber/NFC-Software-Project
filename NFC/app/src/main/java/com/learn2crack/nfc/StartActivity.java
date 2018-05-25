/**
 * @System_name      : NFC Class Visualizer
 * @date_created     : 25 March, 2018
 * @author           : Shaurya Gomber
 * @last_update      : 29 March, 2018
 * @synopsis         : Start activity of our app
 * @functions        : protected void onCreate(Bundle savedInstanceState)
 *                     public void studentView(View view
 *                     public void professorView(View view
 *                     public void adminView(View view
 * @global_variables : NONE
 */


package com.learn2crack.nfc;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class StartActivity extends AppCompatActivity {

    // Instantiates view of this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    // Student Mode of the app
    public void studentView(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Professor Mode of the app
    public void professorView(View view) {
        Intent intent1 = new Intent(this, Login.class);
        startActivity(intent1);
    }

    //Admin View of the app
    public void adminView(View view) {
        Intent intent2 = new Intent(this, Login_Admin.class);
        startActivity(intent2);
    }
}
