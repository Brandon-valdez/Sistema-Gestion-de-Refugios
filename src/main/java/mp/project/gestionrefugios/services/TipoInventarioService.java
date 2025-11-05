package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.TipoInventario;
import mp.project.gestionrefugios.interfaces.ITipoInventarioService;
import mp.project.gestionrefugios.repository.TipoInventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoInventarioService implements ITipoInventarioService {

  @Autowired
  private TipoInventarioRepository data;

  @Override
  public List<TipoInventario> getTipoInventarios() {
    return data.findAll();
  }

  @Override
  public Optional<TipoInventario> getTipoInventarioById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveTipoInventario(TipoInventario tipoInventario) {
    // Validaciones
    validateTipoInventario(tipoInventario);

    data.save(tipoInventario);
    return tipoInventario.getId();
  }

  @Override
  public int updateTipoInventario(TipoInventario tipoInventario) {
    // Verificar que el tipo de inventario existe antes de actualizarlo
    if (tipoInventario.getId() != null && data.existsById(tipoInventario.getId())) {
      // Validaciones
      validateTipoInventario(tipoInventario);

      data.save(tipoInventario);
      return tipoInventario.getId();
    }
    return 0;
  }

  @Override
  public void deleteTipoInventario(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<TipoInventario> tipoInventarioOptional = data.findById(id);
    if (tipoInventarioOptional.isPresent()) {
      TipoInventario tipoInventario = tipoInventarioOptional.get();
      tipoInventario.setEstadoRegistro(false);
      data.save(tipoInventario);
    }
  }

  @Override
  public List<TipoInventario> getTipoInventarioByStatus(Boolean status) {
    return data.getTipoInventarioByStatus(status);
  }

  @Override
  public List<TipoInventario> searchTipoInventarioByNombre(String nombre) {
    return data.searchTipoInventarioByNombre(nombre);
  }

  @Override
  public Optional<TipoInventario> getTipoInventarioByNombreExacto(String nombre) {
    return data.getTipoInventarioByNombreExacto(nombre);
  }

  @Override
  public List<TipoInventario> searchTipoInventarioByDescripcion(String descripcion) {
    return data.searchTipoInventarioByDescripcion(descripcion);
  }

  // Método privado para validaciones
  private void validateTipoInventario(TipoInventario tipoInventario) {
    // Validar que el nombre no esté duplicado (solo para nuevos registros o si el
    // nombre cambió)
    if (tipoInventario.getNombre() != null && !tipoInventario.getNombre().isEmpty()) {
      Optional<TipoInventario> existingByNombre = data.getTipoInventarioByNombreExacto(tipoInventario.getNombre());
      if (existingByNombre.isPresent()) {
        // Si es un nuevo registro o si el nombre pertenece a otro tipo de inventario
        if (tipoInventario.getId() == null || !existingByNombre.get().getId().equals(tipoInventario.getId())) {
          throw new IllegalArgumentException("Ya existe un tipo de inventario con este nombre");
        }
      }
    }

    // Validar longitud de descripción
    if (tipoInventario.getDescripcion() != null && tipoInventario.getDescripcion().length() > 1000) {
      throw new IllegalArgumentException("La descripción no puede exceder los 1000 caracteres");
    }
  }
}