package io.github.vinceglb.filekit.utils

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class, UnsafeNumber::class)
public actual fun ByteArray.toNSData(): NSData = usePinned {
    NSData.create(
        bytes = it.addressOf(0),
        length = this.size.toULong(),
    )
}
