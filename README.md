# NewUplift Android App

NewUplift is an Android application designed to provide users with daily inspiration through quotes. Users can discover quotes based on their mood, save their favorites, manage their personal collection, and manage their profile.

## Features

* **Mood-Based Quote Discovery**: Get random quotes tailored to selected moods (e.g., Happy, Lost, Motivated).
* **Favorite Quotes**: Log in to save favorite quotes fetched from the API.
* **My Quotes**: Create, view, edit, and manage personal, user-created quotes.
* **User Authentication**: Secure registration and login system.
* **Profile Management**: View and edit user profile details.
* **Persistent Storage**: User data (profile, favorites, user quotes) saved locally using SQLite.
* **Modern UI**: Interface built with Material Components and Navigation Component.

## Architecture & Technical Details

The application follows the **MVVM (Model-View-ViewModel)** architecture pattern and utilizes a **Single-Activity** structure (`MainActivity`) with multiple **Fragments** managed by the **AndroidX Navigation Component**.

* **View Layer**: Consists of Activities (`LoginActivity`, `RegisterActivity`, `MainActivity`, etc.) and Fragments (`HomeFragment`, `SettingsFragment`, `ProfileFragment`, etc.) responsible for displaying the UI (defined in XML layouts) and capturing user interactions.
* **ViewModel Layer**: ViewModels (`HomeViewModel`, `QuotesViewModel`, `UserViewModel`, etc.) handle the presentation logic, interact with the data layer, and expose UI state via `LiveData`. ViewModels are often scoped to the navigation graph (`navGraphViewModels`) to share state between related fragments. Asynchronous operations are managed using **Kotlin Coroutines** (`viewModelScope`).
* **Model Layer (Data)**:
    * **Remote**: Uses **Retrofit** and **OkHttp** to define and execute network calls (`QuotableApi`, `RetrofitClient`) for fetching external quotes.
    * **Local**: Employs an **SQLite** database managed by `DatabaseHelper` (schema definition) and accessed via a Data Access Object (`QuoteDao`) which provides methods for all CRUD operations on user and quote data.
* **Navigation**: Defined in `res/navigation/my_nav.xml` and orchestrated by the Navigation Component. **Safe Args** is used for type-safe argument passing between fragments. The `BottomNavigationView` in `MainActivity` is connected to the NavController for primary navigation.
* **Dependency Management**: Handled by **Gradle**, with dependencies specified in `app/build.gradle.kts`.
* **Utilities**: Helper classes like `AuthManager` (manages user login state, likely using SharedPreferences), `QuotesAdapter` (for `RecyclerView` lists), and potentially `ThemeUtils` contribute to core functionality.

## Core Functionality Breakdown

### 1. User Authentication & Profile

* **Registration (`RegisterActivity`)**: Collects user details (name, username, password, etc.) via input fields. Uses `UserViewModel`/`QuoteDao` to insert the new user into the `users` table in the SQLite database.
* **Login (`LoginActivity`)**: Takes username/password. Uses `UserViewModel`/`QuoteDao` to verify credentials against the `users` table. Upon success, `AuthManager` likely stores the user ID to manage the session state.
* **Session Management (`AuthManager`)**: Persists the logged-in user's ID (likely via SharedPreferences) so the app remembers the user across sessions.
* **Profile Viewing (`ProfileFragment`)**: Fetches the current logged-in user's data from the database (via `UserViewModel` -> `QuoteDao`) based on the stored user ID and displays it.
* **Profile Editing (`EditProfileFragment`)**: Allows modification of user details. Loads current data, accepts changes via input fields, and updates the `users` table via `UserViewModel` -> `QuoteDao`.
* **Logout (`SettingsFragment`, `LogoutActivity`)**: Clears the session data in `AuthManager` and typically navigates the user back to the login screen. May show a confirmation dialog (`custom_logout_dialog.xml`).

### 2. Quote Discovery (Home Screen)

* **Display (`HomeFragment`)**: Shows the current date/day, a quote area, author, mood selection spinner, generate button, and favorite button.
* **Fetching (`HomeViewModel`, `RetrofitClient`, `QuotableApi`)**:
    * `HomeViewModel` fetches an initial quote or a new quote when the "Generate Quote" button is pressed.
    * It uses the selected mood from the `Spinner` (mapped to an API tag via `pickMood`) to call the `getRandomQuote` endpoint via Retrofit.
    * Network calls run asynchronously using Coroutines.
    * `LiveData` (`currentQuote`, `errorMessage`, `isLoading`) holds the state and observed by `HomeFragment` to update the UI.
