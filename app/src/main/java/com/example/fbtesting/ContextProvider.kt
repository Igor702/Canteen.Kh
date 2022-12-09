package com.example.fbtesting

import android.content.Context
import javax.inject.Inject

class ContextProvider @Inject constructor( val context: Context) {
    fun getContextToRepo():Context = context

}