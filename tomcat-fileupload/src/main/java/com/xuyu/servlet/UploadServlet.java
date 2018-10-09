package com.xuyu.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.yoke.util.file.FileType;

@WebServlet("/load/UploadServlet")
public class UploadServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String root = req.getServletContext().getRealPath("/upload");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> list = upload.parseRequest(req);
			for (FileItem fileItem : list) {
				if(!fileItem.isFormField()) {
					fileItem.write(new File(root + "/" + fileItem.getName()));
					resp.getWriter().write("success");
				}
				resp.getWriter().write("exception");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// 判断文件是图片格式
	public static FileType getFileType(InputStream is) throws IOException {
		byte[] src = new byte[28];
		is.read(src, 0, 28);
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v).toUpperCase();
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		FileType[] fileTypes = FileType.values();
		for (FileType fileType : fileTypes) {
			if (stringBuilder.toString().startsWith(fileType.getValue())) {
				return fileType;
			}
		}
		return null;
	}

}
