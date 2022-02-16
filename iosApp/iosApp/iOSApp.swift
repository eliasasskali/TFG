import SwiftUI
import Firebase

@main
struct iOSApp: App {
    init() {
        FirebaseApp.configure()
        //fatalError()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
