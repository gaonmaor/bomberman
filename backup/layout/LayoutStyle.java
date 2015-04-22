package layout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import java.awt.*;

public class LayoutStyle {
    public static final int RELATED = 0;
    public static final int UNRELATED = 1;
    public static final int INDENT = 3;
    private static final boolean USE_CORE_LAYOUT_STYLE;
    private static LayoutStyle layoutStyle;
    private static LookAndFeel laf;

    static {
        boolean useCoreLayoutStyle = false;
        try {
            Class.forName("javax.swing.LayoutStyle");
            useCoreLayoutStyle = true;
        } catch (ClassNotFoundException cnfe) {
        }
        USE_CORE_LAYOUT_STYLE = useCoreLayoutStyle;
    }

    public static LayoutStyle getSharedInstance() {
        Object layoutImpl = UIManager.get("LayoutStyle.instance");
        if ((layoutImpl != null) && ((layoutImpl instanceof LayoutStyle))) {
            return (LayoutStyle) layoutImpl;
        }
        LookAndFeel currentLAF = UIManager.getLookAndFeel();
        if ((layoutStyle == null) || (currentLAF != laf)) {
            laf = currentLAF;
            String lafID = laf.getID();
            if (USE_CORE_LAYOUT_STYLE) {
                layoutStyle = new SwingLayoutStyle();
            } else if ("Metal" == lafID) {
                layoutStyle = new MetalLayoutStyle();
            } else if ("Windows" == lafID) {
                layoutStyle = new WindowsLayoutStyle();
            } else if ("GTK" == lafID) {
                layoutStyle = new GnomeLayoutStyle();
            } else if ("Aqua" == lafID) {
                layoutStyle = new AquaLayoutStyle();
            } else {
                layoutStyle = new LayoutStyle();
            }
        }
        return layoutStyle;
    }

    public static void setSharedInstance(LayoutStyle layoutStyle) {
        UIManager.getLookAndFeelDefaults().put("LayoutStyle.instance", layoutStyle);
    }

    public int getPreferredGap(JComponent component1, JComponent component2, int type, int position, Container parent) {
        if ((position != 1) && (position != 5) && (position != 7) && (position != 3)) {
            throw new IllegalArgumentException("Invalid position");
        }
        if ((component1 == null) || (component2 == null)) {
            throw new IllegalArgumentException("Components must be non-null");
        }
        if (type == 0) {
            return 6;
        }
        if (type == 1) {
            return 12;
        }
        if (type == 3) {
            if ((position == 3) || (position == 7)) {
                int gap = getButtonChildIndent(component1, position);
                if (gap != 0) {
                    return gap;
                }
                return 6;
            }
            return 6;
        }
        throw new IllegalArgumentException("Invalid type");
    }

    public int getContainerGap(JComponent component, int position, Container parent) {
        if ((position != 1) && (position != 5) && (position != 7) && (position != 3)) {
            throw new IllegalArgumentException("Invalid position");
        }
        if (component == null) {
            throw new IllegalArgumentException("Component must be non-null");
        }
        return 12;
    }

    boolean isDialog(JComponent component) {
        String name = component.getName();
        return (name != null) && (name.endsWith(".contentPane"));
    }

    int getCBRBPadding(JComponent source, JComponent target, int position, int offset) {
        offset -= getCBRBPadding(source, position);
        if (offset > 0) {
            offset -= getCBRBPadding(target, flipDirection(position));
        }
        if (offset < 0) {
            return 0;
        }
        return offset;
    }

    int getCBRBPadding(JComponent source, int position, int offset) {
        offset -= getCBRBPadding(source, position);
        return Math.max(offset, 0);
    }

    int flipDirection(int position) {
        switch (position) {
            case 1:
                return 5;
            case 5:
                return 1;
            case 3:
                return 7;
            case 7:
                return 3;
        }
        if (!$assertionsDisabled) {
            throw new AssertionError();
        }
        return 0;
    }

    private int getCBRBPadding(JComponent c, int position) {
        if ((c.getUIClassID() == "CheckBoxUI") || (c.getUIClassID() == "RadioButtonUI")) {
            Border border = c.getBorder();
            if ((border instanceof UIResource)) {
                return getInset(c, position);
            }
        }
        return 0;
    }

    private int getInset(JComponent c, int position) {
        Insets insets = c.getInsets();
        switch (position) {
            case 1:
                return insets.top;
            case 5:
                return insets.bottom;
            case 3:
                return insets.right;
            case 7:
                return insets.left;
        }
        if (!$assertionsDisabled) {
            throw new AssertionError();
        }
        return 0;
    }

    private boolean isLeftAligned(AbstractButton button, int position) {
        if (position == 7) {
            boolean ltr = button.getComponentOrientation().isLeftToRight();
            int hAlign = button.getHorizontalAlignment();
            return ((ltr) && ((hAlign == 2) || (hAlign == 10))) || ((!ltr) && (hAlign == 11));
        }
        return false;
    }

    private boolean isRightAligned(AbstractButton button, int position) {
        if (position == 3) {
            boolean ltr = button.getComponentOrientation().isLeftToRight();
            int hAlign = button.getHorizontalAlignment();
            return ((ltr) && ((hAlign == 4) || (hAlign == 11))) || ((!ltr) && (hAlign == 10));
        }
        return false;
    }

    private Icon getIcon(AbstractButton button) {
        Icon icon = button.getIcon();
        if (icon != null) {
            return icon;
        }
        String key = null;
        if ((button instanceof JCheckBox)) {
            key = "CheckBox.icon";
        } else if ((button instanceof JRadioButton)) {
            key = "RadioButton.icon";
        }
        if (key != null) {
            Object oIcon = UIManager.get(key);
            if ((oIcon instanceof Icon)) {
                return (Icon) oIcon;
            }
        }
        return null;
    }

    int getButtonChildIndent(JComponent c, int position) {
        if (((c instanceof JRadioButton)) || ((c instanceof JCheckBox))) {
            AbstractButton button = (AbstractButton) c;
            Insets insets = c.getInsets();
            Icon icon = getIcon(button);
            int gap = button.getIconTextGap();
            if (isLeftAligned(button, position)) {
                return insets.left + icon.getIconWidth() + gap;
            }
            if (isRightAligned(button, position)) {
                return insets.right + icon.getIconWidth() + gap;
            }
        }
        return 0;
    }
}



