package io.github.vinceglb.filekit.dialogs

import io.github.vinceglb.filekit.PlatformFile

internal actual fun createTestPlatformFile(name: String): PlatformFile =
    PlatformFile("/tmp/$name")
