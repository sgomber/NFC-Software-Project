/**
 * @unit_name        : Pop up window
 * @date_created     : 25 March, 2018
 * @author           : Shaurya Gomber
 * @last_update      : 29 March, 2018
 * @synopsis         : Pop up window that shows student's info on tap of professor
 * @functions        : protected void onCreate(@Nullable Bundle savedInstanceState)
 * @global_variables : NONE
 */

package com.learn2crack.nfc;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.TextView;

// Display a popup card showing student info corresponding to the cell
public class Pop extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);                            //Instantiates the view of the activity
        Intent t = getIntent();
        String s = t.getStringExtra("info");
        TextView textView = (TextView) findViewById(R.id.textView4);
        textView.setText(s);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;                                      //Get width of screen
        int height = dm.heightPixels;                                    //Get height if screen

                                                                         //Popup window size is 80% in width and 30% in height
        getWindow().setLayout((int)(width*0.8),(int)(height*0.3));
        getWindow().setBackgroundDrawable(new ColorDrawable(0xff3F51B5));

    }
}
