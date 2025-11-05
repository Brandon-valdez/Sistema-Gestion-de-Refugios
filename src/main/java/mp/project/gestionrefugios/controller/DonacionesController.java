package mp.project.gestionrefugios.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.model.Donaciones;
import mp.project.gestionrefugios.services.DonacionesService;

@RestController
@RequestMapping("/donaciones")
@CrossOrigin(origins = "*")
public class DonacionesController {

  @Autowired
  private DonacionesService service;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<Donaciones>> getDonacionesByStatus(@PathVariable("status") Boolean status) {
    try {
      List<Donaciones> donaciones = service.getDonacionesByStatus(status);
      if (donaciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donaciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<Donaciones>> getDonaciones() {
    try {
      List<Donaciones> donaciones = service.getDonaciones();
      if (donaciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donaciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Donaciones> getDonacionById(@PathVariable Integer id) {
    try {
      Optional<Donaciones> donacion = service.getDonacionById(id);
      return donacion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/refugio/{refugioId}")
  public ResponseEntity<List<Donaciones>> getDonacionesByRefugio(@PathVariable Integer refugioId) {
    try {
      List<Donaciones> donaciones = service.getDonacionesByRefugio(refugioId);
      if (donaciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donaciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/donador/{donadorId}")
  public ResponseEntity<List<Donaciones>> getDonacionesByDonador(@PathVariable Integer donadorId) {
    try {
      List<Donaciones> donaciones = service.getDonacionesByDonador(donadorId);
      if (donaciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donaciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/tipo/{tipoId}")
  public ResponseEntity<List<Donaciones>> getDonacionesByTipo(@PathVariable Integer tipoId) {
    try {
      List<Donaciones> donaciones = service.getDonacionesByTipo(tipoId);
      if (donaciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donaciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/fecha/{fecha}")
  public ResponseEntity<List<Donaciones>> getDonacionesByFecha(@PathVariable String fecha) {
    try {
      LocalDate fechaDate = LocalDate.parse(fecha);
      List<Donaciones> donaciones = service.getDonacionesByFecha(fechaDate);
      if (donaciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donaciones);
    } catch (DateTimeParseException e) {
      return ResponseEntity.badRequest().build();
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/fechaRange/{fechaInicio}/{fechaFin}")
  public ResponseEntity<List<Donaciones>> getDonacionesByFechaRange(@PathVariable String fechaInicio,
      @PathVariable String fechaFin) {
    try {
      LocalDate fechaInicioDate = LocalDate.parse(fechaInicio);
      LocalDate fechaFinDate = LocalDate.parse(fechaFin);

      if (fechaInicioDate.isAfter(fechaFinDate)) {
        return ResponseEntity.badRequest().build();
      }

      List<Donaciones> donaciones = service.getDonacionesByFechaRange(fechaInicioDate, fechaFinDate);
      if (donaciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donaciones);
    } catch (DateTimeParseException e) {
      return ResponseEntity.badRequest().build();
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/montoMinimo/{monto}")
  public ResponseEntity<List<Donaciones>> getDonacionesByMontoMinimo(@PathVariable BigDecimal monto) {
    try {
      List<Donaciones> donaciones = service.getDonacionesByMontoMinimo(monto);
      if (donaciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donaciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/recibidoPor/{usuarioId}")
  public ResponseEntity<List<Donaciones>> getDonacionesByRecibidoPor(@PathVariable Integer usuarioId) {
    try {
      List<Donaciones> donaciones = service.getDonacionesByRecibidoPor(usuarioId);
      if (donaciones.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(donaciones);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveDonacion(@RequestBody Donaciones donacion) {
    try {
      // Validar campos obligatorios
      if (donacion.getRefugio() == null || donacion.getRefugio().getId() == null) {
        return ResponseEntity.badRequest().body("El refugio es obligatorio.");
      }

      if (donacion.getDonador() == null || donacion.getDonador().getId() == null) {
        return ResponseEntity.badRequest().body("El donador es obligatorio.");
      }

      if (donacion.getTipo() == null || donacion.getTipo().getId() == null) {
        return ResponseEntity.badRequest().body("El tipo de donación es obligatorio.");
      }

      if (donacion.getRecibidoPor() == null || donacion.getRecibidoPor().getId() == null) {
        return ResponseEntity.badRequest().body("El usuario que recibió la donación es obligatorio.");
      }

      if (donacion.getFecha() == null) {
        return ResponseEntity.badRequest().body("La fecha de la donación es obligatoria.");
      }

      // Validar longitud de observaciones
      if (donacion.getObservaciones() != null && donacion.getObservaciones().length() > 1000) {
        return ResponseEntity.badRequest().body("Las observaciones no pueden exceder los 1000 caracteres.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (donacion.getEstadoRegistro() == null) {
        donacion.setEstadoRegistro(true);
      }

      int result = service.saveDonacion(donacion);
      return result > 0 ? ResponseEntity.ok("Donación guardada con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar la donación");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateDonacion(@RequestBody Donaciones donacion) {
    try {
      // Validar que el ID esté presente para la actualización
      if (donacion.getId() == null) {
        return ResponseEntity.badRequest().body("El ID de la donación es obligatorio para actualizar.");
      }

      // Validar campos obligatorios
      if (donacion.getRefugio() == null || donacion.getRefugio().getId() == null) {
        return ResponseEntity.badRequest().body("El refugio es obligatorio.");
      }

      if (donacion.getDonador() == null || donacion.getDonador().getId() == null) {
        return ResponseEntity.badRequest().body("El donador es obligatorio.");
      }

      if (donacion.getTipo() == null || donacion.getTipo().getId() == null) {
        return ResponseEntity.badRequest().body("El tipo de donación es obligatorio.");
      }

      if (donacion.getRecibidoPor() == null || donacion.getRecibidoPor().getId() == null) {
        return ResponseEntity.badRequest().body("El usuario que recibió la donación es obligatorio.");
      }

      if (donacion.getFecha() == null) {
        return ResponseEntity.badRequest().body("La fecha de la donación es obligatoria.");
      }

      // Validar longitud de observaciones
      if (donacion.getObservaciones() != null && donacion.getObservaciones().length() > 1000) {
        return ResponseEntity.badRequest().body("Las observaciones no pueden exceder los 1000 caracteres.");
      }

      // Verificar que la donación existe antes de actualizar
      Optional<Donaciones> existingDonacion = service.getDonacionById(donacion.getId());
      if (!existingDonacion.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      // Si no se especifica estadoRegistro, mantener el valor actual
      if (donacion.getEstadoRegistro() == null) {
        donacion.setEstadoRegistro(existingDonacion.get().getEstadoRegistro());
      }

      int result = service.updateDonacion(donacion);
      return result > 0 ? ResponseEntity.ok("Donación actualizada con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar la donación");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteDonacion(@PathVariable Integer id) {
    try {
      // Verificar que la donación existe antes de eliminarla
      Optional<Donaciones> existingDonacion = service.getDonacionById(id);
      if (!existingDonacion.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteDonacion(id);
      return ResponseEntity.ok("Donación eliminada lógicamente con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }
}