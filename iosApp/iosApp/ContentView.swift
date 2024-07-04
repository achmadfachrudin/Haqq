import UIKit
import SwiftUI
import ComposeApp
import CoreLocation
import FirebaseAnalytics

struct ComposeView: UIViewControllerRepresentable {

    @StateObject private var locationViewModel = LocationViewModel()

    func makeUIViewController(context: Context) -> UIViewController {
            let storage = SharedStorage.init(context: NSObject())
            let appRepository = KoinHelper().appRepository

            // Start updating location when the view appears
            locationViewModel.requestLocation()
            locationViewModel.locationUpdateCallback = {
                let currentLocation = appRepository.getSetting().location

                if (currentLocation.name != locationViewModel.locationName) {
                    appRepository.updateLocation(
                        location: AppSetting.Location(
                            latitude: locationViewModel.latitude,
                            longitude: locationViewModel.longitude,
                            name: locationViewModel.locationName
                        )
                    )
                }
            }
            Analytics.logEvent(
                AnalyticsEventAppOpen,
                parameters:[
                    AnalyticsParameterSource: Platform_iosKt.getPlatform().name
                ]
            )
            Analytics.logEvent(
                AnalyticsEventScreenView,
                parameters:[
                    AnalyticsParameterScreenName: AnalyticsConstant().HOME_SCREEN
                ]
            )
            let composeViewController = MainViewControllerKt.MainViewController()
            return composeViewController
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

class LocationViewModel: NSObject, ObservableObject, CLLocationManagerDelegate {

    var locationUpdateCallback: (() -> Void)?

    private var locationManager = CLLocationManager()

    @Published var latitude: Double = 0.0
    @Published var longitude: Double = 0.0
    @Published var locationName: String = ""

    override init() {
        super.init()
        setupLocationManager()
    }

    func requestLocation() {
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
    }

    private func setupLocationManager() {
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
    }

    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let location = locations.last else { return }

        CLGeocoder().reverseGeocodeLocation(location) { placemarks, error in
            if let error = error {
                print("Reverse geocoding error: \(error.localizedDescription)")
                return
            }

            if let placemark = placemarks?.first {
                if (
                    self.latitude != location.coordinate.latitude &&
                    self.longitude != location.coordinate.longitude &&
                    self.locationName != placemark.name
                ) {
                    self.latitude = location.coordinate.latitude
                    self.longitude = location.coordinate.longitude
                    self.locationName = placemark.name ?? ""
                    self.locationUpdateCallback?()
                }
            }
        }
    }

    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Location manager failed with error: \(error.localizedDescription)")
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}



