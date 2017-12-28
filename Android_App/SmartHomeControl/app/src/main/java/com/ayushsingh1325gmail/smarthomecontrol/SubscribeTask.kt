package com.ayushsingh1325gmail.smarthomecontrol

import android.app.Activity
import android.os.AsyncTask
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.JsonObject
import com.pubnub.api.models.consumer.pubsub.PNMessageResult

class SubscribeTask: AsyncTask<Void, Void, Void> {
    var mTextView_temp: TextView
    var mTextView_humidity: TextView
    var mWeather_image: ImageView
    var mMessage: JsonObject
    var mResult = JsonObject()
    var mTemp: String = ""
    var mHumidity: String = ""
    var mLdr: Boolean = true
    var mRain: Boolean = false
    var mWeather: Int = 0
    var tester: Boolean = false
    var mFire: Boolean = false
    var mTheft: Boolean = false
    var mFlood: Boolean = false
    var mQuake: Boolean = false
    var mAtv: Activity
    var listener: dialogListener? = null

    fun exec(listener: dialogListener){
        this.listener = listener
        this.execute()
    }


    constructor(atv: Activity, textView_temp: TextView, textView_humidity: TextView, weather_image: ImageView, message: PNMessageResult){
        mMessage = message.message.asJsonObject
        mTextView_temp = textView_temp
        mTextView_humidity = textView_humidity
        mWeather_image = weather_image
        mAtv = atv
    }

    override fun doInBackground(vararg p0: Void?): Void? {
        if (mMessage.has("pi")){
            mResult = mMessage.getAsJsonObject("pi")
            mTemp = mResult.get("temp").asString
            mHumidity = mResult.get("humidity").asString
            mLdr = mResult.get("ldr").asBoolean
            mRain = mResult.get("rain").asBoolean
            mFire = mResult.get("fire").asBoolean
            mTheft = mResult.get("burglary").asBoolean
            mFlood = mResult.get("flood").asBoolean
            mQuake = mResult.get("quake").asBoolean
            tester = true
            if(mRain){
              mWeather = R.drawable.rainy
            }else if(mLdr){
                mWeather = R.drawable.suny
            }else{
                mWeather = R.drawable.cloudyy
            }
        }
        return null
    }

    override fun onPostExecute(result: Void?) {
        if(tester) {
            mTextView_temp.text = mTemp
            mTextView_humidity.text = mHumidity
            mWeather_image.setImageResource(mWeather)
            if (mFire) {
                listener?.onFinish("Fire", "There might be a fire in the house", R.drawable.house_fire, "Stop Alarm", true)
            }
            if (mTheft){
                listener?.onFinish("Burglary", "There is someone in your house", R.drawable.burglar, "Stop Alarm", true)
            }
            if (mFlood){
                listener?.onFinish("Flood", "There is water in the Room", R.drawable.flood,"Stop Alarm", true)
            }
            if (mQuake){
                listener?.onFinish("EarthQuake", "There might be an earthquake near your house area", R.drawable.earthquake,"Stop Alarm", true)
            }
        }
    }
}