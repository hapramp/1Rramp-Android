# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Ankit\AndroidSdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn javax.annotation.**
-dontwarn org.codehaus.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclasseswithmembers interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn org.apache.pdfbox.pdmodel.graphics.shading.*
-dontwarn org.apache.pdfbox.printing.*
-dontwarn org.apache.pdfbox.rendering.*
-dontwarn com.google.android.gms.vision.barcode.*
-dontwarn com.google.android.gms.vision.face.internal.client.*
-dontwarn com.openhtmltopdf.css.parser.*
-dontwarn com.google.android.gms.internal.*
-dontwarn com.openhtmltopdf.css.value.*
-dontwarn com.openhtmltopdf.simple.*
-dontwarn com.openhtmltopdf.simple.extend.form.*
-dontwarn com.openhtmltopdf.css.parser.property.*
-dontwarn com.openhtmltopdf.render.*
-dontwarn com.openhtmltopdf.extend.*
-dontwarn com.openhtmltopdf.simple.extend.form.*
-dontwarn com.openhtmltopdf.layout.*
-dontwarn com.openhtmltopdf.pdfboxout.*
-dontwarn com.openhtmltopdf.pdfboxout.quads.*
-dontnote junit.framework.*
-dontwarn com.openhtmltopdf.newtable.*
-dontwarn com.openhtmltopdf.simple.extend.*
-dontwarn com.openhtmltopdf.swing.*
-dontwarn com.openhtmltopdf.test.*
-dontwarn com.openhtmltopdf.util.*
-dontwarn com.vladsch.flexmark.util.*
-dontwarn de.rototor.pdfbox.graphics2d.*
-dontwarn okhttp3.internal.platform.*
-dontwarn org.apache.commons.logging.impl.*
-dontwarn org.apache.fontbox.*
-dontwarn org.apache.fontbox.cff.*
-dontwarn org.apache.fontbox.ttf.*
-dontwarn org.apache.fontbox.type1.*
-dontwarn org.apache.pdfbox.contentstream.*
-dontwarn org.apache.pdfbox.contentstream.operator.graphics.*
-dontwarn org.apache.pdfbox.contentstream.operator.graphics.*
-dontwarn org.apache.pdfbox.filter.*
-dontwarn org.apache.pdfbox.multipdf.*
-dontwarn org.apache.pdfbox.pdmodel.*
-dontwarn org.apache.pdfbox.pdmodel.encryption.*
-dontwarn org.apache.pdfbox.pdmodel.fdf.*
-dontwarn org.apache.pdfbox.pdmodel.font.*
-dontwarn org.apache.pdfbox.pdmodel.graphics.blend.*
-dontwarn org.apache.pdfbox.pdmodel.graphics.color.*
-dontwarn org.apache.pdfbox.pdmodel.graphics.form.*
-dontwarn org.apache.pdfbox.pdmodel.graphics.image.*
-dontwarn org.apache.pdfbox.pdmodel.graphics.pattern.*
-dontwarn org.apache.pdfbox.pdmodel.graphics.state.*
-dontwarn org.apache.pdfbox.pdmodel.interactive.digitalsignature.visible.*
-dontwarn org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.*
-dontwarn org.apache.pdfbox.pdmodel.interactive.form.*
-dontwarn android.test.suitebuilder.*
-dontwarn android.test.*
-dontwarn com.openhtmltopdf.context.*
-dontwarn com.openhtmltopdf.css.constants.*
-dontwarn com.openhtmltopdf.css.newmatch.*
-dontwarn com.openhtmltopdf.css.sheet.*
-dontwarn com.openhtmltopdf.css.style.*
-dontwarn com.openhtmltopdf.css.util.*
-dontwarn org.apache.pdfbox.pdmodel.common.*
-dontwarn org.apache.pdfbox.pdmodel.common.function.*
-dontwarn org.apache.pdfbox.text.*
-dontwarn org.apache.pdfbox.util.*
-dontwarn org.apache.xmpbox.*
-dontwarn org.junit.internal.runners.statements.*
-dontwarn org.junit.rules.*
-dontwarn com.openhtmltopdf.css.style.derived.*

-dontnote org.apache.commons.logging.impl.*
-dontnote com.google.android.gms.common.api.internal.*
-dontnote com.google.android.gms.common.util.*
-dontnote com.bumptech.glide.*
-dontnote com.google.android.gms.internal.measurement.*
-dontnote com.google.gson.internal.*
-dontnote com.ibm.icu.impl.*
-dontnote com.ibm.icu.text.*
-dontnote xute.markdeditor.components.*
-dontnote xute.markdeditor.api.*
-dontnote xute.markdeditor.*
-dontnote xute.cryptocoinview.*
-dontnote retrofit2.converter.scalars.*
-dontnote retrofit2.converter.gson.*
-dontnote retrofit2.*
-dontnote okhttp3.*
-dontnote com.hapramp.models.*
-dontnote com.hapramp.views.skills.*
-dontnote com.hapramp.views.post.*
-dontnote com.hapramp.views.hashtag.*
-dontnote com.hapramp.views.feedlist.*
-dontnote com.hapramp.views.extraa.*
-dontnote com.hapramp.views.competition.*
-dontnote com.hapramp.views.comments.*
-dontnote com.hapramp.views.*
-dontnote com.hapramp.api.*
-dontnote junit.runner.*
-dontnote com.google.firebase.iid.*
-dontnote com.google.firebase.analytics.*
-dontnote com.google.android.gms.measurement.*
-dontnote com.google.android.gms.common.images.internal.*
-dontnote com.google.android.gms.common.api.internal.*
-dontnote com.google.android.gms.ads.identifier.*
-dontnote org.apache.pdfbox.pdmodel.encryption.*
-dontnote org.apache.commons.logging.impl.*
-dontnote okhttp3.internal.platform.*
-dontnote io.fabric.sdk.android.*
-dontnote com.ibm.icu.text.*
-dontnote com.ibm.icu.impl.*
-dontnote com.google.gson.internal.*
-dontnote com.google.android.gms.internal.measurement.*
-dontnote com.google.android.gms.common.util.*
-dontnote com.crashlytics.android.core.*
-dontnote com.bumptech.glide.*
-dontnote org.apache.commons.logging.impl.*
-dontnote okio.*
-dontnote org.apache.commons.logging.*
