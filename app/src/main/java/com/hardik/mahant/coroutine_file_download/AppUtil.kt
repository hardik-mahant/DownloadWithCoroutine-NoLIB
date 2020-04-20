package com.hardik.mahant.coroutine_file_download

object AppUtil{

    fun getFileName(fileURL: String): String {
        val data: List<String> = fileURL.split("/")
        return data[data.size - 1]
    }

}