package org.signal.camerax.whitelabel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class ImageViewerFragment extends Fragment {

  @Override
  public @Nullable View onCreateView(@NonNull LayoutInflater inflater,
                                     @Nullable ViewGroup container,
                                     @Nullable Bundle savedInstanceState)
  {
    return inflater.inflate(R.layout.image_viewer_fragment, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    ImageView               target = view.findViewById(R.id.target);
    ImageViewerFragmentArgs args   = ImageViewerFragmentArgs.fromBundle(requireArguments());

    Glide.with(view).load(args.getPath()).into(target);
  }
}
