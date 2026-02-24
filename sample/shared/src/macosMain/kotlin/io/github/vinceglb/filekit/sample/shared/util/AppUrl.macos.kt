package io.github.vinceglb.filekit.sample.shared.util

import platform.AppKit.NSWorkspace
import platform.Foundation.NSURL

internal actual fun AppUrl.openUrlInBrowser() {
    NSURL.URLWithString(this.url)?.let { url ->
        NSWorkspace.sharedWorkspace.openURL(url)
    }
}
