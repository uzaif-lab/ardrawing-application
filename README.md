# 🎨 AR Drawing Studio - Augmented Reality Creative Application

## 🚀 Introduction

**AR Drawing Studio** is a next-generation **Augmented Reality (AR) art application** designed to elevate creativity into the real world. Using the power of your phone’s camera, **AR Drawing Studio lets you draw, trace, and sketch directly on your environment using advanced AR technology.**

Whether you're an aspiring artist, student, designer, or hobbyist — this app is your canvas beyond the screen. With support for gallery images, internet imports, and a library of built-in templates, **AR Drawing Studio transforms your surroundings into an interactive art space.**

---

## ✨ Key Features

### 🔍 Augmented Reality Camera View
- Real-time drawing overlay using **ARCore (Google’s Augmented Reality SDK)**
- Fully interactive and responsive to **device movement, light, and depth perception**
- Superimpose drawings onto physical objects for **immersive sketching**

### 🖼️ Import Image Capabilities
- 📁 **Import from Gallery:** Select images from your device storage
- 🌐 **Import from Internet:** Paste any image URL — no download required
- 🖌️ Use imported images as **live trace overlays** for perfect sketches

### 📚 Built-in Drawing Templates
- Over **100 ready-made templates**, including:
  - Animals, architecture, faces, fantasy, tech, vehicles, characters
- Templates categorized by difficulty and style
- Perfect for **learning proportions, practicing strokes, and guided sketching**

### ✏️ Drawing & Tracing Tools
- Adjustable **brush size, opacity, and color**
- **Undo/Redo**, eraser, and line precision tools
- Toggle between **free drawing** and **template tracing** modes

### 📸 Capture Your Creations
- One-tap **screenshot capture** or **video record** with AR layer
- Save creations to device gallery or **share instantly to social media**

---

## 🧠 Technical Overview

| Component | Description |
|----------|-------------|
| `Kotlin` | Primary programming language |
| `ARCore` | AR platform used for motion tracking and environmental understanding |
| `Sceneform` | Handles rendering and manipulation of 3D and 2D assets |
| `CameraX` | High-performance, lifecycle-aware camera API |
| `Glide` | Image loader for gallery and internet imports |
| `Coroutines` | Handles asynchronous loading and background processes |
| `MVVM Architecture` | Clean separation of logic using ViewModel and LiveData |
| `Room DB` | Stores saved sketches and template history locally |
| `Firebase (Optional)` | For remote template sync and analytics (if enabled) |

---

## 🔧 Permissions Required

> For full functionality, the app requests the following permissions:

| Permission | Purpose |
|------------|---------|
| `CAMERA` | Access real-time camera feed for AR environment |
| `READ_EXTERNAL_STORAGE` | Import images from gallery |
| `INTERNET` | Load templates and images from the web |
| `WRITE_EXTERNAL_STORAGE` | Save screenshots and drawings locally |
| `RECORD_AUDIO` *(optional)* | Enable voice-controlled drawing (coming soon!) |

All permissions are requested **transparently with clear justification** and **never used to track or store personal data**.

---

## 📱 Device Compatibility

- Minimum Android Version: **Android 8.0 (Oreo)**  
- Required: **ARCore-supported device**  
- Recommended: Devices with **Snapdragon 845+** or equivalent for best AR performance  
- RAM: Minimum **3 GB**

---

## 💡 Use Cases

- 🎨 Artists practicing sketching and proportions
- 🏫 Students learning to draw via live guides
- 🧠 Creative thinkers using spatial sketching to prototype ideas
- 👨‍🏫 Teachers showing visual drawing steps in physical classrooms
- 🤹 Hobbyists tracing complex designs on walls, canvases, or paper

---

## 📦 Folder Structure

📦 ar-drawing-studio/
┣ 📂 app
┃ ┣ 📂 src
┃ ┃ ┣ 📂 main
┃ ┃ ┃ ┣ 📂 java
┃ ┃ ┃ ┃ ┣ 📂 com.yourapp.ardraw
┃ ┃ ┃ ┃ ┃ ┣ 📄 MainActivity.kt
┃ ┃ ┃ ┃ ┃ ┣ 📄 ARDrawingFragment.kt
┃ ┃ ┃ ┃ ┃ ┣ 📄 ImageImportManager.kt
┃ ┃ ┃ ┃ ┃ ┣ 📄 TemplateManager.kt
┃ ┃ ┃ ┃ ┃ ┣ 📄 DrawingCanvas.kt
┃ ┃ ┃ ┣ 📂 res
┃ ┃ ┃ ┃ ┣ 📄 activity_main.xml
┃ ┃ ┃ ┃ ┣ 📄 fragment_ar.xml
┃ ┃ ┃ ┣ 📄 AndroidManifest.xml

yaml
Copy
Edit

---

## 🧪 How It Works

1. **AR Initialization:** Camera + ARCore create a digital plane of your real environment
2. **Template Overlay:** User selects an image or template that is anchored to this plane
3. **Drawing Layer:** A canvas is rendered above the physical environment
4. **User Drawing:** User draws using finger gestures or stylus
5. **Output Export:** Final sketch is captured with the AR background or saved as a transparent overlay

---

## 🌐 Coming Soon

- ✨ **AI-assisted drawing suggestions**
- ✨ **Voice-to-draw system**: Say “draw a circle” to auto-create shapes
- ✨ **3D model import**: Sketch over low-poly AR models
- ✨ **Multi-user mode**: Collaborative AR sketching
- ✨ **Online template store**: User-submitted designs and community templates

---

## 🔐 Privacy & Data Handling

We **do not collect or store any personal data**. All activity remains **local to the device** unless the user explicitly shares their creations. The app works **fully offline** after initial install, except for online image loading (which is optional).

---

## 📸 Screenshots

| Camera View | Template Overlay | Final Sketch |
|-------------|------------------|---------------|
| ![](screenshots/camera_view.jpg) | ![](screenshots/template_overlay.jpg) | ![](screenshots/final_sketch.jpg) |

---

## 📥 Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/ar-drawing-studio.git
Open in Android Studio

Sync Gradle and run on a physical device (emulators won't support ARCore properly)

Grant camera & storage permissions

Start sketching in the real world 🎉
or you can just install the apk file and start using it.


👨‍💻 [mohd uzaif khan] - Kotlin/Android Developer, UI/UX & Template Art, ARCore Integration


📄 License
swift
Copy
Edit
MIT License
Permission is hereby granted, free of charge, to any person obtaining a copy...
💬 Feedback & Support
We’d love to hear from you! For feature requests, bug reports, or custom integrations:

📧 Email: uzaifkhan7867@gmail.com
📘 privacy policy:
