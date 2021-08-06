package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.network.SENDING_IMAGE_QUALITY
import com.abduqodirov.guitaronlineshop.databinding.FragmentSubmitNewProductBinding
import com.abduqodirov.guitaronlineshop.view.ShopApplication
import com.abduqodirov.guitaronlineshop.view.model.ProductForSendingScreen
import com.abduqodirov.guitaronlineshop.view.model.Validation
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.adapters.ImageChooserAdapter
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.imagechooser.ImageChooserDialogFragment
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.imagechooser.ImageSource
import com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.imagechooser.ImageSourceCallback
import com.abduqodirov.guitaronlineshop.view.util.PROVIDER_AUTHORITY_PRODUCTS
import com.abduqodirov.guitaronlineshop.view.util.setErrorTextResId
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

const val EDITTEXT_NAME_POSITION = 0
const val EDITTEXT_PRICE_POSITION = 1
const val EDITTEXT_DESC_POSITION = 2

private const val REQUEST_CODE_CAMERA_IMAGE = 101
private const val REQUEST_CODE_PICK_IMAGE = 202

class SubmitNewProductFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentSubmitNewProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SubmitProductViewModel> { viewModelFactory }

    private var bitmapOptions: BitmapFactory.Options? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as ShopApplication).appComponent.submitComponent().create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubmitNewProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewClickListeners()

        setupImageChooser()

        setUpFormValidators()

        observeSendingProductStatusAndData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Saves file to storage instead of storing bitmaps in RAM to use RAM efficiently
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                if (data == null || data.data == null) {
                    showMessageWithSnackBar(getString(R.string.failed_to_get_image))
                    return
                }
                val imgUri: Uri = data.data!!

                val inputStream = requireActivity().contentResolver.openInputStream(imgUri)
                val largeBitmap = BitmapFactory.decodeStream(inputStream)

                viewModel.currentFile?.also {
                    try {
                        FileOutputStream(it).use { out ->
                            largeBitmap.compress(
                                Bitmap.CompressFormat.JPEG,
                                SENDING_IMAGE_QUALITY,
                                out
                            )
                        }
                    } catch (e: IOException) {
                        showMessageWithSnackBar(getString(R.string.failed_to_get_image))
                        e.printStackTrace()
                    }
                }
            }

            if (requestCode == REQUEST_CODE_PICK_IMAGE || requestCode == REQUEST_CODE_CAMERA_IMAGE) {
                val thumbnailBitmap = decodeAndDownscale(viewModel.currentPhotoPath)

                // If bitmap is null, so it will not add bitmap to the ViewModel, so the bitmap won't be sent,
                // and won't be displayed in the Recyclerview
                if (thumbnailBitmap != null) {
                    viewModel.addImage(thumbnailBitmap)
                }
            }
        }
    }

    private fun observeSendingProductStatusAndData() {
        viewModel.sentProduct.observe(
            viewLifecycleOwner,
            {
                it.let { response ->

                    when (response) {

                        is Response.Loading -> {
                            switchUIToLoadingState()
                        }

                        is Response.Success -> {
                            binding.run {
                                submitProductProgressBar.hide()
                                submitProductMessageTxt.text =
                                    getString(R.string.successfully_uploaded)
                                submitProductsProductDetailsBtn.visibility = View.VISIBLE
                            }

                            setUpSuccessfullyUploadedButtonListener(response.data)
                            viewModel.clearStorage()
                        }

                        is Response.Failure -> {
                            switchUIToErrorState(response.errorMessage)
                            viewModel.clearStorage()
                        }
                    }
                }
            }
        )
    }

    private fun addImage() {

        val photoFile: File? = try {
            createImageFile()
        } catch (e: IOException) {
            showMessageWithSnackBar(e.localizedMessage ?: getString(R.string.failed_to_get_image))
            null
        }

        viewModel.currentFile = photoFile

        ImageChooserDialogFragment.newInstance(
            ImageSourceCallback {
                when (it) {
                    ImageSource.GALLERY -> chooseImageFromGallery()
                    ImageSource.CAMERA -> takeImageWithCamera(photoFile)
                }
            }
        ).show(requireActivity().supportFragmentManager, "source_chooser")
    }

    private fun chooseImageFromGallery() {
        val getImage = Intent(Intent.ACTION_GET_CONTENT)
        getImage.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getImage, getString(R.string.select_image))
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, REQUEST_CODE_PICK_IMAGE)
    }

    private fun takeImageWithCamera(photoFile: File?) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        photoFile?.also {
            val photoUri: Uri = FileProvider.getUriForFile(
                requireContext(),
                PROVIDER_AUTHORITY_PRODUCTS,
                it
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            try {
                startActivityForResult(intent, REQUEST_CODE_CAMERA_IMAGE)
            } catch (e: ActivityNotFoundException) {
                showMessageWithSnackBar(getString(R.string.no_camera_app))
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(
            "Product_$timeStamp",
            ".jpg",
            storageDir
        ).apply {
            viewModel.currentPhotoPath = absolutePath
        }
        return file
    }

    private fun setupImageChooser() {

        val imagesAdapter = ImageChooserAdapter(
            ImageChooserAdapter.ImageRemoveCallback {
                viewModel.removeImage(it.id)
            }
        )

        binding.submitImagesReycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.submitImagesReycler.adapter = imagesAdapter
        binding.submitImagesReycler.setHasFixedSize(true)

        viewModel.submittingImages.observe(
            viewLifecycleOwner,
            Observer {
                imagesAdapter.submitList(it)
            }
        )
    }

    private fun setupBitmapOptions(): BitmapFactory.Options {
        val targetH: Int = binding.submitImagesReycler.height
        val targetW: Int = binding.submitImagesReycler.height

        val options = BitmapFactory.Options().apply {

            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }

        bitmapOptions = options
        return options
    }

    private fun decodeAndDownscale(photoPath: String): Bitmap? {

        val options: BitmapFactory.Options = bitmapOptions ?: setupBitmapOptions()

        var bitmap: Bitmap? = null
        BitmapFactory.decodeFile(photoPath, options)?.also {
            bitmap = it
        }
        return bitmap
    }

    private fun setUpSuccessfullyUploadedButtonListener(product: FetchingProductDTO) {
        binding.submitProductsProductDetailsBtn.setOnClickListener {

            navigateToProductDetailsScreen(product)
        }
    }

    private fun setUpFormValidators() {

        // TODO add price validation max integer

        binding.run {

            viewModel.formInputsValidation.observe(
                viewLifecycleOwner,
                Observer {
                    manageButtonVisibilityWithValidation(it)
                    inputLayoutSubmitName.setErrorTextResId(it[EDITTEXT_NAME_POSITION])
                    inputLayoutSubmitPrice.setErrorTextResId(it[EDITTEXT_PRICE_POSITION])
                    inputLayoutSubmitDesc.setErrorTextResId(it[EDITTEXT_DESC_POSITION])
                }
            )

            submitProductNameEdt.addTextChangedListener {
                viewModel.validateEditText(EDITTEXT_NAME_POSITION, it.toString())
            }

            submitProductPriceEdt.addTextChangedListener {
                viewModel.validateEditText(EDITTEXT_PRICE_POSITION, it.toString())
            }

            submitProductDescEdt.addTextChangedListener {
                viewModel.validateEditText(EDITTEXT_DESC_POSITION, it.toString())
            }
        }
    }

    private fun manageButtonVisibilityWithValidation(
        it: Array<Validation>
    ): Boolean {
        var isAllFieldsValid = true
        for (validation in it) {
            if (validation.errorResId != null) {
                isAllFieldsValid = false
                break
            }
        }
        binding.submitProductSendBtn.isEnabled = isAllFieldsValid
        return isAllFieldsValid
    }

    private fun setUpViewClickListeners() {

        binding.run {
            submitAddNewImageBtn.setOnClickListener {
                addImage()
            }

            submitProductSendBtn.setOnClickListener {

                viewModel.validateEditText(EDITTEXT_NAME_POSITION, submitProductNameEdt.text.toString())
                viewModel.validateEditText(EDITTEXT_PRICE_POSITION, submitProductPriceEdt.text.toString())
                viewModel.validateEditText(EDITTEXT_DESC_POSITION, submitProductDescEdt.text.toString())

                viewModel.formInputsValidation.value?.let { validationArray ->
                    val isAllFieldsValid = manageButtonVisibilityWithValidation(validationArray)
                    if (!isAllFieldsValid) {
                        return@setOnClickListener
                    }
                }

                val name = submitProductNameEdt.text.toString()
                val price = submitProductPriceEdt.text.toString().toDouble()
                val desc = submitProductDescEdt.text.toString()

                val images = arrayListOf<Bitmap>()

                viewModel.submittingImages.value?.forEach { uploadingImage ->

                    val image = BitmapFactory.decodeFile(uploadingImage.path)

                    if (image != null) {
                        images.add(image)
                    }
                }

                val sendingProduct = ProductForSendingScreen(
                    name = name,
                    price = price,
                    description = desc,
                    photos = images
                )

                viewModel.sendProduct(sendingProduct)
            }
        }
    }

    // TODO error caselarda statis stringni ko'rsatavermasdan, API responseni olish
    private fun switchUIToErrorState(response: String?) {
        binding.run {
            submitProductProgressBar.hide()
            submitProductSendBtn.visibility = View.VISIBLE
            submitProductMessageTxt.text = response
                ?: getString(R.string.error_on_sending_product)
            submitProductMessageTxt.visibility = View.VISIBLE
        }
    }

    private fun switchUIToLoadingState() {
        binding.run {
            submitProductProgressBar.visibility = View.VISIBLE

            submitProductSendBtn.visibility = View.INVISIBLE
            submitProductMessageTxt.visibility = View.INVISIBLE
        }
    }

    private fun showMessageWithSnackBar(text: String) {
        Snackbar.make(
            binding.submitRoot,
            text,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun navigateToProductDetailsScreen(product: FetchingProductDTO) {
        findNavController().navigate(
            SubmitNewProductFragmentDirections.actionSubmitNewProductFragmentToProductDetailsFragment(
                product.id
            )
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = SubmitNewProductFragment()
    }
}
