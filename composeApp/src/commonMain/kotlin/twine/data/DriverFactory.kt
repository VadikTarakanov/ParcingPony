package twine.data

import app.cash.sqldelight.db.SqlDriver
import sqldelight.sqldelight.twin.data.SplitDatabase

val DB_NAME = "splitdatabse.db"

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory) : SplitDatabase {
    return SplitDatabase(driverFactory.createDriver())
}