package io.github.vinceglb.filekit.dialogs

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.WebFile
import io.github.vinceglb.filekit.fromWebDirectoryFiles
import kotlinx.coroutines.flow.Flow
import kotlin.js.ExperimentalWasmJsInterop

@OptIn(ExperimentalWasmJsInterop::class)
internal actual suspend fun FileKit.platformOpenFilePicker(
    type: FileKitType,
    mode: PickerMode,
    directory: PlatformFile?,
    dialogSettings: FileKitDialogSettings,
): Flow<FileKitPickerState<List<PlatformFile>>> =
    platformOpenFilePickerWeb(
        type = type,
        multipleMode = mode is PickerMode.Multiple,
        directoryMode = false,
    )?.map { PlatformFile(it) }.toPickerStateFlow()

public actual suspend fun FileKit.openDirectoryPicker(
    directory: PlatformFile?,
    dialogSettings: FileKitDialogSettings,
): PlatformFile? {
    val fileList = platformOpenFilePickerWeb(
        type = FileKitType.File(),
        multipleMode = true,
        directoryMode = true,
    )
    return PlatformFile.fromWebDirectoryFiles(fileList.orEmpty())
}

internal expect suspend fun platformOpenFilePickerWeb(
    type: FileKitType,
    multipleMode: Boolean, // select multiple files
    directoryMode: Boolean, // select a directory
): List<WebFile.FileWrapper>?
