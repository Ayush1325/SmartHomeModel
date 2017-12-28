package com.ayushsingh1325gmail.smarthomecontrol


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.fragment_house.*
import kotlinx.android.synthetic.main.fragment_shortcuts.*

class ShortcutsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_shortcuts, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        val act = activity as NavActivity
        toggle_poweroff.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                (activity as NavActivity).publish("poweroff", "system")
            }
        })

        toggle_night_mode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                (activity as NavActivity).publish(1.toString(), "night_mode")
            } else {
                (activity as NavActivity).publish(0.toString(), "night_mode")
            }
        })

        toggle_burglary_mode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            act.burglaryAlert = !isChecked
        })

        toggle_fire_mode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            act.fireAlert = !isChecked
        })

        toggle_night_mode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activity.toggle_burglary_mode.isChecked = true
                activity.toggle_fire_mode.isChecked = true
                activity.toggle_fan1.isChecked = true
                Handler().postDelayed({
                    activity.toggle_fan2.isChecked = true
                }, 2000)
                Handler().postDelayed({
                    activity.toggle_light1.isChecked = false
                }, 4000)
                Handler().postDelayed({
                    activity.toggle_light2.isChecked = false
                }, 6000)
                Handler().postDelayed({
                    activity.toggle_garage.isChecked = false
                }, 8000)
            } else {
                activity.toggle_burglary_mode.isChecked = false
                activity.toggle_fire_mode.isChecked = false
                activity.toggle_light1.isChecked = true
                Handler().postDelayed({
                    activity.toggle_light2.isChecked = true
                }, 2000)
                Handler().postDelayed({
                    activity.toggle_fan1.isChecked = false
                }, 4000)
                Handler().postDelayed({
                    activity.toggle_fan2.isChecked = false
                }, 6000)
            }
        })
    }
}