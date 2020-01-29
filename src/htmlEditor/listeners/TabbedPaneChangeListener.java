package htmlEditor.listeners;


import htmlEditor.View;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class TabbedPaneChangeListener implements ChangeListener {
    private View view;

    public TabbedPaneChangeListener(View view) { this.view = view; }

    @Override
    public void stateChanged(ChangeEvent e) { view.selectedTabChanged(); }

}