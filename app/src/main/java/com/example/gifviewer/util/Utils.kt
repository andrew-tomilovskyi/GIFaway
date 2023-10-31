package com.example.gifviewer.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.example.gifviewer.R

fun downloadGifToStorage(context: Context, url: String, name: String) : String {
    val fileExtension = url.substring(url.lastIndexOf("."))
    val folderName = context.getString(R.string.app_name)
    val fileName = context.getString(R.string.app_name_download)
    val subPath =
        "$folderName/$fileName" +
                name +
                fileExtension

    val title = context.getString(R.string.app_name)
    val description = context.getString(R.string.downloading_message)

    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle(title)
        .setDescription(description)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, subPath)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

    val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val downloadId: Long = manager.enqueue(request)

    val filePath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/" + subPath

    return filePath
}