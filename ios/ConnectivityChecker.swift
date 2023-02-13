import Swift
import CoreLocation
import React

let EVENT_TYPE = "eventType";
let EVENT_STATUS = "eventStatus";
let TOPIC = "RNConnectivityChecker"

enum EventType: String {
  case location = "location"
}

@objc(ConnectivityChecker)
class ConnectivityChecker: RCTEventEmitter {
    
    let manager = CLLocationManager()
    
    override init() {
        super.init()
        manager.delegate = self
    }
    
    func isLocationEnabledSync() -> Bool {
        return CLLocationManager.locationServicesEnabled()
    }
    
    @objc(isLocationEnabled:rejecter:)
    func isLocationEnabled(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
        resolve(isLocationEnabledSync())
    }
    
    func notify(event: EventType, status: Bool) {
        let payload: [String: Any] = [
            EVENT_TYPE: event,
            EVENT_STATUS: status
        ]
        self.sendEvent(withName: TOPIC, body: payload)
    }
    
    override func supportedEvents() -> [String]! {
        return [TOPIC]
    }
    
}

extension ConnectivityChecker : CLLocationManagerDelegate {
    func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        let isEnabled = isLocationEnabledSync()
        notify(event: EventType.location, status: isEnabled)
    }
}
