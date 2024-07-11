## Usage

Integrate ScanDocSDK in just couple of steps:

1. Initialize using **user key** and accepting terms and conditions inside onCreate method.
```kotlin
import com.scandocai.scandocsdk.ScanDocSDK
ScanDocSDK.initialize(
            userKey = userKey,
            acceptTermsAndConditions = true,
            context = applicationContext
        )
```
2. Add **ScanDocCameraFragment** to capture frames and forward them to SDK for processing.
```kotlin
import com.scandocai.scandocsdk.ScanDocCameraFragment
import com.scandocai.scandocsdk.ScanDocSDK
val scanDocCameraFragment = ScanDocCameraFragment.newInstance()
fragmentManager
	.beginTransaction()
	.replace(R.id.container, scanDocCameraFragment)
	.commit()
```

3. Retrieve SDK output events by collecting **outputEvent**.
```kotlin
import com.scandocai.scandocsdk.ScanDocCameraFragment
import com.scandocai.scandocsdk.ScanDocEvent
import com.scandocai.scandocsdk.ScanDocSDK
lifecycleScope.launch {
            ScanDocSDK
                .outputEvent
                .flowOn(Dispatchers.Main)
                .collect {
                    when (it) {
                        is ScanDocEvent.Extracted -> {
                            val fieldTexts = it
                                .fields
                                .filter { it.value != null }
                                .map { "${it.key}: ${it.value}" }
                                .joinToString(separator = "\n")
                            val images: ArrayList<ImageModel> = arrayListOf<ImageModel>()
                            it.faceImage?.let { images.add(ImageModel(it)) }
                            it.signatureImage?.let { images.add(ImageModel(it)) }
                            it.documentImages?.forEach { image -> images.add(ImageModel(image)) }
                            customeAdapter.update(images)
                            textTopView.text = fieldTexts
                            textBotoomView.text = "✅ Extracted!                               "
                        }
                        is ScanDocEvent.ExtractionInProgress ->
                            textBotoomView.text = "🔬 Extraction in progress!                  "
                        is ScanDocEvent.NetworkError ->
                            textBotoomView.text = "❗ Network error          \"${it.error}\"   "
                        is ScanDocEvent.ValidationInProgress ->
                            textBotoomView.text = "🔎 Validation in progress \"${it.infoCode}\""
                    }
                }
        }
```
