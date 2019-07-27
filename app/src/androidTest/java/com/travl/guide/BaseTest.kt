package com.travl.guide

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.UiDevice
import com.travl.guide.ui.activity.MainActivity
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseTest {
    @Rule
    @JvmField var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)
    companion object{
        const val DEFAULT_TIMEOUT = 3000L
    }
    protected val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
}