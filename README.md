# DoggoApp
Doggo App provides a inspection on dog specie images according to their breeds and sub-breeds

## Brief Description
Doggo app consist of 2 different fragments and 1 root activity. Activity holds a container layout in order to manage fragments which will be controlled by navigation component.
According to network connectivity app navigates user with alert dialogs.

[APK Link (https://drive.google.com/file/d/1Zib0crGJA7qMH8adVNXrVWNoKJRDlA2g/view?usp=sharing)](https://drive.google.com/file/d/1Zib0crGJA7qMH8adVNXrVWNoKJRDlA2g/view?usp=sharing)

## App Video

         Normal Run              Network connectivity

<img src="https://user-images.githubusercontent.com/21987335/147372515-932062e8-4039-4efb-b4c6-89463c4fa58f.gif" width="200"/> <img 
src="https://user-images.githubusercontent.com/21987335/147372568-1bd36a51-f24e-4a8d-9bfa-5dd96a8b0c52.gif" width="200"/>  


## 3rd party lib. usages & Tech Specs
* Patterns
    - MVVM design pattern
    - Repository pattern for data management
* JetPack Libs
    - Navigation Component
* Retrofit
* Kotlin & Coroutines
* Room Database 
* Coil Image loading
* Lottie animation Lib.
* Facebook Shimmer
* Fragment Transition Animations
* Moshi Json handler
* Timber Client logging
* Dependency Injection (HILT) 
* DataBinding, ViewBinding, Binding Adapters
* RecyclerView with List Adapter and DiffUtil
* Single Activity multiple Fragments approach
* Unit testing samples & HILT integrations for testing
* MockK library for unit testing
* Junit5
* Thruth (assertions)

#### UI Flow
1- User starts the app with Splash Screen and doggo animation is started.

2- After animation end user navigated to Dashboard Screen automatically

3- Dashboard screen contains welcome section, offline state info sevtion, breed selection list, sub breed selection list and continue button it leads user to Detail Screen

4- If internet connectivity state is changed, offline info section automatically becomes visible or gone

5- When internet connection is lost, data is fetched from ROOM database for offline usage with the data which is fetched until at the moment.

6- Continue button is activated/deactivated according to selected breed and sub breeds

7- Detail screen opens with shimmer (loading animation). When real data is fetched, doggo images are displayed. Each row contains doggo image and random dog name tag.

8- Detail screen can be show network alert dialog if data couldn't be found.

#### ScreenShots
<img src="https://github.com/AttilaAKINCI/DoggoApp/blob/master/images/1.png" width="200">   <img
src="https://github.com/AttilaAKINCI/DoggoApp/blob/master/images/2.png" width="200">   <img
src="https://github.com/AttilaAKINCI/DoggoApp/blob/master/images/3.png" width="200">   <img
src="https://github.com/AttilaAKINCI/DoggoApp/blob/master/images/4.png" width="200">   <img
src="https://github.com/AttilaAKINCI/DoggoApp/blob/master/images/5.png" width="200">   <img
src="https://github.com/AttilaAKINCI/DoggoApp/blob/master/images/6-1.png" width="200">   <img
src="https://github.com/AttilaAKINCI/DoggoApp/blob/master/images/7.png" width="200">   <img
src="https://github.com/AttilaAKINCI/DoggoApp/blob/master/images/8.png" width="200"> 

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

