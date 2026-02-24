package io.github.vinceglb.filekit.sample.shared

import io.github.vinceglb.filekit.sample.shared.viewcontroller.ComposeNSViewDelegate
import platform.AppKit.NSWindow

@Suppress("ktlint:standard:function-naming", "unused", "FunctionName")
public fun AttachMainComposeView(
    window: NSWindow,
): ComposeNSViewDelegate = ComposeNSViewDelegate(
    window = window,
    content = { App() },
)
