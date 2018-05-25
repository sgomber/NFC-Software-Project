/**
 * @Unit_name        : ReadTag
 * @date_created     : 25 March, 2018
 * @author           : Shaurya Gomber
 * @last_update      : 29 March, 2018
 * @synopsis         : Read tags' data on which phone is kept and sends to server
 * @functions        :  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
                        private void initViews(View view)
                        public void onAttach(Context context)
                        public void onDetach()
                        public void onNfcDetected(Ndef ndef, DatabaseReference d, String s, Context c)
 *                      public ArrayList<String> readTag(NdefMessage m)
 *                      public Integer randGenerator()
 * @global_variables : string TAG
 */

package com.learn2crack.nfc;
import android.app.DialogFragment;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class NFCReadFragment extends DialogFragment {

    private Ndef ndef;
    private Handler handler;
    private String s1;
    private DatabaseReference d1;
    private Context x;
    private String newMessage;
    private String message;


    public static final String TAG = NFCReadFragment.class.getSimpleName();

    public static NFCReadFragment newInstance() {

        return new NFCReadFragment();
    }

    private TextView mTvMessage;
    private Listener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        handler = new Handler();
        View view = inflater.inflate(R.layout.fragment_read, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (MainActivity) context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }


    public void onNfcDetected(Ndef ndef, DatabaseReference d, String s, Context c) {
        this.ndef = ndef;
        this.s1 = s;
        this.d1 = d;
        this.x=c;
        handler.post(runnableCode);

    }

    public ArrayList<String> readTag(NdefMessage m){
        message = new String(m.getRecords()[0].getPayload());                                                           //Read contents of the tag
        ArrayList<String> al=new ArrayList<String>();
        newMessage = message.substring(3);
        String[] words=newMessage.split("\\s");                                                                   //Split contents of the tag into room number, row and column number
        al.add(words[0]);
        al.add(words[1]);
        al.add(words[2]);
        return (al);                                                                                                    //return the ArrayList<String> of words containing room number,row and column number
    }

    public Integer randGenerator(){
        Random r = new Random();                                                                                        //Instantiate Random Generator
        Integer t = r.nextInt(10) + 1;                                                                               //Generate an integer in range ( 1 to 10 )
        Toast.makeText(x,"Attention : "+ Integer.toString(t) , Toast.LENGTH_LONG).show();
        return t;
    }


    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            if(ndef!=null){
                try{
                    ndef.connect();
                    ArrayList<String> al = readTag(ndef.getNdefMessage());                                               //Read Tag
                    al.add(Integer.toString(randGenerator()));                                                          // Generate Random Integer between 1 to 10
                    d1.child("Tags").child(s1).setValue(al);                                                            // Add this info under unique id s1 in the database
                    Log.d(TAG, "readFromNFC: " + message);
                    mTvMessage.setText(message);
                    ndef.close();
                    handler.postDelayed(runnableCode,60000);                                                    //Read the tag after every 1 minute
                } catch (IOException | FormatException e) {
                   d1.child("Tags").child(s1).setValue(null);                                                            //If no contact, then delete info in database
                   Toast.makeText(x,"Phone removed from tag", Toast.LENGTH_LONG).show();
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(x, notification);                                          //Tune to notify that mobile is not on the tag
                    r.play();                                                                                            //Play the tune
                    e.printStackTrace();
                }
            }
        }
    };
}


