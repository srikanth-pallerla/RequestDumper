package com.vekomy.request.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vekomy.request.util.JsonParserUtil;

@Controller
public class RequestDumperController {

	@RequestMapping(value = "/dumpRequest", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	public @ResponseBody Object getAllProfileTypes(@RequestBody String str) {
		System.out.println(str);
		FileOutputStream fop = null;
		try {
			Object obj = null;
			if (str.trim().startsWith("{")) {
				JSONObject json = new JSONObject(str.trim());
				obj = JsonParserUtil.jsonToMap(json);
			} else if (str.trim().startsWith("[")) {
				JSONArray json = new JSONArray(str.trim());
				obj = JsonParserUtil.toList(json);
			} else {
				obj = str.trim();
			}

			String pathToWrite = System.getenv("CATALINA_BASE");
			
			System.out.println(pathToWrite);

			if (pathToWrite == null || pathToWrite.isEmpty()) {
				pathToWrite = System.getProperty("java.io.tmpdir");
			} else {
				pathToWrite = pathToWrite + File.separator + "logs";
			}

			System.out.println(pathToWrite);

			DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss:SSS");
			String formattedDate = df2.format(new Date());

			File file = new File(pathToWrite + File.separator + formattedDate
					+ ".txt");

			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = obj.toString().getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			System.out.println("Done");
			System.out.println("absolute path:" + file.getAbsolutePath());

			return obj;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fop;
	}

//	public void parseJson(JSONObject json) throws Exception {
//
//		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss:SSS");
//		String formattedDate = df2.format(new Date());
//		String pathToWrite = System.getenv("CATALINA_BASE");
//
//		if (pathToWrite == null || pathToWrite.isEmpty()) {
//			pathToWrite = System.getProperty("java.io.tmpdir");
//		} else {
//			pathToWrite = pathToWrite + File.separator + "logs";
//		}
//
//		File file = new File(pathToWrite + File.separator + formattedDate
//				+ ".xlsx");
//
//		// if file doesnt exists, then create it
//		if (!file.exists()) {
//			file.createNewFile();
//		}
//
//		SXSSFWorkbook wb = new SXSSFWorkbook(100); // keep 100 rows in memory,
//													// exceeding rows will be
//													// flushed to disk
//		Sheet sh = wb.createSheet();
//		
//		Row row = sh.createRow(++rownum);
//		handleRow(sh, row, json, 0);
//
//		FileOutputStream out = new FileOutputStream(file);
//		wb.write(out);
//
//		out.close();
//
//		// dispose of temporary files backing this workbook on disk
//		wb.dispose();
//		wb.close();
//
//	}
//
//	int rownum = -1;
//
//	private void handleRow(Sheet sh, Row row, JSONObject json, int column) {
//		for (Object key : json.keySet()) {
//			String strKey = key.toString();
//			Object value = json.get(strKey);
//			if (value instanceof JSONObject) {
//				Cell cell = row.createCell(column);
//				cell.setCellValue(strKey);
////				System.out.println(new CellReference(cell).formatAsString()
////						+ "-----" + strKey);
//				handleRow(sh, row, (JSONObject) value, column + 1);
//				row = sh.createRow(rownum);
//			} else {
//				Cell cell = row.createCell(column);
//				cell.setCellValue(strKey);
////				System.out.println(new CellReference(cell).formatAsString()
////						+ "-----" + strKey);
//				Cell cell2 = row.createCell(column + 1);
//				cell2.setCellValue(value.toString());
////				System.out.println(new CellReference(cell2).formatAsString()
////						+ "-----" + value.toString());
//				row = sh.createRow(++rownum);
//			}
//		}
//	}

}
