package com.ayushsingh1325gmail.smarthomecontrol

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class CategoryAdapter(
        /** Context of the app  */
        private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val tabTitles = arrayOf("Shortcuts", "Room")

    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return ShortcutsFragment()
        } else {
            return HouseFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitles[position]
    }
}