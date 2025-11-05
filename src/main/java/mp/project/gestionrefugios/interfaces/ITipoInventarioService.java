package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.TipoInventario;

import java.util.List;
import java.util.Optional;

public interface ITipoInventarioService {

  public List<TipoInventario> getTipoInventarios();

  public Optional<TipoInventario> getTipoInventarioById(Integer id);

  public int saveTipoInventario(TipoInventario tipoInventario);

  public int updateTipoInventario(TipoInventario tipoInventario);

  public void deleteTipoInventario(Integer id);

  public List<TipoInventario> getTipoInventarioByStatus(Boolean status);

  public List<TipoInventario> searchTipoInventarioByNombre(String nombre);

  public Optional<TipoInventario> getTipoInventarioByNombreExacto(String nombre);

  public List<TipoInventario> searchTipoInventarioByDescripcion(String descripcion);
}