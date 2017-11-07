package com.example.dawid.dronemeterclient.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.dawid.dronemeterclient.AppSingleton
import org.jetbrains.anko.db.*


/**
 * Created by dawid on 06.11.17.
 */
class MeasurementDbHelper (ctx: Context = AppSingleton.instance ) : ManagedSQLiteOpenHelper(ctx,
        MeasurementDbHelper.DB_NAME, null, MeasurementDbHelper.DB_VERSION ) {

    companion object {
        val DB_NAME = "MeasurementDb"
        val DB_VERSION = 1
        val instance by lazy { MeasurementDbHelper() }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(MeasurementTable.NAME, true,
                MeasurementTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                MeasurementTable.DATE_TIME to INTEGER + NOT_NULL,
                MeasurementTable.CITY to TEXT + NOT_NULL,
                MeasurementTable.ADDRESS to TEXT + NOT_NULL,
                MeasurementTable.NOISE_LEVEL to INTEGER + NOT_NULL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Implement migrations")
        clearDb(db)
    }

    fun clearDb(db: SQLiteDatabase?) {
        db?.dropTable(MeasurementTable.NAME, true)
        onCreate(db)
    }

}