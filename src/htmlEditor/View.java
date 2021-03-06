package htmlEditor;


import htmlEditor.listeners.FrameListener;
import htmlEditor.listeners.TabbedPaneChangeListener;
import htmlEditor.listeners.UndoListener;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.awt.event.ActionListener;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.*;

import static htmlEditor.MenuHelper.*;

public class View extends JFrame implements ActionListener {
    private Controller controller;
            // это будет панель с двумя вкладками.
    private JTabbedPane tabbedPane = new JTabbedPane();
            // это будет компонент для визуального редактирования html.
    private JTextPane htmlTextPane = new JTextPane();
            // это будет компонент для редактирования html в виде текста,
            // он будет отображать код html(теги и их содержимое).
    private JEditorPane plainTextPane = new JEditorPane();

    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);

        // В конструкторе класса View, через класс UIManager, должен устанавливаться внешний вид
        //      и поведение (look and feel).
    public View () {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { ExceptionHandler.log(e); }
    }
        // Они будут отвечать за инициализацию меню и панелей редактора.
        // В методе initMenuBar() должно создаваться новое меню (объект типа JMenuBar).
        // В методе initMenuBar() c помощью MenuHelper должно быть проинициализировано
        //      меню в следующем порядке: Файл, Редактировать, Стиль, Выравнивание,
        //      Цвет, Шрифт и Помощь.
        // В методе initMenuBar() должно добавляться новосозданное меню в верхнюю часть панели
        //      контента текущего фрейма, используя метод getContentPane().
    public void initMenuBar () {
        JMenuBar jMenuBar = new JMenuBar();
        initFileMenu(this, jMenuBar);
        initEditMenu(this, jMenuBar);
        initStyleMenu(this, jMenuBar);
        initAlignMenu(this, jMenuBar);
        initColorMenu(this, jMenuBar);
        initFontMenu(this, jMenuBar);
        initHelpMenu(this, jMenuBar);
        getContentPane().add(jMenuBar, BorderLayout.NORTH);
    }
        // В методе initEditor() для компонента htmlTextPane должен устанавливаться тип контента
        //      "text/html" через сеттер setContentType.
        // В методе initEditor() должен создаваться новый локальный компонент JScrollPane через
        //      конструктор принимающий htmlTextPane.
        // В методе initEditor() для компонента tabbedPane должна добавляться вкладка с именем "HTML"
        //      и созданным компонентом JScrollPane на базе htmlTextPane.
        // В методе initEditor() должен создаваться новый локальный компонент JScrollPane через
        //      конструктор принимающий plainTextPane.
        // В методе initEditor() для компонента tabbedPane должна добавляться вкладка с именем "Текст"
        //      и созданным компонентом JScrollPane на базе plainTextPane.
        // В методе initEditor() для компонента tabbedPane должен устанавливаться предпочтительный
        //      размер панели, через сеттер setPreferredSize.
        // В методе initEditor() для компонента tabbedPane должен добавляться слушатель
        //      TabbedPaneChangeListener через метод addChangeListener.
        // Метод initEditor() должен добавлять по центру панели контента текущего фрейма нашу панель
        //      с вкладками, через getContentPane().add().
    public void initEditor() {
        htmlTextPane.setContentType("text/html");
        JScrollPane jScrollPane = new JScrollPane(htmlTextPane);
        tabbedPane.addTab("HTML", jScrollPane);
        JScrollPane jScrollPaneNew = new JScrollPane(plainTextPane);
        tabbedPane.addTab("Текст", jScrollPaneNew);
        tabbedPane.setPreferredSize(new Dimension(500, 500));
        tabbedPane.addChangeListener(new TabbedPaneChangeListener(this));
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
                //-------------------------------------------------------------
                // Он будет инициализировать графический интерфейс.
    public void initGui() {
        this.initMenuBar();
        this.initEditor();
        this.pack();
    }
                //-------------------------------------------------------------
    public void init() {
        this.initGui();
        // Добавлять слушателя событий нашего окна. В качестве подписчика создай
        // и используй объект класса FrameListener.
        //В качестве метода для добавления подписчика используй подходящий метод из класса Window
        // от которого наследуется и наш класс через классы JFrame и Frame.
        addWindowListener(new FrameListener(this));
        setVisible(true);
    }
        // отменяет последнее действие. Реализуй его используя undoManager.
    public void undo() {
        try {
            undoManager.undo();
        } catch (CannotUndoException e) {
            ExceptionHandler.log(e);
        }
    }
        // возвращает ранее отмененное действие. Реализуй его по аналогии с предыдущим пунктом.
    public void redo() {
        try {
            undoManager.redo();
        } catch (CannotRedoException e) {
            ExceptionHandler.log(e);
        }
    }
        // должен сбрасывать все правки в менеджере undoManager.
    public void resetUndo() { undoManager.discardAllEdits(); }
        // Этот метод вызывается, когда произошла смена выбранной вкладки. Итак:
        // Метод должен проверить, какая вкладка сейчас оказалась выбранной.
        // Если выбрана вкладка с индексом 0 (html вкладка), значит нам нужно получить текст
        //      из plainTextPane и установить его в контроллер с помощью метода setPlainText.
        // Если выбрана вкладка с индексом 1 (вкладка с html текстом), то необходимо получить текст
        //      у контроллера с помощью метода getPlainText() и установить его в панель plainTextPane.
        // Сбросить правки (вызвать метод resetUndo представления).
    public void selectedTabChanged() {
        if (tabbedPane.getSelectedIndex() == 0) {
            controller.setPlainText(plainTextPane.getText());
        }
        else if (tabbedPane.getSelectedIndex() == 1) {
            plainTextPane.setText(controller.getPlainText());
        }
        resetUndo();
    }

    public boolean isHtmlTabSelected() {
        if (tabbedPane.getSelectedIndex() == 0) return true;
        else return false;
    }

    public void exit() { controller.exit(); }

    public boolean canUndo() { return undoManager.canUndo(); }

    public boolean canRedo() { return undoManager.canRedo(); }
        // Этот метод наследуется от интерфейса ActionListener и будет вызваться при выборе пунктов меню,
        //      у которых наше представление указано в виде слушателя событий.
        // Получи из события команду с помощью метода getActionCommand(). Это будет обычная строка.
        // По этой строке ты можешь понять какой пункт меню создал данное событие.
        // Если это команда "Новый", вызови у контроллера метод createNewDocument().
        // В этом пункте и далее, если необходимого метода в контроллере еще нет - создай заглушки.
        // Если это команда "Открыть", вызови метод openDocument().
        // Если "Сохранить", то вызови saveDocument().
        // Если "Сохранить как..." - saveDocumentAs().
        // Если "Выход" - exit().
        // Если "О программе", то вызови метод showAbout() у представления.
        // Проверь, что заработали пункты меню Выход и О программе.
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case ("Новый"):
                controller.createNewDocument();
                break;
            case ("Открыть"):
                controller.openDocument();
                break;
            case ("Сохранить"):
                controller.saveDocument();
                break;
            case ("Сохранить как..."):
                controller.saveDocumentAs();
                break;
            case ("Выход"):
                controller.exit();
                break;
            case("О программе"):
                showAbout();
                break;
        }
    }

    public Controller getController() { return controller; }

    public UndoListener getUndoListener() { return undoListener; }

    public void setController(Controller controller) { this.controller = controller; }
        // он должен выбирать html вкладку (переключаться на нее).
        // сбрасывать все правки с помощью метода, который ты реализовал ранее.
    public void selectHtmlTab() {
        tabbedPane.setSelectedIndex(0);
        this.resetUndo();
    }
        // должен получать документ у контроллера и устанавливать его в панель
        // редактирования htmlTextPane.
    public void update() { htmlTextPane.setDocument(controller.getDocument()); }
        // должен показывать диалоговое окно с информацией о программе.
        // информацию придумай сам, а вот тип сообщения должен быть JOptionPane.
        // INFORMATION_MESSAGE
    public void showAbout() {
        JOptionPane.showMessageDialog(tabbedPane.getSelectedComponent(),
                "Версия 1.0", "О программме", JOptionPane.INFORMATION_MESSAGE);
    }

}
