package com.example.fbtesting.model

import android.util.Log
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.model.local.MenuDAO
import com.example.fbtesting.model.remote.FirebaseDataLoader
import com.example.fbtesting.models.Dish
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

val TAG = "TAG"

class DataRepository @Inject constructor(
//    private val firebaseDataLoader: FirebaseDataLoader =
//        FirebaseDataLoader().getLoader(),
//    private val appDatabase: MenuDatabase? = MenuDatabase.getDatabase()
//val appDatabase: MenuDatabase
val menuDAO: MenuDAO,
val firebaseDataLoader: FirebaseDataLoader

)

//        RoomDataInjector.injectDb())
{



//    fun getRepository() = DataRepository()



    fun getAuth():FirebaseAuth{
        Log.d(TAG, "DataRepository, getAuth, auth: ${firebaseDataLoader.getFirebaseAuth()}")
        return firebaseDataLoader.getFirebaseAuth()
    }
//   suspend fun getOptions(): FirebaseRecyclerOptions<Dish> {
//       //todo: convert to string and change db entity class to string or something similar and use different recyclerViewAdapters
//        val temp =  firebaseDataLoader.getOptions()
//        appDatabase?.menuDao()?.insertMenuData(MyObjectForRoom(temp.snapshots.toList().map {
//            it as Dish
//        }))
//       Log.d(TAG, "DataRepository, getOptions, insertDataToDb, temp: ${temp.snapshots.toList()}")
//        return temp
//    }

    suspend fun getOptions():List<Dish>{



        val firebaseData = firebaseDataLoader.getOptions()
        val databaseData = menuDAO.getMenuData()


        if (databaseData.isNotEmpty()){
            Log.d(TAG, "getOptions, have data")
            return databaseData
        }else{
            for (i in firebaseData){
                menuDAO.insertMenuData(i)
            }
            Log.d(TAG, "DataRepository, getOptions, data inserted")
        }



//        val databaseData = appDatabase.menuDao().getMenuData()

//        var temp: List<Dish>? = null
//        try {
//             temp = firebaseDataLoader.getOptions()
//            Log.d(TAG, "DataRepository, getOptions, temp is: $temp")
//
//
//        }catch (e: Exception){
//            Log.d(TAG, "DataRepository, exception: $e")
//        }
//        Log.d(TAG, "DataRepository, getOptions, database is: ${databaseData.data}")


//        if (databaseData.data == null){
//            appDatabase.menuDao().insertMenuData(MyObjectForRoom(temp.toString()))
//            Log.d(TAG, "DataRepository, getOptions, temp: $temp")
//
//        }
//        var temp1: MyObjectForRoom? = null
//        try {
//             temp1 = appDatabase?.menuDao()?.getMenuData()
//            Log.d(TAG, "DataRepository, getOptions, temp: $temp1")
//
//        }catch (e: Exception){
//            Log.d(TAG, "DataRepository, getOptions, temp exception: $e")
//
//
//        }



        Log.d(TAG, "DataRepository, getOptions, temp before returning: $firebaseData")


        return firebaseData
    }

//    private suspend fun checkDatabaseData():Boolean{
//        val data = menuDAO.getMenuData()
//        if (data == null){
//            Log.d(TAG, "DataRepository, checkDatabaseData(), data is null")
//            return false
//        }else{
//            Log.d(TAG, "DataRepository, checkDatabaseData(), data is: $data")
//            return true
//        }
//
//    }

//    suspend fun getLocalData(): List<Dish>? {
//        Log.d(TAG, "DataRepository, getLocalData")
//
//        return MyConvertor().stringToList(appDatabase?.menuDao()?.getMenuData()?.data!!)
//
//    }
    suspend fun getLastIndex():Int{
        val temp = firebaseDataLoader.getIndex()
        Log.d(TAG, "DataRepository, getLastIndex, index: $temp")

        return temp

    }


    fun sendOrder(index: String, order: Order){
        Log.d(TAG, "DataRepository, sendOrder, index: $index, order: $order")

        firebaseDataLoader.sendOrder(index, order)
    }
}