import { NativeEventEmitter, NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-connectivity-checker' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const ConnectivityChecker = NativeModules.ConnectivityChecker
  ? NativeModules.ConnectivityChecker
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export type TEvent = 'location';

export type TEventResult = {
  eventType: TEvent;
  status: boolean;
};

export type TEventListener = (res: TEventResult) => void;

const EVENT_TYPE = 'RNConnectivityChecker';

class ConnectivityManager {
  private _eventEmitter = new NativeEventEmitter(ConnectivityChecker);

  addListener(listener: TEventListener) {
    return this._eventEmitter.addListener(EVENT_TYPE, listener);
  }

  removeAll() {
    return this._eventEmitter.removeAllListeners(EVENT_TYPE);
  }

  listenerCount() {
    return this._eventEmitter.listenerCount;
  }

  isLocationEnabled(): Promise<boolean> {
    return ConnectivityChecker.isLocationEnabled();
  }
}

const connectivityManager = new ConnectivityManager();

export default connectivityManager;
