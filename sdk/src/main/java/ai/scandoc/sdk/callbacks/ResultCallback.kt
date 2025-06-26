package ai.scandoc.sdk.callbacks

import ai.scandoc.sdk.datamodels.responses.ExtractionResponse

interface ResultCallback {

    fun onException(throwable: Throwable) {

    }
    fun onResult(result: ExtractionResponse?) {

    }
}