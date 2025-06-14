package ai.scandoc.sdk.callbacks

import ai.scandoc.sdk.datamodels.responses.NFCExtractionResponse

interface NFCResultCallback {

    fun onException(throwable: Throwable) {

    }
    fun onResult(result: NFCExtractionResponse?, faceImage: String?) {

    }
}