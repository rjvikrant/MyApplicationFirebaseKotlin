package vikrant.com.myapplicationfirebase.mykotlin

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import vikrant.com.myapplicationfirebase.EventPOJO
import java.text.SimpleDateFormat
import java.util.*
import vikrant.com.myapplicationfirebase.R





class KMain2Activity : AppCompatActivity() {

    private val TAG = "ListActivity"
    internal lateinit var mListItemRef: DatabaseReference
    private var mListItemsRecyclerView: RecyclerView? = null
    private var mAdapter: ListItemsAdapter? = null
    private var myListItems: ArrayList<EventPOJO>? = null
    private var database: FirebaseDatabase? = null
    private var auth: FirebaseAuth? = null
    internal lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        //  dbRef = database.getReference("User"+auth.getCurrentUser().getUid());// "/User/lavY0VIigzQaZ7xZBuyrSyWKgeC3"    https://myapplicationfirebase-1bd85.firebaseio.com/User/V31ISKWWJZbKB1dKS16R1lhpNHF2
        //   mListItemRef=dbRef.child("posts");

        dbRef = FirebaseDatabase.getInstance().getReference("User/" + auth?.getCurrentUser()!!.getUid())//     auth.getCurrentUser().getUid() https://myapplicationfirebase-1bd85.firebaseio.com/User/V31ISKWWJZbKB1dKS16R1lhpNHF2
        mListItemRef = dbRef.child("posts")


        // mListItemRef = mDB.child("posts");
        myListItems = ArrayList<EventPOJO>()
        mListItemsRecyclerView = findViewById(R.id.list) as RecyclerView
        // mListItemsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        mListItemsRecyclerView?.setLayoutManager(LinearLayoutManager(this))

