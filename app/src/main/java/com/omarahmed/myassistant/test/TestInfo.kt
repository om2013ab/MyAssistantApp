package com.omarahmed.myassistant.test

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "test_table")
@Parcelize
data class TestInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name:String,
    val date: Date?,
    val time: Date?,
    val chapters:String,
    val notification:Boolean,
    val notificationDate:Date?
):Parcelable