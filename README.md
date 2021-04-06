photoWeatherApp

project architecture: clean architecture
project uses: AndroidxCamera, kotlin coroutines, retrofit
the app is responsive for screen orientation changes and does't lose curren state as well
unit tests are written for GetWeatherDataUseCase

screens: SplashScreen, HomeScreen, CameraScreen, HistoryScreen and ShareScreen
   1. splash screen: first screen that redirects to home screen
   2. homeScreen: 
           a. contains search bar to search city by name ad fetch weather data the open camera through camera button. 
           b. contains history button that redirects to history screen
   
   3. cameraScreen:
           a. contains cameraView with capture button and weather data on top, on pressing capture a photo is saved to cache file
           b. when photo captured cameraView and capture disAppear and insted imageView with captured photo, save and cancell buttons appear.
           c. on save clicked the picture is saved in 2 sizes Thumbnal and full sizes containing weather data then redirects to share screen.
           d. on cancel clicked the image, save, cancel disappear and camer, capture show again.
           
   4. HistoryScreen: contain list of thumbnail images for the capured photo on item clicked it redirects to share screen.
   
   5. ShareScreen: 
           a. contains full sized photo with facebook and twitter share
           b. on button clicked the app checks the presence of facebook or twitter on phone according to the clicked button. if is present 
           thh app show various sharing options inside a choser else it redirects to playstory to the app's page to download and share.
           c. on clicking on choser item it opens the specified app to share
