package io.github.vinceglb.filekit.sample.shared.ui.screens.filedetails.components

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.shareFile
import io.github.vinceglb.filekit.sample.shared.ui.icons.LucideIcons
import io.github.vinceglb.filekit.sample.shared.ui.icons.Share
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

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
}
