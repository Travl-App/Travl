package com.travl.guide.ui.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.design.internal.NavigationMenuView
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.NavigationView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.travl.guide.R
import com.travl.guide.ui.App
import kotlinx.android.synthetic.main.fragment_navigation_drawer.*

class BottomNavigationDrawerBehavior: BottomSheetDialogFragment() {

    private var bottomNavigationDrawerListener: BottomNavigationDrawerListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_navigation_drawer, container, false)
        App.getInstance().appComponent.inject(this)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navigation_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.app_bar_start_page -> {
                    bottomNavigationDrawerListener!!.navToStartPageScreen()
                }
                R.id.app_bar_collections -> {
                    bottomNavigationDrawerListener!!.navToPlaceScreen()
                }
                R.id.app_bar_map -> {
                    bottomNavigationDrawerListener!!.navToMapScreen()
                }
            }
            this.dismiss()
            true
        }

        app_bar_close_icon.setOnClickListener {
            this.dismiss()
        }

        disableNavigationViewScrollbars(navigation_view)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        bottomNavigationDrawerListener = context as BottomNavigationDrawerListener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { dialog ->
            val dial = dialog as BottomSheetDialog

            val bottomSheet = dial.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
            bottomSheetBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (slideOffset > 0.5) {
                        app_bar_close_icon.visibility = View.VISIBLE
                    } else {
                        app_bar_close_icon.visibility = View.GONE
                    }
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> dismiss()
                    }
                }
            })
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }

        return dialog
    }

    private fun disableNavigationViewScrollbars(navigationView: NavigationView?) {
        val navigationMenuView = navigationView?.getChildAt(0) as NavigationMenuView
        navigationMenuView.isVerticalScrollBarEnabled = false
    }
}