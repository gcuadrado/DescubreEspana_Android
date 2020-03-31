package es.iesquevedo.descubreespana.ui.nuevopunto;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import java.util.List;

import es.iesquevedo.descubreespana.databinding.NuevoPuntoFragmentBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import es.iesquevedo.descubreespana.utils.FotosAdapter;
import io.vavr.control.Either;

public class NuevoPuntoFragment extends Fragment {

    private NuevoPuntoViewModel nuevoPuntoViewModel;
    private NuevoPuntoFragmentBinding binding;
    private PuntoInteresDtoGetDetalle nuevoPuntoInteres;
    private List<Image> images;
    private ServiciosPuntoInteres serviciosPuntoInteres;

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
        serviciosPuntoInteres=new ServiciosPuntoInteres();
        binding.btnImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ImagePicker.create(NuevoPuntoFragment.this)
                            .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
                            .folderMode(true) // folder mode (false by default)
                            .toolbarFolderTitle("Directorios") // folder selection title
                            .toolbarImageTitle("Toca para seleccionar") // image selection title
                            .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                            .multi() // multi mode (default mode)
                            .limit(5) // max images can be selected (99 by default)
                            .showCamera(true) // show camera or not (true by default)
                            .imageDirectory("Cámara") // directory name for captured image  ("Camera" folder by default)
                            .start();
                } catch (Exception e) {
                    Log.d("descubreespana", null, e);
                }
            }
        });

        binding.btEnviarPunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevoPuntoInteres.setNombre(binding.etNombre.getText().toString());
                nuevoPuntoInteres.setInfoDetallada(binding.etInformacion.getText().toString());
                nuevoPuntoInteres.setResumen(binding.etInformacion.getText().toString());
                nuevoPuntoInteres.setFechaInicio(binding.etFecha.getText().toString());
                nuevoPuntoInteres.setCategoria(binding.spinnerCategoria.getSelectedItem().toString());
                nuevoPuntoInteres.setDireccion(binding.etDireccion.getText().toString());
                nuevoPuntoInteres.setContacto(binding.etContacto.getText().toString());
                nuevoPuntoInteres.setCoste(Double.parseDouble(binding.etCoste.getText().toString()));
                nuevoPuntoInteres.setEnlaceInfo(binding.etEnlace.getText().toString());
                nuevoPuntoInteres.setAccesibilidad(binding.cbAccesibilidad.isChecked());
                nuevoPuntoInteres.setHorario(binding.etHorario.getText().toString());

                new AddPoi().execute();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
             images = ImagePicker.getImages(data);

            if (!images.isEmpty()) {
                binding.recyclerImagenes.setAdapter(new FotosAdapter(images,nuevoPuntoViewModel));

                nuevoPuntoViewModel.getmImagenPrincipal().observe(getViewLifecycleOwner(), new Observer<Image>() {
                    @Override
                    public void onChanged(@Nullable Image image) {
                        Glide.with(requireContext()).load(image.getPath()).into(binding.imageViewPrincipal);
                        //binding.imageViewPrincipal.setTag(image);
                    }
                });
                binding.tvImagenPrincipal.setVisibility(View.VISIBLE);
                binding.imageViewPrincipal.setVisibility(View.VISIBLE);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class AddPoi extends AsyncTask<Void,Void, Either<ApiError,PuntoInteresDtoGetDetalle>>{

        @Override
        protected Either<ApiError, PuntoInteresDtoGetDetalle> doInBackground(Void... voids) {
            return serviciosPuntoInteres.addPoi(nuevoPuntoInteres,images);
        }
    }
}
