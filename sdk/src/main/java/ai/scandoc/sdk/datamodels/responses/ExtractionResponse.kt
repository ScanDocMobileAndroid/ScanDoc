package ai.scandoc.sdk.datamodels.responses

import java.io.Serializable


data class ExtractionResponse(
    val TransactionID: String,
    val UploadedAt: String,
    val ProductName: String,
    val Errors: List<String>,
    val Warnings: List<String>,
    val Status: Int,
    val Method: String,
    val InfoCode: String,
    val AnalysisTime: Double,
    val OS: String,
    val Browser: String,
    val Device: String,
    val Data: ExtractionData,
    val ImageData: ImageData,
    val Metadata: List<MetadataEntry>
) : Serializable

data class ExtractionData(
    val Address: FieldGroup,
    val BirthDate: FieldGroup,
    val AddressCity: FieldGroup,
    val AddressCountry: FieldGroup,
    val CountryOfIssue: FieldGroup,
    val AddressCounty: FieldGroup,
    val DocumentNumber: FieldGroup,
    val ExpiryDate: FieldGroup,
    val IssuedDate: FieldGroup,
    val IssuingAuthority: FieldGroup,
    val Name: FieldGroup,
    val Nationality: FieldGroup,
    val PersonalIdentificationNumber: FieldGroup,
    val PlaceOfBirth: FieldGroup,
    val PlaceOfIssue: FieldGroup,
    val AddressZip: FieldGroup,
    val Gender: FieldGroup,
    val AddressStreet: FieldGroup,
    val Surname: FieldGroup,
    val MothersGivenName: FieldGroup,
    val MothersFamilyName: FieldGroup

): Serializable

data class FieldGroup(
    val Read: Boolean,
    val Validated: Boolean,
    val RecommendedValue: String?,
    val OCR: SourceData,
    val MRZ: SourceData
): Serializable

data class SourceData(
    val Read: Boolean,
    val Validated: Boolean,
    val Value: String?
): Serializable

data class ImageData(
    val Documents: List<String>,
    val FaceImage: String,
    val Signature: String
): Serializable

data class MetadataEntry(
    val Country: String,
    val DocumentSide: String,
    val DocumentType: String,
    val DocumentSeries: String
): Serializable
