Leica Disto D1 Bluetooth communication with tinyb library.
----------------------------------------------------------


Basic Griffon application with JavaFX as the UI toolkit
and Groovy as the main language for testing bluetooth communication.
<p align="center">
  <img src="https://github.com/normanargiolas/disto-leica-bluetooth/blob/master/disto1.png" height="337"/>
  <img src="https://github.com/normanargiolas/disto-leica-bluetooth/blob/master/app.png" height="337"/>
</p>


    gradle build
    gradle test
    gradle run

If you prefer building with Maven then execute the following commands

    mvn compile
    mvn test
    mvn -Prun


Common ERROR:
-------------
java.lang.UnsatisfiedLinkError: no tinyb in java.library.path   
Add environment variables:  
LD_LIBRARY_PATH=path of tinyb sources saved in libs directory
