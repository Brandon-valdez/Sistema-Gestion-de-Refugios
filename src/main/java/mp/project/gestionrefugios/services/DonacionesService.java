package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.Donaciones;
import mp.project.gestionrefugios.interfaces.IDonacionesService;
import mp.project.gestionrefugios.repository.DonacionesRepository;
import mp.project.gestionrefugios.repository.DonadoresRepository;
import mp.project.gestionrefugios.repository.RefugiosRepository;
import mp.project.gestionrefugios.repository.TipoDonacionRepository;
import mp.project.gestionrefugios.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DonacionesService implements IDonacionesService {

  @Autowired
  private DonacionesRepository data;

  @Autowired
  private DonadoresRepository donadoresRepository;

  @Autowired
  private RefugiosRepository refugiosRepository;

  @Autowired
  private TipoDonacionRepository tipoDonacionRepository;

  @Autowired
  private UsuariosRepository usuariosRepository;

  @Override
  public List<Donaciones> getDonaciones() {
    return data.findAll();
  }

  @Override
  public Optional<Donaciones> getDonacionById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveDonacion(Donaciones donacion) {
    // Validaciones
    validateDonacion(donacion);

    data.save(donacion);
    return donacion.getId();
  }

  @Override
  public int updateDonacion(Donaciones donacion) {
    // Verificar que la donación existe antes de actualizarla
    if (donacion.getId() != null && data.existsById(donacion.getId())) {
      // Validaciones
      validateDonacion(donacion);

      data.save(donacion);
      return donacion.getId();
    }
    return 0;
  }

  @Override
  public void deleteDonacion(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<Donaciones> donacionOptional = data.findById(id);
    if (donacionOptional.isPresent()) {
      Donaciones donacion = donacionOptional.get();
      donacion.setEstadoRegistro(false);
      data.save(donacion);
    }
  }

  @Override
  public List<Donaciones> getDonacionesByStatus(Boolean status) {
    return data.getDonacionesByStatus(status);
  }

  @Override
  public List<Donaciones> getDonacionesByRefugio(Integer refugioId) {
    return data.getDonacionesByRefugio(refugioId);
  }

  @Override
  public List<Donaciones> getDonacionesByDonador(Integer donadorId) {
    return data.getDonacionesByDonador(donadorId);
  }

  @Override
  public List<Donaciones> getDonacionesByTipo(Integer tipoId) {
    return data.getDonacionesByTipo(tipoId);
  }

  @Override
  public List<Donaciones> getDonacionesByFechaRange(LocalDate fechaInicio, LocalDate fechaFin) {
    return data.getDonacionesByFechaRange(fechaInicio, fechaFin);
  }

  @Override
  public List<Donaciones> getDonacionesByMontoMinimo(BigDecimal montoMinimo) {
    return data.getDonacionesByMontoMinimo(montoMinimo);
  }

  @Override
  public List<Donaciones> getDonacionesByRecibidoPor(Integer usuarioId) {
    return data.getDonacionesByRecibidoPor(usuarioId);
  }

  @Override
  public List<Donaciones> getDonacionesByFecha(LocalDate fecha) {
    return data.getDonacionesByFecha(fecha);
  }

  // Método privado para validaciones
  private void validateDonacion(Donaciones donacion) {
    // Validar que el refugio existe
    if (donacion.getRefugio() == null || donacion.getRefugio().getId() == null) {
      throw new IllegalArgumentException("El refugio es obligatorio");
    }
    if (!refugiosRepository.existsById(donacion.getRefugio().getId())) {
      throw new IllegalArgumentException("El refugio especificado no existe");
    }

    // Validar que el donador existe
    if (donacion.getDonador() == null || donacion.getDonador().getId() == null) {
      throw new IllegalArgumentException("El donador es obligatorio");
    }
    if (!donadoresRepository.existsById(donacion.getDonador().getId())) {
      throw new IllegalArgumentException("El donador especificado no existe");
    }

    // Validar que el tipo de donación existe
    if (donacion.getTipo() == null || donacion.getTipo().getId() == null) {
      throw new IllegalArgumentException("El tipo de donación es obligatorio");
    }
    if (!tipoDonacionRepository.existsById(donacion.getTipo().getId())) {
      throw new IllegalArgumentException("El tipo de donación especificado no existe");
    }

    // Validar que el usuario que recibió existe
    if (donacion.getRecibidoPor() == null || donacion.getRecibidoPor().getId() == null) {
      throw new IllegalArgumentException("El usuario que recibió la donación es obligatorio");
    }
    if (!usuariosRepository.existsById(donacion.getRecibidoPor().getId())) {
      throw new IllegalArgumentException("El usuario especificado no existe");
    }

    // Validar que la fecha no sea nula
    if (donacion.getFecha() == null) {
      throw new IllegalArgumentException("La fecha de la donación es obligatoria");
    }

    // Validar que la fecha no sea futura
    if (donacion.getFecha().isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("La fecha de la donación no puede ser futura");
    }

    // Validar el monto si está presente
    if (donacion.getMonto() != null) {
      if (donacion.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("El monto debe ser mayor que cero");
      }
      if (donacion.getMonto().compareTo(new BigDecimal("99999999.99")) > 0) {
        throw new IllegalArgumentException("El monto no puede exceder 99,999,999.99");
      }
    }

    // Validar la cantidad si está presente
    if (donacion.getCantidad() != null) {
      if (donacion.getCantidad() <= 0) {
        throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
      }
      if (donacion.getCantidad() > 999999) {
        throw new IllegalArgumentException("La cantidad no puede exceder 999,999");
      }
    }

    // Validar que al menos uno de los campos monto o cantidad esté presente
    if (donacion.getMonto() == null && donacion.getCantidad() == null) {
      throw new IllegalArgumentException("Debe especificar al menos el monto o la cantidad de la donación");
    }

    // Validar longitud de observaciones
    if (donacion.getObservaciones() != null && donacion.getObservaciones().length() > 1000) {
      throw new IllegalArgumentException("Las observaciones no pueden exceder los 1000 caracteres");
    }
  }
}