# FuriganaTextView
[Licensed under Creative Commons BY-SA 3.0](http://creativecommons.org/licenses/by-sa/3.0/)
#General
FuriganaTextView is a Textview for Android that supports rendering of furigana characters above Japanese kanji.
The FuriganaTextView builds upon the furigana-view that what originally authored by [sh0](https://github.com/sh0/furigana-view). The widget textsize and color can be modified from XML, however the text has to be set through the Java code. The library also includes a simple utils class that converts kanji enclosed in `<ruby>` tags into the string format supported by the widget.

#Examples
Adding the furigana textview in XML

`<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin">

    <se.fekete.furiganatextview.FuriganaView
        android:id="@+id/furigana_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="25sp" />
</RelativeLayout>`