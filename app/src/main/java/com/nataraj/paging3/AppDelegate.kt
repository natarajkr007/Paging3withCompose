package com.nataraj.paging3

import android.app.Application
import com.nataraj.paging3.data.InMemoryDatabaseProvider

/**
 * @author nataraj-7085
 * @since 07/10/25
 * */
class AppDelegate : Application() {
    override fun onCreate() {
        super.onCreate()

        InMemoryDatabaseProvider.init(this)
    }
}