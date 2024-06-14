package com.me.naturelens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.me.naturelens.fragments.ExploreFragment
import com.me.naturelens.fragments.FailedFragment
import com.me.naturelens.fragments.HomeFragment
import com.me.naturelens.fragments.SavedFragment
import com.me.naturelens.fragments.SettingFragment
import com.me.naturelens.fragments.SuccessFragment
import com.me.naturelens.fragments.SuccessInterface
import com.me.naturelens.utils.ImageClassifierHelper
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class MainActivity : BaseActivity() , SuccessInterface {
    enum class MediaType {
        IMAGE, VIDEO, UNKNOWN
    }
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var backgroundExecutor: ScheduledExecutorService
    private val getContent =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            // Handle the returned Uri
            uri?.let { mediaUri ->
                when (loadMediaType(mediaUri)) {
                    MediaType.IMAGE -> {
                        runClassificationOnImage(mediaUri)
                    }

                    MediaType.UNKNOWN, MediaType.VIDEO -> {
                        Toast.makeText(
                            this, "Unsupported data type.", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    private fun loadMediaType(uri: Uri): MediaType {
        val mimeType = this.contentResolver?.getType(uri)
        mimeType?.let {
            if (mimeType.startsWith("image")) return MediaType.IMAGE
            if (mimeType.startsWith("video")) return MediaType.VIDEO
        }

        return MediaType.UNKNOWN
    }

    private fun runClassificationOnImage(uri: Uri) {
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(
                this.contentResolver, uri
            )
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(
                this.contentResolver, uri
            )
        }.copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->

            backgroundExecutor.execute {

                imageClassifierHelper = ImageClassifierHelper(
                    context = this,
                )
                imageClassifierHelper.classifyImage(bitmap)?.let { resultBundle ->
                    this.runOnUiThread {

                        val r = resultBundle.results.first().classificationResult()
                            .classifications()[0].categories().sortedBy { it.index() }
                        if (r.size <= 0) {
                            //go failPage
                            setFragmentPage(FailedFragment())
                                title = "Scan"
                            findViewById<BottomNavigationView>(R.id.bottom_nav).selectedItemId =
                                R.id.nav_main

                        }

                        if (r.size > 0) {
                            //go to succeededPage
                            setFragmentPage(
                                fragment = SuccessFragment(),
                                bitmap = bitmap,
                                nameplant = r.first().categoryName().toString()
                            )
                            title = "Scan"
                            findViewById<BottomNavigationView>(R.id.bottom_nav).selectedItemId =
                                R.id.nav_main
                        }
                    }
                } ?: run {
                    Log.e("GalleryFragment", "Error running image classification.")
                }

                imageClassifierHelper.clearImageClassifier()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setLogo(R.drawable.test2)


        //scan nav_btn
        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            getContent.launch(arrayOf("image/*", "video/*"))
        }

        //Navbar
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> {
                    setFragmentPage(HomeFragment())
                    title = getString(R.string.menu_home)
                }

                R.id.ic_explor -> {
                    setFragmentPage(ExploreFragment())
                    title = getString(R.string.menu_explore)
                }

                R.id.ic_bookmark -> {
                    setFragmentPage(SavedFragment())
                    title = getString(R.string.menu_saved)
                }

                R.id.ic_settings -> {
                    setFragmentPage(SettingFragment())
                    title = getString(R.string.menu_settings)
                }
            }
            true
        }
        title = getString(R.string.menu_home)
        bottomNav.selectedItemId = R.id.ic_home
    }
    private val BACK_STACK_ROOT_TAG = "root_fragment"

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0 &&
            findViewById<BottomNavigationView>(R.id.bottom_nav).selectedItemId != R.id.ic_home
            ) {
            supportFragmentManager.popBackStackImmediate()
            title = getString(R.string.menu_home)
            findViewById<BottomNavigationView>(R.id.bottom_nav).selectedItemId = R.id.ic_home
        } else {
            super.onBackPressed()
        }
    }
    fun setFragmentPage(
        fragment: Fragment,
        bitmap: Bitmap? = null,
        nameplant: String? = null,
        exploreFSearch: Boolean? = null
    ) {
       if (fragment is SuccessFragment) {
            val stream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            val bundle = Bundle()
            bundle.putByteArray("image", byteArray)
            bundle.putString("mText", nameplant)
            fragment.arguments = bundle
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_wrapper, fragment)
                .addToBackStack(null)
                .commit()
        } else if (fragment is ExploreFragment && exploreFSearch == true) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            val bundle = Bundle()
            bundle.putString("mText", nameplant)
            fragment.arguments = bundle
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_wrapper, fragment)
                .addToBackStack(null)
                .commit()
            findViewById<BottomNavigationView>(R.id.bottom_nav).selectedItemId = R.id.nav_main
        }else if(fragment is HomeFragment) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_wrapper, fragment, BACK_STACK_ROOT_TAG)
                .commit()
        } else {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_wrapper, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
    override fun callSetFragmentPage(myString: String) {
        setFragmentPage(ExploreFragment(), nameplant = myString, exploreFSearch = true)
    }
}