package com.yzk18.GUI;

import javax.swing.*;
import javax.swing.event.AncestorEvent;

public class DefaultFocusAncestorListener extends  AncestorListenerAdapter{
    private JComponent defaultFocusedComponent;
    public DefaultFocusAncestorListener(JComponent defaultFocusedComponent)
    {
        this.defaultFocusedComponent = defaultFocusedComponent;
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        this.defaultFocusedComponent.grabFocus();
    }

    public static void setDefaultFocusedComponent(JComponent parent,JComponent focusedComponent)
    {
        parent.addAncestorListener(new DefaultFocusAncestorListener(focusedComponent));
    }
}
