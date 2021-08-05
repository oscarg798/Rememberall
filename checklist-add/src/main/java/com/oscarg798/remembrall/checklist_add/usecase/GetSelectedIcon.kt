package com.oscarg798.remembrall.checklist_add.usecase

import com.oscarg798.remembrall.ui_common.ui.AwesomeIcon
import javax.inject.Inject
import kotlin.random.Random

class GetSelectedIcon @Inject constructor(private val icons: Set<AwesomeIcon>) {

   operator fun invoke(): AwesomeIcon{
        return icons.toList()[Random.nextInt(icons.size)]
    }
}