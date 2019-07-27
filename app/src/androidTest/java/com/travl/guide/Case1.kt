package com.travl.guide

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.uiautomator.By
import android.support.test.uiautomator.Until
import com.travl.guide.ui.adapter.articles.travlzine.TravlZineArticlesAdapter
import org.junit.Test

class Case1 : BaseTest(){
    @Test
    fun test1(){
        //Check all screens available
        //Check start page
        onView(withId(R.id.container)).check(matches(isDisplayed()))
        device.wait(Until.findObject(By.res("start_page_travl_zine_container_title")), DEFAULT_TIMEOUT)
        onView(withId(R.id.start_page_travl_zine_container_title)).check(matches(isDisplayed()))
        device.wait(Until.findObject(By.res("start_page_travl_zine_container")), DEFAULT_TIMEOUT)
        onView(withId(R.id.start_page_travl_zine_container)).check(matches(isDisplayed()))
        //Go to Travlzine article
        onView(withId(R.id.travlzine_articles_preview_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition<TravlZineArticlesAdapter.TravlZineArticlesViewHolder>(0,click()))
        //Go to place view
        device.wait(Until.findObject(By.res("article_place_card_view")), DEFAULT_TIMEOUT)
        onView(withId(R.id.article_place_card_view)).perform(click())
        device.wait(Until.findObject(By.res("ic_map")), DEFAULT_TIMEOUT)
        //Open Map
        onView(withId(R.id.app_bar_fab)).perform(click())
    }
}