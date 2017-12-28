package com.ayushsingh1325gmail.smarthomecontrol

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import com.pubnub.api.PubNub

class Pubnub: AsyncTask<Void, Void, Void>{
    var y: PubNub
    var mMessage: String
    var mMessage_context: String
    var mContext: Context
    var json = JsonObject()

    constructor(x: PubNub, message: String, message_context: String, context: Context){
        y = x
        mMessage = message
        mContext = context
        mMessage_context = message_context
    }

    override fun doInBackground(vararg p0: Void?): Void? {
        var message = JsonObject()
        message.addProperty(mMessage_context, mMessage)
        json.add("phone", message)
        Log.v("json", json.toString())
        y.publish().channel("test").message(json).sync()
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        Toast.makeText(mContext, "Done!", Toast.LENGTH_LONG).show()
    }
}