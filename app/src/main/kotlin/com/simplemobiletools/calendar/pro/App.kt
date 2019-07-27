package com.simplemobiletools.calendar.pro

import androidx.multidex.MultiDexApplication
import com.simplemobiletools.commons.extensions.checkUseEnglish

class App : MultiDexApplication() {

    companion object {

    }
    override fun onCreate() {

        super.onCreate()
        checkUseEnglish()
    }
}
