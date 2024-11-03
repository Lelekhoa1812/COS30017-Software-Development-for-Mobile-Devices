# Exercise Activity Tracker

## Overview  
The **Exercise Activity Tracker** is an Android app that allows users to log, update, and delete their daily physical activities. The app provides users with an intuitive interface to manage their exercise routines and track their progress. It is built using the MVVM architecture and integrates with Android components such as Room, LiveData, ViewModel, and RecyclerView.

## Features  
- **CRUD Operations:** Users can create, read, update, and delete activities.
- **RecyclerView with Dynamic Data:** Displays all activities with sorting and filtering options.
- **Room Database for Data Persistence:** Ensures that exercise activities are saved between app sessions.
- **Light and Dark Themes:** Users can toggle between light and dark themes.
- **Date-based Filtering:** Allows users to filter activities by date.

## Installation  
To set up and run the app on your local machine:
1. **Clone the repository:**
    ```
    git clone https://github.com/SoftDevMobDev-2024-Classrooms/assignment03-Lelekhoa1812
    ```
2. **Open the project in Android Studio.**
3. **Sync the project** with Gradle to download all dependencies.
4. **Run the app** on an emulator or a physical Android device.

## Architecture  
The app follows the MVVM (Model-View-ViewModel) architecture:
- **Model:** `Exercise.kt` defines the data model for the app, which is stored in a Room database.
- **ViewModel:** `ExerciseViewModel.kt` handles data between the UI and the database.
- **View:** The UI is built using fragments (`YourActivityFragment.kt`, `AddActivityFragment.kt`, and `UpdateActivityFragment.kt`), which communicate with the ViewModel for data operations.

## Testing  
### Functional Testing
- **UI Testing:** Espresso tests validate the existence of UI components and their expected behavior in the "Your Activity", "Add Activity", and "Update Activity" screens.
- **Form Validation:** Tests ensure that the form inputs (activity name, date, and duration) are validated before submission.
- **Logs:** Using logs to enhance debugging accessibility. 

### Non-functional Testing
- **Performance Testing:** Tested responsiveness of RecyclerView with large data loads.

## Theme Support
The app supports both light and dark themes. The user can toggle between themes using the toolbar button.

## Investigation
### Setup
Set up 2 variants with the same dataset (pre-recorded RecyclerView item set) and identical list interactions (CRUD).
Variant 1 is the “ExerciseActivityTracker” app while Variant 2 “ExerciseActivityTrackerVariant” will apply some changes to accommodate manual list operations, include modifying ViewModel, DAO, create Repository and apply minor changes to fragment controllers, some methods are removed from the original app-program since they are not tested (e.g., date filter function is removed, animation is simpler/incomplete, and  adjust the app logics with separated helper methods etc). 

Each test case consist of:
- Launch Your Activity fragment
- Navigate to Add Activity fragment
- Add an exercise and navigate back to Your Activity (each test case having the same input)
- Navigate to Update Activity fragment (exercise id will be passed with SafeArgs8)
- Update an exercise and navigate back to Your Activity (each test case having the same input)
- Delete that exercise (with animation)

Use Android Profiler to monitor and record CPU and memory usage during list operations.

### Conclusion
The investigation compared the performance of two variants of the "Exercise Activity Tracker" app, Variant 1 (using LiveData) and Variant 2 (manual data management), focusing on CPU and memory usage during common operations like adding, updating, and deleting exercises.  

1.	CPU Usage:  
- Variant 1 (LiveData) exhibited more consistent and stable CPU usage, with most operations ranging between 28% and 36%. LiveData's ability to automatically manage data helped reduce fluctuations in performance.
- Variant 2 (Manual Updates) showed more variation in CPU usage, with operations ranging from 19% to 44%. Deletion operations used less CPU due to simpler animations, but other actions like updating exercises had more unpredictable performance.
2.	Memory Usage:  
- Variant 1 had slightly higher memory usage, likely due to LiveData retaining observability features, but it handled memory more predictably.
- Variant 2 showed lower memory usage increases, particularly when adding and updating data, but it experienced more memory spikes in certain tests.
3.	Animation Impact:  
- Deletion animations in Variant 1 caused higher CPU spikes, while Variant 2 used less CPU due to simpler animations (from ExerciseAdapter.kt) during deletion.
4.	Test Case Limitation:  
-- The small number of test cases (4) might limit the accuracy of the comparison, especially for larger datasets or more complex scenarios.

**Conclusion:** LiveData (Variant 1) provided more predictable performance with stable CPU and memory usage, while manual list management (Variant 2) had less consistency but performed better in certain cases like deletion.
