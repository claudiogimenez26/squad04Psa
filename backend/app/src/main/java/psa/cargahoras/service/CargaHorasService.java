package psa.cargahoras.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.repository.CargaHorasRepository;

@Service
public class CargaHorasService {

  @Autowired private CargaHorasRepository cargaHorasRepository;

  public List<CargaDeHoras> obtenerTodasLasCargas() {
    return cargaHorasRepository.findAll();
  }
}
