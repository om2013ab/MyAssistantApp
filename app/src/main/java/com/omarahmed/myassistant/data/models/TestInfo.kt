package com.omarahmed.myassistant.data.models

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
    val code:String,
    val date: Date?,
    val time: Date?,
    val chapters:String,
    val notification:Boolean
):Parcelable