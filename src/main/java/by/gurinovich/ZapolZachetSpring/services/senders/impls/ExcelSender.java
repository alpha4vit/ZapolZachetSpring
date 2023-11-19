package by.gurinovich.ZapolZachetSpring.services.senders.impls;

import by.gurinovich.ZapolZachetSpring.models.*;
import by.gurinovich.ZapolZachetSpring.services.EmailService;
import by.gurinovich.ZapolZachetSpring.services.StudentService;
import by.gurinovich.ZapolZachetSpring.services.ZachetService;
import by.gurinovich.ZapolZachetSpring.services.senders.Sender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelSender implements Sender {

    private final StudentService studentService;
    private final ZachetService zachetService;
    private final EmailService emailService;

    private final String GLOBAL_PATH = "src/main/resources/excel/global.xls";
    private final String GROUP_PATH = "src/main/resources/excel/group.xls";
    private final String GROUP_SUBJECT_PATH = "src/main/resources/excel/group-subject.xls";

    @SneakyThrows
    @Override
    public void send() {
        synchronized (GLOBAL_PATH) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            List<Student> students = studentService.getAll();
            createStudentsSheetDoc(workbook, students);
            workbook.close();
            emailService.sendFile(GLOBAL_PATH);
        }
    }

    @SneakyThrows
    @Override
    public void send(Group group) {
        synchronized (GROUP_PATH) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            createGroupStudentsDoc(workbook, group);
            workbook.close();
            emailService.sendFile(GROUP_PATH);
        }
    }

    @SneakyThrows
    @Override
    public void send(Group group, Subject subject) {
        synchronized (GROUP_SUBJECT_PATH) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            createGroupSubjectDoc(workbook, group, subject);
            workbook.close();
            emailService.sendFile(GROUP_SUBJECT_PATH);
        }
    }

    private void createGroupSubjectDoc(HSSFWorkbook workbook, Group group, Subject subject){
        HSSFSheet sheet = workbook.createSheet("Группа "+group.getName());
        List<Laba> labas = subject.getLabas().stream().sorted(Comparator.comparing(Laba::getNumber)).toList();
        fillHeaderGroupSubject(sheet, group, labas);
        int rowNum =1;
        for (Student student : group.getStudents()){
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(student.getFio());
            List<Zachet> zachets = zachetService.getByStudentAndSubject(student, subject);
            for (int i = 0; i < zachets.size(); ++i){
                row.createCell(i+1).setCellValue(zachets.get(i).getValue());
            }
            ++rowNum;
        }

        try (OutputStream out = new FileOutputStream(new File(GROUP_SUBJECT_PATH))){
            workbook.write(out);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void createGroupStudentsDoc(HSSFWorkbook workbook, Group group){
        HSSFSheet sheet = workbook.createSheet("Группа "+group.getName());
        List<Student> students = group.getStudents().stream().sorted(Comparator.comparing(Student::getFio)).toList();
        int rowNum = 0;
        Row row =  sheet.createRow(rowNum);
        row.createCell(0).setCellValue("ФИО");
        row.createCell(1).setCellValue("Номер группы");
        row.createCell(2).setCellValue("Дата рождения");
        row.createCell(3).setCellValue("Успеваемость");
        rowNum++;
        createTableStudents(workbook, sheet, rowNum, students);

        try (OutputStream out = new FileOutputStream(new File(GROUP_PATH))){
            workbook.write(out);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void createStudentsSheetDoc(HSSFWorkbook workbook, List<Student> students){
        HSSFSheet sheet = workbook.createSheet("Cтуденты");
        int rowNum = 0;
        Row row =  sheet.createRow(rowNum);
        row.createCell(0).setCellValue("ФИО");
        row.createCell(1).setCellValue("Номер группы");
        row.createCell(2).setCellValue("Дата рождения");
        row.createCell(3).setCellValue("Успеваемость");
        rowNum++;
        createTableStudents(workbook, sheet, rowNum, students);

        try (OutputStream out = new FileOutputStream(new File(GLOBAL_PATH))){
            workbook.write(out);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void fillHeaderGroupSubject(HSSFSheet sheet, Group group, List<Laba> labas){
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Группа "+group.getName());
        int colNum = 1;
        for (Laba laba : labas){
            row.createCell(colNum).setCellValue(String.format("%d) %s", laba.getNumber(), laba.getTitle()));
            ++colNum;
        }
    }

    private void createTableStudents(HSSFWorkbook workbook, HSSFSheet sheet, int rowNum, List<Student> students){
        for (Student student : students){
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(student.getFio());
            row.createCell(1).setCellValue(student.getGroup().getName());


            Cell cellDate = row.createCell(2);
            createDateCell(cellDate, workbook);
            cellDate.setCellValue(student.getDateOfBirth());
            Cell cellPerfomance = row.createCell(3);
            createNumberCell(cellPerfomance, workbook);
            cellPerfomance.setCellValue(student.getPerformance());
            ++rowNum;
        }
    }


    private void createNumberCell(Cell cell, HSSFWorkbook workbook){
        DataFormat dataFormat = workbook.createDataFormat();
        short numberFormat = dataFormat.getFormat("0.00");
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(numberFormat);
        cell.setCellStyle(cellStyle);
    }

    private void createDateCell(Cell cell, HSSFWorkbook workbook){
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat((short) 14);
        cell.setCellStyle(cellStyle);
    }
}
