/**
 * @Unit_name        : Student Mapping and attention level generation
 * @date_created     : 25 March, 2018
 * @author           : Shaurya Gomber
 * @last_update      : 29 March, 2018
 * @synopsis         : Helps to visualize class with color coded symbols
 * @functions        : protected void onCreate(Bundle savedInstanceState)
 *                      public void drawClass()
 *                      public void showStudent()
 *                      public void readDatabase()
 */


package com.learn2crack.nfc;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import  android.content.Context;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClassView extends AppCompatActivity {
    private Integer r;
    private Integer c;
    private Integer i;
    private Integer j;
    private Integer left;
    private Integer top;
    private Integer gaph=30;
    private Integer gapv=30;
    private String room;
    private String [][] mobile;
    private Integer [][] att;
    private String Name;
    private String Roll_No;
    MyView draw;
    HorizontalScrollView h_scroll_view;
    ScrollView scrollView;
    private long startClickTime;
    private Float mDownX;
    private Float mDownY;
    private boolean mSwiping;
    private RelativeLayout x;
    private Handler handler;
    private Long n;
    private Long counter;
    private Integer check;
    Context ct;

    // Instantiates view of this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.activity_class_view);
        Intent intent = getIntent();
        String message = intent.getStringExtra(ProfActivity.EXTRA_MESSAGE1);
        String[] words = message.split("\\s");
        r = Integer.parseInt(words[0]);
        c = Integer.parseInt(words[1]);
        room = words[2];
        ct=this;
        check=0;
        handler.post(repeat);                                                                                       //To refresh the class view every minute
    }

    private Runnable repeat = new Runnable() {
        @Override
        public void run() {
            setContentView(R.layout.activity_class_view);
            TextView textView = (TextView) findViewById(R.id.textView2);
            textView.setText("Preparing Class View.....");
            ProgressBar spinner;
            spinner = (ProgressBar)findViewById(R.id.progressBar1);
            spinner.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            if(check!=0)
                Toast.makeText(ct,"Class view refreshed!", Toast.LENGTH_LONG).show();
            else
                check=check+1;
            mobile  = new String[r][c];                                                                 //2D array to store mobile ID's of all students in the classroom
            att  = new Integer[r][c];                                                                  // 2D array to store attention levels ID's of all students in the classroom
            Integer a;
            Integer b;
            for(a=0;a<r;a++)                                                                            //Initializing the arrays
            {
                for(b=0;b<c;b++)
                {
                    att[a][b]=-1;
                    mobile[a][b]="";
                }
            }
            readDatabase(); //Reading the database
            handler.postDelayed(repeat, 60000);
        }
    };

    //Read and store all data related to tags of the same room
    public void readDatabase(){
        DatabaseReference testRef =FirebaseDatabase.getInstance().getReference().child("Tags");
        counter=0L;
        testRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                n=dataSnapshot.getChildrenCount();
                if (dataSnapshot.exists()) {
                    for(DataSnapshot snap : dataSnapshot.getChildren()){
                        counter+=1;
                        if(snap.child("0").getValue().toString().equals(room))                         //If room of the tag is same, store its information
                        {
                            Integer r1 = Integer.parseInt(snap.child("1").getValue().toString());
                            Integer c1 = Integer.parseInt(snap.child("2").getValue().toString());
                            Integer at = Integer.parseInt(snap.child("3").getValue().toString());
                            att[r1-1][c1-1]=at;
                            mobile[r1-1][c1-1]=snap.getKey().toString();

                        }
                        if(counter==n)
                            drawClass();                                                                 //Draws the classroom matrix
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Database should do nothing on Cancelling the query
            }
        });

    }
    public void handleHorizontalScrollView(HorizontalScrollView s){
        s.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN)
                    startClickTime = System.currentTimeMillis();
                else if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    if (System.currentTimeMillis() - startClickTime < ViewConfiguration.getTapTimeout()) {
                        Float x1 = event.getX();
                        Float y1 = event.getY();
                        Integer col = -1,z,lc,rc;
                        for(z=1;z<=c;z++)                                                                    //get Column of touched cell
                        {
                            lc = 30 + (150+gaph)*(z-1);
                            rc = lc + 150;

                            if(x1>=lc && x1<=rc)
                            {                                                                               // get row of touched cell
                                col = z;
                                break;
                            }

                        }
                        Integer row = -1,y,ur,dr;
                        for(y=1;y<=r;y++)
                        {
                            ur = 62 + (150+gapv)*(y-1);
                            dr = ur + 150;

                            if(y1>=ur && y1<=dr)
                            {
                                row = y;
                                break;
                            }
                        }
                        if(row!=-1 && col!=-1 && !mobile[r-row][col-1].equals("")) {
                            Toast.makeText(ct,"Processing Student's Info......", Toast.LENGTH_LONG).show();
                            FirebaseDatabase db3 = FirebaseDatabase.getInstance();
                            DatabaseReference refer1 = db3.getReference();
                            DatabaseReference testref1 = refer1.child("Student").child(mobile[r-row][col-1]);
                            testref1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        Name = dataSnapshot.child("0").getValue().toString();
                                        Roll_No = dataSnapshot.child("1").getValue().toString();
                                        showStudent();

                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {


                                }
                            });



                        }
                        else
                            Toast.makeText(ct,"Invalid Touch", Toast.LENGTH_LONG).show();


                    } else {

                        // Touch was a not a simple tap
                        Toast.makeText(ct,"Scrolling", Toast.LENGTH_LONG).show();

                    }

                }
                return false;
            }
        });

    }

    public void handleScrollView(ScrollView s){
        s.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN)
                    startClickTime = System.currentTimeMillis();
                else if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    if (System.currentTimeMillis() - startClickTime < ViewConfiguration.getTapTimeout()) {
                        Float x1 = event.getX();
                        Float y1 = event.getY();
                        Integer col = -1,z,lc,rc;
                        for(z=1;z<=c;z++)
                        {
                            lc = 30 + (150+gaph)*(z-1);
                            rc = lc + 150;

                            if(x1>=lc && x1<=rc)
                            {
                                col = z;
                                break;
                            }

                        }
                        Integer row = -1,y,ur,dr;
                        for(y=1;y<=r;y++)
                        {
                            ur = 62 + (150+gapv)*(y-1);
                            dr = ur + 150;

                            if(y1>=ur && y1<=dr)
                            {
                                row = y;
                                break;
                            }
                        }
                        if(row!=-1 && col!=-1 && !mobile[r-row][col-1].equals("")) {
                            Toast.makeText(ct,"Processing Student's Info......", Toast.LENGTH_LONG).show();
                            FirebaseDatabase db3 = FirebaseDatabase.getInstance();
                            DatabaseReference refer1 = db3.getReference();
                            DatabaseReference testref1 = refer1.child("Student").child(mobile[r-row][col-1]);
                            testref1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        Name = dataSnapshot.child("0").getValue().toString();
                                        Roll_No = dataSnapshot.child("1").getValue().toString();
                                        showStudent();

                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {


                                }
                            });



                        }
                        else
                            Toast.makeText(ct,"Invalid Touch", Toast.LENGTH_LONG).show();


                    } else {

                        // Touch was a not a simple tap
                        Toast.makeText(ct,"Scrolling", Toast.LENGTH_LONG).show();

                    }

                }
                return false;
            }
        });

    }

    public void drawClass(){
        draw = new MyView(ct);                                                                                  //Initializing Class View (Empty Matrix)
        scrollView = new ScrollView(ct);                                                                         //Making view scrollable
        h_scroll_view = new HorizontalScrollView(ct);                                                              //Making view horizontal scrollable
        h_scroll_view.addView(draw);
        scrollView.addView(h_scroll_view);
        setContentView(scrollView);                                                                             //Add content to the scrollView
        handleScrollView(scrollView);
        handleHorizontalScrollView(h_scroll_view);
    }

    public void showStudent(){
        Intent in = new  Intent(ClassView.this,Pop.class);                                           //Initialize pop up window to show student's info
        in.putExtra("info",Name+" "+Roll_No);                                                        //Add name and roll no. of the student to the pop window
        startActivity(in);
    }




    public class MyView extends View {
        Paint paint;
        Path path;
        public MyView(Context context) {
            super(context);
            init();
        }
        public MyView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }
        public MyView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // Compute the height required to render the view
            // Assume Width will always be MATCH_PARENT.
            int width = 150*c + 60 + 30*(c-1);
            int height = 150*r + 60 + 30*(r-1);
            setMeasuredDimension(width, height);
        }
        private void init(){
            paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(10);
            paint.setStyle(Paint.Style.STROKE);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name);                                  //Image of blue question mark
            Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.red_cross);                                        //Image of red cross
            Bitmap bmp3 = BitmapFactory.decodeResource(getResources(), R.drawable.tick);                                             // Image of green tick
            super.onDraw(canvas);
            left=30;
            top=30;
            for(j=0;j<r;j++) {
                for (i = 0; i < c; i++) {
                    canvas.drawRect(left, top, left + 150, top + 150, paint);                                            //drawing an empty cell
                    if(att[r-j-1][i]>=1 && att[r-j-1][i]<=4){                                                                       //If attention is between 1 to 4, choose red cross image
                        canvas.drawBitmap(bmp2,left+30,top+30, null);}
                    else if(att[r-j-1][i]>=5 && att[r-j-1][i]<=7) {                                                                  //If attention is between 5 to 7, choose blue question mark
                        canvas.drawBitmap(bmp3,left+30,top+30, null);}
                    else if(att[r-j-1][i]>=8 && att[r-j-1][i]<=10) {                                                                //If attention is between 8 to 10, choose green tick
                        canvas.drawBitmap(bmp1,left+30,top+30, null);}
                    left = left + 150+gaph;//d
                }
                top = top +150+gapv;
                left=30;
            }
        }
    }
}
