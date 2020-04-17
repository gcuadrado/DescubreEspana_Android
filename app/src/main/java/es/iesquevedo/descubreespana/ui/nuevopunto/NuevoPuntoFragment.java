package es.iesquevedo.descubreespana.ui.nuevopunto;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.asynctask.AddPoiTask;
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
    private Validator validator;
    @NotEmpty(message = "Debes introducir un nombre")
    private EditText etNombre;
    @NotEmpty(message = "Debes introducir una descripción del lugar")
    private EditText etInformacion;
    private EditText etDireccion;
    private EditText etContacto;
    @NotEmpty(message = "Es necesario indicar el coste")
    private EditText etCoste;
    private EditText etEnlace;
    private EditText etFecha;
    @NotEmpty(message = "Establece un horario, si no lo conoces, indícalo")
    private EditText etHorario;
    private NavController navController;
    private AlertDialog dialog;

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
        nuevoPuntoInteres = NuevoPuntoFragmentArgs.fromBundle(getArguments()).getNuevoPunto();
        binding.etDireccion.setText(nuevoPuntoInteres.getDireccion());
        serviciosPuntoInteres = new ServiciosPuntoInteres();
        inicializarViews();
        setListeners();

    }

    private void setListeners() {
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                if (images != null && !images.isEmpty()) {
                    if (nuevoPuntoViewModel.getmImagenPrincipal().getValue() != null) {
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

                        new AddPoiTask(serviciosPuntoInteres, nuevoPuntoInteres, images, nuevoPuntoViewModel.getmImagenPrincipal().getValue()) {
                            @Override
                            protected void onPreExecute() {
                                dialog.show();
                            }

                            @Override
                            protected void onPostExecute(Either<ApiError, PuntoInteresDtoGetDetalle> result) {
                                dialog.dismiss();
                                if (result.isRight()) {
                                    new AlertDialog.Builder(requireContext())
                                            .setCancelable(false)
                                            .setTitle("¡Perfecto!")
                                            .setMessage("Tu solicitud ha sido recogida correctamente. Te enviaremos un email en cuanto la aceptemos")
                                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    navController.navigate(R.id.navigation_home);
                                                }
                                            })
                                            .create().show();
                                } else {
                                    Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.execute();
                    } else {
                        binding.imageViewPrincipal.setBackgroundResource(R.drawable.error_imagen_principal);
                        Toast.makeText(requireContext(), "Selecciona una de tus imágenes como imagen principal", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //Le metemos el background como tag para recuperarlo luego
                    binding.btnImagePicker.setTag(binding.btnImagePicker.getBackground());
                    binding.btnImagePicker.setBackgroundResource(R.drawable.error_imagen_principal);
                    Toast.makeText(requireContext(), "Debes incluir al menos una fotografía", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(requireContext());

                    // Display error messages ;)
                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
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
                validator.validate();
            }
        });


    }

    private void inicializarViews() {
        etNombre = binding.etNombre;
        etInformacion = binding.etInformacion;
        etDireccion = binding.etDireccion;
        etContacto = binding.etContacto;
        etCoste = binding.etCoste;
        etEnlace = binding.etEnlace;
        etFecha = binding.etFecha;
        etHorario = binding.etHorario;
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        validator = new Validator(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        dialog = builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            //Eliminamos el recuadro rojo en caso de que lo tuviera
            if (binding.btnImagePicker.getTag() != null) {
                binding.btnImagePicker.setBackground((Drawable) binding.btnImagePicker.getTag());
            }

            // Get a list of picked images
            images = ImagePicker.getImages(data);

            if (!images.isEmpty()) {
                binding.recyclerImagenes.setAdapter(new FotosAdapter(images, nuevoPuntoViewModel));

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


}
