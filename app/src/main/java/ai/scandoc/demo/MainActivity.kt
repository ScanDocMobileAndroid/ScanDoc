package ai.scandoc.demo

import ai.scandoc.sdk.ScanDocFragment
import ai.scandoc.sdk.ScanDocSDKConfig
import ai.scandoc.sdk.callbacks.ResultCallback
import ai.scandoc.sdk.datamodels.Settings
import ai.scandoc.sdk.datamodels.responses.ExtractionResponse
import ai.scandoc.sdk.nfc.ScanDocNFCFragment
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ai.scandoc.demo.databinding.ActivityMainBinding
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resultCallback = object : ResultCallback {
            override fun onResult(result: ExtractionResponse?) {
                Log.i(TAG, "Extraction success: $result")
                // process the results
                if (result != null) {
                    supportFragmentManager.findFragmentByTag("ScanDocFragment")?.let {
                        supportFragmentManager.beginTransaction().remove(it).commit()
                    }
                    showDisplayDataFragment(result)
                }
            }

            override fun onException(throwable: Throwable) {
                // process the exception
                Log.e(TAG, "Extraction failed", throwable)
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
            userKey = "", // This is your user key
            subClient = "Android",
            requestSettings = settings,
            acceptTermsAndConditions = true
        )

        val scanDocFragment = ScanDocFragment()
        supportFragmentManager.commit {
            replace(R.id.fragment_container_view, scanDocFragment, "ScanDocFragment")
        }
    }

    private fun showDisplayDataFragment(result: ExtractionResponse) {
        val displayFragment = DisplayDataFragment(result)
        supportFragmentManager.commit {
            replace(R.id.fragment_container_view, displayFragment, "DisplayDataFragment")
            addToBackStack(null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val fragment = supportFragmentManager.findFragmentByTag("ScanDocNFCFragment")
        if (fragment is ScanDocNFCFragment && intent != null) {
            fragment.handleIntent(intent)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val current = supportFragmentManager.findFragmentById(R.id.fragment_container_view)
        if (current is DisplayDataFragment) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container_view, ScanDocFragment(), "ScanDocFragment")
            }
        } else {
            super.onBackPressed()
        }
    }
}

