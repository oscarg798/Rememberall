package com.oscarg798.remembrall.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oscarg798.remembrall.common.ui.theming.Dimensions

@Composable
internal fun RemembrallPage(
    pageConfigurator: PageConfigurator,
    content: @Composable () -> Unit
) {
   Column(Modifier.fillMaxSize()) {
       FakeTopBar(pageConfigurator)
       Box(
           Modifier
               .fillMaxSize()
               .background(
                   color = pageConfigurator.pageBackgroundColor,
                   shape = RoundedCornerShape(
                       topStart = Dimensions.CornerRadius.ExtraLarge,
                       topEnd = Dimensions.CornerRadius.ExtraLarge
                   )
               )
       ) {
           content()
       }
   }
}
