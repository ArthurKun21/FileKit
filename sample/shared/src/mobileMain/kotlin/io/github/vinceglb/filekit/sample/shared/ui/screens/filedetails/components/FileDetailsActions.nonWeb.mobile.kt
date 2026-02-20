package io.github.vinceglb.filekit.sample.shared.ui.screens.filedetails.components

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.shareFile
import io.github.vinceglb.filekit.sample.shared.ui.icons.Film
import io.github.vinceglb.filekit.sample.shared.ui.icons.Images
import io.github.vinceglb.filekit.sample.shared.ui.icons.LucideIcons
import io.github.vinceglb.filekit.sample.shared.ui.icons.Share
import io.github.vinceglb.filekit.sample.shared.util.isImageFile
import io.github.vinceglb.filekit.sample.shared.util.isVideoFile
import io.github.vinceglb.filekit.saveImageToGallery
import io.github.vinceglb.filekit.saveVideoToGallery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
internal actual fun FileDetailsMobileActions(
    file: PlatformFile,
    scope: CoroutineScope,
) {
    // Share File
    FileDetailsActionRow(
        text = "Share File",
        icon = LucideIcons.Share,
        onClick = {
            scope.launch {
                FileKit.shareFile(file = file)
            }
        },
    )

    // Save Image to Gallery
    if (file.isImageFile()) {
        FileDetailsActionRow(
            text = "Save Image to Gallery",
            icon = LucideIcons.Images,
            onClick = {
                scope.launch {
                    FileKit.saveImageToGallery(file = file)
                }
            },
        )
    }

    // Save Video to Gallery
    if (file.isVideoFile()) {
        FileDetailsActionRow(
            text = "Save Video to Gallery",
            icon = LucideIcons.Film,
            onClick = {
                scope.launch {
                    FileKit.saveVideoToGallery(file = file, "${Uuid.random().toHexString()}")
                }
            },
        )
    }
}
