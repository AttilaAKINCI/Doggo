# Doggo
Doggo provides a inspection on dogs according to their breeds and sub-breeds 


App base URL: https://dog.ceo/

## Brief Description
Doggo is a similar repository to [Doggo](https://github.com/AttilaAKINCI/DoggoApp) that wraps UI development part with Jetpack Compose!! 

[APK Link (https://drive.google.com/file/d/1VWNpmRe8YC3-50uTNY_HtpUL2N3SriRd/view?usp=sharing)](https://drive.google.com/file/d/1VWNpmRe8YC3-50uTNY_HtpUL2N3SriRd/view?usp=sharing)

## App Video

       OfflineMode Run               Normal Run               Validation Error

<img src="https://user-images.githubusercontent.com/21987335/147820989-637b0058-f770-45c0-b870-c9f7105f4a46.gif" width="200"/> <img 
src="https://user-images.githubusercontent.com/21987335/147821007-ec61cef4-4181-4f6d-94de-62aa9b152060.gif" width="200"/>  <img 
src="https://user-images.githubusercontent.com/21987335/147821026-9a780384-d1a4-4f85-8ea5-0ef0adc3c6a5.gif" width="200"/>


## 3rd party lib. usages & Tech Specs
* Patterns
    - MVVM design pattern
    - Repository pattern for data management
* JetPack Libs
    - Compose
    - Compose UI testing
* Retrofit
* Kotlin & Coroutines
* Room Database 
* Coil Image loading
* Lottie compose animation Lib.
* Moshi Json handler
* Timber Client logging
* Dependency Injection (HILT) 
* Single Activity multiple Composable approach
* Unit testing samples & HILT integrations for testing
* MockK library for unit testing
* Junit5
* Thruth (assertions)

#### UI Flow
1- App starts with splash screen that plays doggo animation with the help of lottie compose library. After animation end, user navigated to dashboard screen

2- In dashboard screen, user is welcomed by a information and breed/sub breed list. Sub breed list becomes visible when any breed selected (selected breed need to have a subreed)

3- Pressing continue button, a validation starts and checks your selections. if everything is ok, app navigates to Detail Screen.

4- Detail screen loads images for doggos which is selected on Dashboard screen

5- App need to be connected to network so if network is un avaiable, user informs with a dialog

#### ScreenShots
<img src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/1.png" width="200">   <img
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/2-1.png" width="200">   <img
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/3-1.png" width="200">   <img                                                                             
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/4-1.png" width="200">   <img                                                                             
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/5-1.png" width="200">   <img                                                                             
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/6-1.png" width="200"> <img 
src="https://github.com/AttilaAKINCI/Doggo/blob/master/images/7.png" width="200"> 
                                                                                        

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
