# Doggo - Under Refactoring 
Doggo app provides a inspection on dogs according to their breeds and sub-breeds

[APK Link (https://drive.google.com/file/d/1mupugSa4wd49kUxyL2qm1ZkH9nuTZXAl/view?usp=sharing)](https://drive.google.com/file/d/1mupugSa4wd49kUxyL2qm1ZkH9nuTZXAl/view?usp=sharing)

## How to run
In order to run project in your local be aware below points ->
* Developed by Android Studio Hedgehog | 2023.1.1 RC 3 Build #AI-231.9392.1.2311.11047128, built on Dec 4, 2023
* Checkout master branch
* add *SERVICE_ENDPOINT_BASE_URL=https://dog.ceo/* to your local.properties file.

## App Video

       Normal Run             OfflineMode Run   

<img src="https://github.com/AttilaAKINCI/Doggo/assets/21987335/ebcf9882-1548-4635-979d-d39cc6880a61" width="200"/> <img 
src="https://github.com/AttilaAKINCI/Doggo/assets/21987335/51128919-6960-43f0-b45d-167d5b09c5d3" width="200"/>  

## 3rd party lib. usages & Tech Specs
* Kotlin
* [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
* [Kotlin DSL](https://developer.android.com/build/migrate-to-kotlin-dsl)
* Patterns
    - MVVM
    - Clean Architecture
    - Repository
* [JetPack Compose](https://developer.android.com/jetpack/compose?gclid=Cj0KCQiAjMKqBhCgARIsAPDgWlyVg8bZaasX_bdQfYrAXsuDQ6vD-2SmFcTv34Fb-jLQxgGqPD7UxKgaAso5EALw_wcB&gclsrc=aw.ds)
* [Edge to Edge UI design](https://developer.android.com/jetpack/compose/layouts/insets)
* Shimmer Loading
* Native Splash Screen
* Dark/Light UI Mode 
* [Compose Destinations](https://github.com/raamcosta/compose-destinations) / [Documentation](https://composedestinations.rafaelcosta.xyz/)
* [Room Database](https://developer.android.com/jetpack/androidx/releases/room)
    - Suspend response handling
    - Reactive Flow response handling
* [Ktor Client](https://ktor.io/docs/client-supported-platforms.html)
* [Lottie Animations](https://github.com/airbnb/lottie-android)
* [Coil](https://github.com/coil-kt/coil)
    - Asynch image loading
    - Gif play support
* [Timber Client logging](https://github.com/JakeWharton/timber)
* [Dependency Injection (HILT)](https://developer.android.com/training/dependency-injection/hilt-android)
* [Turbine](https://github.com/cashapp/turbine)
* [MockK](https://mockk.io/)
* Unit testing
* Instrumentation testing
* Junit5

#### UI Flow
1- App starts with custom splash screen that plays doggo animation with the help of lottie compose library. After animation end, user navigated to dashboard screen

2- In dashboard screen, user is welcomed by a information and breed/sub breed list. Sub breed list becomes visible when any breed selected.

3- Pressing floating action navigates user to Detail Screen.

4- Detail screen loads images of doggos which is selected(breed/subBreed) on Dashboard screen

5- App need to be connected to network so if network is un available, user informs with a dialog

6- During offline mode app uses previously fetched data until restore network connection again.

#### ScreenShots
Light Mode:

<img src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/1-light.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/2-light.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/3-light.png" width="110">   <img                                                                             
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/4-light.png" width="110">   <img                                                                             
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/5-light.png" width="110">   <img                                                                             
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/6-light.png" width="110">   <img 
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/7-light.png" width="110"> 

Dark Mode:

<img src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/1-dark.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/2-dark.png" width="110">   <img
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/3-dark.png" width="110">   <img                                                                             
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/4-dark.png" width="110">   <img                                                                             
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/5-dark.png" width="110">   <img                                                                             
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/6-dark.png" width="110">   <img 
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/7-dark.png" width="110"> 

# License

The code is licensed as:

```
Copyright 2021 Attila Akıncı

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
