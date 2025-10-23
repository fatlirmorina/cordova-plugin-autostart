# Cordova Autostart Plugin

[![npm](https://img.shields.io/npm/v/cordova-plugin-autostart.svg)](https://www.npmjs.com/package/cordova-plugin-autostart)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/fatlirmorina/cordova-plugin-autostart/blob/main/LICENSE)

Automatically start your Cordova application after device boot or application update. This plugin provides autostart functionality for Android and macOS platforms.

## Features

- ✅ **Android**: Auto-start app after device boot
- ✅ **macOS**: Launch helper applications at boot
- ✅ **Service Support**: Start background services automatically
- ✅ **Easy Control**: Enable/disable autostart functionality

## Supported Platforms

- **Android** (API level 14+)
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

## API Reference

### Methods

#### `enable()`

Enables autostart for your main application.

**Platforms**: Android

```javascript
cordova.plugins.autoStart.enable();
```

#### `enableService(serviceClass)`

Enables autostart for a specific service or helper application.

**Parameters**:

- `serviceClass` (string):
  - **Android**: Full class name of the service (e.g., `"com.yourcompany.yourapp.YourService"`)
  - **macOS**: Bundle identifier of the helper application

**Platforms**: Android, macOS

```javascript
// Android
cordova.plugins.autoStart.enableService("com.yourcompany.yourapp.YourService");

// macOS
cordova.plugins.autoStart.enableService("com.yourcompany.helper-app");
```

#### `disable()`

Disables autostart functionality for both app and services.

**Platforms**: Android, macOS

```javascript
cordova.plugins.autoStart.disable();
```

## Platform-Specific Notes

### Android

- Requires `RECEIVE_BOOT_COMPLETED` permission (automatically added)
- App must be installed on internal storage (not SD card)
- Some Android (fireOS) versions may require users to manually enable autostart in device settings
- For some Android (fireOS) generations, additional permissions may need to be granted manually via ADB:

```bash
adb shell pm grant com.yourapp android.permission.SYSTEM_ALERT_WINDOW
```

### macOS

- Requires helper application to be properly signed
- May require user permission for login items
- Helper applications should be lightweight and follow macOS guidelines

## Permissions

### Android

The plugin automatically adds the required permission:

```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

For some Android (fireOS) generations, you may need to manually grant additional permissions via ADB:

```bash
# Grant system alert window permission (if required for your app)
adb shell pm grant com.yourapp android.permission.SYSTEM_ALERT_WINDOW
```

## TypeScript Support

This plugin includes TypeScript definitions. Example usage:

```typescript
declare var cordova: any;

// Enable autostart
cordova.plugins.autoStart.enable();

// Enable service autostart
cordova.plugins.autoStart.enableService("com.yourcompany.yourapp.YourService");
```

## Troubleshooting

### Android

- **App doesn't start after reboot**: Check if the app is installed on internal storage, not SD card
- **Permission denied**: Ensure the app has been launched at least once manually
- **Battery optimization**: Some devices may require disabling battery optimization for the app
- **Missing permissions**: For some Android generations, manually grant permissions via ADB:
  ```bash
  adb shell pm grant com.yourapp android.permission.SYSTEM_ALERT_WINDOW
  ```

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
