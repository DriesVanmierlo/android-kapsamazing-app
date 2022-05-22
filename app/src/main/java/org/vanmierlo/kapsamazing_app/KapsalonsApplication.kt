package org.vanmierlo.kapsamazing_app

import android.app.Application
import org.vanmierlo.kapsamazing_app.data.KapsalonDatabase
import org.vanmierlo.kapsamazing_app.data.MyRepository

class KapsalonsApplication: Application() {
    val database by lazy { KapsalonDatabase.getDatabase(this) }
    val repository by lazy { MyRepository(database.kapsalonDao()) }
}