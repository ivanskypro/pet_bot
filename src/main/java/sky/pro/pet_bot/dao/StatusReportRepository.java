package sky.pro.pet_bot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sky.pro.pet_bot.model.StatusReport;

public interface StatusReportRepository extends JpaRepository<StatusReport,Long> {
}
