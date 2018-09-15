package org.sopac.web.rest;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRSaver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

@WebServlet(name = "Report")
public class Report extends HttpServlet {

    //select project.*, c.name as country, s.name as sector, ds.name as detailedSector from project inner join country c  on project.country_id = c.id left outer join sector s on project.sector_id = s.id left outer join detailed_sector ds on project.detailed_sector_id = ds.id;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.getWriter().println("Sachindra Singh");

        try {

            int id = 1415;
            if (request.getParameter("id") != null){
                id = Integer.valueOf(request.getParameter("id").trim());
            }


            ServletContext cntx = getServletContext();
            //String path = cntx.getRealPath("/content/project.jrxml");
            String path = "/home/ubuntu/project.jrxml";
            String sql = "select project.*, c.name as country, s.name as sector, ds.name as detailedSector from project inner join country c  on project.country_id = c.id left outer join sector s on project.sector_id = s.id left outer join detailed_sector ds on project.detailed_sector_id = ds.id where project.id=" + id;
            InputStream is = new FileInputStream(new File(path));
            JasperReport jasperReport = JasperCompileManager.compileReport(is);
            JRSaver.saveObject(jasperReport, "project.jasper");

            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/climatefinance", "postgres", "erlang44");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);

            //Map<String, Object> parameters = new HashMap<>();
            //parameters.put("Parameter1", 1415);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, jrRS);

            response.setContentType("application/x-pdf");
            response.setHeader("Content-disposition", "inline; filename=pacific_climate_finance.pdf");

            final OutputStream outStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);


            rs.close();
            conn.close();
            is.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    protected void doGet_Test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext cntx = getServletContext();
        // Get the absolute path of the image
        String filename = cntx.getRealPath("/content/images/hipster.png");
        // retrieve mimeType dynamically
        String mime = cntx.getMimeType(filename);
        if (mime == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        response.setContentType(mime);
        File file = new File(filename);
        FileInputStream in = new FileInputStream(file);
        OutputStream out = response.getOutputStream();
        BufferedImage bigImg = ImageIO.read(file);
        BufferedImage small = bigImg.getSubimage(0, 0, 200, 200);

        // Copy the contents of the file to the output stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(small, "png", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        response.setContentLength(imageInByte.length);
        out.write(imageInByte);
        out.close();
        in.close();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
