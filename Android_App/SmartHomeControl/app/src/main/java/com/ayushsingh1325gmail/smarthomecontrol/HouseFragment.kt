package com.ayushsingh1325gmail.smarthomecontrol

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.fragment_house.*
import android.widget.CompoundButton

class HouseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_house, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        light1_intensity_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var mProgress: Int = 0
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                (activity as NavActivity).publish(mProgress.toString(), "light1")
                if(mProgress == 255) {
                    toggle_light1.isChecked = true
                }else if (mProgress == 0){
                    toggle_light1.isChecked = false
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mProgress = progress
            }
        })

        light2_intensity_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var mProgress: Int = 0
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                (activity as NavActivity).publish(mProgress.toString(), "light2")
                if(mProgress == 255) {
                    toggle_light2.isChecked = true
                }else if (mProgress == 0){
                    toggle_light2.isChecked = false
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mProgress = progress
            }
        })

        fan1_intensity_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var mProgress: Int = 0
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                (activity as NavActivity).publish((mProgress * 51).toString(), "fan1")
                if(mProgress == 5) {
                    toggle_fan1.isChecked = true
                }else if (mProgress == 0){
                    toggle_fan1.isChecked = false
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mProgress = progress
            }
        })

        fan2_intensity_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var mProgress: Int = 0
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                (activity as NavActivity).publish((mProgress * 51).toString(), "fan2")
                if(mProgress == 5) {
                    toggle_fan2.isChecked = true
                }else if (mProgress == 0){
                    toggle_fan2.isChecked = false
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mProgress = progress
            }
        })

        toggle_door.setOnClickListener {
            if (toggle_door.isChecked){
                (activity as NavActivity).showEditDialog(1)
            }else {
                (activity as NavActivity).showEditDialog(0)
            }
        }

        toggle_garage.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                (activity as NavActivity).publish(1.toString(), "garage_door")
            } else {
                (activity as NavActivity).publish(0.toString(), "garage_door")
            }
        })

        toggle_light1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                (activity as NavActivity).publish(255.toString(), "light1")
                light1_intensity_bar.progress = 255
            } else {
                (activity as NavActivity).publish(0.toString(), "light1")
                light1_intensity_bar.progress = 0
            }
        })

        toggle_light2.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                (activity as NavActivity).publish(255.toString(), "light2")
                light2_intensity_bar.progress = 255
            } else {
                (activity as NavActivity).publish(0.toString(), "light2")
                light2_intensity_bar.progress = 0
            }
        })

        toggle_fan1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                (activity as NavActivity).publish(255.toString(), "fan1")
                fan1_intensity_bar.progress = 5
            } else {
                (activity as NavActivity).publish(0.toString(), "fan1")
                fan1_intensity_bar.progress = 0
            }
        })

        toggle_fan2.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                (activity as NavActivity).publish(255.toString(), "fan2")
                fan2_intensity_bar.progress = 5
            } else {
                (activity as NavActivity).publish(0.toString(), "fan2")
                fan2_intensity_bar.progress = 0
            }
        })
    }
}
