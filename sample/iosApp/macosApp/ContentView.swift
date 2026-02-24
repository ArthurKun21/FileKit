import SwiftUI
import AppKit
import SampleSharedKit

struct ComposeView: NSViewRepresentable {
    final class AttachAwareView: NSView {
        var onMoveToWindow: ((NSWindow?) -> Void)?

        override func viewDidMoveToWindow() {
            super.viewDidMoveToWindow()
            onMoveToWindow?(window)
        }
    }

    final class Coordinator {
        var delegate: ComposeNSViewDelegate?
    }

    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    func makeNSView(context: Context) -> NSView {
        let view = AttachAwareView()
        view.onMoveToWindow = { [weak coordinator = context.coordinator] window in
            guard let coordinator else { return }
            guard let window else { return }
            attachCompose(to: window, coordinator: coordinator)
        }
        return view
    }

    func updateNSView(_ nsView: NSView, context: Context) {
        guard let view = nsView as? AttachAwareView else { return }
        view.onMoveToWindow = { [weak coordinator = context.coordinator] window in
            guard let coordinator else { return }
            guard let window else { return }
            attachCompose(to: window, coordinator: coordinator)
        }
    }

    static func dismantleNSView(_ nsView: NSView, coordinator: Coordinator) {
        coordinator.delegate?.destroy()
        coordinator.delegate = nil
    }

    private func attachCompose(to window: NSWindow, coordinator: Coordinator) {
        configureWindowAppearance(window)
        guard coordinator.delegate == nil else { return }
        coordinator.delegate = ComposeEntryPointKt.AttachMainComposeView(window: window)
    }

    private func configureWindowAppearance(_ window: NSWindow) {
        window.styleMask.insert(.fullSizeContentView)
        window.titlebarAppearsTransparent = true
        window.titleVisibility = .hidden
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}
