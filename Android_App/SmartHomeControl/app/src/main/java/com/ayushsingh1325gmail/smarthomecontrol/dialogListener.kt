package com.ayushsingh1325gmail.smarthomecontrol

interface dialogListener {
    fun onFinish(title: String, content: String, imageid: Int, button: String, boolean: Boolean)
}