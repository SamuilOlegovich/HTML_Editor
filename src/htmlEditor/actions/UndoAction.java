package htmlEditor.actions;


import htmlEditor.View;

import java.awt.event.ActionEvent;
import javax.swing.*;


        // Класс отмены действия UndoAction.
public class UndoAction extends AbstractAction {
    private View view;

    public UndoAction(View view){ this.view = view; }

    @Override
    public void actionPerformed(ActionEvent e) { view.undo(); }
}
