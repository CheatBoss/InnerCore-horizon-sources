package org.mozilla.javascript.tools.debugger;

import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

class FileTextArea extends JTextArea implements ActionListener, PopupMenuListener, KeyListener, MouseListener
{
    private static final long serialVersionUID = -25032065448563720L;
    private FilePopupMenu popup;
    private FileWindow w;
    
    public FileTextArea(final FileWindow w) {
        this.w = w;
        (this.popup = new FilePopupMenu(this)).addPopupMenuListener(this);
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setFont(new Font("Monospaced", 0, 12));
    }
    
    private void checkPopup(final MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            this.popup.show(this, mouseEvent.getX(), mouseEvent.getY());
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent actionCommand) {
        final int viewToModel = this.viewToModel(new Point(this.popup.x, this.popup.y));
        this.popup.setVisible(false);
        actionCommand = (ActionEvent)actionCommand.getActionCommand();
        int lineOfOffset = -1;
        try {
            lineOfOffset = this.getLineOfOffset(viewToModel);
        }
        catch (Exception ex) {}
        if (((String)actionCommand).equals("Set Breakpoint")) {
            this.w.setBreakPoint(lineOfOffset + 1);
            return;
        }
        if (((String)actionCommand).equals("Clear Breakpoint")) {
            this.w.clearBreakPoint(lineOfOffset + 1);
            return;
        }
        if (((String)actionCommand).equals("Run")) {
            this.w.load();
        }
    }
    
    @Override
    public void keyPressed(final KeyEvent keyEvent) {
        final int keyCode = keyEvent.getKeyCode();
        if (keyCode != 127) {
            switch (keyCode) {
                default: {
                    return;
                }
                case 8:
                case 9:
                case 10: {
                    break;
                }
            }
        }
        keyEvent.consume();
    }
    
    @Override
    public void keyReleased(final KeyEvent keyEvent) {
        keyEvent.consume();
    }
    
    @Override
    public void keyTyped(final KeyEvent keyEvent) {
        keyEvent.consume();
    }
    
    @Override
    public void mouseClicked(final MouseEvent mouseEvent) {
        this.checkPopup(mouseEvent);
        this.requestFocus();
        this.getCaret().setVisible(true);
    }
    
    @Override
    public void mouseEntered(final MouseEvent mouseEvent) {
    }
    
    @Override
    public void mouseExited(final MouseEvent mouseEvent) {
    }
    
    @Override
    public void mousePressed(final MouseEvent mouseEvent) {
        this.checkPopup(mouseEvent);
    }
    
    @Override
    public void mouseReleased(final MouseEvent mouseEvent) {
        this.checkPopup(mouseEvent);
    }
    
    @Override
    public void popupMenuCanceled(final PopupMenuEvent popupMenuEvent) {
    }
    
    @Override
    public void popupMenuWillBecomeInvisible(final PopupMenuEvent popupMenuEvent) {
    }
    
    @Override
    public void popupMenuWillBecomeVisible(final PopupMenuEvent popupMenuEvent) {
    }
    
    public void select(final int n) {
        if (n >= 0) {
            try {
                final int lineOfOffset = this.getLineOfOffset(n);
                Rectangle modelToView = this.modelToView(n);
                if (modelToView == null) {
                    this.select(n, n);
                }
                else {
                    try {
                        final Rectangle modelToView2 = this.modelToView(this.getLineStartOffset(lineOfOffset + 1));
                        if (modelToView2 != null) {
                            modelToView = modelToView2;
                        }
                    }
                    catch (Exception ex) {}
                    final Rectangle viewRect = ((JViewport)this.getParent()).getViewRect();
                    if (viewRect.y + viewRect.height > modelToView.y) {
                        this.select(n, n);
                    }
                    else {
                        modelToView.y += (viewRect.height - modelToView.height) / 2;
                        this.scrollRectToVisible(modelToView);
                        this.select(n, n);
                    }
                }
            }
            catch (BadLocationException ex2) {
                this.select(n, n);
            }
        }
    }
}
