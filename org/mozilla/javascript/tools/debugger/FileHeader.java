package org.mozilla.javascript.tools.debugger;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;
import java.awt.*;

class FileHeader extends JPanel implements MouseListener
{
    private static final long serialVersionUID = -2858905404778259127L;
    private FileWindow fileWindow;
    private int pressLine;
    
    public FileHeader(final FileWindow fileWindow) {
        this.pressLine = -1;
        this.fileWindow = fileWindow;
        this.addMouseListener(this);
        this.update();
    }
    
    @Override
    public void mouseClicked(final MouseEvent mouseEvent) {
    }
    
    @Override
    public void mouseEntered(final MouseEvent mouseEvent) {
    }
    
    @Override
    public void mouseExited(final MouseEvent mouseEvent) {
    }
    
    @Override
    public void mousePressed(final MouseEvent mouseEvent) {
        this.pressLine = mouseEvent.getY() / this.getFontMetrics(this.fileWindow.textArea.getFont()).getHeight();
    }
    
    @Override
    public void mouseReleased(final MouseEvent mouseEvent) {
        if (mouseEvent.getComponent() == this && (mouseEvent.getModifiers() & 0x10) != 0x0) {
            final int n = mouseEvent.getY() / this.getFontMetrics(this.fileWindow.textArea.getFont()).getHeight();
            if (n == this.pressLine) {
                this.fileWindow.toggleBreakPoint(n + 1);
                return;
            }
            this.pressLine = -1;
        }
    }
    
    @Override
    public void paint(final Graphics graphics) {
        super.paint(graphics);
        final FileTextArea textArea = this.fileWindow.textArea;
        final Font font = textArea.getFont();
        graphics.setFont(font);
        final FontMetrics fontMetrics = this.getFontMetrics(font);
        final Rectangle clipBounds = graphics.getClipBounds();
        graphics.setColor(this.getBackground());
        graphics.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
        final int maxAscent = fontMetrics.getMaxAscent();
        final int height = fontMetrics.getHeight();
        final int n = textArea.getLineCount() + 1;
        if (Integer.toString(n).length() < 2) {}
        int i = clipBounds.y / height;
        final int n2 = (clipBounds.y + clipBounds.height) / height + 1;
        final int width = this.getWidth();
        int n3;
        if ((n3 = n2) > n) {
            n3 = n;
        }
        while (i < n3) {
            int lineStartOffset = -2;
            try {
                lineStartOffset = textArea.getLineStartOffset(i);
            }
            catch (BadLocationException ex) {}
            final boolean breakPoint = this.fileWindow.isBreakPoint(i + 1);
            final StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(i + 1));
            sb.append(" ");
            final String string = sb.toString();
            final int n4 = i * height;
            graphics.setColor(Color.blue);
            graphics.drawString(string, 0, n4 + maxAscent);
            final int n5 = width - maxAscent;
            if (breakPoint) {
                graphics.setColor(new Color(128, 0, 0));
                final int n6 = n4 + maxAscent - 9;
                graphics.fillOval(n5, n6, 9, 9);
                graphics.drawOval(n5, n6, 8, 8);
                graphics.drawOval(n5, n6, 9, 9);
            }
            if (lineStartOffset == this.fileWindow.currentPos) {
                final Polygon polygon = new Polygon();
                final int n7 = n4 + (maxAscent - 10);
                polygon.addPoint(n5, n7 + 3);
                polygon.addPoint(n5 + 5, n7 + 3);
                int j;
                int n8;
                for (j = n5 + 5, n8 = n7; j <= n5 + 10; ++j, ++n8) {
                    polygon.addPoint(j, n8);
                }
                for (int k = n5 + 9; k >= n5 + 5; --k, ++n8) {
                    polygon.addPoint(k, n8);
                }
                polygon.addPoint(n5 + 5, n7 + 7);
                polygon.addPoint(n5, n7 + 7);
                graphics.setColor(Color.yellow);
                graphics.fillPolygon(polygon);
                graphics.setColor(Color.black);
                graphics.drawPolygon(polygon);
            }
            ++i;
        }
    }
    
    public void update() {
        final FileTextArea textArea = this.fileWindow.textArea;
        final Font font = textArea.getFont();
        this.setFont(font);
        final FontMetrics fontMetrics = this.getFontMetrics(font);
        final int height = fontMetrics.getHeight();
        final int n = textArea.getLineCount() + 1;
        String string;
        if ((string = Integer.toString(n)).length() < 2) {
            string = "99";
        }
        final Dimension dimension = new Dimension();
        dimension.width = fontMetrics.stringWidth(string) + 16;
        dimension.height = n * height + 100;
        this.setPreferredSize(dimension);
        this.setSize(dimension);
    }
}
