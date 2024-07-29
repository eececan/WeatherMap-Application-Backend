package backend.weatherApp.repository;
import backend.weatherApp.model.Pollution;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
/**
 * @author Elif Ece Can
 * @date 17.07.2024
 */
@Transactional
@Repository
public interface IPollutionRepository extends JpaRepository<Pollution, Long>
{
    List<Pollution> findByCityAndDate(String cityName, LocalDate date);
}
