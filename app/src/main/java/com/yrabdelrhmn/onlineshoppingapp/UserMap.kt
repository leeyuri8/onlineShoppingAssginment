package com.yrabdelrhmn.onlineshoppingapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.yrabdelrhmn.onlineshoppingapp.databinding.ActivityUserMapBinding
import kotlinx.android.synthetic.main.activity_user_map.*

class UserMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityUserMapBinding
    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var dbReference : DatabaseReference = db.getReference("address")
    private lateinit var find_location:Button
    private lateinit var latLng : LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        find_location = findViewById(R.id.btn_find_location)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        dbReference.addValueEventListener(locListener)

    }
    val locListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
           if(snapshot.exists()) {
               val location = snapshot.child("address").getValue(LocationInfo::class.java)
               val locationLat = location?.latitude
               val locationLong = location?.longitude

               find_location.setOnClickListener {

                   if(locationLat!=null && locationLong!=null){
                       val latLng = LatLng(locationLat, locationLong)
                       mMap.addMarker(MarkerOptions().position(latLng)
                           .title("The user is currently here"))

                       val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)
                       mMap.moveCamera(update)

                   }else{
                       Log.e("UserMap", "user location cannot be found")

                   }
               }
           }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(applicationContext, "Could not read from database", Toast.LENGTH_LONG).show()
        }

    }

        /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
   mMap.uiSettings.isCompassEnabled=true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        // Add a marker in Sydney and move the camera
        val storeLocation = LatLng(37.60082647180433, 127.00909177794884)
        mMap.addMarker(MarkerOptions().position(storeLocation).title("Marker in South Korea"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation,14f))

            // path between store and user
  path.setOnClickListener {
      val polyLine = mMap.addPolyline(
          PolylineOptions()
              .add(storeLocation)
              .add(latLng)

      )
      polyLine.isVisible=true

  }

    }
}
@IgnoreExtraProperties
data class LocationInfo(
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0
)