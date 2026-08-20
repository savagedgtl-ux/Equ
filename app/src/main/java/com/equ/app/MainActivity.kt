package com.equ.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.equ.app.ui.navigation.EquNavHost
import com.equ.app.ui.theme.EquTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EquTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    EquNavHost()
                }
            }
        }
    }
}
