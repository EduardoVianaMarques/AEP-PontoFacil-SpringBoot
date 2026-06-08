package aep.PontoFacil.repository;

import aep.PontoFacil.model.ContatoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<ContatoModel, Long> {
}
