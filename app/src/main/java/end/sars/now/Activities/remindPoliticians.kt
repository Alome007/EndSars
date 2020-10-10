package end.sars.now.Activities

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import end.sars.now.Adapters.politiciansAdapter
import end.sars.now.Helpers.politiciansHelper
import end.sars.now.Listeners.RecyclerItemClickListener
import end.sars.now.R
import java.util.*
class remindPoliticians : AppCompatActivity() {
   lateinit var recyclerView: RecyclerView
    var adapter: politiciansAdapter? = null
    var searchView: SearchView? = null
    var search: ImageView? = null
    var toolbar: Toolbar? = null
    var dialog:ProgressDialog?=null
    var arrayList = ArrayList<politiciansHelper>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.remind_politicians)
        window.statusBarColor = Color.BLACK
        initUI()
        initData()
        dialog= ProgressDialog(this)
        dialog?.setMessage("Please Wait...")
        dialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        toolbar!!.setNavigationOnClickListener { onBackPressed() }
        search!!.setOnClickListener { searchView!!.visibility = View.VISIBLE }
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
        recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(this, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                if (arrayList[position].phoneNo.isEmpty()) {
                    val options = arrayOf<CharSequence>("SMS", "Email")
                    val alert = AlertDialog.Builder(this@remindPoliticians)
                    alert.setTitle(arrayList[position].name)
                    alert.setItems(options) { dialogInterface, i ->
                        when (i) {
                            0 -> {
                                val intent = Intent(Intent.ACTION_SENDTO)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                intent.data = Uri.parse("smsto:" + arrayList[position].phoneNo) // This ensures only SMS apps respond
                                intent.putExtra("sms_body", resources.getString(R.string.text_template))
                                startActivity(intent)
                            }
                            1 -> {
                                intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + arrayList[position].email))
                                intent!!.putExtra(Intent.EXTRA_SUBJECT, "#EndSARS - End SARS!")
                                intent!!.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.text_template))
                                startActivity(intent)
                            }
                        }
                    }
                    alert.show()
                } else {
                    val options = arrayOf<CharSequence>("SMS", "Email", "Phone Call")
                    val alert = AlertDialog.Builder(this@remindPoliticians)
                    alert.setTitle(arrayList[position].name)
                    alert.setItems(options) { dialogInterface, i ->
                        when (i) {
                            0 -> {
                                intent = Intent(Intent.ACTION_SENDTO)
                                intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                intent!!.data = Uri.parse("smsto:" + arrayList[position].phoneNo) // This ensures only SMS apps respond
                                intent!!.putExtra("sms_body", resources.getString(R.string.text_template))
                                startActivity(intent)
                            }
                            1 -> {
                                intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + arrayList[position].email))
                                intent!!.putExtra(Intent.EXTRA_SUBJECT, "#EndSARS - End SARS!")
                                intent!!.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.text_template))
                                startActivity(intent)
                            }
                            2 -> Dexter.withContext(this@remindPoliticians)
                                    .withPermission(Manifest.permission.CALL_PHONE)
                                    .withListener(object : PermissionListener {
                                        override fun onPermissionGranted(response: PermissionGrantedResponse) {
                                            intent = Intent(Intent.ACTION_CALL)
                                            intent!!.data = Uri.parse("tel:" + arrayList[position].phoneNo)
                                            startActivity(intent)
                                        }

                                        override fun onPermissionDenied(response: PermissionDeniedResponse) {
                                            Toast.makeText(this@remindPoliticians, "We need your permission fir that!", Toast.LENGTH_SHORT).show()
                                        }

                                        override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) { /* ... */
                                        }
                                    }).check()
                        }
                    }
                    alert.show()
                }
            }

            override fun onLongItemClick(view: View, position: Int) {}
        }))
    }

    private fun initData() {
        dialog?.show()
        val db = FirebaseDatabase.getInstance().reference
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (snap in snapshot.children) {
                    val helper = snap.getValue(politiciansHelper::class.java)!!
                    val name = helper.name
                    val email = helper.email
                    val location = helper.state
                    val id = helper.id
                    val phone = helper.phoneNo
                    val politiciansHelper = politiciansHelper()
                    politiciansHelper.email = email
                    politiciansHelper.id = id
                    politiciansHelper.phoneNo = phone
                    politiciansHelper.name = name
                    politiciansHelper.state = location
                    arrayList.add(politiciansHelper)
                }
                if (arrayList.size!=0){
                    dialog?.dismiss()
                }
                recyclerView!!.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun initUI() {
        toolbar = findViewById(R.id.tool_bar)
        search = findViewById(R.id.search_img)
        searchView = findViewById(R.id.search_view)
        recyclerView = findViewById(R.id.recyclerview)
        adapter = politiciansAdapter(arrayList, this)
        recyclerView.setLayoutManager(LinearLayoutManager(this, RecyclerView.VERTICAL, false))
    }
}