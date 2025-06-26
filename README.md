# ScanDoc Android SDK

The ScanDoc SDK enables easy integration of document scanning and NFC biometric data reading within your Android application.

## ✨ Features

- Real-time camera scanning for ID documents.
- Document image, face, and signature extraction.
- NFC biometric chip reading (BAC protocol).
- Configurable SDK settings via `ScanDocSDKConfig`.

---

## 🚀 Getting Started

### Step 1: Add to Your Project via Version Catalog

In `libs.versions.toml`:

```toml
[versions]
scandoc = "2.1.0"

[libraries]
scandoc-sdk = { group = "ai.scandoc", name = "sdk", version.ref = "scandoc" }
```

In your `build.gradle.kts`:

```kotlin
dependencies {
    implementation(libs.scandoc.sdk)
}
```

---

## 📱 Manifest Requirements

Ensure your `AndroidManifest.xml` includes:

```xml
<root>
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.NFC" />
    
    <uses-feature android:name="android.hardware.camera" android:required="false" />
    <uses-feature android:name="android.hardware.nfc" android:required="false" />
    
    <application
        android:networkSecurityConfig="@xml/network_security_config"
        android:launchMode="singleTop">
    </application>
</root>
```
In order to enable the camera, internet and NFC interfaces.

Also include this in `res/xml/network_security_config.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">api.scandoc.ai</domain>
    </domain-config>
</network-security-config>
```
In order to allow requests to the api.scandoc.ai domain.

---

## 🧪 Usage Example

### Initialize the SDK and Start Scanning

```kotlin
val resultCallback = object : ResultCallback {
    override fun onResult(result: ExtractionResponse?) {
        // Handle extracted data
    }

    override fun onException(throwable: Throwable) {
        // Handle exception
    }
}

val settings = Settings(
    shouldReturnSignatureIfDetected = true,
    shouldReturnFaceIfDetected = true,
    shouldReturnDocumentImage = true,
    validationTimeout = null
)

ScanDocSDKConfig.initialize(
    resultCallback = resultCallback,
    userKey = "YOUR_API_KEY",
    subClient = "Android",
    requestSettings = settings,
    acceptTermsAndConditions = true
)

supportFragmentManager.commit {
    replace(R.id.fragment_container_view, ScanDocFragment(), "ScanDocFragment")
}
```

---

### 🖼 Customizing the UI

You may override the following method in a subclass of `ScanDocFragment`:

```kotlin
override fun updateInstructionsText(code: ValidationInfoCodes, description: String? = null) {
    super.updateInstructionsText(code, "This will render as text on screen")
    // super.updateInstructionsText(code) - this renders default code description
}
```

Default codes include:

- 1000 (EXTRACTING) -> Proceeding with extraction
- 1001 (DEPRECATED) -> Deprecated info code - focusing replaced by 1009
- 1002 (DOCUMENT_NOT_VISIBLE) -> Document not fully visible, all four corners of the document should be on-screen.
- 1003 (DOCUMENT_NOT_PRESENT) -> Document not present.
- 1004 (ANGLED_DOCUMENT) -> Captured document is at an angle. Please correct the document angle.
- 1005 (DOCUMENT_TOO_SMALL) -> Captured document is too small. Please move it closer to the camera.
- 1006 (DOCUMENT_UNSUPPORTED) -> Document not supported.
- 1007 (FLIP_DOCUMENT) -> Image is valid, but the document has a back side - flip the document.
- 1008 (BACKGROUND_TOO_SIMILAR) -> Background color is too similar to the color of the document.
- 1009 (CAMERA_FOCUSING) -> Waiting for camera to focus.
- 1010 (LOW_IMAGE_QUALITY) -> Low image quality detected, keep the camera stable.
- 0001 (not an enum) -> Raises an error (you do not need to cover this case)

You may also call:

```kotlin
hideDefaultInstructionText()
showDefaultInstructionText()
```

To control the visibility of the default instruction messages.
Additionally, you may stack other visual elements on top of the fragment to enhance user experience.

---

## 📲 NFC Reading

### Option A: Use `ScanDocNFCFragment`

After successful extraction:

```kotlin
val nfcFragment = ScanDocNFCFragment(extractionResponse, object : NFCResultCallback {
    override fun onResult(result: NFCExtractionResponse?, faceImage: String?) {
        // NFC read successful
    }

    override fun onException(e: Throwable) {
        // Handle failure
    }
})

supportFragmentManager.commit {
    add(nfcFragment, "ScanDocNFCFragment")
}
```

Make sure your `Activity` also overrides:

```kotlin
@RequiresApi(Build.VERSION_CODES.O)
override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    val fragment = supportFragmentManager.findFragmentByTag("ScanDocNFCFragment")
    if (fragment is ScanDocNFCFragment && intent != null) {
        fragment.handleIntent(intent)
    }
}
```

### Option B: Use `NFCHandler` directly

For custom handling without a fragment:

```kotlin
val handler = NFCHandler(extractionResponse, object : NFCResultCallback {
    override fun onResult(result: NFCExtractionResponse?, faceImage: String?) {
        // Success
    }

    override fun onException(e: Throwable) {
        // Failure
    }
}, context = this)

handler.handleIntent(intent)
```

## 📊 Working with Extracted Data

After a successful document scan or NFC read, the SDK returns structured data objects containing the results.

### 📷 `ExtractionResponse` (Camera Scan)

The `ExtractionResponse` object contains all data extracted from the document, such as:

- **Transaction metadata** (e.g., `TransactionID`, `UploadedAt`, `Status`)
- **Field data** (e.g., name, birth date, document number)
- **Images** (`FaceImage`, `Signature`, `Documents`)
- **Metadata** describing the document type

You can access fields like so:

```kotlin
val name = response.Data.Name.RecommendedValue
val documentNumber = response.Data.DocumentNumber.RecommendedValue
val faceImageBase64 = response.ImageData.FaceImage
```

Each field is returned as a `FieldGroup`:

```kotlin
data class FieldGroup(
    val Read: Boolean,
    val Validated: Boolean,
    val RecommendedValue: String?,
    val OCR: SourceData,
    val MRZ: SourceData
)
```

This allows you to inspect raw OCR and MRZ values and determine their validity.
We recommend using the RecommendedValue attribute.

### 📲 `NFCExtractionResponse` (NFC Chip Read)

When NFC reading is completed, you receive an `NFCExtractionResponse`:

```kotlin
val fullName = "${nfcResult.name} ${nfcResult.surname}"
val issuingAuthority = nfcResult.issuingAuthority
val expiry = nfcResult.expiryDate
```

Fields are mapped from ICAO data groups (DGs) read from the biometric chip. Null values indicate missing or unsupported fields for the given document.

---

## 📩 Support

For integration assistance or API access, contact [mvlaic@scandoc.ai](mailto:mvlaic@scandoc.ai) and [support@scandoc.ai](mailto:support@scandoc.ai)

---

## 🧾 License

This SDK is proprietary. For any kind of use, contact [info@scandoc.ai](mailto:info@scandoc.ai).
