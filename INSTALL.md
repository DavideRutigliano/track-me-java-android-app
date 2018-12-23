# Common
* Install git
* Clone the repository:
```bash
git clone https://github.com/DavideRutigliano/FerranteMattaRutigliano
```
# Server
* Install mysql

* Create a new database schema called __trackme_db__

* Add user __trackmeadmin__ with password __password__ and grant all privileges on the schema created in step above.

* __(Optional)__ If you prefer to use an existing user, you can change the access information in the project after the import in _src/main/resources/application.properties_

## Build with IDE
* Open as a project: ___ferrantemattarutigliano/software/server/src/pom.xml___ and wait until import is complete

* Setup project sdk: file -> project structure -> project sdk (you need at least project language level 11)

* Add configuration -> application -> main class: ServerApplication

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

* Select file->project structure. Make sure that Android NDK is installed.

* Run the application and create a new virtual device (you can choose the default one, Nexus 6). Select an API >= 16.

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
