# PopularMoviesStage1

## Project Overview
Popular Movies Stage 1, created as a part of Udacity Android Developer Nanodegree Program.
In this project, I built an app to help users discover the most popular and top rated movies.

Please note : The app uses the Movie Database API. To run the app, you need to request a free API key from themoviedb.org. Create your account here: https://www.themoviedb.org/account/signup
Enter the API key in the file JsonUtils.java where it says "Enter your API key here".

## Your app will
- Present the user with a grid arrangement of movie posters upon launch
- Allow your user to change sort order via preference menu: The sort order is by most popular, highest-rated or favorite
- Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
      original title
      movie poster image thumbnail
      plot synopsis (called overview in the api)
      user rating (called vote_average in the api)
      release date
- In addition to this in movie details view:
      - you’ll allow users to view and play trailers (by opening a youtube link in a web browser via intent)
      - you’ll allow users to read reviews of a selected movie
      - you’ll also allow users to mark a movie as a favorite by tapping a button
      
![image](https://user-images.githubusercontent.com/30228915/68761600-b2607580-0614-11ea-9541-166d3de23d92.png)
![5dcc0e8e710a7117539112](https://user-images.githubusercontent.com/30228915/68771213-dda09000-0627-11ea-97cb-d2574888d84a.gif)



## What Will I Practise?
- to fetch data from the Internet with theMovieDB API
- to use adapter and RecycleView to populate grid layout
- to incorporate libraries to simplify the amount of code you need to write (e.g. Picasso)
- to launch activity via Intent (using Parcelable)
- to use of Android Architecture Components (Room, LiveData, ViewModel and Lifecycle) to create a robust and efficient app
- to create a database using Room to store the information needed to display the favorites collection while offline
- handle screen rotation to save selected menu items on the screen 
