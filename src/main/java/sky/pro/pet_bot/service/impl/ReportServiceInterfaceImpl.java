package sky.pro.pet_bot.service.impl;

import org.springframework.stereotype.Service;
import sky.pro.pet_bot.dao.ReportRepository;
import sky.pro.pet_bot.model.Report;
import sky.pro.pet_bot.service.ReportServiceInterface;

import java.util.List;

@Service
public class ReportServiceInterfaceImpl implements ReportServiceInterface {

    public final ReportRepository reportRepository;

    public ReportServiceInterfaceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }


    public List<Report> getAllReports() {
      return reportRepository.findAll();
    }
}
