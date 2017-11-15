package com.example.dawid.dronemeterserver

import android.support.v4.content.ContextCompat
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by dawid on 15.11.17.
 */
class MainUI : AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        relativeLayout {
            button("Record"){
                onClick { toast("Hello") }
            }
        }
    }
}