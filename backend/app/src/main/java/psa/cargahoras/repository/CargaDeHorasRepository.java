package psa.cargahoras.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psa.cargahoras.entity.CargaDeHoras;

@Repository
public interface CargaDeHorasRepository extends JpaRepository<CargaDeHoras, UUID> {
  List<CargaDeHoras> findByTareaId(UUID tareaId);
}