* **Mood Selection (`HomeFragment`)**: A `Spinner` allows users to choose a mood, which determines the tag used for the API request.

### 3. Quote Management

* **Favoriting (`HomeFragment`, `QuoteDao`)**:
    * The heart icon (`btn_heart`) acts as a toggle.
    * Requires login (`AuthManager.currentUserId`).
    * Checks if the quote exists in the `favorite_quotes` table for the user via `QuoteDao`.
    * If exists, toggles the `is_favorite` flag using `QuoteDao.updateFavorite`.
    * If not exists, inserts the quote using `QuoteDao.insertQuote` with `is_favorite = 1`.
    * The heart icon's state is updated visually based on the database status (`updateHeartButtonState` checking `QuoteDao.isQuoteFavorite`).
* **Viewing Favorites (`FavoriteQuotesFragment`, `QuotesViewModel`, `QuoteDao`)**:
    * Retrieves all quotes marked as favorite (`is_favorite = 1`) for the current user from the database via `QuotesViewModel` -> `QuoteDao.getFavoriteQuotes`.
    * Displays the list using a `RecyclerView` and `QuotesAdapter`.
* **Viewing "My Quotes" (`MyQuotesFragment`, `QuotesViewModel`, `QuoteDao`)**:
    * Retrieves quotes created by the user (`is_user_made = 1`) via `QuotesViewModel` -> `QuoteDao.getUserMadeQuotes`.
    * Displays them in a `RecyclerView`, likely with options to add new ones or view details.
* **Creating Quotes (`MakeQuotesFragment`, `QuotesViewModel`, `QuoteDao`)**:
    * Provides fields to input quote content, author, etc.
    * Saves the new quote to the database via `QuotesViewModel` -> `QuoteDao.insertQuote` with `is_user_made = 1`.
* **Viewing Details (`QuoteDetailsFragment`)**:
    * Receives a `Quote` object (via Safe Args) when a user taps a quote in `MyQuotesFragment` or `FavoriteQuotesFragment`.
    * Displays the full details of the quote. Provides navigation to edit.
* **Editing Quotes (`EditQuoteFragment`, `QuotesViewModel`, `QuoteDao`)**:
    * Receives a `Quote` object (via Safe Args).
    * Loads data into input fields.
    * Saves changes to the database via `QuotesViewModel` -> `QuoteDao.updateQuote`.
* **Deleting Quotes (Likely in `QuoteDetailsFragment` or `EditQuoteFragment`)**:
    * A delete action would trigger `QuotesViewModel` -> `QuoteDao.deleteQuote`.

### 4. Settings & Navigation

* **Main Navigation (`MainActivity`, `BottomNavigationView`)**: Provides access to Home, Favorites, My Quotes, and Settings via the bottom bar, using the Navigation Component for fragment transitions.
* **Settings Screen (`SettingsFragment`)**: Acts as a hub, providing navigation links (using `NavController`) to Profile, Favorites, My Quotes, Developer Info, and Logout.
* **Developer Info (`DeveloperFragment`)**: Displays static information about the app's creators.

## Technology Stack

* **Language**: Kotlin
* **Architecture**: MVVM (Model-View-ViewModel)
* **UI**: Android XML Layouts, Material Components
* **Navigation**: AndroidX Navigation Component (with Safe Args)
* **Asynchronous Programming**: Kotlin Coroutines
* **Networking**: Retrofit & OkHttp
* **Database**: SQLite (via `SQLiteOpenHelper` and custom DAO)
* **Lifecycle Management**: AndroidX Lifecycle (ViewModel, LiveData)
* **Build System**: Gradle
* **Other Libraries**: Lottie (Animations), RecyclerView, SwipeRefreshLayout

## Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd NewUplift
    ```
2.  **Open in Android Studio:**
    * Launch Android Studio.
    * Select "Open an existing Android Studio project".
    * Navigate to the cloned `NewUplift` directory and select it.
3.  **Build the project:**
    * Allow Gradle to sync and download dependencies.
    * Build the project (`Build > Make Project`).
4.  **Run the application:**
    * Select a target device (emulator or physical device).
    * Run the app (`Run > Run 'app'`).

## Usage

* Launch the app to see the Home screen with a daily quote.
* Use the dropdown to select a mood and tap "Generate Quote" for a new one.
* Log in/Register via Settings to enable personal features.
* Tap the heart icon on the Home screen to favorite a quote.
* Use the bottom navigation bar to explore Home, Favorites, My Quotes, and Settings.
* Manage your profile and view/edit/create quotes in their respective sections.

## API

The app requires the `INTERNET` permission to fetch random quotes from an external API, implemented using the `QuotableApi` interface and `RetrofitClient`.
