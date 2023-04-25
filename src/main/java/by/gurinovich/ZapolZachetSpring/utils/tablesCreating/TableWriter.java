package by.gurinovich.ZapolZachetSpring.utils.tablesCreating;

import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.Zachet;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Component
public class TableWriter {

//    public void showTable(Group group) throws IOException {
//        List<Student> students = group.getStudents();
//        FileWriter writer = new FileWriter("src/main/resources/templates/users/showGroupInfo.html", false);
//        readAndWriteFirstPart(writer);
//        writeThead(writer, group);
//        writeTbody(writer, group);
//        writer.write("\n</table>\n</p>\n</div>\n");
//        writer.write("</body>\n</html>");
//        writer.close();
//    }

    private void readAndWriteFirstPart(FileWriter writer) throws IOException {
        FileReader fr = new FileReader("src/main/resources/static/firstPart.txt");
        Scanner reader = new Scanner(fr);
        while (reader.hasNextLine()){
            String line = reader.nextLine();
            writer.write(line+"\n");
            writer.flush();
        }
        reader.close();
    }

    private void writeThead(FileWriter writer, Group group) throws IOException {
        String style = "style = \"background-color: #FFFFFF;font-family: Century Gothic, sans-serif;font-size: medium;color: #548235;text-align: left;border-bottom: 2px solid #548235;padding: 0px 20px 0px 0px;width: auto\">";
        writer.write("<th "+ style + group.getName() + "</th>\n");
        for (int i =1; i <= 50; ++i){
            writer.write("<th "+ style + i + "</th>\n");
        }
        writer.write("</tr>\n</thead>\n");
    }

//    private void writeTbody(FileWriter writer, Group group) throws IOException {
//        String style = "style = \"background-color: #E2EFDA;font-family: Century Gothic, sans-serif;font-size: medium;text-align: left;padding: 0px 20px 0px 0px;width: auto\">";
//        writer.write("<tbody>\n");
//        List<Student> students = group.getStudents();
//        for (Student student : students){
//            writer.write("<tr>\n<th " +style + student.getFio() + "</th>\n");
//            List<Zachet> zachets = student.getZachety();
//            if (!zachets.isEmpty()) {
//                zachets = zachets.stream().sorted((zachet1, zachet2) -> Integer.compare(zachet1.getLabaNumber(), zachet2.getLabaNumber())).toList();
//                int zachetIndex = 0;
//                for (int i = 0; i < 50; ++i) {
//                    if (zachets.get(zachetIndex).getLabaNumber() == i) {
//                        writer.write("<td " + style + "+</td>\n");
//                        zachetIndex++;
//                    } else
//                        writer.write("<td " + style + "-</td>\n");
//                }
//            }
//            writer.write("</tr>\n");
//        }
//        writer.write("</tbody>\n");
//    }


}
