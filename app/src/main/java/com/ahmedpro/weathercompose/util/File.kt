package com.ahmedpro.weathercompose.util

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val WEATHER_DIR = "ComposeWeather"

fun createImageUri(context: Context): Uri? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val currentDate = dateFormat.format(Date())
    val contentResolver = context.contentResolver
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$currentDate.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/$WEATHER_DIR")
    }
    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
}

fun getAllImagesUris(context: Context): List<Uri> {
    val imageList = mutableListOf<Uri>()

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_TAKEN
    )

    val selection = "${MediaStore.Images.Media.DATA} like ?"
    val selectionArgs = arrayOf("%/$WEATHER_DIR/%")

    val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

        while (cursor.moveToNext()) {
            val imageId = cursor.getLong(idColumn)
            val contentUri: Uri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageId
            )
            imageList.add(contentUri)
        }
    }

    return imageList
}

fun getFileDate(context: Context, fileUri: Uri?): Date? {
    if (fileUri == null) return null

    val projection = arrayOf(
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.DATE_MODIFIED
    )

    context.contentResolver.query(fileUri, projection, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val dateTakenIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val dateAddedIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            val dateModifiedIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)

            // Choose the most relevant date from available options
            val dateTaken = cursor.getLong(dateTakenIndex)
            val dateAdded = cursor.getLong(dateAddedIndex)
            val dateModified = cursor.getLong(dateModifiedIndex)

            val chosenDate = when {
                dateTaken > 0 -> dateTaken
                dateAdded > 0 -> dateAdded
                dateModified > 0 -> dateModified
                else -> return null
            }

            return Date(chosenDate)
        }
    }

    return null
}

fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(date)
}