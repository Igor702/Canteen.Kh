package com.example.fbtesting.data_models

import com.example.fbtesting.models.Dish
import com.firebase.ui.database.FirebaseRecyclerOptions

data class RoomDataObject(
    val data: FirebaseRecyclerOptions<Dish>? = null,
    val status: Boolean? = null
)