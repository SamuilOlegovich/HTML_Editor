package htmlEditor;

import javax.swing.filechooser.FileFilter;
import java.io.File;

        // Сейчас мы напишем свой фильтр:
        //Создал публичный класс HTMLFileFilter унаследованный от FileFilter.
        //Мы хотим, чтобы окно выбора файла отображало только html/htm файлы или папки.
public class HTMLFileFilter extends FileFilter {

        // Переопределил метод accept(File file), чтобы он возвращал true, если переданный файл директория
        //      или содержит в конце имени ".html" или ".htm" без учета регистра.
    @Override
    public boolean accept(File file) {
        String s = file.getName().substring(file.getName().length() - 4);
        String s1 = file.getName().substring(file.getName().length() - 5);
        return file.isDirectory() || ".htm".equalsIgnoreCase(s) || ".html".equalsIgnoreCase(s1);
    }
        // Чтобы в окне выбора файла в описании доступных типов файлов отображался текст "HTML и HTM файлы"
        //      переопределил соответствующим образом метод getDescription().
    @Override
    public String getDescription() { return "HTML и HTM файлы"; }
}
