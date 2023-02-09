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

export type TListener = (res: TEventResult) => void;

export default class ConnectivityManager {
  static _eventEmitter = new NativeEventEmitter(ConnectivityChecker);

  static addStatusListener(listener: TListener) {
    return ConnectivityManager._eventEmitter.addListener(
      'RNConnectivityStatus',
      listener
    );
  }

  static isLocationEnabled(): Promise<boolean> {
    return ConnectivityChecker.isLocationEnabled();
  }

  static isLocationEnabledSync(): boolean {
    return ConnectivityChecker.isLocationEnabledSync();
  }
}
