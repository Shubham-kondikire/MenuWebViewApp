# Experiment 8: Implement Menus and WebView in an Android Application

**Name:** Shubham Shivaji Kondikire
**USN:** 25MCAR0102
**Course:** MCA, Jain (Deemed-to-be University)

---

## 1. Experiment Overview

This experiment demonstrates two core Android UI concepts working together in a
single app:

1. **Options Menu** — the menu that appears from the toolbar (3-dot icon),
   used to trigger app-wide actions.
2. **WebView** — an embedded browser component that loads and displays web
   content directly inside the app, without needing the user's phone browser.

## 2. Concept / Technology Behind It

- **Options Menu (`android.view.Menu`)**: Defined declaratively in an XML file
  (`res/menu/main_menu.xml`), then inflated into the Activity's toolbar via
  `onCreateOptionsMenu()`. Each tap is routed through `onOptionsItemSelected()`,
  which checks the tapped item's ID and performs the matching action. This is
  the standard Android pattern for global actions (Settings, Refresh, About,
  Exit, etc.) that don't belong to one specific screen element.

- **WebView (`android.webkit.WebView`)**: A view that renders web pages using
  the device's WebView engine (Chromium-based on modern Android). Key pieces:
  - `WebViewClient` — intercepts navigation so links open **inside** the app
    instead of launching an external browser.
  - `WebChromeClient` — reports browser-chrome events like load progress,
    used here to drive an on-screen progress bar.
  - `loadUrl()` — loads a page from the internet.
  - `loadData()` — loads raw HTML from a String, with no internet needed
    (used for the local "My Info" page in this app).
  - `INTERNET` permission in `AndroidManifest.xml` — required for any network
    page to load.

## 3. Scenario Used to Demonstrate It

The app is a **mini in-app browser**. The toolbar's Options Menu offers 5
choices, each changing what the WebView displays:

| Menu Item   | Action |
|-------------|--------|
| Home        | Loads `https://www.google.com` |
| Wikipedia   | Loads `https://www.wikipedia.org` |
| GitHub      | Loads `https://www.github.com` |
| My Info     | Loads a local (offline) HTML page showing the student's Name & USN |
| Refresh     | Reloads whatever page is currently open, shows a confirmation Toast |
| Exit        | Closes the app |

The device Back button is overridden to step back through the WebView's own
browsing history first (like a real browser), and only closes the app once
there's no more history left.

## 4. Project / File Structure

```
MenuWebViewApp/
├── app/
│   ├── build.gradle                # App-level Gradle config (SDK versions, dependencies)
│   └── src/main/
│       ├── AndroidManifest.xml     # Declares INTERNET permission + MainActivity
│       ├── java/com/example/menuwebview/
│       │   └── MainActivity.kt     # All logic: menu handling + WebView setup
│       └── res/
│           ├── layout/
│           │   └── activity_main.xml   # Toolbar + ProgressBar + WebView layout
│           ├── menu/
│           │   └── main_menu.xml       # Options Menu items (Home/Wikipedia/GitHub/My Info/Refresh/Exit)
│           └── values/
│               ├── strings.xml         # App name string
│               └── themes.xml          # App theme/colors
├── build.gradle                    # Project-level Gradle config
└── settings.gradle                 # Module registration
```

**What each key file does:**
- `main_menu.xml` → defines *what* menu items exist.
- `MainActivity.kt` → defines *what happens* when each item is tapped
  (`onOptionsItemSelected`) and *how* the WebView behaves (`onCreate`,
  `webViewClient`, `webChromeClient`, `onBackPressed`).
- `activity_main.xml` → defines *where* the WebView and progress bar sit on
  screen, under the toolbar.

## 5. Output Screenshot

*(Insert a screenshot here showing the app open with the Home page loaded
and the Options Menu visible.)*

`![App Output](screenshots/output.png)`

## 6. Test Cases

### Test Case 1 — Menu navigation loads correct page
**Steps:** Launch app → tap menu (⋮) → select "Wikipedia"
**Expected result:** WebView loads wikipedia.org, progress bar shows briefly.
**Screenshot:** *(insert screenshot here — `screenshots/testcase1.png`)*

### Test Case 2 — Refresh reloads current page
**Steps:** With any page open → tap menu (⋮) → select "Refresh"
**Expected result:** Page reloads, a Toast "Page refreshed" appears.
**Screenshot:** *(insert screenshot here — `screenshots/testcase2.png`)*

### Test Case 3 — My Info page shows student details (Name & USN)
**Steps:** Tap menu (⋮) → select "My Info"
**Expected result:** WebView shows an offline page with:
- Name: Shubham Shivaji Kondikire
- USN: 25MCAR0102
**Screenshot:** *(insert screenshot here — `screenshots/testcase3.png`)*
👉 This is the test case that satisfies the "USN and Name" requirement.

## 7. How to Run
1. Open the project in Android Studio → let Gradle sync.
2. Run on an emulator or physical device (internet needed for Home/Wikipedia/GitHub; My Info works offline).
3. Tap the 3-dot menu (top-right) to try each option.

## 8. Viva Questions
- **Q: Difference between Options Menu and Context Menu?**
  Options Menu (this experiment) appears from the toolbar and applies to the
  whole Activity. Context Menu appears on a long-press of a specific view.
- **Q: Why override `onBackPressed()`?**
  So Back steps through WebView history first, like a real browser, instead
  of immediately closing the Activity.
- **Q: Why does WebView need the INTERNET permission?**
  Without it, the app cannot make any network request, so remote pages
  (Home/Wikipedia/GitHub) won't load — though `loadData()` pages like "My Info"
  still work since they don't need network access.
- **Q: `WebViewClient` vs `WebChromeClient`?**
  `WebViewClient` handles page navigation (keeps links inside the app).
  `WebChromeClient` handles browser-chrome events (load progress, JS dialogs).
