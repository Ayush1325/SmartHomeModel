package com.ayushsingh1325gmail.smarthomecontrol

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_nav.*
import kotlinx.android.synthetic.main.app_bar_nav.*
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.util.Log
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import kotlinx.android.synthetic.main.app_bar_nav.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_house.*
import java.util.*


class NavActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val pnconfig = PNConfiguration()
    var pubnub = PubNub(pnconfig)
    val status_fragment: Fragment = HomeFragment()
    val commands_fragment: Fragment = MainFragment()
    var floodAlert: Boolean = false
    var fireAlert: Boolean = true
    var burglaryAlert: Boolean = true
    var quakeAlert: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        fragmentChange("status", status_fragment, commands_fragment)

        if (isNetworkConnected()) {
            var mProgressDialog = ProgressDialog(this)
            mProgressDialog.setMessage("Please wait...")
            mProgressDialog.setCancelable(true)
            mProgressDialog.show()
            pnconfig.setPublishKey("pub-c-7087e6f9-65a8-4608-93c9-7e1264fd5743")
            pnconfig.setSubscribeKey("sub-c-8091636e-a1d2-11e7-a52c-d6a48d4f2d9f")
            pubnub = PubNub(pnconfig)
            pubnub.addListener(object : SubscribeCallback() {
                override fun status(pubnub: PubNub, status: PNStatus) {
                }
                override fun message(pubnub: PubNub, message: PNMessageResult) {
                    SubscribeTask(this@NavActivity, temp, humidity, weather, message).exec(object : dialogListener{
                        override fun onFinish(title: String, content: String, imageid: Int, button: String, boolean: Boolean) {
                            when(title){
                                "Fire" -> {
                                    if (!fireAlert) {
                                        fireAlert = true
                                        showAlertDialog(title, content, imageid, button)
                                    }
                                }
                                "Burglary" -> {
                                    if (!burglaryAlert) {
                                        burglaryAlert = true
                                        showAlertDialog(title, content, imageid, button)
                                    }
                                }
                                "Flood" -> {
                                    if (!floodAlert) {
                                        floodAlert = true
                                        showAlertDialog(title, content, imageid, button)
                                    }
                                }
                                "EarthQuake" -> {
                                    if (!quakeAlert) {
                                        quakeAlert = true
                                        showAlertDialog(title, content, imageid, button)
                                    }
                                }
                            }
                        }
                    })
                }
                override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {
                }
            })
            pubnub.subscribe().channels(Arrays.asList("test")).execute()
            mProgressDialog.dismiss()
        } else {
            AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("It looks like your internet connection is off. Please turn it " + "on and try again")
                    .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which -> }).setIcon(android.R.drawable.ic_dialog_alert).show()
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_status -> {
                // Handle the camera action
                fragmentChange("status", status_fragment, commands_fragment)
            }
            R.id.nav_controls -> {
                fragmentChange("commands", commands_fragment, status_fragment)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun isNetworkConnected(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager // 1
        val networkInfo = connMgr.activeNetworkInfo // 2
        return networkInfo != null && networkInfo.isConnected // 3
    }

    fun fragmentChange(tag: String, fragment: Fragment, hide1: Fragment){
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        Log.v("test", fragment.isAdded.toString())
        if(fragment.isAdded){
            ft.show(fragment)
            ft.hide(hide1)
        }else {
            ft.add(R.id.mainFragment, fragment, tag)
        }
        ft.commit()
    }

    override fun onStop() {
        super.onStop()
        pubnub.destroy()
    }

    fun publish(message: String, message_context: String){
        Pubnub(pubnub, message, message_context, this).execute()
    }

    fun showEditDialog(message: Int) {
        val fm = supportFragmentManager
        val passDialogFragment = checkFragment(message.toString()).newInstance("password", message)
        passDialogFragment.show(fm, "fragment_edit_name")
    }

    fun showAlertDialog(title: String, content: String, imageid: Int, button: String) {
        val fm = supportFragmentManager
        val alertDialogFragment = AlertFragment(title, content, imageid, button).newInstance(title, content, imageid, button)
        alertDialogFragment.show(fm, "fragment_alert")
    }
}