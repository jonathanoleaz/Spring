package com.bolsadeideas.springboot.app.view.xlsx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        response.setHeader("Content-Disposition", "attachment; filename=\"factura_view.xlsx\"");

        Factura factura = null;
        factura= (Factura) model.get("factura");

        Sheet sheet = workbook.createSheet("Factura com spring");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);

        cell.setCellValue("Datos del cliente");
        row = sheet.createRow(1);
        cell= row.createCell(0);
        cell.setCellValue(factura.getCliente().getNombre());

        row= sheet.createRow(2);
        cell=row.createCell(0);
        cell.setCellValue(factura.getCliente().getEmail());

        sheet.createRow(4).createCell(0).setCellValue("Datos de la factura");
        sheet.createRow(5).createCell(0).setCellValue("Folio:"+factura.getId());
        sheet.createRow(6).createCell(0).setCellValue("Descripcion:"+factura.getDescripcion());
        sheet.createRow(7).createCell(0).setCellValue("Fecha:"+factura.getCreatedAt());

        CellStyle theaderStyle = workbook.createCellStyle();
        theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
        theaderStyle.setBorderTop(BorderStyle.MEDIUM);
        theaderStyle.setBorderRight(BorderStyle.MEDIUM);
        theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
        theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
        theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);;

        CellStyle tbodyStyle = workbook.createCellStyle();
        tbodyStyle.setBorderBottom(BorderStyle.THIN);
        tbodyStyle.setBorderTop(BorderStyle.THIN);
        tbodyStyle.setBorderRight(BorderStyle.THIN);
        tbodyStyle.setBorderLeft(BorderStyle.THIN);

        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Producto");

        header.createCell(1).setCellValue("Precio");
        header.createCell(2).setCellValue("Cantidad");
        header.createCell(3).setCellValue("Total");

        header.getCell(0).setCellStyle(theaderStyle);
        header.getCell(1).setCellStyle(theaderStyle);
        header.getCell(2).setCellStyle(theaderStyle);
        header.getCell(3).setCellStyle(theaderStyle);

        int rownum= 10;
        for(ItemFactura item: factura.getItems()){
            Row fila = sheet.createRow(rownum ++);
            cell = fila.createCell(0);
            cell.setCellStyle(tbodyStyle);
            cell.setCellValue(item.getProducto().getNombre());

            cell = fila.createCell(1);
            cell.setCellStyle(tbodyStyle);
            cell.setCellValue(item.getProducto().getPrecio());

            cell = fila.createCell(2);
            cell.setCellStyle(tbodyStyle);
            cell.setCellValue(item.getCantidad());

            cell = fila.createCell(3);
            cell.setCellStyle(tbodyStyle);
            cell.setCellValue(item.calcularImporte());

        }

        Row filatotal = sheet.createRow(rownum);
        filatotal.createCell(2).setCellValue("Gran total: ");
        filatotal.createCell(3).setCellValue(factura.getTotal());
    }
    
}
