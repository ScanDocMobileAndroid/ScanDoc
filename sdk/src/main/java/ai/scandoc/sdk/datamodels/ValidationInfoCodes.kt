package ai.scandoc.sdk.datamodels

enum class ValidationInfoCodes(val code: String, val description: String) {
    EXTRACTING("1000", "Proceeding with extraction..."),
    DEPRECATED("1001", "DEPRECATED CODE"),
    DOCUMENT_NOT_VISIBLE("1002", "Document not fully visible, all four corners should be on-screen."),
    DOCUMENT_NOT_PRESENT("1003", "Document not present."),
    ANGLED_DOCUMENT("1004", "Document is at an angle. Please correct."),
    DOCUMENT_TOO_SMALL("1005", "Document too small. Move closer."),
    DOCUMENT_UNSUPPORTED("1006", "Document not supported."),
    FLIP_DOCUMENT("1007", "Back side detected – please flip."),
    BACKGROUND_TOO_SIMILAR("1008", "Background too similar to document."),
    CAMERA_FOCUSING("1009", "Waiting for camera to focus."),
    LOW_IMAGE_QUALITY("1010", "Low image quality. Keep the camera stable."),
}