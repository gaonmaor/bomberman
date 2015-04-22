package layout;

import javax.swing.*;
import java.awt.*;

class GnomeLayoutStyle
        extends LayoutStyle {
    public int getPreferredGap(JComponent source, JComponent target, int type, int position, Container parent) {
        super.getPreferredGap(source, target, type, position, parent);
        if (type == 3) {
            if ((position == 3) || (position == 7)) {
                int gap = getButtonChildIndent(source, position);
                if (gap != 0) {
                    return gap;
                }
                return 12;
            }
            type = 0;
        }
        if ((position == 3) || (position == 7)) {
            boolean sourceLabel = source.getUIClassID() == "LabelUI";
            boolean targetLabel = target.getUIClassID() == "LabelUI";
            if (((sourceLabel) && (!targetLabel)) || ((!sourceLabel) && (targetLabel))) {
                return 12;
            }
        }
        if (type == 0) {
            return 6;
        }
        return 12;
    }

    public int getContainerGap(JComponent component, int position, Container parent) {
        super.getContainerGap(component, position, parent);


        return 12;
    }
}



