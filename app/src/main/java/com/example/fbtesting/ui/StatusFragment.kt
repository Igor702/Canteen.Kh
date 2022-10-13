package com.example.fbtesting.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.fbtesting.MainActivity
import com.example.fbtesting.R
import com.example.fbtesting.TAG
import com.example.fbtesting.data.SharedViewModel
import com.example.fbtesting.data.Order
import com.example.fbtesting.data.convertOrderToString
import com.example.fbtesting.data.toOrder
import com.example.fbtesting.databinding.FragmentStatusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class StatusFragment: Fragment() {

    val CHANNEL_ID = "channel_id"

    private val database = Firebase.database

    private val orderRef = database.getReference("orders")
   private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var auth:FirebaseAuth

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "StatusFragment, onAttach")
          auth = viewModel.auth.value!!

    }



    val args: StatusFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStatusBinding.inflate(inflater, container, false)
        val order = SummaryFragment.order[args.orderKey]

        binding.apply {
            val temp = order?.dishes?.convertOrderToString()
            Log.d(TAG, "StatusFragment, apply, dishes.toList is:$temp")
            tvOrderIs.text = temp
            tvTotalPriceIs.text = order?.totalPrice.toString()
            tvPaymentMethodIs.text = order?.payBy.toString()
        }



        orderRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "child added: ${snapshot.value}")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                val temp =  checkWhoseOrder(getOrder(snapshot), auth)
                Log.d(TAG, "child changed: $temp")
                if (temp){
//                    Toast.makeText(context, "Your order is ready!!!", Toast.LENGTH_LONG).show()
                    showNotification()
                    binding.tvOrderStatusIs.text = "Your order is ready!!!"

                }


            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d(TAG, "child removed: ${snapshot.value}")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "child moved: ${snapshot.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "child error: $error")
            }

        })


        return binding.root
    }

    private fun getOrder(snapshot: DataSnapshot): Order {
        val changedOrderMap = snapshot.value as HashMap<*, *>
        return changedOrderMap.toOrder()
    }

    private fun checkWhoseOrder(order: Order, auth: FirebaseAuth?):Boolean{
        Log.d(TAG, "checkOrder order: ${order.currentUser}, auth.user: ${auth?.currentUser?.email.toString()} ")
        if (order.currentUser.toString() == auth?.currentUser?.email.toString()){
            Log.d(TAG, "checkWhoseOrder: true")
            return true
        }
        Log.d(TAG, "checkWhoseOrder: false")

        return false
    }
    private fun showNotification(){

        createNotificationChannel()
        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(requireActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(requireActivity().applicationContext, CHANNEL_ID)
                //todo:change notification icon when app icon will be changed
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Order")
            .setContentText("Your order is ready!!!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
//            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        with(  NotificationManagerCompat.from(requireContext())){
            notify(1, builder.build())
        }
        //val notificationManagerCompat  = NotificationManagerCompat.from(requireContext()).notify(1110101, builder.build())


    }

    fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel_name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}