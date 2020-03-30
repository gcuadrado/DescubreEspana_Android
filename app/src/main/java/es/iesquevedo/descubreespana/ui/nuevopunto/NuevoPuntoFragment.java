package es.iesquevedo.descubreespana.ui.nuevopunto;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import java.util.List;

import es.iesquevedo.descubreespana.databinding.NuevoPuntoFragmentBinding;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.utils.FotosAdapter;

public class NuevoPuntoFragment extends Fragment {

    private NuevoPuntoViewModel nuevoPuntoViewModel;
    private NuevoPuntoFragmentBinding binding;
    private PuntoInteresDtoGetDetalle nuevoPuntoInteres;

    public static NuevoPuntoFragment newInstance() {
        return new NuevoPuntoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NuevoPuntoFragmentBinding.inflate(inflater, container, false);
        nuevoPuntoViewModel = new ViewModelProvider(requireActivity()).get(NuevoPuntoViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nuevoPuntoInteres=NuevoPuntoFragmentArgs.fromBundle(getArguments()).getNuevoPunto();
        binding.etDireccion.setText(nuevoPuntoInteres.getDireccion());

        binding.btnImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ImagePicker.create(NuevoPuntoFragment.this)
                            .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
                            .folderMode(true) // folder mode (false by default)
                            .toolbarFolderTitle("Folder") // folder selection title
                            .toolbarImageTitle("Tap to select") // image selection title
                            .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                            .multi() // multi mode (default mode)
                            .limit(5) // max images can be selected (99 by default)
                            .showCamera(true) // show camera or not (true by default)
                            .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                            .start();
                } catch (Exception e) {
                    Log.d("descubreespana", null, e);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            if (!images.isEmpty()) {
                binding.recyclerImagenes.setAdapter(new FotosAdapter(images,nuevoPuntoViewModel));

                nuevoPuntoViewModel.getmImagenPrincipal().observe(getViewLifecycleOwner(), new Observer<Image>() {
                    @Override
                    public void onChanged(@Nullable Image image) {
                        binding.imageViewPrincipal.setImageBitmap(BitmapFactory.decodeFile(image.getPath()));
                        binding.imageViewPrincipal.setTag(image);
                    }
                });
                binding.tvImagenPrincipal.setVisibility(View.VISIBLE);
                binding.imageViewPrincipal.setVisibility(View.VISIBLE);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
