import Swift
import CoreLocation

let EVENT_TYPE = "eventType";
let EVENT_STATUS = "eventStatus";
let TOPIC = "RNConnectivityChecker"

@objc(ConnectivityChecker)
class ConnectivityChecker: NSObject, CLLocationManagerDelegate {
    
    let manager = CLLocationManager()
    init() {
        manager.delegate = self
    }
    
    @objc
    func isLocationEnabledSync() {
        return CLLocationManager.locationServicesEnabled()
    }
    
    @objc(isLocationEnabled:rejecter:)
    func isLocationEnabled(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
        resolve(isLocationEnabledSync())
    }
    
    func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        let isEnabled = isLocationEnabledSync()
        sendEvent(EventType.location, isEnabled)
    }
    
    func sendEvent(event: EventType, status: Bool) {
        let payload: [String: Any] = [
            EVENT_TYPE: event,
            EVENT_STATUS: status
        ]
        self.sendEventWithName(TOPIC, payload)
    }
    
    enum EventType: String {
      case location = "location"
    }
    
}
