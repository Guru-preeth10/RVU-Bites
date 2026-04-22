# 🍽️ RVU Bites

A food ordering Android application built for **RV University (RVU)** students and staff. RVU Bites brings the campus canteen/cafeteria experience to your fingertips — browse menus, place orders, and more, all from your Android device.

---

## 📱 About

RVU Bites is a native Android app that streamlines the food ordering experience within RV University. Instead of waiting in long canteen queues, users can browse available food items, place orders, and track them through the app.

---

## 🚀 Features

- Browse campus canteen menu items
- Place and manage food orders
- User authentication (via Firebase)
- Real-time order updates powered by Firebase backend
- Clean and intuitive Android UI

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Platform | Android (Native) |
| Language | Java (primary), Kotlin |
| Build System | Gradle (Kotlin DSL) |
| Backend / Database | Firebase (Firestore / Realtime DB) |
| Authentication | Firebase Auth |
| IDE | Android Studio |

---

## 📁 Project Structure

```
RVU-Bites/
├── app/                   # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/      # Java source files (Activities, Adapters, Models)
│   │   │   ├── res/       # Layouts, drawables, strings, etc.
│   │   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/                # Gradle wrapper files
├── build.gradle.kts       # Root build file
├── settings.gradle.kts    # Project settings
└── gradle.properties
```

---

## ⚙️ Getting Started

### Prerequisites

- Android Studio (Hedgehog or later recommended)
- JDK 11+
- A Firebase project with Firestore and Authentication enabled
- Android device or emulator running API 21+

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Guru-preeth10/RVU-Bites.git
   cd RVU-Bites
   ```

2. **Connect Firebase**
   - Go to the [Firebase Console](https://console.firebase.google.com/)
   - Create a new project (or use an existing one)
   - Add an Android app with the package name from `AndroidManifest.xml`
   - Download `google-services.json` and place it in the `app/` directory

3. **Open in Android Studio**
   - Open Android Studio → `File > Open` → select the cloned folder
   - Let Gradle sync complete

4. **Run the app**
   - Connect a physical device or start an emulator
   - Click **Run ▶** or press `Shift + F10`

---

## 🔧 Build

To build a debug APK from the command line:

```bash
./gradlew assembleDebug
```

To run tests:

```bash
./gradlew test
```

---

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## 📄 License

This project is currently unlicensed. Feel free to reach out to the author for usage permissions.

---

## 👤 Author

**Guru Preeth**  
GitHub: [@Guru-preeth10](https://github.com/Guru-preeth10)

---

> Built with ❤️ for RV University, Bengaluru
