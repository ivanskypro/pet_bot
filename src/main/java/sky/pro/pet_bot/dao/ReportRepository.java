package sky.pro.pet_bot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sky.pro.pet_bot.model.Report;

import java.util.Collection;
import java.util.List;

/**
 * репозиторий для хранения отчетов владельца животного
 */

public interface ReportRepository extends JpaRepository<Report,Long> {

    @Query(value = "SELECT * from reports order by id DESC LIMIT ?1", nativeQuery = true)
    Report getLastReport(int count);

}
