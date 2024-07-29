package backend.weatherApp.repository;

import backend.weatherApp.model.Location;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * @author Elif Ece Can
 * @date 17.07.2024
 */
@Transactional
@Repository
public interface ILocationRepository extends JpaRepository<Location, Long>
{
    Location findByCityName(String cityName);
}
