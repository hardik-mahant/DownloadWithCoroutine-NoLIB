package com.hardik.mahant.coroutine_file_download

import android.app.Activity
import android.util.Log
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference
import java.net.URL
import java.net.URLConnection
import kotlin.coroutines.CoroutineContext

/**
 * TO MIMIC AsyncTask :)
 */
class CRDownloadFile(
    private val activity: Activity,
    private val fileURL: String,
    private val callBack: DownloadCallBack
) : CoroutineScope {

    private val activityWeakRef = WeakReference(activity)

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun execute() {
        onPreExecute()
    }

    private fun onPreExecute() = launch {
        callBack.onDownloadStart()
        val result = doInBackground()
        onPostExecute(result)
    }

    private suspend fun doInBackground(): String = withContext(Dispatchers.IO) {
        val url = URL(fileURL)
        val urlConnection: URLConnection = url.openConnection()
        urlConnection.connectTimeout = 15000
        urlConnection.connect()

        val lenghtOfFile = urlConnection.contentLength

        // download the file
        val input = BufferedInputStream(url.openStream())

        val filePath: String =
            activity.getExternalFilesDir("")!!.absolutePath + "/test_" + AppUtil.getFileName(fileURL)
        if(File(filePath).exists()){
            return@withContext filePath
        }
        val output = FileOutputStream(File(filePath))

        Log.i(
            "DOWNLOAD", "PATH = ${activity.getExternalFilesDir("")!!.absolutePath}"
        )

        val data = ByteArray(1024)
        var total: Long = 0

        var count = input.read(data)
        while (count != -1) {
            total += count
            // publishing the progress....
            // After this onProgressUpdate will be called
            Log.i("DOWNLOAD", "PROGRESS = ${((total * 100) / lenghtOfFile)}")

            // writing data to file
            output.write(data, 0, count)

            count = input.read(data)
        }

        // flushing output
        output.flush()

        // closing streams
        output.close()
        input.close()

        return@withContext filePath
    }

    private fun onPostExecute(filePath: String) {
        if (fileURL.isEmpty()) {
            callBack.onDownloadFailed()
        } else {
            callBack.onDownloadComplete(filePath)
        }
    }

    fun cancelDownload() {
        job.cancel()
    }
}