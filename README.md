# Current location

Version: 1.0.0

# 1. Overview

This plugin uses "[osmdroid](https://github.com/osmdroid/osmdroid)" to display your location in the map data provided by "[Open Street Map](https://www.openstreetmap.org/copyright/)".

You can display a map of any point by dragging the screen.

You can adjust the zoom level by operating the "+" and "-" buttons displayed by tapping the screen.

If the current location is unfollowed by the above screen operation, you can return to the state of following the current location again by pressing the shutter button.
However, the display position does not change in places where radio waves from GNSS (Global Navigation Satellite System) cannot be received, such as indoors. Please be careful.


When used offline, you can use a map of the range and zoom level that you have seen while online in the past.
If the map is not displayed, please use it online.
(It is recommended to use it online at the first startup)


# 2. Terms of Service

> You agree to comply with all applicable export and import laws and regulations applicable to the jurisdiction in which the Software was obtained and in which it is used. Without limiting the foregoing, in connection with use of the Software, you shall not export or re-export the Software  into any U.S. embargoed countries (currently including, but necessarily limited to, Crimea  Region of Ukraine, Cuba, Iran, North Korea, Sudan and Syria) or  to anyone on the U.S. Treasury Departmentﾂ’s list of Specially Designated Nationals or the U.S. Department of Commerce Denied Personﾂ’s List or Entity List. By using the Software, you represent and warrant that you are not located in any such country or on any such list. You also agree that you will not use the Software for any purposes prohibited by any applicable laws, including, without limitation, the development, design, manufacture or production of missiles, nuclear, chemical or biological weapons.

By using the Current location plug-in, you are agreeing to the above and the license terms, [LICENSE.txt](LICENSE.txt).

Copyright &copy; 2022 Ricoh Company, Ltd.

# 3. Development Environment

* RICOH THETA X 
* Firmware version 1.10.1 later


# 4. Install

Android Studio install apk after build automatically. Or use the following command after build.

```
adb install -r app-debug.apk
```

Display "settings" with the following adb command, and set the permissions of the target plug-in from "settings".

```
adb shell am start com.android.settings
```

To see the plug-ins installed on the "plug-in selection screen", turn the camera off-> on or restart the camera using the following command.

```
adb reboot
```


# 5. How to Use


## Other than Japanese model

1. Turn on the THETA.
2. After displaying "plug-in selection screen", tap "Current location" to start this plug-in.


## Japanese model

The Japanese model has the following points to note.

- You cannot check the operation online unless you use THETA X, which is allowed to be developed in CL mode (Client mode).
- If you use your own package name, you cannot start the plug-in from the plug-in start menu when the wireless LAN status is CL mode (Client mode). Display "settings" with the adb command described in the previous chapter, and start the target plug-in from "settings".


# 6. History
* ver.1.0.0 (2022/06/**): Initial version.

---

## Trademark Information

The names of products and services described in this document are trademarks or registered trademarks of each company.

* Android, Nexus, Google Chrome, Google Play, Google Play logo, Google Maps, Google+, Gmail, Google Drive, Google Cloud Print and YouTube are trademarks of Google Inc.
* Apple, Apple logo, Macintosh, Mac, Mac OS, OS X, AppleTalk, Apple TV, App Store, AirPrint, Bonjour, iPhone, iPad, iPad mini, iPad Air, iPod, iPod mini, iPod classic, iPod touch, iWork, Safari, the App Store logo, the AirPrint logo, Retina and iPad Pro are trademarks of Apple Inc., registered in the United States and other countries. The App Store is a service mark of Apple Inc.
* Microsoft, Windows, Windows Vista, Windows Live, Windows Media, Windows Server System, Windows Server, Excel, PowerPoint, Photosynth, SQL Server, Internet Explorer, Azure, Active Directory, OneDrive, Outlook, Wingdings, Hyper-V, Visual Basic, Visual C ++, Surface, SharePoint Server, Microsoft Edge, Active Directory, BitLocker, .NET Framework and Skype are registered trademarks or trademarks of Microsoft Corporation in the United States and other countries. The name of Skype, the trademarks and logos associated with it, and the "S" logo are trademarks of Skype or its affiliates.
* Wi-Fi, Wi-Fi Certified Miracast, Wi-Fi Certified logo, Wi-Fi Direct, Wi-Fi Protected Setup, WPA, WPA 2 and Miracast are trademarks of the Wi-Fi Alliance.
* The official name of Windows is Microsoft Windows Operating System.
* All other trademarks belong to their respective owners.
