# Common
* Install git
* Clone the repository
```bash
git clone https://github.com/DavideRutigliano/FerranteMattaRutigliano
```

# End User Installation Instructions
The [client application](DeliveryFolder/software/trackme.apk) is also available in a pre assembled apk for Android devices (tested against Oreo 8.0). This version of the application communicates with the last active deployment of the [server application](https://github.com/DavideRutigliano/FerranteMattaRutigliano/deployments) on Heroku.

Even if it has not been tested against an hardware device (but working on builtin Android Studio emulator), we provided a pre assembled version of the [wearable application](DeliveryFolder/software/trackme-wear.apk) also for this part of the client package.

# Server
* Install postgreSQL

* Create a new database schema called __trackme_db__

* Add user __trackmeadmin__ with password __password__ and grant all privileges on the schema created in step above.

* __(Optional)__ If you prefer to use an existing user, you can change the access information in the project after the import in _src/main/resources/application.properties_

## Build with IDE
* Open as a project: ___ferrantemattarutigliano/software/server/src/pom.xml___ and wait until import is complete

* Setup project sdk: file -> project structure -> project sdk (you need at least project language level 11);

* Add configuration -> application -> main class: ServerApplication.

Now you can run the TrackMe Server from your IDE!

## Build with Maven
### Unix
```bash
cd FerranteMattaRutigliano/software/server
sudo chmod +x mvnw
./mvnw package
java -jar ./target/server/server-0.0.1-SNAPSHOT.jar #runs the server 
```

### Windows
```bash
cd FerranteMattaRutigliano\software\server
mvnw.cmd package
java -jar .\target\server\server-0.0.1-SNAPSHOT.jar #runs the server 
```

# Client
## Build with Android Studio
* Download android studio.

* Install android studio and Android Virtual Device.
  * (**important note:** when you create the android SDK directory, don't use whitespace or '-' characters. This causes problem with the Android framework.

* Open an existing android studio project: ___ferrantemattarutigliano/software/client/build.gradle___ wait until import is complete.

* __(Optional)__ If you prefer to launch the application on your own android device you can do so: you just have to enable USB debug on your smartphone and connect it to your PC with an USB cable.

  * Enabling developer option on your device depends on your OS version.
  
  * On Android Oreo: you should go on Settings->System->Phone Info and tap several times on build number; for earlier versions of Android, refers to andoid offical guide.

  * (**important note:** if you want to host *TrackMe* server on your machine and you choose to run the client on your smartphone, remember to change the server ip to your local ip address. You can change the server ip in *client/app/...../httpConstant/*)

Now you can run the TrackMe client with Android Studio!

***Known Error:***
1) Emulator: ERROR: x86 emulation currently requires hardware acceleration!
***Solution:*** tools->SDK Manager->SDK Tools (tab) Make sure that Intel x86 Emulator Accelerator is installed. If already installed, uninstall and reinstall it.

## Build with Gradle
### Unix
```bash
cd FerranteMattaRutigliano/software/client
sudo chmod +x gradlew
#./gradlew assembleDebug #build only
./gradlew installDebug #install apk on running emulator or connected device
```

### Windows
```bash
cd FerranteMattaRutigliano\software\client
#gradlew.bat assembleDebug #build only
gradlew.bat installDebug #install apk on running emulator or connected device
```

# External Device Emulator
The package *"External Device Emulator"* is a very small wearable application for smartwatches or similar (android) devices, which is able to send data from the smartwatch to the mobile phone (and vice-versa).

In order to build this part of the application you can follow the same instructions for client package build and installation and then run the wearable application. You can run it both on a smartwatch emulator or on a physical one. Before you install the application, you should install [Google Wear OS](https://play.google.com/store/apps/category/ANDROID_WEAR?hl=it) on your mobile phone and pair it via bluethoot with your smartwatch/emulator.

Each time you run the application on the emulator you should allow  through adb request forwarding from the smartwatch to the phone and vice-versa, running the following command:
```
adb -d forward tcp:5601 tcp:5601
```

***NOTE:*** This part of the application has been tested **only** against emulator.
