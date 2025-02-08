package com.ad.mvvmstarter.utility.extension

import java.io.File


/**
 * Checking file size
 */
fun File.isFileLessThanMiBs(
    mb: Int
): Boolean {
    val maxFileSize = mb * 1024 * 1024
    val l = length()
    val fileSize = l.toString()
    val finalFileSize = fileSize.toInt()
    return finalFileSize >= maxFileSize
}