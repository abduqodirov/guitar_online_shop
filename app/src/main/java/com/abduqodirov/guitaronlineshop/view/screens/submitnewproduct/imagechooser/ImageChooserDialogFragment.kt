package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.imagechooser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abduqodirov.guitaronlineshop.databinding.DialogImageChooserBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ImageChooserDialogFragment(private val listener: ImageSourceCallback) : BottomSheetDialogFragment() {

    private var _binding: DialogImageChooserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogImageChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chooserCameraBtn.setOnClickListener {
            listener.onSourceSelected(ImageSource.CAMERA)
            dismiss()
        }

        binding.chooserGalleryBtn.setOnClickListener {
            listener.onSourceSelected(ImageSource.GALLERY)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(imageChooserCallback: ImageSourceCallback): ImageChooserDialogFragment {
            return ImageChooserDialogFragment(imageChooserCallback)
        }
    }
}
