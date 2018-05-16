# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\dpk_docs\and_sdk\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepattributes SourceFile,LineNumberTable

-dontwarn com.squareup.okhttp.**
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** 
-dontwarn okio.**
-dontwarn rx.**

-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8

-assumenosideeffects
 class android.util.Log {
 public static *** d(...);
 public static *** i(...);
 public static *** v(...);
}

-dontusemixedcaseclassnames 
-dontskipnonpubliclibraryclasses 
-verbose