        updateUI()

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)

       var btn = findViewById(R.id.btn) as Button
        var btn2 = findViewById(R.id.btn2) as Button
        var btn3 = findViewById(R.id.btn3) as Button
        var btn4 = findViewById(R.id.btn4) as Button
        var imgbtn = findViewById(R.id.signout) as AppCompatImageButton


        btn4.setOnClickListener(View.OnClickListener {


            val calendar = Calendar.getInstance()
            val y = calendar.get(Calendar.YEAR)
            val m = calendar.get(Calendar.MONTH)
            val d = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(this@KMain2Activity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                //  dd-M-yyyy hh:mm:ss


                var date = Calendar.getInstance()
                date.set(year,month,dayOfMonth,0,0)
                date.set(Calendar.DAY_OF_MONTH,date.getActualMinimum(Calendar.DAY_OF_MONTH))

                val before = java.lang.Long.valueOf(date.timeInMillis.toString())
                Log.e("current time",  before.toString())

                date.add(Calendar.DAY_OF_MONTH,date.getActualMaximum(Calendar.DAY_OF_MONTH))
                val after = java.lang.Long.valueOf(date.timeInMillis.toString())

                //This method returns the time in millis
                Log.e("next time", after.toString())


                val mDatabaseRef = FirebaseDatabase.getInstance().getReference("User/" + auth?.getCurrentUser()!!.getUid() + "/posts")

                val query = mDatabaseRef.orderByChild("Date").startAt(before.toDouble()).endAt(after.toDouble())

                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        myListItems?.clear()

                        for (data in dataSnapshot.children) {


                            val models = data.getValue(EventPOJO::class.java)
                            myListItems?.add(models!!)

                        }

                        updateUI()

                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })



            }, y, m, d)
            datePicker.show()



        })


        imgbtn.setOnClickListener(View.OnClickListener {
            auth?.signOut()

            startActivity(Intent(this@KMain2Activity, KLoginActivity::class.java))
            finish()
        })

        btn.setOnClickListener(View.OnClickListener {


            val calendar = Calendar.getInstance()
            val y = calendar.get(Calendar.YEAR)
            val m = calendar.get(Calendar.MONTH)
            val d = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(this@KMain2Activity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                //  dd-M-yyyy hh:mm:ss


                var date = Calendar.getInstance()
                date.set(year,month,dayOfMonth,0,0)
              //  date.set(Calendar.DAY_OF_WEEK,date.getActualMinimum(Calendar.DAY_OF_WEEK))
                date.add(Calendar.DAY_OF_MONTH,  Calendar.MONDAY-calendar.get(Calendar.DAY_OF_WEEK));
                val before = java.lang.Long.valueOf(date.timeInMillis.toString())
                Log.e("current time",  before.toString())

                date.add(Calendar.DAY_OF_MONTH,6)
                val after = java.lang.Long.valueOf(date.timeInMillis.toString())

                //This method returns the time in millis
                Log.e("next time", after.toString())


                val mDatabaseRef = FirebaseDatabase.getInstance().getReference("User/" + auth?.getCurrentUser()!!.getUid() + "/posts")

                val query = mDatabaseRef.orderByChild("Date").startAt(before.toDouble()).endAt(after.toDouble())

                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        myListItems?.clear()

                        for (data in dataSnapshot.children) {


                            val models = data.getValue(EventPOJO::class.java)
                            myListItems?.add(models!!)

                        }

                        updateUI()

                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })



            }, y, m, d)
            datePicker.show()


        })


        btn2.setOnClickListener(View.OnClickListener {

            val calendar = Calendar.getInstance()
            val y = calendar.get(Calendar.YEAR)
            val m = calendar.get(Calendar.MONTH)
            val d = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(this@KMain2Activity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                //  dd-M-yyyy hh:mm:ss


                var date = Calendar.getInstance()
                date.set(year,month,dayOfMonth,0,0)
                val before = java.lang.Long.valueOf(date.timeInMillis.toString())
                Log.e("current time", before.toString())

                date.add(Calendar.DATE, +1)
                val after = java.lang.Long.valueOf(date.timeInMillis.toString())

                //This method returns the time in millis
                Log.e("next time", after.toString())


                val mDatabaseRef = FirebaseDatabase.getInstance().getReference("User/" + auth?.getCurrentUser()!!.getUid() + "/posts")

                val query = mDatabaseRef.orderByChild("Date").startAt(before.toDouble()).endAt(after.toDouble())

                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        myListItems?.clear()

                        for (data in dataSnapshot.children) {


                            val models = data.getValue(EventPOJO::class.java)
                            myListItems?.add(models!!)

                        }

                        updateUI()

                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })



            }, y, m, d)
            datePicker.show()

        })


        btn3.setOnClickListener(View.OnClickListener { startActivity(Intent(this@KMain2Activity, KAddNewEvents::class.java)) })


        mListItemRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                Log.e(TAG + "Added", dataSnapshot.getValue(EventPOJO::class.java)!!.toString())
                fetchData(dataSnapshot)

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                Log.e(TAG + "Changed", dataSnapshot.getValue(EventPOJO::class.java)!!.toString())
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.e(TAG + "Removed", dataSnapshot.getValue(EventPOJO::class.java)!!.toString())
                fetchData(dataSnapshot)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                Log.e(TAG + "Moved", dataSnapshot.getValue(EventPOJO::class.java)!!.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG + "Cancelled", databaseError.toString())
            }

        })

    }


    private fun fetchData(dataSnapshot: DataSnapshot) {
        val listItem = dataSnapshot.getValue(EventPOJO::class.java)
        myListItems?.add(listItem!!)
        updateUI()
    }

    private fun updateUI() {
        mAdapter = ListItemsAdapter(myListItems!!)
        mListItemsRecyclerView?.setAdapter(mAdapter)
    }

    private inner class ListItemsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mNameTextView: TextView
        var mEmailTextView: TextView
        var mDateTextView: TextView
        var mTimeTextView: TextView

        init {
            mNameTextView = itemView.findViewById(R.id.list_title) as TextView
            mEmailTextView = itemView.findViewById(R.id.list_desc) as TextView
            mDateTextView = itemView.findViewById(R.id.date) as TextView
            mTimeTextView = itemView.findViewById(R.id.time) as TextView
        }

        fun bindData(s: EventPOJO) {
            mNameTextView.text = s.eventAgenda
            mEmailTextView.text = s.emails
            val simple = SimpleDateFormat("dd-MMM-yyyy")
            val result = Date(s.date)
            mDateTextView.text = simple.format(result)
            mTimeTextView.text = SimpleDateFormat("hh:mm:ss").format(s.date)
        }
    }

    private inner class ListItemsAdapter(private val mListItems: ArrayList<EventPOJO>) : RecyclerView.Adapter<ListItemsHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemsHolder {
            val layoutInflater = LayoutInflater.from(this@KMain2Activity)
            val view = layoutInflater.inflate(R.layout.list_item, parent, false)
            return ListItemsHolder(view)

        }

        override fun onBindViewHolder(holder: ListItemsHolder, position: Int) {
            val s = mListItems[position]
            holder.bindData(s)
        }

        override fun getItemCount(): Int {
            return mListItems.size
        }
    }


}
