adb uninstall study.android
gradle build
adb install build/outputs/apk/StudyAndroid-release.apk
adb shell am start -n study.android/.MainActivity
