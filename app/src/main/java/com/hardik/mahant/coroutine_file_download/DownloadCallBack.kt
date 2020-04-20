package com.hardik.mahant.coroutine_file_download

interface DownloadCallBack {
    fun onDownloadStart()
    fun onDownloadComplete(uri: String)
    fun onDownloadFailed()
}