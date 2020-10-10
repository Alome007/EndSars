package end.sars.now.Activities

import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import end.sars.now.Activities.MainActivity
import end.sars.now.Adapters.cardAdapter
import end.sars.now.Helpers.cardHelper
import end.sars.now.Listeners.RecyclerItemClickListener
import end.sars.now.R
import im.delight.android.location.SimpleLocation
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    var adapter: cardAdapter? = null
    var greetings = ArrayList<String>()
    var menu: ImageView? = null
    var locationManager: LocationManager? = null
    var latitude: String? = null
    var longitude: String? = null
    var location: SimpleLocation? = null
    var lon = 0.0
    var lat = 0.0
    var arrayList = ArrayList<cardHelper>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //        Dexter.withContext(MainActivity.this)
//                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new PermissionListener() {
//                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
////                        getLocation();
//                    }
//                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
////                        Toast.makeText(MainActivity.this, "We need your permission fr that!", Toast.LENGTH_SHORT).show();
//                    }
//                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
//                }).check();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        initUI()
        initGreetings()
        menu!!.setOnClickListener {
            val sequence = arrayOf<CharSequence>("Use Web Version")
            val alert = AlertDialog.Builder(this@MainActivity)
            alert.setTitle("Options")
            alert.setItems(sequence) { dialogInterface, i ->
                when (i) {
                    0 -> {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://endsars.vercel.app/politicians"))
                        startActivity(browserIntent)
                    }
                }
            }
            alert.show()
        }
        var helper = cardHelper("Twitter", R.drawable.ic_twitter)
        arrayList.add(helper)
        helper = cardHelper("Broadcast", R.drawable.ic_whatsapp)
        arrayList.add(helper)
        helper = cardHelper("Remind Politicians", R.drawable.ic_feather)
        arrayList.add(helper)
        recyclerView!!.adapter = adapter
        recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(this, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                when (position) {
                    0 -> {
                        val handles_string = resources.getString(R.string.handles)
                        val handles = handles_string.split(",").toTypedArray()
                        var r = Random()
                        val randomNumber = r.nextInt(handles.size)
                        r = Random()
                        Log.d("RandomNum", handles[randomNumber])
                        val messagesRes = resources.getString(R.string.default_messages)
                        val messages = messagesRes.split(",").toTypedArray()
                        val randomMessage = r.nextInt(messages.size)
                        Log.d("greeting", getRandomGreeting(greetings))
                        Toast.makeText(this@MainActivity, messages[randomMessage].replace("message:", "") + "#EndSars #EndPloiceBrutality #EndSarsNow", Toast.LENGTH_SHORT).show()
                        val tweetUrl = "https://twitter.com/intent/tweet?text=" + getRandomGreeting(greetings) + " " + handles[randomNumber] + " " + "" + messages[randomMessage].replace("message:", "") + "&hashtags=EndSars,EndPoliceBrutality,EndSarsNow"
                        val uri = Uri.parse(tweetUrl)
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                    1 -> openWhatsApp(resources.getString(R.string.whatsapp_msg))
                    2 -> startActivity(Intent(this@MainActivity, remindPoliticians::class.java))
                }
            }
            override fun onLongItemClick(view: View, position: Int) {}
        }))
    }

    fun openWhatsApp(msg: String) {
        val pm = packageManager
        try {
            val waIntent = Intent(Intent.ACTION_SEND)
            waIntent.type = "text/plain"
            val info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp")
            waIntent.putExtra(Intent.EXTRA_TEXT, msg)
            startActivity(Intent.createChooser(waIntent, "Share with"))
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show()
        } catch (e: Exception) {
//            e.printStacktrace();
        }
    }

    private fun initGreetings() {
        greetings.add("Dear")
        greetings.add("Distinguished")
        greetings.add("Hello")
        greetings.add("Hi, just a reminder")
    }

    private fun initUI() {
        menu = findViewById(R.id.menu)
        recyclerView = findViewById(R.id.recyclerview)
        adapter = cardAdapter(arrayList, this)
        recyclerView.setLayoutManager(LinearLayoutManager(this, RecyclerView.VERTICAL, false))
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    } //    private void getLocation() {

    //        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    //        if (ActivityCompat.checkSelfPermission(
    //                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
    //                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
    ////            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 199);
    //        } else {
    //            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    //            if (locationGPS != null) {
    //                double lat = locationGPS.getLatitude();
    //                double longi = locationGPS.getLongitude();
    //                latitude = String.valueOf(lat);
    //                longitude = String.valueOf(longi);
    //                Log.d("Locations",latitude);
    //            } else {
    //                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
    //            }
    //        }
    //    }
    companion object {
        fun getRandomGreeting(items: ArrayList<String>): String {
            return items[Random().nextInt(items.size)]
        }
    }
}