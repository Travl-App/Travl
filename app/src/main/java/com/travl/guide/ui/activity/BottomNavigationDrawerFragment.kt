package com.travl.guide.ui.activity

import android.app.Dialog
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
import com.travl.guide.navigator.Screens
import com.travl.guide.ui.App
import kotlinx.android.synthetic.main.fragment_navigation_drawer.*
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class BottomNavigationDrawerFragment: BottomSheetDialogFragment() {

    @Inject lateinit var router: Router

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        App.getInstance().getAppComponent().inject(this)
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.app_bar_collections -> {
                    router.replaceScreen(Screens.PlacesScreen())
                    toPlacesScreen()
                }
                R.id.app_bar_map -> {
                    router.replaceScreen(Screens.MapScreen())
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

    private fun toPlacesScreen() {
        //TODO: Fab behavior
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
        }

        return dialog
    }

    private fun disableNavigationViewScrollbars(navigationView: NavigationView?) {
        val navigationMenuView = navigationView?.getChildAt(0) as NavigationMenuView
        navigationMenuView.isVerticalScrollBarEnabled = false
    }
}