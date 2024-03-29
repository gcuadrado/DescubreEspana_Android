package es.iesquevedo.descubreespana.ui.editar_fotos_poi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.asynctask.InsertFotosTask;
import es.iesquevedo.descubreespana.databinding.EdicionFotosFragmentBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.FotoPuntoInteresDtoGet;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.servicios.ServiciosFotos;
import es.iesquevedo.descubreespana.ui.edicion_poi.EdicionPoiFragmentArgs;
import es.iesquevedo.descubreespana.utils.EdicionFotosAdapter;
import es.iesquevedo.descubreespana.utils.FotosAdapter;
import io.vavr.control.Either;

public class EdicionFotosFragment extends Fragment {

    private EdicionFotosViewModel edicionFotosViewModel;
    private EdicionFotosFragmentBinding binding;
    private List<Image> images;
    private PuntoInteresDtoGetDetalle poi;
    private ServiciosFotos serviciosFotos;
    private AlertDialog dialog;

    public static EdicionFotosFragment newInstance() {
        return new EdicionFotosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= EdicionFotosFragmentBinding.inflate(inflater,container,false);
        edicionFotosViewModel = new ViewModelProvider(requireActivity()).get(EdicionFotosViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inicializarViews();
        setListeners();
    }

    private void inicializarViews() {
        serviciosFotos=new ServiciosFotos();
        poi= EdicionPoiFragmentArgs.fromBundle(getArguments()).getPoi();
        binding.recyclerFotosActuales.setAdapter(new EdicionFotosAdapter(poi));
        dialog=new AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setView(R.layout.layout_loading_dialog)
                .create();
    }

    private void setListeners() {
        binding.btExplorarAlmacenamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ImagePicker.create(EdicionFotosFragment.this)
                            .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
                            .folderMode(true) // folder mode (false by default)
                            .toolbarFolderTitle("Directorios") // folder selection title
                            .toolbarImageTitle("Toca para seleccionar") // image selection title
                            .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                            .multi() // multi mode (default mode)
                            .limit(5) // max images can be selected (99 by default)
                            .showCamera(true) // show camera or not (true by default)
                            .imageDirectory("Cámara") // directory name for captured image  ("Camera" folder by default)
                            .enableLog(true)
                            .start();
                } catch (Exception e) {
                    Log.d("descubreespana", null, e);
                }
            }
        });

        binding.btSubirFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(images!=null && !images.isEmpty() && poi!=null) {
                    dialog.show();
                    getInsertFotosTask().execute();
                }
            }
        });
    }

    @NotNull
    private InsertFotosTask getInsertFotosTask() {
        return new InsertFotosTask(serviciosFotos, poi, images){
            @Override
            protected void onPostExecute(Either<ApiError, List<FotoPuntoInteresDtoGet>> result) {
                dialog.dismiss();
                if(result.isRight()) {
                    //Añadimos las fotos nuevas a la lista de fotos del POI
                    poi.getFotoPuntoInteresByIdPuntoInteres().addAll(result.get());
                    //Notificamos al RecyclerView de que la lista de fotos ha cambiado
                    ((EdicionFotosAdapter) binding.recyclerFotosActuales.getAdapter()).notifyDataSetChanged();
                    //Limpiamos la lista de imagenes recogidas de almacenamiento interno
                    images.clear();
                    //Notificamos al recycler de fotos nuevas que la lista ha cambiado
                    ((FotosAdapter) binding.recyclerFotosNuevas.getAdapter()).notifyDataSetChanged();

                }else{
                    Toast.makeText(requireContext(),result.getLeft().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            images = ImagePicker.getImages(data);

            if (!images.isEmpty()) {
                binding.recyclerFotosNuevas.setAdapter(new FotosAdapter(images));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
