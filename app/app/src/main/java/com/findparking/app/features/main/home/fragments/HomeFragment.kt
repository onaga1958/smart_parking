package com.findparking.app.features.main.home.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.findparking.app.AppViewModelsFactory
import com.findparking.app.R
import com.findparking.app.ToolbarFragment
import com.findparking.app.data.remote.responsebodies.HomeResponseBody
import com.findparking.app.features.main.home.viewmodel.HomeViewModel
import com.findparking.app.toolbox.extensions.hide
import com.findparking.app.toolbox.extensions.toastSh
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.bottom_sheet_player.*
import kotlinx.android.synthetic.main.fragment_home.*
import viewModelProvider
import javax.inject.Inject

private lateinit var lastLocation: Location
private lateinit var fusedLocationClient: FusedLocationProviderClient

class HomeFragment : ToolbarFragment(),
    OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener {
    override fun onCameraIdle() {

    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return true
    }

    override fun onMapClick(p0: LatLng?) {
        viewModel.getHomeData(
            String.format("%.7f", p0?.latitude)+","+
                    String.format("%.7f", p0?.longitude),
            "47.3718918,28.5249408"
        )
    }

    private var googleMap: GoogleMap? = null

    override fun setToolbar() {
        // toolbar.inflateMenu(R.menu.profile_navigation)
    }

    companion object {
        val TAG = HomeFragment::class.java.simpleName
        fun newInstance() = HomeFragment()
    }

    override fun setListeners() {
    }

    @Inject
    lateinit var vmFactory: AppViewModelsFactory
    private lateinit var viewModel: HomeViewModel

    override fun layoutId(): Int = R.layout.fragment_home

    override fun onViewReady(inflatedView: View, args: Bundle?) {
        val mapFragment = frg as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapFragment.onExitAmbient()
        activity?.let { safeActivity ->
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(safeActivity)
        }

    }

    override fun initViewModel() {
        viewModel = viewModelProvider(vmFactory)
        viewModel.collectionWithBooks.observe(this, Observer {
            it?.let { parking ->
                setMarker(parking.adress)
                setDataToBottomSheet(parking)
            }
        })
    }

    private fun setDataToBottomSheet(parking: HomeResponseBody) {
        tvTime.text = "Arrival time: " + parking.arrival_time
        tvAddress.text = parking.adress
        tvOccupation.text = parking.occupation.toString()
        pb_main.hide()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        try {
            this.googleMap?.setOnCameraIdleListener(this)
            //this.googleMap?.setOnCameraMoveStartedListener(this)
            this.googleMap?.setOnMarkerClickListener(this)
            this.googleMap?.setOnMapClickListener(this)
            this.googleMap?.uiSettings?.isZoomControlsEnabled = true
            activity?.let {
                fusedLocationClient.lastLocation.addOnSuccessListener(it) { location ->
                    if (location != null) {
                        lastLocation = location
                        viewModel.getHomeData(
                            "47.3720682,8.5359213",
                            "47.3686744,8.5458139"
                        )
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        this.googleMap?.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                currentLatLng,
                                12f
                            )
                        )
                    }
                }
            }
//            clusterManager = ClusterManager(context, googleMap)
//            if (context != null && googleMap != null && clusterManager != null) {
//                clusterRenderer = ToUClusterRenderer(context!!, googleMap, clusterManager!!, bitmapMarkerHandler, bus)
//                clusterManager?.renderer = clusterRenderer
//            }
//                if (arguments != null && arguments!!.containsKey(Constants.ToUJobSchedule.NEAREST_LOCATION)) {
//                    val latitude = arguments!!.getDouble(Constants.ToUJobSchedule.NEAREST_LOCATION_LAT)
//                    val longitude = arguments!!.getDouble(Constants.ToUJobSchedule.NEAREST_LOCATION_LONG)
//                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 15F))
//                    arguments!!.remove(Constants.ToUJobSchedule.NEAREST_LOCATION)
//                    arguments!!.remove(Constants.ToUJobSchedule.NEAREST_LOCATION_LAT)
//                    arguments!!.remove(Constants.ToUJobSchedule.NEAREST_LOCATION_LONG)
//                } else
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(presenter.getUserCountry()!!.latitude,
//                        presenter.getUserCountry()!!.longitude), presenter.getUserCountry()!!.zoom))
//            }
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
        enableMyLocation()
    }

    private fun setMarker(coordinate: String) {
        val strs = coordinate.split(",").toTypedArray()
        this.googleMap?.addMarker(
            MarkerOptions().position(
                LatLng(strs[0].toDouble(),strs[1].toDouble())
        ))
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            this.googleMap?.isMyLocationEnabled = true
            this.googleMap?.uiSettings?.isMyLocationButtonEnabled = false
        } else {
            activity?.parent?.let { parentActivity ->
                ActivityCompat.requestPermissions(
                    parentActivity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1110
                )
            }

        }
    }

}
