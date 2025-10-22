/**
 *
 * */
export interface Window {
  cordova: {
    plugins: {
      autoStart: AutoStartInterface;
    };
  };
}
/**
 *
 * */
export interface AutoStartInterface {
  enable(): never;
  enableService(id: string): never;
  disable(): never;
  isFireOS(successCallback: (isFireOS: boolean) => void, errorCallback?: (error: any) => void): void;
  isFireTV(successCallback: (isFireTV: boolean) => void, errorCallback?: (error: any) => void): void;
  getFireOSDeviceType(successCallback: (deviceType: string) => void, errorCallback?: (error: any) => void): void;
}
/**
 *
 * */
export declare var cordova: {
  plugins: {
    autoStart: AutoStartInterface;
  };
};
