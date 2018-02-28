package com.chronos.agenda.extensoes

import java.math.BigDecimal

/**
 * Created by John Vanderson M L on 26/02/2018.
 */
fun Int.intToBigDecimal():BigDecimal{

    return BigDecimal(this)
}

fun Double.doubleToBigDecimal():BigDecimal{
    return BigDecimal(this)
}