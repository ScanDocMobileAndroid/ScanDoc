package ai.scandoc.demo

import ai.scandoc.sdk.datamodels.responses.ExtractionResponse
import ai.scandoc.sdk.nfc.ScanDocNFCFragment
import ai.scandoc.sdk.callbacks.NFCResultCallback
import ai.scandoc.sdk.datamodels.responses.NFCExtractionResponse
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

class DisplayDataFragment(
    private val extractionResponse: ExtractionResponse
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_display_data, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.fullNameTextView).text =
            "${extractionResponse.Data.Name.RecommendedValue} ${extractionResponse.Data.Surname.RecommendedValue}"

        view.findViewById<TextView>(R.id.documentNumberTextView).text =
            "Document Number: ${extractionResponse.Data.DocumentNumber.RecommendedValue}"

        view.findViewById<TextView>(R.id.birthDateTextView).text =
            "Birth Date: ${extractionResponse.Data.BirthDate.RecommendedValue}"

        view.findViewById<TextView>(R.id.expiryDateTextView).text =
            "Expiry Date: ${extractionResponse.Data.ExpiryDate.RecommendedValue}"

        // Auto-start NFC fragment if API level is supported
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startNfcFragment()
        } else {
            Toast.makeText(requireContext(), "NFC requires API 26+", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startNfcFragment() {
        val nfcFragment = ScanDocNFCFragment(extractionResponse, object : NFCResultCallback {
            override fun onResult(result: NFCExtractionResponse?, faceImage: String?) {
                Toast.makeText(requireContext(), "NFC read complete", Toast.LENGTH_SHORT).show()
            }

            override fun onException(e: Throwable) {
                Toast.makeText(requireContext(), "NFC failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        })

        parentFragmentManager.commit {
            add(nfcFragment, "ScanDocNFCFragment")
        }
    }
}
