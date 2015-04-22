package layout;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MetalLayoutStyle
        extends LayoutStyle {
    private boolean isOcean;

    public MetalLayoutStyle() {
        this.isOcean = false;
        try {
            Method method = MetalLookAndFeel.class.getMethod("getCurrentTheme", (Class[]) null);

            this.isOcean = (((MetalTheme) method.invoke(null, (Object[]) null)).getName() == "Ocean");
        } catch (NoSuchMethodException nsme) {
        } catch (IllegalAccessException iae) {
        } catch (IllegalArgumentException iae2) {
        } catch (InvocationTargetException ite) {
        }
    }

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
        String sourceCID = source.getUIClassID();
        String targetCID = target.getUIClassID();
        int offset;
        int offset;
        if (type == 0) {
            if ((sourceCID == "ToggleButtonUI") && (targetCID == "ToggleButtonUI")) {
                ButtonModel sourceModel = ((JToggleButton) source).getModel();
                ButtonModel targetModel = ((JToggleButton) target).getModel();
                if (((sourceModel instanceof DefaultButtonModel)) && ((targetModel instanceof DefaultButtonModel)) && (((DefaultButtonModel) sourceModel).getGroup() == ((DefaultButtonModel) targetModel).getGroup()) && (((DefaultButtonModel) sourceModel).getGroup() != null)) {
                    return 2;
                }
                if (this.isOcean) {
                    return 6;
                }
                return 5;
            }
            offset = 6;
        } else {
            offset = 12;
        }
        if (((position == 3) || (position == 7)) && (((sourceCID == "LabelUI") && (targetCID != "LabelUI")) || ((sourceCID != "LabelUI") && (targetCID == "LabelUI")))) {
            return getCBRBPadding(source, target, position, offset + 6);
        }
        return getCBRBPadding(source, target, position, offset);
    }

    int getCBRBPadding(JComponent source, JComponent target, int position, int offset) {
        offset = super.getCBRBPadding(source, target, position, offset);
        if (offset > 0) {
            int buttonAdjustment = getButtonAdjustment(source, position);
            if (buttonAdjustment == 0) {
                buttonAdjustment = getButtonAdjustment(target, flipDirection(position));
            }
            offset -= buttonAdjustment;
        }
        if (offset < 0) {
            return 0;
        }
        return offset;
    }

    private int getButtonAdjustment(JComponent source, int edge) {
        String uid = source.getUIClassID();
        if ((uid == "ButtonUI") || (uid == "ToggleButtonUI")) {
            if ((!this.isOcean) && ((edge == 3) || (edge == 5))) {
                return 1;
            }
        } else if ((edge == 5) && (
                (uid == "RadioButtonUI") || ((!this.isOcean) && (uid == "CheckBoxUI")))) {
            return 1;
        }
        return 0;
    }

    public int getContainerGap(JComponent component, int position, Container parent) {
        super.getContainerGap(component, position, parent);


        return getCBRBPadding(component, position, 12 - getButtonAdjustment(component, position));
    }
}



