package com.hmh.dao;

import com.hmh.model.QualityLevel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QualityLevelDAO {
    private Connection connection;

    public QualityLevelDAO(Connection connection) {
        this.connection = connection;
    }

    // Fetch all quality levels
    public List<QualityLevel> getAllQualityLevels() throws SQLException {
        List<QualityLevel> qualityLevels = new ArrayList<>();
        String query = "SELECT * FROM QualityLevels";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                QualityLevel qualityLevel = new QualityLevel(
                        rs.getInt("quality_id"),
                        rs.getString("quality_name"), // Fetch quality_name
                        rs.getDouble("daily_rate")
                );
                qualityLevels.add(qualityLevel);
            }
        }
        return qualityLevels;
    }

    // Add a new quality level
    public void addQualityLevel(QualityLevel qualityLevel) throws SQLException {
        String query = "INSERT INTO QualityLevels (quality_name, daily_rate) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, qualityLevel.getQualityName()); // Insert quality_name
            pstmt.setDouble(2, qualityLevel.getDailyRate());
            pstmt.executeUpdate();
        }
    }
}
