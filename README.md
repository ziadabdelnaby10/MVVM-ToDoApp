# :pencil: MVVM-ToDo App

**TODO APP** is a Demo app which demonstrates the use of **MVVM architecture, Dependency Injection, Navigation Component Library, Room Database, Flow, Coroutines, Datastore** and other **Modern Android Development**.
This project is inspired from [Link](https://www.youtube.com/playlist?list=PLrnPJCHvNZuCLOE6tNcoOHSJ5rvhi0t0p) CodingInFlow Playlist

# :wrench: Built With
- [Kotlin](https://kotlinlang.org/) - Programming Language Used in the app.
- [Navigation Component Library](https://developer.android.com/guide/navigation) - For setting up app's navigation.
- [Room Database](https://developer.android.com/training/data-storage/room) - For persisting data/tasks in device's local storage.
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - To execute code asynchronously.
- [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) - In the simplest of ways consider this as a pipe through which the data flows in the app, by using flow
   a developer doesn't need to refresh variables which contain data.  
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Used to observe the data on the UI layer
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - To Store UI-related data that isnt destroyed on UI changes.
- [RecylerView](https://developer.android.com/guide/topics/ui/layout/recyclerview) - To Display data in a list format.
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - For Dependency Injection
- [Datastore](https://developer.android.com/topic/libraries/architecture/datastore) - For Storing Data

### Dependencies

```
    String nav_version = "2.5.3"
    String room_version = "2.4.3"

    implementation 'androidx.core:core-ktx:1.9.0'
    implementation 'androidx.appcompat:appcompat:1.5.1'
    implementation 'com.google.android.material:material:1.7.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.4'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.0'

    // Navigation
    implementation "androidx.navigation:navigation-runtime-ktx:2.5.3"
    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

    // Fragment
    implementation "androidx.fragment:fragment-ktx:1.5.5"

    // LifeCycle
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.5.1"

    // LiveData
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.5.1"

    // Room
    implementation "androidx.room:room-ktx:2.4.3"
    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    //ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1"

    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4'

    //DataStore
    implementation "androidx.datastore:datastore-preferences:1.0.0-alpha02"

    //DaggerHilt
    implementation "com.google.dagger:hilt-android:2.44"
    kapt "com.google.dagger:hilt-compiler:2.44"
```

### Plugins

```
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'androidx.navigation.safeargs.kotlin'
    id 'kotlin-kapt'
    id 'kotlin-parcelize'
    id 'com.google.dagger.hilt.android'
```
# 🏗️ Architecture Design
The app uses [MVVM Architecture](https://developer.android.com/jetpack/docs/guide#recommended-app-arch)
