package com.example.fbtesting

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fbtesting.databinding.FragmentSandboxBinding
import com.example.fbtesting.model.remote.TAG
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SandboxFragment : Fragment() {

//    private lateinit var dataStore: DataStore<Preferences>
val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSandboxBinding.inflate(inflater, container, false)

        binding.btnSave.setOnClickListener {
            val key = binding.edtInsertKey.text.toString()
            val value = binding.edtInsertValue.text.toString()
            Log.d(TAG, "Sandbox, save, key:$key, value: $value")
            lifecycleScope.launch {
                save(
                    key, value
                )
                Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRead.setOnClickListener {
            val key = binding.edtReadKey.text.toString()
            var myValue: String? = "nothing"
            Log.d(TAG, "Sandbox, read, key:$key, value: $myValue")

            lifecycleScope.launch {
                 myValue = read(key)
                Toast.makeText(context, "Read!", Toast.LENGTH_SHORT).show()

            }
            Log.d(TAG, "Sandbox, read, restoredValue: $myValue")

            binding.tvRestoredData.text = myValue
        }


        return binding.root
    }

    private suspend fun save(key: String, value: String){
        val dataStoreKey = stringPreferencesKey(key)

        context?.dataStore?.edit {settings->
            settings[dataStoreKey] = value
        }
    }
    private suspend fun read(key: String):String?{
        val dataStoreKey = stringPreferencesKey(key)

      val preferences = context?.dataStore?.data?.first()
        return preferences?.get(dataStoreKey)
    }
}