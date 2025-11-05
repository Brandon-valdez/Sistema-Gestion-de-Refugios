package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Donaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DonacionesRepository extends JpaRepository<Donaciones, Integer> {

  @Query("select d from Donaciones d where d.estadoRegistro = :status")
  public List<Donaciones> getDonacionesByStatus(@Param("status") Boolean status);

  @Query("select d from Donaciones d where d.refugio.id = :refugioId and d.estadoRegistro = true")
  public List<Donaciones> getDonacionesByRefugio(@Param("refugioId") Integer refugioId);

  @Query("select d from Donaciones d where d.donador.id = :donadorId and d.estadoRegistro = true")
  public List<Donaciones> getDonacionesByDonador(@Param("donadorId") Integer donadorId);

  @Query("select d from Donaciones d where d.tipo.id = :tipoId and d.estadoRegistro = true")
  public List<Donaciones> getDonacionesByTipo(@Param("tipoId") Integer tipoId);

  @Query("select d from Donaciones d where d.fecha between :fechaInicio and :fechaFin and d.estadoRegistro = true")
  public List<Donaciones> getDonacionesByFechaRange(@Param("fechaInicio") LocalDate fechaInicio,
      @Param("fechaFin") LocalDate fechaFin);

  @Query("select d from Donaciones d where d.monto >= :montoMinimo and d.estadoRegistro = true")
  public List<Donaciones> getDonacionesByMontoMinimo(@Param("montoMinimo") BigDecimal montoMinimo);

  @Query("select d from Donaciones d where d.recibidoPor.id = :usuarioId and d.estadoRegistro = true")
  public List<Donaciones> getDonacionesByRecibidoPor(@Param("usuarioId") Integer usuarioId);

  @Query("select d from Donaciones d where d.fecha = :fecha and d.estadoRegistro = true")
  public List<Donaciones> getDonacionesByFecha(@Param("fecha") LocalDate fecha);
}
