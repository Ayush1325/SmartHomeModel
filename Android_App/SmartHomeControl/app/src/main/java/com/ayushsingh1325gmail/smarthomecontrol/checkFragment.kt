package com.ayushsingh1325gmail.smarthomecontrol

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_check.*
import kotlinx.android.synthetic.main.fragment_house.*

class checkFragment(var message: String) : DialogFragment() {
    private var passTest: Boolean = false

    fun newInstance(title: String, mMessage: Int): checkFragment {
        val checkfrag = checkFragment(mMessage.toString())
        val args = Bundle()
        args.putString("title", title)
        checkfrag.arguments = args
        return checkfrag
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_check, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pass.requestFocus()
        passTest = !activity.toggle_door.isChecked
        submit.setOnClickListener {
            if (pass.text.toString() == "1325"){
                Toast.makeText(activity,"correct pass", Toast.LENGTH_SHORT).show()
                (activity as NavActivity).publish(message, "main_door")
                passTest = !passTest
                dialog.dismiss()
            }else{
                Toast.makeText(activity,"wrong pass", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface?){
        super.onDismiss(dialog)
        activity.toggle_door.isChecked = passTest
    }
}
