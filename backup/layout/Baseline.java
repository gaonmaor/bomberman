package layout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Baseline {
    private static final Rectangle viewRect = new Rectangle();
    private static final Rectangle textRect = new Rectangle();
    private static final Rectangle iconRect = new Rectangle();
    private static final int EDGE_SPACING = 2;
    private static final int TEXT_SPACING = 2;
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    private static final Map BASELINE_MAP = Collections.synchronizedMap(new HashMap());
    private static JLabel TABLE_LABEL;
    private static JLabel LIST_LABEL;
    private static JLabel TREE_LABEL;
    private static Class CLASSIC_WINDOWS;
    private static boolean checkedForClassic;
    private static Method COMPONENT_BASELINE_METHOD = null;

    static {
        try {
            COMPONENT_BASELINE_METHOD = class$java$awt$Component.getMethod("getBaseline", new Class[]{Integer.TYPE, Integer.TYPE});
        } catch (NoSuchMethodException nsme) {
        }
    }

    public static int getBaseline(JComponent component) {
        Dimension pref = component.getPreferredSize();
        return getBaseline(component, pref.width, pref.height);
    }

    private static Method getBaselineMethod(JComponent component) {
        if (COMPONENT_BASELINE_METHOD != null) {
            return COMPONENT_BASELINE_METHOD;
        }
        Class klass = component.getClass();
        while (klass != null) {
            if (BASELINE_MAP.containsKey(klass)) {
                Method method = (Method) BASELINE_MAP.get(klass);
                return method;
            }
            klass = klass.getSuperclass();
        }
        klass = component.getClass();
        Method[] methods = klass.getMethods();
        for (int i = methods.length - 1; i >= 0; i--) {
            Method method = methods[i];
            if ("getBaseline".equals(method.getName())) {
                Class[] params = method.getParameterTypes();
                if ((params.length == 2) && (params[0] == Integer.TYPE) && (params[1] == Integer.TYPE)) {
                    BASELINE_MAP.put(klass, method);
                    return method;
                }
            }
        }
        BASELINE_MAP.put(klass, null);
        return null;
    }

    private static int invokeBaseline(Method method, JComponent c, int width, int height) {
        int baseline = -1;
        try {
            baseline = ((Integer) method.invoke(c, new Object[]{new Integer(width), new Integer(height)})).intValue();
        } catch (IllegalAccessException iae) {
        } catch (IllegalArgumentException iae2) {
        } catch (InvocationTargetException ite2) {
        }
        return baseline;
    }

    public static int getBaseline(JComponent component, int width, int height) {
        Method baselineMethod = getBaselineMethod(component);
        if (baselineMethod != null) {
            return invokeBaseline(baselineMethod, component, width, height);
        }
        Object baselineImpl = UIManager.get("Baseline.instance");
        if ((baselineImpl != null) && ((baselineImpl instanceof Baseline))) {
            return ((Baseline) baselineImpl).getComponentBaseline(component, width, height);
        }
        String lookAndFeelID = UIManager.getLookAndFeel().getID();
        if ((lookAndFeelID != "Windows") && (lookAndFeelID != "Metal") && (lookAndFeelID != "GTK") && (lookAndFeelID != "Aqua")) {
            return -1;
        }
        String uid = component.getUIClassID();
        int baseline = -1;
        if ((uid == "ButtonUI") || (uid == "CheckBoxUI") || (uid == "RadioButtonUI") || (uid == "ToggleButtonUI")) {
            baseline = getButtonBaseline((AbstractButton) component, height);
        } else {
            if (uid == "ComboBoxUI") {
                return getComboBoxBaseline((JComboBox) component, height);
            }
            if (uid == "TextAreaUI") {
                return getTextAreaBaseline((JTextArea) component, height);
            }
            if ((uid == "FormattedTextFieldUI") || (uid == "PasswordFieldUI") || (uid == "TextFieldUI")) {
                baseline = getSingleLineTextBaseline((JTextComponent) component, height);
            } else if (uid == "LabelUI") {
                baseline = getLabelBaseline((JLabel) component, height);
            } else if (uid == "ListUI") {
                baseline = getListBaseline((JList) component, height);
            } else if (uid == "PanelUI") {
                baseline = getPanelBaseline((JPanel) component, height);
            } else if (uid == "ProgressBarUI") {
                baseline = getProgressBarBaseline((JProgressBar) component, height);
            } else if (uid == "SliderUI") {
                baseline = getSliderBaseline((JSlider) component, height);
            } else if (uid == "SpinnerUI") {
                baseline = getSpinnerBaseline((JSpinner) component, height);
            } else if (uid == "ScrollPaneUI") {
                baseline = getScrollPaneBaseline((JScrollPane) component, height);
            } else if (uid == "TabbedPaneUI") {
                baseline = getTabbedPaneBaseline((JTabbedPane) component, height);
            } else if (uid == "TableUI") {
                baseline = getTableBaseline((JTable) component, height);
            } else if (uid == "TreeUI") {
                baseline = getTreeBaseline((JTree) component, height);
            }
        }
        return Math.max(baseline, -1);
    }

    private static Insets rotateInsets(Insets topInsets, int targetPlacement) {
        switch (targetPlacement) {
            case 2:
                return new Insets(topInsets.left, topInsets.top, topInsets.right, topInsets.bottom);
            case 3:
                return new Insets(topInsets.bottom, topInsets.left, topInsets.top, topInsets.right);
            case 4:
                return new Insets(topInsets.left, topInsets.bottom, topInsets.right, topInsets.top);
        }
        return new Insets(topInsets.top, topInsets.left, topInsets.bottom, topInsets.right);
    }

    private static int getMaxTabHeight(JTabbedPane tp) {
        int fontHeight = tp.getFontMetrics(tp.getFont()).getHeight();
        int height = fontHeight;
        boolean tallerIcons = false;
        for (int counter = tp.getTabCount() - 1; counter >= 0; counter--) {
            Icon icon = tp.getIconAt(counter);
            if (icon != null) {
                int iconHeight = icon.getIconHeight();
                height = Math.max(height, iconHeight);
                if (iconHeight > fontHeight) {
                    tallerIcons = true;
                }
            }
        }
        Insets tabInsets = UIManager.getInsets("TabbedPane.tabInsets");
        height += 2;
        if ((!isMetal()) || (!tallerIcons)) {
            height += tabInsets.top + tabInsets.bottom;
        }
        return height;
    }

    private static int getTabbedPaneBaseline(JTabbedPane tp, int height) {
        if (tp.getTabCount() > 0) {
            if (isAqua()) {
                return getAquaTabbedPaneBaseline(tp, height);
            }
            Insets insets = tp.getInsets();
            Insets contentBorderInsets = UIManager.getInsets("TabbedPane.contentBorderInsets");

            Insets tabAreaInsets = rotateInsets(UIManager.getInsets("TabbedPane.tabAreaInsets"), tp.getTabPlacement());


            FontMetrics metrics = tp.getFontMetrics(tp.getFont());
            int maxHeight = getMaxTabHeight(tp);
            iconRect.setBounds(0, 0, 0, 0);
            textRect.setBounds(0, 0, 0, 0);
            viewRect.setBounds(0, 0, 32767, maxHeight);
            SwingUtilities.layoutCompoundLabel(tp, metrics, "A", null, 0, 0, 0, 11, viewRect, iconRect, textRect, 0);


            int baseline = textRect.y + metrics.getAscent();
            switch (tp.getTabPlacement()) {
                case 1:
                    baseline += insets.top + tabAreaInsets.top;
                    if (isWindows()) {
                        if (tp.getTabCount() > 1) {
                            baseline++;
                        } else {
                            baseline--;
                        }
                    }
                    return baseline;
                case 3:
                    baseline = tp.getHeight() - insets.bottom - tabAreaInsets.bottom - maxHeight + baseline;
                    if (isWindows()) {
                        if (tp.getTabCount() > 1) {
                            baseline--;
                        } else {
                            baseline++;
                        }
                    }
                    return baseline;
                case 2:
                case 4:
                    if (isAqua()) {
                        return -1;
                    }
                    baseline += insets.top + tabAreaInsets.top;
                    if (isWindows()) {
                        baseline += maxHeight % 2;
                    }
                    return baseline;
            }
        }
        return -1;
    }

    private static int getAquaTabbedPaneBaseline(JTabbedPane tp, int height) {
        Font font = tp.getFont();
        FontMetrics metrics = tp.getFontMetrics(font);
        int ascent = metrics.getAscent();
        switch (tp.getTabPlacement()) {
            case 1:
                int offset = 5;
                if (tp.getFont().getSize() > 12) {
                    offset = 6;
                }
                int yOffset = 20 - metrics.getHeight();
                yOffset /= 2;
                return offset + yOffset + ascent - 1;
            case 3:
                int offset;
                int offset;
                if (tp.getFont().getSize() > 12) {
                    offset = 6;
                } else {
                    offset = 4;
                }
                return height - (20 - ((20 - metrics.getHeight()) / 2 + ascent)) - offset;
            case 2:
            case 4:
                return -1;
        }
        return -1;
    }

    private static int getSliderBaseline(JSlider slider, int height) {
        if ((slider.getPaintLabels()) && (!isGTK())) {
            boolean isAqua = isAqua();
            FontMetrics metrics = slider.getFontMetrics(slider.getFont());
            Insets insets = slider.getInsets();
            Insets focusInsets = (Insets) UIManager.get("Slider.focusInsets");
            if (slider.getOrientation() == 0) {
                int tickLength = 8;
                int contentHeight = height - insets.top - insets.bottom - focusInsets.top - focusInsets.bottom;

                int thumbHeight = 20;
                if (isMetal()) {
                    tickLength = ((Integer) UIManager.get("Slider.majorTickLength")).intValue() + 5;

                    thumbHeight = UIManager.getIcon("Slider.horizontalThumbIcon").getIconHeight();
                } else if ((isWindows()) && (isXP())) {
                    thumbHeight++;
                }
                int centerSpacing = thumbHeight;
                if ((isAqua) || (slider.getPaintTicks())) {
                    centerSpacing += tickLength;
                }
                centerSpacing += metrics.getAscent() + metrics.getDescent();
                int trackY = insets.top + focusInsets.top + (contentHeight - centerSpacing - 1) / 2;
                if (isAqua) {
                    if (slider.getPaintTicks()) {
                        int prefHeight = slider.getUI().getPreferredSize(slider).height;

                        int prefDelta = height - prefHeight;
                        if (prefDelta > 0) {
                            trackY -= Math.min(1, prefDelta);
                        }
                    } else {
                        trackY--;
                    }
                }
                int trackHeight = thumbHeight;
                int tickY = trackY + trackHeight;
                int tickHeight = tickLength;
                if ((!isAqua) && (!slider.getPaintTicks())) {
                    tickHeight = 0;
                }
                int labelY = tickY + tickHeight;
                return labelY + metrics.getAscent();
            }
            boolean inverted = slider.getInverted();
            Integer value = inverted ? getMinSliderValue(slider) : getMaxSliderValue(slider);
            if (value != null) {
                int thumbHeight = 11;
                if (isMetal()) {
                    thumbHeight = UIManager.getIcon("Slider.verticalThumbIcon").getIconHeight();
                }
                int trackBuffer = Math.max(metrics.getHeight() / 2, thumbHeight / 2);

                int contentY = focusInsets.top + insets.top;
                int trackY = contentY + trackBuffer;
                int trackHeight = height - focusInsets.top - focusInsets.bottom - insets.top - insets.bottom - trackBuffer - trackBuffer;


                int maxValue = getMaxSliderValue(slider).intValue();
                int min = slider.getMinimum();
                int max = slider.getMaximum();
                double valueRange = max - min;
                double pixelsPerValue = trackHeight / valueRange;

                int trackBottom = trackY + (trackHeight - 1);
                if (isAqua) {
                    trackY -= 3;
                    trackBottom += 6;
                }
                int yPosition = trackY;
                double offset;
                double offset;
                if (!inverted) {
                    offset = pixelsPerValue * (max - value.intValue());
                } else {
                    offset = pixelsPerValue * (value.intValue() - min);
                }
                if (isAqua) {
                    yPosition = (int) (yPosition + Math.floor(offset));
                } else {
                    yPosition = (int) (yPosition + Math.round(offset));
                }
                yPosition = Math.max(trackY, yPosition);
                yPosition = Math.min(trackBottom, yPosition);
                if (isAqua) {
                    return yPosition + metrics.getAscent();
                }
                return yPosition - metrics.getHeight() / 2 + metrics.getAscent();
            }
        }
        return -1;
    }

    private static Integer getMaxSliderValue(JSlider slider) {
        Dictionary dictionary = slider.getLabelTable();
        if (dictionary != null) {
            Enumeration keys = dictionary.keys();
            int max = slider.getMinimum() - 1;
            while (keys.hasMoreElements()) {
                max = Math.max(max, ((Integer) keys.nextElement()).intValue());
            }
            if (max == slider.getMinimum() - 1) {
                return null;
            }
            return new Integer(max);
        }
        return null;
    }

    private static Integer getMinSliderValue(JSlider slider) {
        Dictionary dictionary = slider.getLabelTable();
        if (dictionary != null) {
            Enumeration keys = dictionary.keys();
            int min = slider.getMaximum() + 1;
            while (keys.hasMoreElements()) {
                min = Math.min(min, ((Integer) keys.nextElement()).intValue());
            }
            if (min == slider.getMaximum() + 1) {
                return null;
            }
            return new Integer(min);
        }
        return null;
    }

    private static int getProgressBarBaseline(JProgressBar pb, int height) {
        if ((pb.isStringPainted()) && (pb.getOrientation() == 0)) {
            FontMetrics metrics = pb.getFontMetrics(pb.getFont());
            Insets insets = pb.getInsets();
            int y = insets.top;
            if ((isWindows()) && (isXP())) {
                if (pb.isIndeterminate()) {
                    y = -1;
                    height--;
                } else {
                    y = 0;
                    height -= 3;
                }
            } else {
                if (isGTK()) {
                    return (height - metrics.getAscent() - metrics.getDescent()) / 2 + metrics.getAscent();
                }
                if (isAqua()) {
                    if (pb.isIndeterminate()) {
                        return -1;
                    }
                    y--;
                    height -= insets.top + insets.bottom;
                } else {
                    height -= insets.top + insets.bottom;
                }
            }
            return y + (height + metrics.getAscent() - metrics.getLeading() - metrics.getDescent()) / 2;
        }
        return -1;
    }

    private static int getTreeBaseline(JTree tree, int height) {
        int rowHeight = tree.getRowHeight();
        if (TREE_LABEL == null) {
            TREE_LABEL = new JLabel("X");
            TREE_LABEL.setIcon(UIManager.getIcon("Tree.closedIcon"));
        }
        JLabel label = TREE_LABEL;
        label.setFont(tree.getFont());
        if (rowHeight <= 0) {
            rowHeight = label.getPreferredSize().height;
        }
        return getLabelBaseline(label, rowHeight) + tree.getInsets().top;
    }

    private static int getTableBaseline(JTable table, int height) {
        if (TABLE_LABEL == null) {
            TABLE_LABEL = new JLabel("");
            TABLE_LABEL.setBorder(new EmptyBorder(1, 1, 1, 1));
        }
        JLabel label = TABLE_LABEL;
        label.setFont(table.getFont());
        int rowMargin = table.getRowMargin();
        int baseline = getLabelBaseline(label, table.getRowHeight() - rowMargin);

        return baseline += rowMargin / 2;
    }

    private static int getTextAreaBaseline(JTextArea text, int height) {
        Insets insets = text.getInsets();
        FontMetrics fm = text.getFontMetrics(text.getFont());
        return insets.top + fm.getAscent();
    }

    private static int getListBaseline(JList list, int height) {
        int rowHeight = list.getFixedCellHeight();
        if (LIST_LABEL == null) {
            LIST_LABEL = new JLabel("X");
            LIST_LABEL.setBorder(new EmptyBorder(1, 1, 1, 1));
        }
        JLabel label = LIST_LABEL;
        label.setFont(list.getFont());
        if (rowHeight == -1) {
            rowHeight = label.getPreferredSize().height;
        }
        return getLabelBaseline(label, rowHeight) + list.getInsets().top;
    }

    private static int getScrollPaneBaseline(JScrollPane sp, int height) {
        Component view = sp.getViewport().getView();
        if ((view instanceof JComponent)) {
            int baseline = getBaseline((JComponent) view);
            if (baseline > 0) {
                return baseline + sp.getViewport().getY();
            }
        }
        return -1;
    }

    private static int getPanelBaseline(JPanel panel, int height) {
        Border border = panel.getBorder();
        if ((border instanceof TitledBorder)) {
            TitledBorder titledBorder = (TitledBorder) border;
            if ((titledBorder.getTitle() != null) && (!"".equals(titledBorder.getTitle()))) {
                Font font = titledBorder.getTitleFont();
                if (font == null) {
                    font = panel.getFont();
                    if (font == null) {
                        font = new Font("Dialog", 0, 12);
                    }
                }
                Border border2 = titledBorder.getBorder();
                Insets borderInsets;
                Insets borderInsets;
                if (border2 != null) {
                    borderInsets = border2.getBorderInsets(panel);
                } else {
                    borderInsets = EMPTY_INSETS;
                }
                FontMetrics fm = panel.getFontMetrics(font);
                int fontHeight = fm.getHeight();
                int descent = fm.getDescent();
                int ascent = fm.getAscent();
                int y = 2;
                int h = height - 4;
                switch (((TitledBorder) border).getTitlePosition()) {
                    case 1:
                        int diff = ascent + descent + (Math.max(2, 4) - 2);

                        return y + diff - (descent + 2);
                    case 0:
                    case 2:
                        int diff = Math.max(0, ascent / 2 + 2 - 2);

                        return y + diff - descent + (borderInsets.top + ascent + descent) / 2;
                    case 3:
                        return y + borderInsets.top + ascent + 2;
                    case 4:
                        return y + h - (borderInsets.bottom + descent + 2);
                    case 5:
                        h -= fontHeight / 2;
                        return y + h - descent + (ascent + descent - borderInsets.bottom) / 2;
                    case 6:
                        h -= fontHeight;
                        return y + h + ascent + 2;
                }
            }
        }
        return -1;
    }

    private static int getSpinnerBaseline(JSpinner spinner, int height) {
        JComponent editor = spinner.getEditor();
        if ((editor instanceof JSpinner.DefaultEditor)) {
            JSpinner.DefaultEditor defaultEditor = (JSpinner.DefaultEditor) editor;

            JTextField tf = defaultEditor.getTextField();
            Insets spinnerInsets = spinner.getInsets();
            Insets editorInsets = defaultEditor.getInsets();
            int offset = spinnerInsets.top + editorInsets.top;
            height -= offset + spinnerInsets.bottom + editorInsets.bottom;
            if (height <= 0) {
                return -1;
            }
            return offset + getSingleLineTextBaseline(tf, height);
        }
        Insets insets = spinner.getInsets();
        FontMetrics fm = spinner.getFontMetrics(spinner.getFont());
        return insets.top + fm.getAscent();
    }

    private static int getLabelBaseline(JLabel label, int height) {
        Icon icon = label.isEnabled() ? label.getIcon() : label.getDisabledIcon();

        FontMetrics fm = label.getFontMetrics(label.getFont());

        resetRects(label, height);

        SwingUtilities.layoutCompoundLabel(label, fm, "a", icon, label.getVerticalAlignment(), label.getHorizontalAlignment(), label.getVerticalTextPosition(), label.getHorizontalTextPosition(), viewRect, iconRect, textRect, label.getIconTextGap());


        return textRect.y + fm.getAscent();
    }

    private static int getComboBoxBaseline(JComboBox combobox, int height) {
        Insets insets = combobox.getInsets();
        int y = insets.top;
        height -= insets.top + insets.bottom;
        if (combobox.isEditable()) {
            ComboBoxEditor editor = combobox.getEditor();
            if ((editor != null) && ((editor.getEditorComponent() instanceof JTextField))) {
                JTextField tf = (JTextField) editor.getEditorComponent();
                return y + getSingleLineTextBaseline(tf, height);
            }
        }
        if (isMetal()) {
            if (isOceanTheme()) {
                y += 2;
                height -= 4;
            }
        } else if (isWindows()) {
            String osVersion = System.getProperty("os.version");
            if (osVersion != null) {
                Float version = Float.valueOf(osVersion);
                if (version.floatValue() > 4.0D) {
                    y += 2;
                    height -= 4;
                }
            }
        }
        ListCellRenderer renderer = combobox.getRenderer();
        if ((renderer instanceof JLabel)) {
            int baseline = y + getLabelBaseline((JLabel) renderer, height);
            if (isAqua()) {
                return baseline - 1;
            }
            return baseline;
        }
        FontMetrics fm = combobox.getFontMetrics(combobox.getFont());
        return y + fm.getAscent();
    }

    private static int getSingleLineTextBaseline(JTextComponent textComponent, int h) {
        View rootView = textComponent.getUI().getRootView(textComponent);
        if (rootView.getViewCount() > 0) {
            Insets insets = textComponent.getInsets();
            int height = h - insets.top - insets.bottom;
            int y = insets.top;
            View fieldView = rootView.getView(0);
            int vspan = (int) fieldView.getPreferredSpan(1);
            if (height != vspan) {
                int slop = height - vspan;
                y += slop / 2;
            }
            FontMetrics fm = textComponent.getFontMetrics(textComponent.getFont());

            y += fm.getAscent();
            return y;
        }
        return -1;
    }

    private static int getButtonBaseline(AbstractButton button, int height) {
        FontMetrics fm = button.getFontMetrics(button.getFont());

        resetRects(button, height);

        String text = button.getText();
        if ((text != null) && (text.startsWith("<html>"))) {
            return -1;
        }
        SwingUtilities.layoutCompoundLabel(button, fm, "a", button.getIcon(), button.getVerticalAlignment(), button.getHorizontalAlignment(), button.getVerticalTextPosition(), button.getHorizontalTextPosition(), viewRect, iconRect, textRect, text == null ? 0 : button.getIconTextGap());
        if (isAqua()) {
            return textRect.y + fm.getAscent() + 1;
        }
        return textRect.y + fm.getAscent();
    }

    private static void resetRects(JComponent c, int height) {
        Insets insets = c.getInsets();
        viewRect.x = insets.left;
        viewRect.y = insets.top;
        viewRect.width = (c.getWidth() - (insets.right + viewRect.x));
        viewRect.height = (height - (insets.bottom + viewRect.y));
        textRect.x = (textRect.y = textRect.width = textRect.height = 0);
        iconRect.x = (iconRect.y = iconRect.width = iconRect.height = 0);
    }

    private static boolean isOceanTheme() {
        try {
            Field field = MetalLookAndFeel.class.getDeclaredField("currentTheme");
            field.setAccessible(true);
            Object theme = field.get(null);
            return "javax.swing.plaf.metal.OceanTheme".equals(theme.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private static boolean isWindows() {
        return UIManager.getLookAndFeel().getID() == "Windows";
    }

    private static boolean isMetal() {
        return UIManager.getLookAndFeel().getID() == "Metal";
    }

    private static boolean isGTK() {
        return UIManager.getLookAndFeel().getID() == "GTK";
    }

    private static boolean isAqua() {
        return UIManager.getLookAndFeel().getID() == "Aqua";
    }

    private static boolean isXP() {
        if (!checkedForClassic) {
            try {
                CLASSIC_WINDOWS = Class.forName("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
            } catch (ClassNotFoundException e) {
            }
            checkedForClassic = true;
        }
        if ((CLASSIC_WINDOWS != null) && (CLASSIC_WINDOWS.isInstance(UIManager.getLookAndFeel()))) {
            return false;
        }
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Boolean themeActive = (Boolean) toolkit.getDesktopProperty("win.xpstyle.themeActive");
        if (themeActive == null) {
            themeActive = Boolean.FALSE;
        }
        return themeActive.booleanValue();
    }

    public int getComponentBaseline(JComponent component, int width, int height) {
        return -1;
    }
}



