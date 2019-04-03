package com.travl.guide.navigator

//Created by Pereved on 02.04.2019.
object CurrentScreen {

    private var selectedScreen = Screen.StartPage

    val currentScreen: Screen
        get() = selectedScreen

    enum class Screen {
        StartPage, TravlzinePage, FavoritePage, MapPage, PostPage
    }

    fun travlzine(): Screen {
        selectedScreen = Screen.TravlzinePage
        return selectedScreen
    }

    fun start(): Screen {
        selectedScreen = Screen.StartPage
        return selectedScreen
    }

    fun favorite(): Screen {
        selectedScreen = Screen.FavoritePage
        return selectedScreen
    }

    fun map(): Screen {
        selectedScreen = Screen.MapPage
        return selectedScreen
    }

    fun post(): Screen {
        selectedScreen = Screen.PostPage
        return selectedScreen
    }
}