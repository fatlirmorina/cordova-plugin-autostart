# Cordova Autostart Plugin

[![npm](https://img.shields.io/npm/v/cordova-plugin-autostart.svg)](https://www.npmjs.com/package/cordova-plugin-autostart)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/fatlirmorina/cordova-plugin-autostart/blob/main/LICENSE)

Automatically start your Cordova application after device boot or application update. This plugin provides autostart functionality for Android (including Amazon Fire OS/Fire TV) and macOS platforms.

## Features

- ✅ **Android**: Auto-start app after device boot
- ✅ **macOS**: Launch helper applications at boot
- ✅ **Fire OS/Fire TV**: Optimized autostart for Amazon devices
- ✅ **Service Support**: Start background services automatically
- ✅ **Easy Control**: Enable/disable autostart functionality
- ✅ **Device Detection**: Identify Fire OS and Fire TV devices

## Supported Platforms

- **Android** (API level 14+)
- **Amazon Fire OS / Fire TV**
- **macOS**

## Installation

### Install from GitHub

```bash
cordova plugin add https://github.com/fatlirmorina/cordova-plugin-autostart.git
```

## Usage

### Basic Autostart

```javascript
document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
  // Enable autostart for your main application
  cordova.plugins.autoStart.enable();

  // Disable autostart
  // cordova.plugins.autoStart.disable();
}
```

### Service Autostart

```javascript
// Android: Start a service automatically
// The service class name should be the full class name of your service
cordova.plugins.autoStart.enableService("com.yourcompany.yourapp.YourServiceClass");

// macOS: Launch a helper application with bundle identifier
cordova.plugins.autoStart.enableService("com.yourcompany.helper-app");
```

### Fire OS Detection

```javascript
// Check if running on Amazon Fire OS
cordova.plugins.autoStart.isFireOS(
  function (isFireOS) {
    if (isFireOS) {
      console.log("Running on Amazon Fire OS");

      // Check if it's specifically Fire TV
      cordova.plugins.autoStart.isFireTV(
        function (isFireTV) {
          if (isFireTV) {
            console.log("Optimized for Fire TV");
            // Enable autostart with Fire TV optimizations
            cordova.plugins.autoStart.enable();
          } else {
            console.log("Running on Fire Tablet");
          }
        },
        function (error) {
          console.error("Error checking Fire TV:", error);
        }
      );
    }
  },
  function (error) {
    console.error("Error checking Fire OS:", error);
  }
);

// Get detailed device type information
cordova.plugins.autoStart.getFireOSDeviceType(
  function (deviceType) {
    console.log("Device type:", deviceType);
    // Possible values: "Fire TV", "Fire Tablet", "Not Fire OS"
  },
  function (error) {
    console.error("Error getting device type:", error);
  }
);
```

## API Reference

### Methods

#### `enable()`

Enables autostart for your main application.

**Platforms**: Android, Fire OS

```javascript
cordova.plugins.autoStart.enable();
```

#### `enableService(serviceClass)`

Enables autostart for a specific service or helper application.

**Parameters**:

- `serviceClass` (string):
  - **Android/Fire OS**: Full class name of the service (e.g., `"com.yourcompany.yourapp.YourService"`)
  - **macOS**: Bundle identifier of the helper application

**Platforms**: Android, Fire OS, macOS

```javascript
// Android/Fire OS
cordova.plugins.autoStart.enableService("com.yourcompany.yourapp.YourService");

// macOS
cordova.plugins.autoStart.enableService("com.yourcompany.helper-app");
```

#### `disable()`

Disables autostart functionality for both app and services.

**Platforms**: Android, Fire OS, macOS

```javascript
cordova.plugins.autoStart.disable();
```

#### `isFireOS(successCallback, errorCallback)`

Checks if the device is running Amazon Fire OS.

**Parameters**:

- `successCallback` (function): Called with boolean result
- `errorCallback` (function): Called on error

**Platforms**: Android, Fire OS

```javascript
cordova.plugins.autoStart.isFireOS(
  function (isFireOS) {
    console.log("Is Fire OS:", isFireOS);
  },
  function (error) {
    console.error("Error:", error);
  }
);
```

#### `isFireTV(successCallback, errorCallback)`

Checks if the device is an Amazon Fire TV.

**Parameters**:

- `successCallback` (function): Called with boolean result
- `errorCallback` (function): Called on error

**Platforms**: Android, Fire OS

```javascript
cordova.plugins.autoStart.isFireTV(
  function (isFireTV) {
    console.log("Is Fire TV:", isFireTV);
  },
  function (error) {
    console.error("Error:", error);
  }
);
```

#### `getFireOSDeviceType(successCallback, errorCallback)`

Gets a description of the Fire OS device type.

**Parameters**:

- `successCallback` (function): Called with device type string
- `errorCallback` (function): Called on error

**Returns**: `"Fire TV"`, `"Fire Tablet"`, or `"Not Fire OS"`

**Platforms**: Android, Fire OS

```javascript
cordova.plugins.autoStart.getFireOSDeviceType(
  function (deviceType) {
    console.log("Device type:", deviceType);
  },
  function (error) {
    console.error("Error:", error);
  }
);
```

## Platform-Specific Notes

### Android & Fire OS

- Requires `RECEIVE_BOOT_COMPLETED` permission (automatically added)
- App must be installed on internal storage (not SD card)
- Some Android versions may require users to manually enable autostart in device settings
- Fire TV devices receive optimized intent flags for better autostart behavior

### macOS

- Requires helper application to be properly signed
- May require user permission for login items
- Helper applications should be lightweight and follow macOS guidelines

## Permissions

### Android & Fire OS

The plugin automatically adds the required permission:

```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

## TypeScript Support

This plugin includes TypeScript definitions. Example usage:

```typescript
declare var cordova: any;

// Enable autostart
cordova.plugins.autoStart.enable();

// Check Fire OS with proper typing
cordova.plugins.autoStart.isFireOS((isFireOS: boolean) => {
  if (isFireOS) {
    cordova.plugins.autoStart.getFireOSDeviceType((deviceType: string) => {
      console.log(`Running on ${deviceType}`);
    });
  }
});
```

## Troubleshooting

### Android

- **App doesn't start after reboot**: Check if the app is installed on internal storage, not SD card
- **Permission denied**: Ensure the app has been launched at least once manually
- **Battery optimization**: Some devices may require disabling battery optimization for the app

### Fire OS/Fire TV

- **Fire TV not starting**: Ensure the Fire TV device allows unknown sources if using a custom build
- **Detection not working**: Verify the device has the proper Fire OS system features

### macOS

- **Helper not launching**: Check macOS Security & Privacy settings for login items
- **Permission issues**: Ensure helper application is properly signed

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Author

**fatlirmorina**

- GitHub: [@fatlirmorina](https://github.com/fatlirmorina)
- Repository: [cordova-plugin-autostart](https://github.com/fatlirmorina/cordova-plugin-autostart)

## Support

If you encounter any issues or have questions:

1. Check the [existing issues](https://github.com/fatlirmorina/cordova-plugin-autostart/issues)
2. Create a [new issue](https://github.com/fatlirmorina/cordova-plugin-autostart/issues/new) with detailed information
3. Include device type, OS version, and plugin version in your report

---

⭐ **Star this repository if you find it helpful!**
