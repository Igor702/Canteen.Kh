package com.example.fbtesting.data

import android.util.Log
import com.example.fbtesting.ORDER_COOKING
import com.example.fbtesting.ORDER_READY
import com.example.fbtesting.data.local.ILocalDataSource
import com.example.fbtesting.data.remote.IRemoteDataSource
import com.example.fbtesting.data_models.Dish
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.data_models.toOrder
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

const val TAG = "TAG"

class DataRepository @Inject constructor(
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource

) : IDataRepository {


    private val orderRef = Firebase.database.getReference("orders")


    //todo: migrate to proper result instead of simple String
    override fun onOrderStatusChangedListener() = callbackFlow<String> {
        //should be located in repo for better testing approach
        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "child added: ${snapshot.value}")
                this@callbackFlow.trySendBlocking(ORDER_COOKING).onFailure {
                    Log.d(TAG, "Repo onChildAdded, error: ${it?.message}")

                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                val isUsersOrder = checkWhoseOrder(getOrder(snapshot), getCurrentUserEmail())
                Log.d(TAG, "child changed: $isUsersOrder")
                if (isUsersOrder) {

                    this@callbackFlow.trySendBlocking(ORDER_READY).onFailure {
                        Log.d(TAG, "Repo onChildChanged, error: ${it?.message}")

                    }
                }


            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d(TAG, "child removed: ${snapshot.value}")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "child moved: ${snapshot.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking("Error").onFailure {
                    Log.d(TAG, "Repo onCanceled, error: ${it?.message}")
                }
                Log.d(TAG, "child error: $error")
            }

        }

        orderRef.addChildEventListener(listener)

        awaitClose {
            Log.d(TAG, "Repo awaitClose, remove listener")
            orderRef.removeEventListener(listener)

        }
    }

    private fun getOrder(snapshot: DataSnapshot): Order {
        val changedOrderMap = snapshot.value as HashMap<*, *>
        return changedOrderMap.toOrder()
    }


    private fun checkWhoseOrder(order: Order, currentUserEmail: String?): Boolean {
        Log.d(
            TAG,
            "checkOrder order: ${order.currentUser}, auth.user: $currentUserEmail "
        )
        if (order.currentUser == currentUserEmail) {
            Log.d(TAG, "checkWhoseOrder: true")
            return true
        }
        Log.d(TAG, "checkWhoseOrder: false")

        return false
    }


    override fun getCurrentUserEmail(): String? {
        val mail = remoteDataSource.getCurrentUserEmail()
        return mail
    }


    override suspend fun getData(): List<Dish> {

        val databaseData = localDataSource.getMenuData()


        if (databaseData.isNotEmpty()) {
            Log.d(TAG, "DataRepository, databaseData,  $databaseData")

            return databaseData
        } else {
            val firebaseData = remoteDataSource.getMenuData()

            for (i in firebaseData) {
                localDataSource.insertMenuData(i)
            }

            Log.d(TAG, "DataRepository, firebaseData,  $firebaseData")

            return firebaseData
        }
    }

    override suspend fun getLastIndex(): Int {
        val index = remoteDataSource.getIndex()
        Log.d(TAG, "DataRepository, getLastIndex, index: $index")

        return index
    }

    override fun sendOrder(index: String, order: Order) {
        Log.d(TAG, "DataRepository, sendOrder, index: $index, order: $order")

        remoteDataSource.sendOrder(index, order)
    }
}