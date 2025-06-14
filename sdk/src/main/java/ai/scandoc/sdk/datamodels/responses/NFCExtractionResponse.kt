package ai.scandoc.sdk.datamodels.responses

data class NFCExtractionResponse(
    val documentNumber: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val birthDate: String? = null,
    val fullBirthDate: String? = null,
    val expiryDate: String? = null,
    val personalNumber: String? = null,
    val gender: String? = null,
    val nationality: String? = null,
    val permanentAddress: String? = null,
    val telephone: String? = null,
    val title: String? = null,
    val profession: String? = null,
    val custodyInformation: String? = null,
    val nameOfHolder: String? = null,
    val personalSummary: String? = null,
    val issuedDate: String? = null,
    val issuingAuthority: String? = null,
) {
    constructor(data: Map<String, String>) : this(
        documentNumber = data["DocumentNumber"],
        name = data["Name"],
        surname = data["Surname"],
        birthDate = data["BirthDate"],
        fullBirthDate = data["FullBirthDate"],
        expiryDate = data["ExpiryDate"],
        personalNumber = data["PersonalNumber"],
        gender = data["Gender"],
        nationality = data["Nationality"],
        permanentAddress = data["PermanentAddress"],
        telephone = data["Telephone"],
        title = data["Title"],
        profession = data["Profession"],
        custodyInformation = data["CustodyInformation"],
        nameOfHolder = data["NameOfHolder"],
        personalSummary = data["PersonalSummary"],
        issuedDate = data["IssuedDate"],
        issuingAuthority = data["IssuingAuthority"]
    )
}