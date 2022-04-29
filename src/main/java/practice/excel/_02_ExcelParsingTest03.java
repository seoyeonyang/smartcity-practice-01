package practice.excel;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

public class _02_ExcelParsingTest03 {

	
	// "디버깅" -  생활코딩 
	public static void main(String[] args) throws Exception{
		
		// 엑셀 데이터에 맞게 header 정의
		String[] headers = {"rank", "movie_name", "open_date", "income", "attendence", "number_of_screens", "representative_nationality","nationality", "film_distribution"};
		
		// 엑셀 파일을 가져온다.
		String filePath = "C:/Users/pine/Desktop/OT/1_smart_city/excel/영화순위.xlsx";
		FileInputStream file = new FileInputStream(filePath);
		
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0); // 첫번째 sheet를 가져온다.
		
		
		// row 수를 구해서 loop count를 정한다.
		int rows = sheet.getPhysicalNumberOfRows();

		int rowNo = 0;
		int cellIndex = 0;

		// 엑셀 데이터를 읽어서 JSON으로 파싱 & List에 담는다.
		List<JSONObject> parsedData = new ArrayList<JSONObject>();
		
		for(rowNo = 0 ; rowNo < rows; rowNo++) {

			XSSFRow row = sheet.getRow(rowNo);
			
			// JSONObject를 생성하는 위치 중요 
			// (row for문 안, cell for문 밖)
			JSONObject jobj = new JSONObject();
			
			// 엑셀에서 첫번째 row는 header이기 때문에 스킵했다.
			if(rowNo == 0) {
				continue;
			}
			
			if(row != null) {
				
				int cells = row.getPhysicalNumberOfCells();
				
				for(cellIndex = 0; cellIndex <= cells ; cellIndex++) {
					
					XSSFCell cell = row.getCell(cellIndex);
					
					if(cell == null) { // 빈 셀 체크
						continue;
					}else {
						
						// 타입 별로 내용을 읽는다.
						String value = "";

						switch(cell.getCellType()) {
						case FORMULA:
							value = cell.getCellFormula();
							break;
						case NUMERIC:
							value = cell.getNumericCellValue() + "";
							break;
						case STRING:
							value = cell.getStringCellValue();
							break;
						case BOOLEAN:
							value = cell.getBooleanCellValue() + "";
							break;
						case BLANK:
							value = cell.getBooleanCellValue() + "";
							break;
						case ERROR:
							value = cell.getErrorCellString() + "";
							break;
						}
						
						jobj.put(headers[cellIndex], value);
					}
					
				} // end of for cellIndex
				 
				//System.out.println(jobj);
				parsedData.add(jobj);
			} // end of for rowIndex
		}
		
		String driverName = "org.postgresql.Driver";
		String connectionUrl = "jdbc:postgresql://192.168.1.162:5432/postgres";
		String user = "postgres";
		String password = "0000";
		
		Connection conn = null;
		PreparedStatement psmt = null;
		
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(connectionUrl, user, password);
			
			String sql = "INSERT INTO sc.movie_rank(rank, movie_name, open_date, income, attendence, number_of_screens, representative_nationality, nationality, film_distribution) "
					+" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			
			psmt = conn.prepareStatement(sql);
			
			int insertedCount = 0;
			
			
			for(int i=0; i<parsedData.size(); i++) {
			
			JSONObject tmpObj = parsedData.get(i);
			
			psmt.setString(1,  tmpObj.getString("rank"));
			psmt.setString(2,  tmpObj.getString("movie_name"));
			psmt.setString(3,  tmpObj.getString("open_date"));
			psmt.setString(4,  tmpObj.getString("income"));
			psmt.setString(5,  tmpObj.getString("attendence"));
			psmt.setString(6,  tmpObj.getString("number_of_screens"));
			psmt.setString(7,  tmpObj.getString("representative_nationality"));
			psmt.setString(8,  tmpObj.getString("nationality"));
			psmt.setString(9,  tmpObj.getString("film_distribution"));
			
			insertedCount += psmt.executeUpdate();
			}
			
			System.out.println("insertedCount : " + insertedCount);
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} finally {
			if(psmt != null) {psmt.close();}
			if(conn != null) {conn.close();} 
			
		}
		
	}
}
