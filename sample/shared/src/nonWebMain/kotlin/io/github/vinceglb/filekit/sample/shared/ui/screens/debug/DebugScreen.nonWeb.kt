package io.github.vinceglb.filekit.sample.shared.ui.screens.debug

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.saveImageToGallery

internal actual suspend fun debugPlatformTest(file: PlatformFile) {
    FileKit.saveImageToGallery(file)
}
