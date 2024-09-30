package twine.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import sqldelight.sqldelight.twin.data.SplitDatabase

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = SplitDatabase.Schema,
            context = context,
            name = DB_NAME
        )
    }
}