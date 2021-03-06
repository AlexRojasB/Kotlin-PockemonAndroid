package com.example.android.pockemonandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var PockemonList = ArrayList<Pockemon>()
    var PlayerPower = 0.0
    var AccessLocationCode = 123
    var location:Location?= null

    fun LoadPockemon(){
        PockemonList.add(Pockemon("Bulbasaur", R.drawable.bulbasaur, "Bulbasaur living in USA", 55.0, 37.7789994893035,-122.401846647263))
        PockemonList.add(Pockemon("Charmander", R.drawable.charmander, "Charmander living in Japan", 90.5, 37.7949568502667,-122.410494089127))
        PockemonList.add(Pockemon("Squirtle", R.drawable.squirtle, "Bulbasaur living in Iraq", 33.5, 37.7816621152613,-122.41225361824))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        LoadPockemon()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
    }

    fun checkPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), AccessLocationCode)
                return
            }
        }
        GetUserLocation()
    }

    @SuppressLint("MissingPermission")
    fun GetUserLocation(){
       var userLocation = UserLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3,3f, userLocation)
        var locThread = locationThread()
        locThread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            AccessLocationCode ->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    GetUserLocation()
                }else{
                    Toast.makeText(this, "We cannot access to the user Location", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions()
                .position(sydney)
                .title("Me")
                .snippet(" here is my location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14f))
    }

    inner class UserLocationListener:LocationListener{

        constructor(){
            location = Location("Start")
            location!!.longitude = 0.0
            location!!.latitude = 0.0

        }

        override fun onLocationChanged(locationLocal: Location?) {
            location = locationLocal
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }

    }

    inner class  locationThread:Thread {
        constructor():super(){

        }

        override fun run() {
           while (true){
               try {
                   runOnUiThread {
                       mMap.clear()

                       for (pockemon in PockemonList){
                           if (!pockemon.IsCatched){
                               mMap.addMarker(MarkerOptions()
                                       .position(LatLng(pockemon.Locationn!!.latitude, pockemon.Locationn!!.longitude))
                                       .title(pockemon.Name)
                                       .snippet(pockemon.Descr + ", power: " + pockemon.Power)
                                       .icon(BitmapDescriptorFactory.fromResource(pockemon.Image!!)))
                               if (location!!.distanceTo(pockemon.Locationn) < 2){
                                   pockemon.IsCatched = true
                                   PlayerPower += pockemon.Power!!
                                   Toast.makeText(applicationContext, "You catch new pockemon your new power is: " + PlayerPower, Toast.LENGTH_SHORT).show()
                               }
                           }
                       }
                       val userLoc = LatLng(location!!.latitude, location!!.longitude)
                       mMap.addMarker(MarkerOptions()
                               .position(userLoc)
                               .title("Me")
                               .snippet(" here is my location")
                               .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                       //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 14f))
                   }
                   Thread.sleep(1000)
               }catch (ex:Exception){

               }
           }
        }
    }
}
