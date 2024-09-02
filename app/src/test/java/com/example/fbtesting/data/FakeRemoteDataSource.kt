package com.example.fbtesting.data

import com.example.fbtesting.data.remote.IRemoteDataSource
import com.example.fbtesting.data_models.Order
import com.example.fbtesting.models.Dish
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

const val email = "ivan.zolo@gmail.com"
const val pass = "nekoglay"

class FakeRemoteDataSource:IRemoteDataSource {

    val sentOrders = mutableMapOf<String, Dish>()
    private val listOfDishesFake = mutableListOf<Dish>(
        Dish("1", "title1", "500", "url1"),
        Dish("1", "title2", "500", "url2"),
        Dish("1", "title3", "500", "url3")
    )


    @Mock
    private lateinit var mockFirebaseAuth: FirebaseAuthInterface

    override fun getFirebaseAuth(): FirebaseAuth? {
        val authResult = mock(AuthResult::class.java)
        `when`(mockFirebaseAuth.signInWithEmailAndPassword(email, pass)).thenReturn(Tasks.forResult(authResult))

        return authResult as FirebaseAuth
    }


    override suspend fun getMenuData(): List<Dish> {
        return listOfDishesFake
    }

    override fun sendOrder(index: String, order: Order) {
        sendOrder(index, order)
    }

    override suspend fun getIndex(): Int {
        return 15
    }
}