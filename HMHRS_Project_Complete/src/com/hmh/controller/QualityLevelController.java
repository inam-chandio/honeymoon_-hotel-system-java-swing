package com.hmh.controller;

import com.hmh.dao.QualityLevelDAO;
import com.hmh.model.QualityLevel;

import java.sql.Connection;
import java.util.List;

public class QualityLevelController {
    private QualityLevelDAO qualityLevelDAO;

    public QualityLevelController(Connection connection) {
        this.qualityLevelDAO = new QualityLevelDAO(connection);
    }

    public List<QualityLevel> getAllQualityLevels() throws Exception {
        return qualityLevelDAO.getAllQualityLevels();
    }

    public void addQualityLevel(QualityLevel qualityLevel) throws Exception {
        qualityLevelDAO.addQualityLevel(qualityLevel);
    }
}
