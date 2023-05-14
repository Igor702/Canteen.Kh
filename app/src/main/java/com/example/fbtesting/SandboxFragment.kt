package com.example.fbtesting

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fbtesting.databinding.FragmentSandboxBinding

class SandboxFragment : Fragment() {

    var counter = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSandboxBinding.inflate(inflater, container, false)

        binding.buttonSandbox.setOnClickListener {
            Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()
            counter++
            if (counter==10){
                binding.buttonSandbox.isEnabled = false
            }
        }

        return binding.root
    }
}