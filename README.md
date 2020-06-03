# MoviesApp
## What is this app?
This app is a client for a web service named “Movies” which you can find [here](http://moviesapi.ir/). You can see what this app does in this GIF:

![screenCapture](https://user-images.githubusercontent.com/41632899/83388505-d2665380-a403-11ea-9b2d-dd9a821fdd94.gif)


## Why did I develop this app?
I developed this app with these goals in mind:
1. Learn new technologies and libraries
2. expand my knowledge of libraries and concepts I was already familiar with
3. use what I’d learned over the years to develop an actual app
4. engage in the complete process of developing an app, from design to deployment, so I can understand and experience the challenges better (All the billion things that can AND DO go wrong!)


## Architecture, libraries, etc.
* Java (+ Java 8 functional programming features)
* MVVM architecture
* Lifecycle Library:
  * LiveData, MutableLiveData, MediatorLiveData (Transformations)
  * ViewModel, ViewModelProvider
* Room ORM
* Retrofit Library
* Paging Library
* Glide Library
* Animations


## How this app reacts to different situations and event

### internet disconnections

![disconnection](https://user-images.githubusercontent.com/41632899/83419465-59362300-a43a-11ea-839c-c34ad1024eff.gif)


### poor internet connection

![badConnection](https://user-images.githubusercontent.com/41632899/83419472-5d624080-a43a-11ea-85cd-0f65d305b796.gif)

### download interruptions
If a user presses the back button in the middle of downloading data of a specific genre’s movies, the download is immediately stopped and later resumed once the user clicks on that genre again

![downloadInterruption](https://user-images.githubusercontent.com/41632899/83419478-5f2c0400-a43a-11ea-9737-26114126a278.gif)
