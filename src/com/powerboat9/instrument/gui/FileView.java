package com.powerboat9.instrument.gui;

import com.powerboat9.instrument.IFSManager;

import javax.swing.*;

public class FileView extends JFrame {
    private IFSManager fileManager;
    private JList list;


    public FileView(IFSManager fileManagerIn) {
        fileManager = fileManagerIn;
    }
}
