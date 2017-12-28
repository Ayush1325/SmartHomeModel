package com.ayushsingh1325gmail.smarthomecontrol


import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_alert.*
import kotlinx.android.synthetic.main.fragment_check.*

class AlertFragment(val mTitle: String, val mContent: String, val mImageId: Int, val mButton: String) : DialogFragment() {

    fun newInstance(title: String, content: String, imageid: Int, button: String): AlertFragment {
        val alertfrag = AlertFragment(title, content, imageid, button)
        val args = Bundle()
        args.putString("title", title)
        alertfrag.arguments = args
        return alertfrag
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_alert, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val act = activity as NavActivity
        title.text = mTitle
        alertContent.text = mContent
        alertImageId.setImageResource(mImageId)
        alertButton.text = mButton
        alertButton.setOnClickListener {
            (activity as NavActivity).publish(false.toString(), mTitle.toLowerCase())
            when(mTitle) {
                "Fire" -> {
                    act.fireAlert = false
                }
                "Burglary" -> {
                    act.burglaryAlert = false
                }
                "Flood" -> {
                    act.floodAlert = false
                }
                "EarthQuake" -> {
                    act.quakeAlert = false
                }
            }
            dialog.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
    }
}