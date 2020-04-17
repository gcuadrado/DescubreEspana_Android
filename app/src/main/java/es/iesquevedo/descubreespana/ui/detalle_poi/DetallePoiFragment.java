package es.iesquevedo.descubreespana.ui.detalle_poi;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.iesquevedo.descubreespana.R;
import es.iesquevedo.descubreespana.asynctask.AceptarPoiTask;
import es.iesquevedo.descubreespana.asynctask.EliminarPoiTask;
import es.iesquevedo.descubreespana.asynctask.GetPoiDetalleTask;
import es.iesquevedo.descubreespana.databinding.DetallePoiFragmentBinding;
import es.iesquevedo.descubreespana.modelo.ApiError;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetDetalle;
import es.iesquevedo.descubreespana.modelo.dto.PuntoInteresDtoGetMaestro;
import es.iesquevedo.descubreespana.modelo.dto.UsuarioDtoGet;
import es.iesquevedo.descubreespana.servicios.ServiciosPuntoInteres;
import es.iesquevedo.descubreespana.utils.Constantes;
import es.iesquevedo.descubreespana.utils.GetSharedPreferences;
import es.iesquevedo.descubreespana.utils.VPGaleriaAdapter;
import es.iesquevedo.descubreespana.utils.ValoracionAdapter;
import io.vavr.control.Either;

public class DetallePoiFragment extends Fragment {

    private DetallePoiViewModel detallePoiViewModel;
    private DetallePoiFragmentBinding binding;
    private PuntoInteresDtoGetMaestro poiMaestro;
    private ServiciosPuntoInteres serviciosPuntoInteres;
    private NavController navController;
    private boolean mostrarInfoPulsado;
    private PuntoInteresDtoGetDetalle poiDetalle;
    private GetPoiDetalleTask getPoiDetalleTask;

    public static DetallePoiFragment newInstance() {
        return new DetallePoiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Seleccionar el item en la barra de navigación inferior cada vez que se pone este fragment
        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.nav_view);
        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);

        binding = DetallePoiFragmentBinding.inflate(inflater, container, false);
        detallePoiViewModel = new ViewModelProvider(this).get(DetallePoiViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        poiMaestro = DetallePoiFragmentArgs.fromBundle(getArguments()).getPuntoInteres();
        serviciosPuntoInteres = new ServiciosPuntoInteres();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        getPoiDetalleTask = getPoiAsyncTask();

        UsuarioDtoGet currentUser = GetSharedPreferences.getInstance().getCurrentUser(requireContext());
        if (currentUser != null && currentUser.getTipoUsuario() == Constantes.ADMIN) {
            binding.linearAceptarEliminar.setVisibility(View.VISIBLE);
            binding.btEditPoi.setVisibility(View.VISIBLE);
        }

        setListeners();


        getPoiDetalleTask.execute(poiMaestro.getIdPuntoInteres());
    }

    private GetPoiDetalleTask getPoiAsyncTask() {
        //Creamos el Alert dialog que mostrará el cartel de cargando
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        AlertDialog dialog = builder.create();

        return new GetPoiDetalleTask(serviciosPuntoInteres) {
            @Override
            protected void onPreExecute() {
                dialog.show();
            }

            @Override
            protected void onPostExecute(Either<ApiError, PuntoInteresDtoGetDetalle> result) {
                dialog.dismiss();
                if (result.isRight()) {
                    poiDetalle = result.get();

                    binding.detallesInfobasica.setText(poiDetalle.getInfoDetallada());
                    binding.detallesFecha.setText(poiDetalle.getFechaInicio());
                    binding.detallesCategoria.setText(poiDetalle.getCategoria());
                    binding.detallesDireccion.setText(poiDetalle.getDireccion());
                    binding.detallesAccesibilidad.setChecked(poiDetalle.getAccesibilidad());
                    binding.detallesHorario.setText(poiDetalle.getHorario());
                    binding.detallesCoste.setText(poiDetalle.getCoste().toString());
                    binding.viewPagerGaleria.setAdapter(new VPGaleriaAdapter(poiDetalle));
                    binding.indicator.setViewPager(binding.viewPagerGaleria);
                    if (poiDetalle.getPuntuacion() != null) {
                        binding.detallesPuntuacion.setText(poiDetalle.getPuntuacion().toString());
                        binding.rbValoracion.setRating(poiDetalle.getPuntuacion().floatValue());
                    }

                    binding.recyclerValoraciones.setAdapter(new ValoracionAdapter(poiDetalle.getValoraciones(), requireContext()));

                    //En caso de que haya más de una valoración, fijamos la altura del RecyclerView a 250dp, si solo hay una lo dejamos en wrap_content
                    if (poiDetalle.getValoraciones().size() > 1) {
                        binding.recyclerValoraciones.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, requireContext().getResources().getDisplayMetrics());
                    } else {
                        binding.recyclerValoraciones.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }

                } else {
                    Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_LONG).show();
                    navController.navigate(R.id.navigation_home);
                }
            }
        };
    }

    private void setListeners() {
        mostrarInfoPulsado = false;
        binding.btValoraciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(poiDetalle!=null) {
                    navController.navigate(DetallePoiFragmentDirections.actionDetallePoiToValoracionFragment(poiDetalle));
                }
            }
        });

        binding.mostrarMasInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mostrarInfoPulsado) {
                    mostrarInfoPulsado = true;
                    binding.mostrarMasInfo.setText(R.string.boton_menos_info);
                    binding.detallesInfobasica.setMaxLines(Integer.MAX_VALUE);
                } else {
                    mostrarInfoPulsado = false;
                    binding.mostrarMasInfo.setText(R.string.boton_mas_info);
                    binding.detallesInfobasica.setMaxLines(4);
                }
            }
        });
        if (!poiMaestro.getActivado()) {
            binding.btAceptar.setVisibility(View.VISIBLE);
            binding.btAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AceptarPoiTask(serviciosPuntoInteres) {
                        @Override
                        protected void onPostExecute(Either<ApiError, String> result) {
                            if (result.isRight()) {
                                binding.linearAceptarEliminar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }.execute(poiMaestro.getIdPuntoInteres());
                }
            });
        }
        binding.btEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EliminarPoiTask(serviciosPuntoInteres) {
                    @Override
                    protected void onPostExecute(Either<ApiError, String> result) {
                        if (result.isRight()) {
                            navController.popBackStack();
                        } else {
                            Toast.makeText(requireContext(), result.getLeft().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute(poiMaestro.getIdPuntoInteres());
            }
        });
        binding.btEditPoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (poiDetalle != null) {
                    navController.navigate(DetallePoiFragmentDirections.actionDetallePoiToEdicionPoiFragment(poiDetalle));
                }
            }
        });
    }


}
