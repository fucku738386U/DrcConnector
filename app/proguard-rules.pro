# ProGuard rules for DrcConnector
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep JSch classes
-keep class com.jcraft.jsch.** { *; }
-dontwarn com.jcraft.jsch.**

# Keep Compose
-keep class androidx.compose.** { *; }
-keep class androidx.compose.material3.** { *; }
-keep class androidx.compose.ui.** { *; }

# Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keep data classes
-keepclassmembers class com.anonymous.drcconnector.** {
    <fields>;
    <init>(...);
}
