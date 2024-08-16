package com.example.wishlistapp.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="wish-table")
data class Wish(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name="wish-title")
    val title: String = "",
    @ColumnInfo(name="wish-desc")
    val description: String = "") {}

object DummyWish {
    val wishList = listOf(
        Wish(title = "Google Watch 2", description = "An Andriod Watch"),
        Wish(title = "Apple Watch 2", description = "An Apple Watch"),
        Wish(title = "Samsung Watch 2", description = "An Samsung Watch"),
        Wish(title = "Rado Watch 2", description = "An Rado Watch")
    )
}