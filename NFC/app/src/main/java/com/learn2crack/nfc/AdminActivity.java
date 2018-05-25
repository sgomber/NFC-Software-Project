/**
 * @module_name      : Creating Database
 * @date_created     : 25 March, 2018
 * @author           : Shaurya Gomber
 * @last_update      : 29 March, 2018
 * @synopsis         : Options to build the required Database
 * @functions        : public void addStudentData (View view)
 *                     public void addProfData (View view)
 *                     public void addRoomData (View view)
 *                     public void addAdminData (View view)
                       protected void onCreate (Bundle savedInstanceState)

 * @global_variables : NONE
 */

package com.learn2crack.nfc;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AdminActivity extends AppCompatActivity {

    // Instantiates view of this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    //Add Students' Data
    public void addStudentData(View view) {
        Intent intent = new Intent(this, AddStudent.class);
        startActivity(intent);
    }

    //Add Professors' Data
    public void addProfessorData(View view) {
        Intent intent = new Intent(this, AddProfessor.class);
        startActivity(intent);
    }

    //Add Rooms' Data
    public void addRoomData(View view) {
        Intent intent = new Intent(this, AddRoom.class);
        startActivity(intent);
    }

    //Add Admins' Data
    public void addAdminData(View view) {
        Intent intent = new Intent(this, AddAdmin.class);
        startActivity(intent);
    }
}
