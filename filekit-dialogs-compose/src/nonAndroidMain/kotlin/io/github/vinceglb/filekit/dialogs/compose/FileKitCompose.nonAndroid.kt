package io.github.vinceglb.filekit.dialogs.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import kotlinx.coroutines.launch

@Composable
internal actual fun <PickerResult, ConsumedResult> rememberPlatformFilePickerLauncher(
    type: FileKitType,
    mode: FileKitMode<PickerResult, ConsumedResult>,
    directory: PlatformFile?,
    dialogSettings: FileKitDialogSettings,
    onResult: (ConsumedResult) -> Unit,
): PickerResultLauncher {
    val coroutineScope = rememberCoroutineScope()

    val currentType by rememberUpdatedState(type)
    val currentMode by rememberUpdatedState(mode)
    val currentDirectory by rememberUpdatedState(directory)
    val currentOnConsumed by rememberUpdatedState(onResult)

    return remember {
        PickerResultLauncher {
            coroutineScope.launch {
                val result = FileKit.openFilePicker(
                    type = currentType,
                    mode = currentMode,
                    directory = currentDirectory,
                    dialogSettings = dialogSettings,
                )
                currentMode.consumeResult(result, currentOnConsumed)
            }
        }
    }
}
