package com.pierredlvm.todo.user

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.google.modernstorage.mediastore.FileType
import com.google.modernstorage.mediastore.MediaStoreRepository
import com.google.modernstorage.mediastore.SharedPrimary
import com.pierredlvm.todo.R
import com.pierredlvm.todo.databinding.FragmentUserInfoBinding
import com.pierredlvm.todo.network.Api
import com.pierredlvm.todo.tasklist.TasksViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*


class UserInfoFragment : Fragment() {
    private val viewModel: UserInfoViewModel by viewModels()

    private lateinit var binding: FragmentUserInfoBinding;
    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ accepted ->
            if (accepted)
                launchCameraWithPermission()
                // lancer l'action souhaitée
            else
                showExplanation()
                // afficher une explication
        }

   val mediaStore by lazy { MediaStoreRepository(Api.appContext) }

    // stocker une uri pour sauvegarder l'image:
    private lateinit var photoUri: Uri

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { accepted ->
        val view = binding.root;// n'importe quelle vue (ex: un bouton, binding.root, window.decorView, ...)
            if (accepted) handleImage()
            else Snackbar.make(view, "Échec!", Snackbar.LENGTH_LONG).show()
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {uri ->
        if(uri!=null)
        {
            photoUri = uri;
        }
        handleImage();

    }

    private fun launchCameraWithPermission() {

        val camPermission = Manifest.permission.CAMERA
        val permissionStatus = Api.appContext.checkSelfPermission(camPermission);
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        when {
            isAlreadyAccepted -> launchCamera();// lancer l'action souhaitée
            isExplanationNeeded -> showExplanation()// afficher une explication
            else -> launchAppSettings()// lancer la demande de permission
        }
    }

    private fun showExplanation() {
        // ici on construit une pop-up système (Dialog) pour expliquer la nécessité de la demande de permission
        AlertDialog.Builder(Api.appContext)
            .setMessage("🥺 On a besoin de la caméra, vraiment! 👉👈")
            .setPositiveButton("Bon, ok") { _, _ -> /* ouvrir les paramètres de l'app */ }
            .setNegativeButton("Nope") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun launchAppSettings() {
        // Cet intent permet d'ouvrir les paramètres de l'app (pour modifier les permissions déjà refusées par ex)
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", Api.appContext.packageName, null)
        )
        // ici pas besoin de vérifier avant car on vise un écran système:
        startActivity(intent)
    }

    private fun handleImage() {
        // afficher l'image dans l'ImageView
        binding.imageView.load(photoUri);
        lifecycleScope.launchWhenStarted {
            Api.userWebService.updateAvatar(convert(photoUri));
        }

    }

    private fun launchCamera() {
        // à compléter à l'étape suivante

        cameraLauncher.launch(photoUri);
    }

    private fun launchGallery()
    {
        galleryLauncher.launch("image/*")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding = FragmentUserInfoBinding.inflate(layoutInflater);

        if(mediaStore.canReadSharedEntries()) {
            Log.e("error media storage","Read an image created by another app")
        }

        val view = binding.root;


        var camButton = binding.takePictureButton;
        camButton.setOnClickListener {
            launchCameraWithPermission()
        }
        binding.uploadImageButton.setOnClickListener {
            launchGallery()
        }
        // initialiser l'uri dans onCreate:
        lifecycleScope.launchWhenStarted {
            photoUri = mediaStore.createMediaUri(
                filename = "picture-${UUID.randomUUID()}.jpg",
                type = FileType.IMAGE,
                location = SharedPrimary
            ).getOrThrow()

            viewModel.loadInfo()



        }

        lifecycleScope.launch() {
            viewModel.userInfo.collectLatest  { newInfo ->
                binding.imageView.load(viewModel.userInfo.value.avatar)

                binding.email.setText(newInfo.email)
                binding.firstName.setText(newInfo.firstName)
                binding.lastName.setText(newInfo.lastName)

            }


        }

        return view;

    }

    private fun convert(uri: Uri): MultipartBody.Part {

        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = Api.appContext.contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()

        )
    }

    override fun onPause() {
        super.onPause()
        val newInfo = UserInfo(binding.email.text.toString(),binding.firstName.text.toString(),binding.lastName.text.toString(),photoUri.toString())
        viewModel.updateInfo(newInfo);
        //handleImage()

    }
}