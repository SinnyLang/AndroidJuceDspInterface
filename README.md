# Equalizer library for Android 

A Android Equalizer Library depend on JUCE.
Subproject `:app` is an example of how to use in Exoplayer. Run `gradlew :app:build` to build example.  

## TODO List
 - Enable PCM processer interface of equalizer
 - To add 10 frequencies control interface
 - To add other Audio Effect

## How to use

### Clone project as subproject
1. Clone this project to `<your_path>`.
```shell
git clone https://github.com/SinnyLang/AndroidJuceDspInterface.git
cd AndroidJuceDspInterface
git submodule update --init --recursive
```

2. Export this library. In your project `settings.gradle` add
```groovy
include ':AndroidJuceDspInterface:dsp-lib'
```
then add 
```groovy
implementation project(':dsp-lib')
```
in your `build.gradle`. 

### Use `.aar` file
1. Clone this project and enter this project root path.
2. Run gradle build. 
```shell
git clone https://github.com/SinnyLang/AndroidJuceDspInterface.git
cd AndroidJuceDspInterface
git submodule update --init --recursive
gradlew :mylibrary:build
```
3. The `.aar` files will be generated in `dsp-lib/build/outputs/aar/<xxx>.aar` and copy an AAR file to your project.