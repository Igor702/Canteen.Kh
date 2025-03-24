package com.example.fbtesting

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                CanteenApp(onFinish = { this.finish() })

            }

        }
    }
}


@Composable
fun CanteenApp(onFinish: () -> Unit) {

    MaterialTheme {
        //TODO: change app theme
        val navController = rememberNavController()
        val context = LocalContext.current


        CanteenNavHost(navController = navController, context = context, onFinish = { onFinish() })

    }
}







