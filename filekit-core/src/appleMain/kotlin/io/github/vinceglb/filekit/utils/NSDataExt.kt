package io.github.vinceglb.filekit.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.refTo
import platform.Foundation.NSData
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class, UnsafeNumber::class)
public fun NSData.toByteArray(): ByteArray = let { nsData ->
    ByteArray(nsData.length.toInt()).apply {
        memcpy(this.refTo(0), nsData.bytes, nsData.length)
    }
}

public expect fun ByteArray.toNSData(): NSData

// @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class, UnsafeNumber::class)
// public fun ByteArray.toNSData(): NSData = usePinned {
//    NSData.create(
//        bytes = it.addressOf(0),
//        length = this.size.toULong(),
//    )
// }
