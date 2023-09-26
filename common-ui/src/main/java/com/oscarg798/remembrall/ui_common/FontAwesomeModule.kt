package com.oscarg798.remembrall.ui_common

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.oscarg798.remembrall.ui_common.ui.AwesomeIcon
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object FontAwesomeModule {

    @Reusable
    @Provides
    fun provideAwesomeIcons(
        @ApplicationContext context: Context,
        gson: Gson
    ): Set<AwesomeIcon> {
        val awesomeIconsInputStream =
            context.resources.openRawResource(R.raw.font_awesome_cheatsheet)
        val awesomeIconsByteArray = ByteArray(awesomeIconsInputStream.available())
        awesomeIconsInputStream.read(awesomeIconsByteArray, 0, awesomeIconsByteArray.size)
        val awesomeIconJson = String(awesomeIconsByteArray)

        return gson.fromJson(awesomeIconJson, AwesomeIconsJsonResponse::class.java).icons.toSet()
    }
}

data class AwesomeIconsJsonResponse(@SerializedName("icons") val icons: List<AwesomeIcon>)
