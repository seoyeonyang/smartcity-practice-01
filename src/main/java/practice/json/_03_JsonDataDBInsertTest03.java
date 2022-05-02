package practice.json;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import practice.util.JsonParsingUtil;

public class _03_JsonDataDBInsertTest03 {

	public static void main(String[] args) throws IOException, SQLException {

		String filePath = "C:/Users/pine/Desktop/OT/1_smart_city/json/sample03.json";

		JSONObject json = JsonParsingUtil.jsonFileToJsonObject(filePath);

		JSONObject body = json.getJSONObject("body");
		JSONObject items = body.getJSONObject("items");
		JSONArray itemArr = items.getJSONArray("item");

		String driverName = "org.postgresql.Driver";
		String connectionUrl = "jdbc:postgresql://192.168.1.162:5432/postgres";
		String user = "postgres";
		String password = "0000";

		Connection conn = null;
		PreparedStatement psmt = null;

		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(connectionUrl, user, password);

			String sql = "INSERT INTO sc.seoul_energy_plant_temperature(dates, hour, outdoor_temp, branch_nm, branch_cd) "
					+ " VALUES(?, ?, ?, ?, ?) ";

			psmt = conn.prepareStatement(sql);

			for (int i = 0; i < itemArr.length(); i++) {
				JSONObject tempJobj = itemArr.getJSONObject(i);

				psmt.setString(1, tempJobj.getString("dates"));
				psmt.setString(2, tempJobj.getString("hour"));
				psmt.setString(3, tempJobj.getString("outdoorTemp"));
				psmt.setString(4, tempJobj.getString("branchNm"));
				psmt.setString(5, tempJobj.getString("branchCd"));

				psmt.addBatch();

				if (i % 1000 == 0) {
					int[] executeBatch = psmt.executeBatch();
					System.out.println("executeBatchCount : " + executeBatch.length);
					psmt.clearBatch();
				}
			}
			int[] executeBatch = psmt.executeBatch();
			System.out.println("executeBatchCount : " + executeBatch.length);
			psmt.clearBatch();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			if (psmt != null) {
				psmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

	}

}
