package com.uzaif.ardrawing.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.uzaif.ardrawing.data.ImageData
import com.uzaif.ardrawing.data.BuiltInImage
import com.uzaif.ardrawing.R

class ARDrawingViewModel : ViewModel() {
    
    // List of selected images
    private val _selectedImages = mutableStateListOf<ImageData>()
    val selectedImages: List<ImageData> = _selectedImages
    
    // Current image being displayed
    private val _currentImageIndex = mutableStateOf(0)
    val currentImageIndex = _currentImageIndex
    
    // Whether camera is active
    private val _isCameraActive = mutableStateOf(false)
    val isCameraActive = _isCameraActive
    
    // List of built-in images
    private val _builtInImages = mutableStateListOf<BuiltInImage>()
    val builtInImages: List<BuiltInImage> = _builtInImages
    
    // Initialize with built-in images (you'll add actual resource IDs later)
    init {
        // These are placeholder IDs - you need to replace them with your actual drawable resources
        // Once you've placed your images in res/drawable
        // Example: _builtInImages.add(BuiltInImage(R.drawable.image1, "Image 1"))
        
        // You'll replace these lines with your actual images after adding them to res/drawable
        // _builtInImages.add(BuiltInImage(R.drawable.image1, "Image 1"))
        // _builtInImages.add(BuiltInImage(R.drawable.image2, "Image 2"))
        // etc.
    }
    
    // Add a selected image
    fun addImage(uri: Uri) {
        _selectedImages.add(ImageData(uri))
    }
    
    // Add a built-in image to selected images
    fun selectBuiltInImage(builtInImage: BuiltInImage) {
        // For demonstration purposes, add this to selected images
        // You might need to convert drawable to URI or modify your ImageData class
        // For now, we'll just use a placeholder URI and handle it in the UI
        _selectedImages.clear()
        _currentImageIndex.value = 0
        
        // Create a special URI for built-in images using android.resource:// scheme
        val uri = Uri.parse("android.resource://built-in/${builtInImage.resourceId}")
        _selectedImages.add(ImageData(uri))
    }
    
    // Clear all selected images
    fun clearImages() {
        _selectedImages.clear()
        _currentImageIndex.value = 0
    }
    
    // Toggle camera state
    fun setCameraActive(active: Boolean) {
        _isCameraActive.value = active
    }
    
    // Update transparency for current image
    fun updateTransparency(transparency: Float) {
        if (_selectedImages.isNotEmpty() && _currentImageIndex.value < _selectedImages.size) {
            _selectedImages[_currentImageIndex.value].transparency = transparency
        }
    }
    
    // Get current image or null if none
    fun getCurrentImage(): ImageData? {
        return if (_selectedImages.isNotEmpty() && _currentImageIndex.value < _selectedImages.size) {
            _selectedImages[_currentImageIndex.value]
        } else null
    }
    
    // Move to next image
    fun nextImage() {
        if (_selectedImages.isNotEmpty()) {
            _currentImageIndex.value = (_currentImageIndex.value + 1) % _selectedImages.size
        }
    }
    
    // Move to previous image
    fun previousImage() {
        if (_selectedImages.isNotEmpty()) {
            _currentImageIndex.value = if (_currentImageIndex.value > 0) 
                _currentImageIndex.value - 1 
            else 
                _selectedImages.size - 1
        }
    }
    
    // Check if the image is a built-in image
    fun isBuiltInImage(uri: Uri): Boolean {
        return uri.toString().startsWith("android.resource://built-in/")
    }
    
    // Get the resource ID from a built-in image URI
    fun getResourceIdFromUri(uri: Uri): Int {
        if (isBuiltInImage(uri)) {
            return uri.toString().substringAfterLast("/").toInt()
        }
        return 0
    }
    
    // Update built-in images
    fun updateBuiltInImages(images: List<BuiltInImage>) {
        _builtInImages.clear()
        _builtInImages.addAll(images)
    }
} 