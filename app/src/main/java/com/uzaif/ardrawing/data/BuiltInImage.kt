package com.uzaif.ardrawing.data

import androidx.annotation.DrawableRes

/**
 * Represents a built-in image that comes bundled with the application
 */
data class BuiltInImage(
    @DrawableRes val resourceId: Int,
    val name: String,
    var transparency: Float = 1.0f
) 