# Rent With Intent - Musical Instrument Rental App
Rent With Intent is an Android app that allows users to browse, filter, and rent various musical instruments, diverse in categories and brands. Users can navigate through a list of available instruments, apply filters based on categories and/or brands, and proceed to rent their selected instrument at any time, with the desired period they would like to borrow.
Rent With Intent is an Android app that allows users to browse, filter, and rent various musical instruments, diverse in categories and brands. Users can navigate through a list of available instruments, apply filters based on categories and/or brands, and proceed to rent their selected instrument at any time, with their desired period they would like to borrow.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Features](#features)
- [Key Techniques](#key-techniques)
- [Credit Calculation](#credit-calculation)
- [Image Citation](#image-citation)

## Installation
**Clone the Repository**  
Clone the repository from GitHub to local machine:
git clone https://github.com/your-username/rent-with-intent.git
**Open in Android Studio**
**Build the Project**
Sync Project with Gradle Files.
**Run the App**
Run the app on an emulator or a physical device.

**Clone the Repository**  
Clone the repository from GitHub to the local machine:
```
git clone https://github.com/SoftDevMobDev-2024-Classrooms/assignment02-Lelekhoa1812.git
```

**Open in Android Studio**

**Build the Project**  
Sync Project with Gradle Files.

**Run the App**  
Run the app on an emulator or a physical device.

## Usage

**Browse Instruments**  
When the app is opened, it will display the details of the first instrument from the list, including its name, price, category, brand, rating, and image.

**Next Instrument**  
Use the "Next" button to navigate through the list of instruments. Each click will display the next instrument in the available list.

**Filter Instruments**  
Tap the "Filter" button to open a modal dialog that allows filtering instruments based on their categories and brands. After selecting filters, tap "Submit" to apply the filters and display the filtered results.

**Set Borrow Period**  
Use the "Set Borrow Period" button on the booking screen to open a DatePicker dialog. This dialog allows users to set the end date of their rental period with appropriate validation to ensure selected dates are after the current date.

**Rent an Instrument**  
Once you select an instrument, tap the "Borrow" button to navigate to the booking page. Confirm or cancel the booking on the booking screen. If the booking is confirmed, the updated rating (if changed during booking) will reflect on the main screen.

**Intent**  
The app effectively utilizes Intent to share data between activities, including instrument details and booking status.

**Set Borrow Period**
Use the "Set Borrow Period" button on the booking screen to open a DatePicker dialog. This dialog allows users to set the start and end dates of their rental period using two DatePickers placed side by side. Users must select valid dates (start date must be before the end date, and both must be after the current date). This feature enhances the user experience by allowing precise control over the rental period.

## Features
**Main Screen (MainActivity)**

- **Instrument Display:** The app displays information about instruments, including name, price, category, brand, rating, and an associated image.
- **Next Instrument Navigation:** Users can browse through available instruments using the "Next" button.
- **Borrow Instrument Navigation:** Users can navigate to the booking page using the "Borrow" button.
- **Filter Functionality:** Users can filter instruments by categories and brands using a dialog with selectable chips.
- **RatingBar Display:** Displays the instrument's rating, which is view-only and cannot be changed directly from the Main Activity.
- **Landscape Mode Adaptation:** The main screen is optimized for landscape mode to ensure a smooth user experience when the device orientation changes.

**Booking Screen (Booking Activity)**

- **Booking Confirmation:** Users can view details of the selected instrument on the booking page and confirm or cancel the booking.
- **Set Borrow Period:** Users can set their rental period using a DatePicker dialog, which includes validation checks for selecting a proper timeframe.
- **Rating Adjustment:** Users can adjust the rating of the instrument, which, if changed, will update the rating on the main screen upon confirmation of booking.
- **Landscape Mode Adaptation:** The booking screen adjusts to landscape orientation, providing an uninterrupted user experience.

**Filtering Instruments**

- **Chip Filter Dialog:** Instruments can be filtered by categories and brands using ChipGroup components in a dialog box. Selected filters are applied to display a customized list of instruments.

## Key Techniques

**Dynamic Instrument List Display**  
Instruments are dynamically displayed using the TextView, ImageView, and RatingBar widgets. The app fetches instrument details and updates the UI accordingly.

**Parceling Data Across Activities**  
The app passes the selected instrument to the Booking activity using Parcelable. The instrument details are retrieved and displayed on the booking screen.

**Filtering Using Chips (ChipGroup)**  
The ChipFilterDialog allows users to select categories and brands using ChipGroup. The filtered instruments are displayed after applying the filter.

**DatePicker for Rent Period (DatePicker)**  
The app includes a DatePicker dialog for setting the end date of the rental period, enhancing the booking process and validating user input.

**Managing UI Visibility**  
UI components (buttons and text views) are dynamically shown or hidden depending on the presence of instruments in the filtered list using `View.GONE` and `View.VISIBLE`.

**Landscape Mode Handling**  
Adaptations were made to the main, booking, and DatePicker views to ensure they display properly in landscape mode, improving the overall user experience.

**RatingBar Integration**  
Two RatingBars are used in the app: one in the Main Activity (view-only) and another in the Booking Activity (modifiable). Changes made to the rating in Booking are reflected in the Main Activity upon return, showcasing effective data sharing between activities.

**Parcelable Implementation**  
The Instrument data class implements Parcelable to allow passing objects between activities efficiently.
**DatePicker for Rent Period (DatePicker)**
The app includes a DatePicker dialog for setting the start and end dates of the rental period, enhancing the booking process and validating user input.
**Managing UI Visibility**
UI components (buttons and text views) are dynamically shown or hidden depending on the presence of instruments in the filtered list using View.GONE and View.VISIBLE.
**Landscape Mode Handling**
Adaptations were made to the main, booking, and DatePicker views to ensure they display properly in landscape mode, improving the overall user experience.

## Credit Calculation
This assumed rent rate (in credits per month) equals one-twentieth of the actual prices (in AUD):

- Cordoba C5 Classical Guitar: $629.00 - 31.45 credits
- Steinway Model B Grand Piano in Satin Ebony: $64,995.00 - 3429.75 credits
- Gliga Vasile Violin: $1,575.00 - 78.75 credits
- Yamaha Piaggero NP-15B 61Key Keyboard: $359.00 - 17.95 credits
- Cambridge TR620L Trumpet: $995.00 - 49.75 credits
- Yamaha YTS480 Intermediate Tenor Saxophone: $3,859.00 - 192.95 credits
- Sanchez Soprano Ukulele - Natural Satin: $69.95 - 3.50 credits
- Jet Black Pearl Roadshow Fusion Plus Drum Kit: $999.00 - 49.95 credits
- Artist ST62 Fiesta Red Electric Guitar: $329.00 - 16.45 credits
- Cambridge Double Key Tenor Flute: $495.99 - 24.80 credits
- Yamaha SLB300 Silent Double Bass: $6,474.13 - 33.71 credits
- Yamaha Stage Custom Birch Drum Kit: $1,649.00 - 82.45 credits

## Image Citation

Original images with screenshots from the website can be found in the `web_images` folder, and further references of the image citations are available in the report's "Acknowledgment" section.
