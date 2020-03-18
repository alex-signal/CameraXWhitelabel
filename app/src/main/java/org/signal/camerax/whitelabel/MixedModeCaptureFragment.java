package org.signal.camerax.whitelabel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.view.CameraView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.util.Executors;

import java.io.File;
import java.util.Locale;

public class MixedModeCaptureFragment extends Fragment {

  private static final short TAKE_PICTURE_PERMISSION_REQUEST = 17363;

  private CameraView cameraView;

  @Override
  public @Nullable View onCreateView(@NonNull LayoutInflater inflater,
                                     @Nullable ViewGroup container,
                                     @Nullable Bundle savedInstanceState)
  {
    return inflater.inflate(R.layout.mixed_mode_capture_fragment, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    cameraView = view.findViewById(R.id.camera);

    View takePhoto  = view.findViewById(R.id.take_photo);
    View swapCamera = view.findViewById(R.id.swap_camera);

    takePhoto.setOnClickListener(v -> {
      final File photo = new File(requireContext().getCacheDir(), String.format(Locale.ENGLISH, "photo-%d.jpg", System.currentTimeMillis()));
      cameraView.takePicture(photo, Executors.mainThreadExecutor(), new ImageCapture.OnImageSavedCallback() {
        @Override
        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
          NavDirections directions = MixedModeCaptureFragmentDirections.actionMixedModeCaptureFragmentToImageViewerFragment(Uri.fromFile(photo));

          Navigation.findNavController(v).navigate(directions);
        }

        @Override
        public void onError(@NonNull ImageCaptureException exception) {
          Toast.makeText(requireContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }
      });
    });

    swapCamera.setOnClickListener(v -> cameraView.toggleCamera());

    if (hasCameraPermission()) {
      cameraView.bindToLifecycle(getViewLifecycleOwner());
    } else {
      requestPermissions(new String[]{Manifest.permission.CAMERA}, TAKE_PICTURE_PERMISSION_REQUEST);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == TAKE_PICTURE_PERMISSION_REQUEST) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        cameraView.bindToLifecycle(getViewLifecycleOwner());
      } else {
        Toast.makeText(requireActivity(), "Camera Permission Required", Toast.LENGTH_LONG).show();
        requireActivity().finish();
      }
    }
  }

  private boolean hasCameraPermission() {
    return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
  }
}
