package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Donaciones;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IDonacionesService {

  public List<Donaciones> getDonaciones();

  public Optional<Donaciones> getDonacionById(Integer id);

  public int saveDonacion(Donaciones donacion);

  public int updateDonacion(Donaciones donacion);

  public void deleteDonacion(Integer id);

  public List<Donaciones> getDonacionesByStatus(Boolean status);

  public List<Donaciones> getDonacionesByRefugio(Integer refugioId);

  public List<Donaciones> getDonacionesByDonador(Integer donadorId);

  public List<Donaciones> getDonacionesByTipo(Integer tipoId);

  public List<Donaciones> getDonacionesByFechaRange(LocalDate fechaInicio, LocalDate fechaFin);

  public List<Donaciones> getDonacionesByMontoMinimo(BigDecimal montoMinimo);

  public List<Donaciones> getDonacionesByRecibidoPor(Integer usuarioId);

  public List<Donaciones> getDonacionesByFecha(LocalDate fecha);
}