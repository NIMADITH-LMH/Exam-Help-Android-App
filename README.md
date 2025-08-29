# Exam Help Android App

An Android application designed to help students prepare for exams by providing courses, quizzes, certificates, and more. This app is built with Kotlin and follows modern Android development practices.

## Features

- **User Authentication:** Sign up and sign in to access personalized content.
- **Course Catalog:** Browse and enroll in various courses.
- **Lessons & Quizzes:** Interactive lessons and quizzes for self-assessment.
- **Certificates:** Earn certificates upon course completion.
- **Profile Management:** Edit your profile, view enrolled courses, and track progress.
- **Notifications:** Stay updated with reminders and announcements.
- **Payment Integration:** Manage course payments and view payment history.
- **Help Center & FAQs:** Access support and frequently asked questions.
- **Search Functionality:** Quickly find courses, lessons, and more.
- **Privacy & Terms:** View privacy policy and terms & conditions within the app.


## Getting Started

### Prerequisites
- Android Studio (latest version recommended)
- Android device or emulator (minSdk 24, targetSdk 36)
- Java 11

### Installation
1. **Clone the repository:**
	```sh
	git clone https://github.com/NIMADITH-LMH/Exam-Help-Android-App.git
	```
2. **Open in Android Studio:**
	- Open Android Studio and select `Open an existing project`.
	- Navigate to the `Exam-Help-Android-App` folder.
3. **Sync Gradle:**
	- Let Android Studio sync and download all dependencies.
4. **Run the app:**
	- Connect your device or start an emulator.
	- Click the Run button or use `Shift+F10`.

### Build Configuration
- **Application ID:** `com.example.myapplication`
- **minSdk:** 24
- **targetSdk:** 36
- **Language:** Kotlin

### Main Dependencies
- AndroidX Core, AppCompat, Material Components
- Navigation Component
- ViewPager2
- Gson
- ConstraintLayout
- CardView
- SwipeRefreshLayout
- JUnit, Espresso (for testing)

## Project Structure

```
Exam-Help-Android-App/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/myapplication/  # Kotlin source files
│   │   │   ├── res/                             # Resources (layouts, drawables, etc.)
│   │   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
└── ...
```

## How to Contribute

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

For questions or support, please open an issue or contact the maintainer.

