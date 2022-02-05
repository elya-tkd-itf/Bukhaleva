package com.dasonick.bukhaleva.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class CardDataBase(context: Context?) :
    SQLiteOpenHelper(context, "myDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("LOG_TAG", "--- onCreate database ---")
        // создаем таблицу с полями
        db.execSQL(
            "create table hot ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "url text" + ");"
        )
        db.execSQL(
            "create table top ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "url text" + ");"
        )
        db.execSQL(
            "create table latest ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "url text" + ");"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}