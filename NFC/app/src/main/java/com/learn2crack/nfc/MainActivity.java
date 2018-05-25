/**
 * @Module_name      : Student Mapping and attention level generation
 * @date_created     : 25 March, 2018
 * @author           : Shaurya Gomber
 * @last_update      : 29 March, 2018
 * @synopsis         : Marks student in the classroom matrix
 * @functions        : protected void onCreate(Bundle savedInstanceState)
                       private void initViews()
                       private void initNFC()
                       private void showReadFragment()
                       public void onDialogDisplayed()
                       public void onDialogDismissed()
                       public void onResume()
                       public void onPause()
                       public void onNewIntent()
 * @global_variables : string TAG
 */

package com.learn2crack.nfc;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements Listener{
    public static final String TAG = MainActivity.class.getSimpleName();
    DatabaseReference myRef;
    private EditText mEtMessage;
    private Button mBtWrite;
    private Button mBtRead;
    private NFCWriteFragment mNfcWriteFragment;
    private NFCReadFragment mNfcReadFragment;
    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;
    private NfcAdapter mNfcAdapter;
    String unid;

    // Instantiates view of this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);                                                                         //Start Main Activity
        TextView text = (TextView) findViewById(R.id.Text3);
        text.setTextColor(Color.parseColor("#FF0000"));
        unid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);   //Get Unique Mobile ID
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();                                                                             //Make column in database corresponding to the unique ID
        initViews();                                                                                                 //Shows views
        initNFC();                                                                                                   //Starts NFC reading process
    }

    private void initViews() {

        showReadFragment();                                                                                             //NFC Reader Function
    }

    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);                                                           // Create an adapter for NFC Reading
    }


    private void showReadFragment() {
        mNfcReadFragment = (NFCReadFragment) getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);
        if (mNfcReadFragment == null) {
            mNfcReadFragment = NFCReadFragment.newInstance();
        }
        mNfcReadFragment.show(getFragmentManager(),NFCReadFragment.TAG);                                                //Displaying NFC Reader

    }

    @Override
    public void onDialogDisplayed() {
        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {
        isDialogDisplayed = false;
        isWrite = false;
    }

    @Override
    protected void onResume() {                                                                                         //Handle onResume case
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override

    // Reads or Writes NFC
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Log.d(TAG, "onNewIntent: "+intent.getAction());
        if(tag != null) {
            Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);
            if (isDialogDisplayed) {
                if (isWrite) {                                                                                              //Writing functionality of NFC
                    String messageToWrite = mEtMessage.getText().toString();
                    mNfcWriteFragment = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);
                    mNfcWriteFragment.onNfcDetected(ndef,messageToWrite);

                }
                else {                                                                                                       //Reading Functionality of NFC
                    mNfcReadFragment = (NFCReadFragment)getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);
                    mNfcReadFragment.onNfcDetected(ndef,myRef,unid,this);
                }
            }
        }
    }
}


