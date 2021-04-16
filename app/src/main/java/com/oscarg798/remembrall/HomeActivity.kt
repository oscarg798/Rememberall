package com.oscarg798.remembrall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.oscarg798.remembrall.theming.RemembrallTheme

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RemembrallTheme {
                DateChooser(days = GetDaysUseCase().execute())
            }
        }
    }
}