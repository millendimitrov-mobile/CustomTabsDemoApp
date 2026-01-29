# WebView vs Custom Tabs Autofill Demo

A demonstration Android app showing why Android Password Manager / Autofill is generally more reliable in Chrome Custom Tabs than in an embedded WebView wrapper.

## Purpose

This app allows you to compare autofill behavior between:

1. **WebView** - An embedded browser component within the app
2. **Chrome Custom Tabs** - A Chrome-powered browser tab that opens over the app

The goal is to demonstrate that Custom Tabs typically provides a more consistent and reliable autofill experience, similar to using Chrome directly.

## Features

- Configure any HTTPS login URL via Settings
- Test the same URL in both WebView and Custom Tabs
- Persisted settings using DataStore
- Clean Material3 UI

## How to Use

### Setting Your Login URL

1. Open the app
2. Tap **Settings**
3. Paste your HTTPS login URL (must start with `https://`)
4. Tap **Save**

### Testing Autofill

1. Tap **Open in WebView**
   - Tap the username field: do saved credentials appear?
   - Tap the password field: does it offer autofill?
   - Does it fill both fields without extra taps?

2. Go back and tap **Open in Custom Tab**
   - Repeat the same checks

3. Compare:
   - Speed to show suggestions
   - Whether both fields fill automatically
   - Number of taps required

## Demo URL & Credentials

For testing purposes, you can use the built-in demo URL:

- **URL**: `https://697bc73c87d6a92e477ae7f9--velvety-malasada-9996dd.netlify.app/`
- **Username**: Any value (2+ characters)
- **Password**: Any value (2+ characters)

This demo page is specifically built to be friendly to Google Password Manager. Enter any credentials to "log in" and save them, then compare the autofill experience.

Tap **Use Demo URL** on the home screen or **Reset to Demo URL** in Settings.

## Important Notes

Autofill behavior can vary significantly based on:

- **Android version** - Newer versions have better autofill support
- **Device/OEM** - Samsung, Pixel, etc. may have different implementations
- **Password manager app** - Google Password Manager, 1Password, Bitwarden, etc.
- **Web page implementation** - HTML semantics, dynamic rendering, iframes, SSO flows

Custom Tabs typically behaves like Chrome because it IS Chrome, just displayed over your app. WebView is a separate component that may not have the same level of password manager integration.

## Technical Details

- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 36
- **Architecture**: Single-module, MVVM with Jetpack Compose
- **Persistence**: DataStore Preferences
- **Navigation**: Navigation Compose

### WebView Autofill Configuration

The WebView is configured to be autofill-friendly:

```kotlin
settings.javaScriptEnabled = true
settings.domStorageEnabled = true
isFocusable = true
isFocusableInTouchMode = true
importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_YES
```

Despite these settings, autofill support may still be limited compared to Custom Tabs.

## Building

Open in Android Studio and run on a device or emulator. No special configuration required.

## License

This is a demonstration app for educational purposes.
