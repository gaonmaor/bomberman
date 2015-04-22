package layout;

import javax.swing.*;
import java.awt.*;

class WindowsLayoutStyle
        extends LayoutStyle {
    private int baseUnitX;
    private int baseUnitY;

    public int getPreferredGap(JComponent source, JComponent target, int type, int position, Container parent) {
        super.getPreferredGap(source, target, type, position, target);
        if (type == 3) {
            if ((position == 3) || (position == 7)) {
                int gap = getButtonChildIndent(source, position);
                if (gap != 0) {
                    return gap;
                }
                return 10;
            }
            type = 0;
        }
        if (type == 1) {
            return getCBRBPadding(source, target, position, dluToPixels(7, position));
        }
        boolean sourceLabel = source.getUIClassID() == "LabelUI";
        boolean targetLabel = target.getUIClassID() == "LabelUI";
        if (((sourceLabel) && (!targetLabel)) || ((targetLabel) && (!sourceLabel) && ((position == 3) || (position == 7)))) {
            return getCBRBPadding(source, target, position, dluToPixels(3, position));
        }
        return getCBRBPadding(source, target, position, dluToPixels(4, position));
    }

    public int getContainerGap(JComponent component, int position, Container parent) {
        super.getContainerGap(component, position, parent);
        return getCBRBPadding(component, position, dluToPixels(7, position));
    }

    private int dluToPixels(int dlu, int direction) {
        if (this.baseUnitX == 0) {
            calculateBaseUnits();
        }
        if ((direction == 3) || (direction == 7)) {
            return dlu * this.baseUnitX / 4;
        }
        assert ((direction == 1) || (direction == 5));

        return dlu * this.baseUnitY / 8;
    }

    private void calculateBaseUnits() {
        FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(UIManager.getFont("Button.font"));

        this.baseUnitX = metrics.stringWidth("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");

        this.baseUnitX = ((this.baseUnitX / 26 + 1) / 2);

        this.baseUnitY = (metrics.getAscent() + metrics.getDescent() - 1);
    }
}



