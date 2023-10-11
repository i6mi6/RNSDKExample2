import SwiftUI

struct RNSDKCoreView: UIViewControllerRepresentable {
    typealias UIViewControllerType = UINavigationController

    func makeUIViewController(context: Context) -> UINavigationController {
        // Here, we create and return our SDK's UI
        let sdkVC = RNSDKCore.SDKHandler.SDKViewController()
        let navigationController = UINavigationController(rootViewController: sdkVC)
        return navigationController
    }

    func updateUIViewController(_ uiViewController: UINavigationController, context: Context) {
        // Update code if needed
    }
}
