import UIKit
import SwiftUI
import ComposeApp
import FirebaseCore


 class AppDelegate: NSObject, UIApplicationDelegate {
     func applicationDidFinishLaunching(_ application: UIApplication) {
         FirebaseApp.configure()
     }
 }

@main
struct iOSApp: App {
      @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    init() {
       KoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
            .environment(\.locale, Locale(identifier: "id"))
        }
    }
}
