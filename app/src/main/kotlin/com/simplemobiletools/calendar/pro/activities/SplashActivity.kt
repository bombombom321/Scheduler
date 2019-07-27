package com.simplemobiletools.calendar.pro.activities

import android.content.Intent
import com.simplemobiletools.calendar.pro.extensions.config
import com.simplemobiletools.calendar.pro.extensions.getNewEventTimestampFromCode
import com.simplemobiletools.calendar.pro.helpers.*
import com.simplemobiletools.commons.activities.BaseSplashActivity
import org.joda.time.DateTime

class SplashActivity : BaseSplashActivity() {
    override fun getAppPackageName() = packageName



    override fun initActivity() {

Global.mod = this

        when {

            //처음으로 실행할 액티비티 선택
            config.firstLaunch == true -> Intent(this, LoginActivity::class.java).apply {
                startActivity(this)
            }

            intent.extras?.containsKey(DAY_CODE) == true -> Intent(this, MainActivity::class.java).apply {
                putExtra(DAY_CODE, intent.getStringExtra(DAY_CODE))
                putExtra(VIEW_TO_OPEN, intent.getIntExtra(VIEW_TO_OPEN, LAST_VIEW))
                startActivity(this)
            }

            intent.extras?.containsKey(EVENT_ID) == true -> Intent(this, MainActivity::class.java).apply {
                putExtra(EVENT_ID, intent.getLongExtra(EVENT_ID, 0L))
                putExtra(EVENT_OCCURRENCE_TS, intent.getLongExtra(EVENT_OCCURRENCE_TS, 0L))
                startActivity(this)
            }
            intent.action == SHORTCUT_NEW_EVENT -> {
                val dayCode = Formatter.getDayCodeFromDateTime(DateTime())
                Intent(this, EventActivity::class.java).apply {
                    putExtra(NEW_EVENT_START_TS, getNewEventTimestampFromCode(dayCode))
                    startActivity(this)
                }
            }
            else -> startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }


}
