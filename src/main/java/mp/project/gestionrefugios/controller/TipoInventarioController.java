package mp.project.gestionrefugios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mp.project.gestionrefugios.model.TipoInventario;
import mp.project.gestionrefugios.services.TipoInventarioService;

@RestController
@RequestMapping("/tipoInventario")
@CrossOrigin(origins = "*")
public class TipoInventarioController {

  @Autowired
  private TipoInventarioService service;

  @GetMapping("/getByStatus/{status}")
  public ResponseEntity<List<TipoInventario>> getTipoInventarioByStatus(@PathVariable("status") Boolean status) {
    try {
      List<TipoInventario> tipoInventarios = service.getTipoInventarioByStatus(status);
      if (tipoInventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(tipoInventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<TipoInventario>> getTipoInventarios() {
    try {
      List<TipoInventario> tipoInventarios = service.getTipoInventarios();
      if (tipoInventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(tipoInventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<TipoInventario> getTipoInventarioById(@PathVariable Integer id) {
    try {
      Optional<TipoInventario> tipoInventario = service.getTipoInventarioById(id);
      return tipoInventario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/nombre/{nombre}")
  public ResponseEntity<List<TipoInventario>> searchTipoInventarioByNombre(@PathVariable String nombre) {
    try {
      List<TipoInventario> tipoInventarios = service.searchTipoInventarioByNombre(nombre);
      if (tipoInventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(tipoInventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/nombre/exacto/{nombre}")
  public ResponseEntity<TipoInventario> getTipoInventarioByNombreExacto(@PathVariable String nombre) {
    try {
      Optional<TipoInventario> tipoInventario = service.getTipoInventarioByNombreExacto(nombre);
      return tipoInventario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @GetMapping("/search/descripcion/{descripcion}")
  public ResponseEntity<List<TipoInventario>> searchTipoInventarioByDescripcion(@PathVariable String descripcion) {
    try {
      List<TipoInventario> tipoInventarios = service.searchTipoInventarioByDescripcion(descripcion);
      if (tipoInventarios.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(tipoInventarios);
    } catch (Exception e) {
      return ResponseEntity.status(500).build();
    }
  }

  @PostMapping("/save")
  public ResponseEntity<String> saveTipoInventario(@RequestBody TipoInventario tipoInventario) {
    try {
      // Validar campos obligatorios
      if (tipoInventario.getNombre() == null || tipoInventario.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del tipo de inventario es obligatorio.");
      }

      // Validar longitud del nombre
      if (tipoInventario.getNombre().length() > 50) {
        return ResponseEntity.badRequest().body("El nombre no puede exceder los 50 caracteres.");
      }

      // Validar longitud de la descripción
      if (tipoInventario.getDescripcion() != null && tipoInventario.getDescripcion().length() > 1000) {
        return ResponseEntity.badRequest().body("La descripción no puede exceder los 1000 caracteres.");
      }

      // Si no se especifica estadoRegistro, establecer como activo por defecto
      if (tipoInventario.getEstadoRegistro() == null) {
        tipoInventario.setEstadoRegistro(true);
      }

      int result = service.saveTipoInventario(tipoInventario);
      return result > 0 ? ResponseEntity.ok("Tipo de inventario guardado con ID: " + result)
          : ResponseEntity.status(500).body("Error al guardar el tipo de inventario");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateTipoInventario(@RequestBody TipoInventario tipoInventario) {
    try {
      // Validar que el ID esté presente para la actualización
      if (tipoInventario.getId() == null) {
        return ResponseEntity.badRequest().body("El ID del tipo de inventario es obligatorio para actualizar.");
      }

      // Validar campos obligatorios
      if (tipoInventario.getNombre() == null || tipoInventario.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del tipo de inventario es obligatorio.");
      }

      // Validar longitud del nombre
      if (tipoInventario.getNombre().length() > 50) {
        return ResponseEntity.badRequest().body("El nombre no puede exceder los 50 caracteres.");
      }

      // Validar longitud de la descripción
      if (tipoInventario.getDescripcion() != null && tipoInventario.getDescripcion().length() > 1000) {
        return ResponseEntity.badRequest().body("La descripción no puede exceder los 1000 caracteres.");
      }

      // Verificar que el tipo de inventario existe antes de actualizarlo
      Optional<TipoInventario> existingTipoInventario = service.getTipoInventarioById(tipoInventario.getId());
      if (!existingTipoInventario.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      int result = service.updateTipoInventario(tipoInventario);
      return result > 0 ? ResponseEntity.ok("Tipo de inventario actualizado con ID: " + result)
          : ResponseEntity.status(500).body("Error al actualizar el tipo de inventario");

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteTipoInventario(@PathVariable Integer id) {
    try {
      // Verificar que el tipo de inventario existe antes de eliminarlo
      Optional<TipoInventario> existingTipoInventario = service.getTipoInventarioById(id);
      if (!existingTipoInventario.isPresent()) {
        return ResponseEntity.notFound().build();
      }

      service.deleteTipoInventario(id);
      return ResponseEntity.ok("Tipo de inventario eliminado lógicamente con ID: " + id);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
    }
  }
}