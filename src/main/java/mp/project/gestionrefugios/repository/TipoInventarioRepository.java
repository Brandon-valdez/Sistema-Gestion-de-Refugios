package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.TipoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoInventarioRepository extends JpaRepository<TipoInventario, Integer> {

  @Query("select t from TipoInventario t where t.estadoRegistro = :status")
  List<TipoInventario> getTipoInventarioByStatus(@Param("status") Boolean status);

  @Query("select t from TipoInventario t where lower(t.nombre) like lower(concat('%', :nombre, '%')) and t.estadoRegistro = true")
  List<TipoInventario> searchTipoInventarioByNombre(@Param("nombre") String nombre);

  @Query("select t from TipoInventario t where lower(t.nombre) = lower(:nombre) and t.estadoRegistro = true")
  Optional<TipoInventario> getTipoInventarioByNombreExacto(@Param("nombre") String nombre);

  @Query("select t from TipoInventario t where lower(t.descripcion) like lower(concat('%', :descripcion, '%')) and t.estadoRegistro = true")
  List<TipoInventario> searchTipoInventarioByDescripcion(@Param("descripcion") String descripcion);
}
