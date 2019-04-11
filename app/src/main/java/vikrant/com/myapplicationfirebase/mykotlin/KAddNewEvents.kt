package vikrant.com.myapplicationfirebase.mykotlin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vikrant.com.myapplicationfirebase.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class KAddNewEvents : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
/*    var  yy : Int ?= null
    var  mm : Int ?= null
    var  dd : Int ?= null
    var  hr : Int ?= null
    var  ms : Int ?= null
    var msec : Long ?= null
    */

    internal var hr: Int = 0;
    var ms:Int = 0;
    internal var yy:Int = 0;
    internal var mm:Int = 0;
    internal var dd:Int = 0
    internal var msec:Long = 0;


   //  var auth: FirebaseAuth? = null
  var authListener: FirebaseAuth.AuthStateListener ?= null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_event)


        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)

        //get firebase auth instance
      var  database = FirebaseDatabase.getInstance()
         auth = FirebaseAuth.getInstance()
        var dbRef = database.getReference("User/" + auth?.getCurrentUser()!!.getUid())//     auth.getCurrentUser().getUid() https://myapplicationfirebase-1bd85.firebaseio.com/User/V31ISKWWJZbKB1dKS16R1lhpNHF2
        var mListItemRef = dbRef.child("posts")

        //get current user
        val user = FirebaseAuth.getInstance().currentUser
        val userId = auth?.getCurrentUser()!!.getUid()

       var  mDatabase = database.getReference("User")

        var timeTxt = findViewById(R.id.time) as TextView
       var  dateTxt = findViewById(R.id.date) as TextView

        dateTxt.setOnClickListener(View.OnClickListener {
            val calendar = Calendar.getInstance()
            val y = calendar.get(Calendar.YEAR)
            val m = calendar.get(Calendar.MONTH)
            val d = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(this@KAddNewEvents, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                //  dd-M-yyyy hh:mm:ss
                val date = (dayOfMonth.toString() + "-" + (month + 1).toString()
                        + "-" + year.toString())

                dateTxt.setText(date)

              var  startDate = year.toString() + "-" + (month + 1).toString() + "-" + dayOfMonth.toString()

              var  mDate = date

                yy = year
                mm = month+1
                dd = dayOfMonth

                /*  Date date1= null;
                try {

                    date1 = new SimpleDateFormat("dd/mm/yyyy").parse(date);
                    dateTxt.setText(date);
                    Log.e("date",date1.toString());
                     msec = date1.getTime();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
*/
            }, y, m, d)
            datePicker.show()
        })

        timeTxt.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val mHour = c.get(Calendar.HOUR_OF_DAY)
            val mMinute = c.get(Calendar.MINUTE)
            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(this@KAddNewEvents,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        timeTxt.setText("$hourOfDay:$minute")

                        hr = hourOfDay
                        ms = minute

                        Log.e("hr", hr.toString())
                        // time_formated = hourOfDay + ":" + minute + ":" + "00";
                    }, mHour, mMinute, true)
            timePickerDialog.show()
        })

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@KAddNewEvents, KLoginActivity::class.java))
                finish()
            }
        }

       var addEvent = findViewById(R.id.addEvent) as Button


      var  agenda = findViewById(R.id.agenda) as EditText
       var newEmail = findViewById(R.id.new_email) as EditText



        addEvent.setOnClickListener(View.OnClickListener {
            val EmailString = newEmail.getText().toString()
            val list = ArrayList(Arrays.asList<String>(*EmailString.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()))
            //  EventPOJO eventPOJO=new EventPOJO( agenda.getText().toString(),dateTxt.getText().toString(),timeTxt.getText().toString(),newEmail.getText().toString());

            //  String userId1 = auth.getCurrentUser().getUid();
            // DatabaseReference currentUser = mDatabase.child(userId1).child();


            // String userId1 = auth.getCurrentUser().getUid();
            //  DatabaseReference currentUser = mDatabase.child(userId1);


var formatter =  SimpleDateFormat("dd.MM.yyyy, HH:mm");
formatter.setLenient(false);

var curDate =  Date();
var curMillis = curDate.getTime();
var curTime = formatter.format(curDate);

var oldTime = ""+dd+"."+mm+"."+yy+", "+hr+":"+ms;//"05.01.2011, 12:45"
var oldDate = formatter.parse(oldTime);
msec = oldDate.getTime();
            Log.e("Date Is",msec.toString())


          /*  val date121 = LocalDateTime.of(yy, mm, dd, hr, ms)


            msec = date121.toInstant(ZoneOffset.ofTotalSeconds(19800)).toEpochMilli()
            Log.e("Date Is ", date121.toString() + "  " + date121.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli())

*/
            val userId1 = auth?.getCurrentUser()!!.getUid()
            val currentUser = mDatabase.child(userId1).child("posts").push()

            /*               FirebaseDatabase database = FirebaseDatabase.getInstance();



                String pattern = " HH:mm";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(hr+":"+ms);
                String date = simpleDateFormat.format(new Date());
                System.out.println(date);




                String s;
                DateFormat formatter;

                formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date11 = null;
                try {
                    date11 = formatter.parse(startDate);//"2013-07-17"
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                formatter = new SimpleDateFormat("yyMMdd");
                s = formatter.format(date11);
               Log.e("new ",s);





                LocalDate datePart = date11.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
               Calendar cal = Calendar.getInstance();
              // cal.setTime(date);
                Instant instant = Instant.ofEpochMilli(cal.getTimeInMillis());
                LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();

                LocalTime timePart = LocalTime.parse(date);
                LocalDateTime dt = LocalDateTime.of(datePart, timePart);

                Log.e("Final date is ",dt.toString());
              //  msec= dt.toEpochSecond(OffsetDateTime.now().getOffset());



               *//* Date date1= null;
                try {

                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");//yyyy-MM-dd HH:mm:ss.SSS
                    Date dateStr = formatter1.parse(mDate+" "+date);
                    String formattedDate = formatter1.format(dateStr);
                    System.out.println("yyyy-MM-dd date is ==>"+formattedDate);


                    msec = dateStr.getTime();

                } catch (ParseException e) {
                    e.printStackTrace();
                }*/

            val data = HashMap<String, Any>()
            data["EventAgenda"] = agenda.getText().toString()
            data["Date"] = msec
            data["Time"] = timeTxt.getText().toString()
            data["Emails"] = newEmail.getText().toString()

            currentUser.setValue(data)


            currentUser.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dateTxt.setText("")
                    agenda.setText("")
                    timeTxt.setText("")
                    newEmail.setText("")

                    Toast.makeText(this@KAddNewEvents, "Event added ", Toast.LENGTH_SHORT).show()

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@KAddNewEvents, "Unable to add Event  ", Toast.LENGTH_SHORT).show()
                }
            })

            //    currentUser.setValue(users);


            //  String userId1 = auth.getCurrentUser().getUid();
            //  DatabaseReference currentUser = mDatabase.child(userId1);
            //  currentUser.child("Emails").setValue(list);
        })


        /*  mListItemRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG+"Added",dataSnapshot.getValue(EventPOJO.class).EventAgenda.toString());
               // fetchData(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG+"Changed",dataSnapshot.getValue(EventPOJO.class).EventAgenda.toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG+"Removed",dataSnapshot.getValue(EventPOJO.class).EventAgenda.toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG+"Moved",dataSnapshot.getValue(EventPOJO.class).EventAgenda.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG+"Cancelled",databaseError.toString());
            }

        });*/
    }


    //sign out method
    fun signOut() {
       auth?.signOut()
    }

    override fun onResume() {
        super.onResume()

    }

    public override fun onStart() {
        super.onStart()
        auth?.addAuthStateListener(authListener!!)
    }

    public override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth?.removeAuthStateListener(authListener!!)
        }
    }

}