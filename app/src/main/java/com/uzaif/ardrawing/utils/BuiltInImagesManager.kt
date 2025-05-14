package com.uzaif.ardrawing.utils

import com.uzaif.ardrawing.R
import com.uzaif.ardrawing.data.BuiltInImage

/**
 * Utility class to manage built-in images
 */
object BuiltInImagesManager {
    /**
     * Register built-in images in the viewmodel
     * This needs to be called when the app starts
     */
    fun registerBuiltInImages(onRegister: (List<BuiltInImage>) -> Unit) {
        val builtInImages = mutableListOf<BuiltInImage>()
        
        // Add all images from drawable folder
        builtInImages.add(BuiltInImage(R.drawable.a1, "Free Image 1"))
        builtInImages.add(BuiltInImage(R.drawable.a2, "Free Image 2"))
        builtInImages.add(BuiltInImage(R.drawable.a3, "Free Image 3"))
        builtInImages.add(BuiltInImage(R.drawable.a4, "Free Image 4"))
        builtInImages.add(BuiltInImage(R.drawable.a5, "Free Image 5"))
        builtInImages.add(BuiltInImage(R.drawable.a6, "Free Image 6"))
        builtInImages.add(BuiltInImage(R.drawable.a7, "Free Image 7"))
        builtInImages.add(BuiltInImage(R.drawable.a8, "Free Image 8"))
        builtInImages.add(BuiltInImage(R.drawable.a9, "Free Image 9"))
        builtInImages.add(BuiltInImage(R.drawable.a10, "Free Image 10"))
        builtInImages.add(BuiltInImage(R.drawable.a11, "Free Image 11"))
        builtInImages.add(BuiltInImage(R.drawable.a12, "Free Image 12"))
        builtInImages.add(BuiltInImage(R.drawable.a14, "Free Image 13"))
        builtInImages.add(BuiltInImage(R.drawable.a15, "Free Image 15"))
        builtInImages.add(BuiltInImage(R.drawable.a16, "Free Image 16"))
        builtInImages.add(BuiltInImage(R.drawable.a17, "Free Image 17"))
        builtInImages.add(BuiltInImage(R.drawable.a18, "Free Image 18"))
        builtInImages.add(BuiltInImage(R.drawable.a19, "Free Image 19"))
        builtInImages.add(BuiltInImage(R.drawable.a20, "Free Image 20"))
        builtInImages.add(BuiltInImage(R.drawable.a21, "Free Image 21"))
        builtInImages.add(BuiltInImage(R.drawable.a22, "Free Image 22"))
        builtInImages.add(BuiltInImage(R.drawable.a23, "Free Image 23"))
        builtInImages.add(BuiltInImage(R.drawable.a24, "Free Image 24"))
        builtInImages.add(BuiltInImage(R.drawable.a25, "Free Image 25"))
        builtInImages.add(BuiltInImage(R.drawable.a26, "Free Image 26"))
        builtInImages.add(BuiltInImage(R.drawable.a27, "Free Image 27"))
        builtInImages.add(BuiltInImage(R.drawable.a28, "Free Image 28"))
        builtInImages.add(BuiltInImage(R.drawable.a29, "Free Image 29"))
        
        // Register the images with the callback
        onRegister(builtInImages)
    }
} 