package layout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.applet.Applet;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class AquaLayoutStyle
        extends LayoutStyle {
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    private static final int MINI = 0;
    private static final int SMALL = 1;
    private static final int REGULAR = 2;
    private static final Object[][] containerGapDefinitions = {{"TabbedPaneUI", new Insets(6, 10, 10, 10), new Insets(6, 10, 10, 12), new Insets(12, 20, 20, 20)}, {"RootPaneUI", new Insets(8, 10, 10, 10), new Insets(8, 10, 10, 12), new Insets(14, 20, 20, 20)}, {"default", new Insets(8, 10, 10, 10), new Insets(8, 10, 10, 12), new Insets(14, 20, 20, 20)}};
    private static final Object[][] relatedGapDefinitions = {{"ButtonUI", "ButtonUI.push", "ButtonUI.text", "ToggleButtonUI.push", "ToggleButtonUI.text", new Insets(8, 8, 8, 8), new Insets(10, 10, 10, 10), new Insets(12, 12, 12, 12)}, {"ButtonUI.metal", "ToggleButtonUI.metal", new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8), new Insets(12, 12, 12, 12)}, {"ButtonUI.bevel", "ButtonUI.toggle", "ButtonUI.square", "ToggleButtonUI", "ToggleButtonUI.bevel", "ToggleButtonUI.square", "ToggleButtonUI.toggle", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0)}, {"ButtonUI.bevel.largeIcon", "ToggleButtonUI.bevel.largeIcon", new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8)}, {"ButtonUI.icon", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0)}, {"ButtonUI.icon.largeIcon", new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8)}, {"ButtonUI.round", "ToggleButtonUI.round", new Insets(12, 12, 12, 12), new Insets(12, 12, 12, 12), new Insets(12, 12, 12, 12)}, {"ButtonUI.help", new Insets(12, 12, 12, 12), new Insets(12, 12, 12, 12), new Insets(12, 12, 12, 12)}, {"ButtonUI.toggleCenter", "ToggleButtonUI.toggleCenter", new Insets(8, 0, 8, 0), new Insets(10, 0, 10, 0), new Insets(12, 0, 12, 0)}, {"ButtonUI.toggleEast", "ToggleButtonUI.toggleEast", new Insets(8, 0, 8, 8), new Insets(10, 0, 10, 10), new Insets(12, 0, 12, 12)}, {"ButtonUI.toggleWest", "ToggleButtonUI.toggleWest", new Insets(8, 8, 8, 0), new Insets(10, 10, 10, 0), new Insets(12, 12, 12, 0)}, {"ButtonUI.toolBarTab", "ToggleButtonUI.toolBarTab", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0)}, {"ButtonUI.colorWell", "ToggleButtonUI.colorWell", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0)}, {"CheckBoxUI", new Insets(6, 5, 6, 5), new Insets(7, 6, 7, 6), new Insets(7, 6, 7, 6)}, {"ComboBoxUI", new Insets(8, 5, 8, 5), new Insets(10, 6, 10, 6), new Insets(12, 8, 12, 8)}, {"LabelUI", new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8), new Insets(8, 8, 8, 8)}, {"ListUI", new Insets(5, 5, 5, 5), new Insets(6, 6, 6, 6), new Insets(6, 6, 6, 6)}, {"PanelUI", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0)}, {"ProgressBarUI", new Insets(8, 8, 8, 8), new Insets(10, 10, 10, 10), new Insets(12, 12, 12, 12)}, {"RadioButtonUI", new Insets(5, 5, 5, 5), new Insets(6, 6, 6, 6), new Insets(6, 6, 6, 6)}, {"ScrollPaneUI", new Insets(6, 8, 6, 8), new Insets(6, 8, 6, 8), new Insets(8, 10, 8, 10)}, {"SeparatorUI", new Insets(8, 8, 8, 8), new Insets(10, 10, 10, 10), new Insets(12, 12, 12, 12)}, {"SliderUI.horizontal", new Insets(8, 8, 8, 8), new Insets(10, 10, 10, 10), new Insets(12, 12, 12, 12)}, {"SliderUI.vertical", new Insets(8, 8, 8, 8), new Insets(10, 10, 10, 10), new Insets(12, 12, 12, 12)}, {"SpinnerUI", new Insets(6, 8, 6, 8), new Insets(6, 8, 6, 8), new Insets(8, 10, 8, 10)}, {"SplitPaneUI", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0)}, {"TabbedPaneUI", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0)}, {"TableUI", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0)}, {"TextAreaUI", "EditorPaneUI", "TextPaneUI", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0)}, {"TextFieldUI", "FormattedTextFieldUI", "PasswordFieldUI", new Insets(6, 8, 6, 8), new Insets(6, 8, 6, 8), new Insets(8, 10, 8, 10)}, {"TreeUI", new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0)}};
    private static final Object[][] unrelatedGapDefinitions = {{"ButtonUI.help", new Insets(24, 24, 24, 24), new Insets(24, 24, 24, 24), new Insets(24, 24, 24, 24)}, {"default", new Insets(10, 10, 10, 10), new Insets(12, 12, 12, 12), new Insets(14, 14, 14, 14)}};
    private static final Object[][] indentGapDefinitions = {{"CheckBoxUI", "RadioButtonUI", new Insets(16, 24, 16, 24), new Insets(20, 24, 20, 24), new Insets(24, 24, 24, 24)}, {"default", new Insets(16, 16, 16, 16), new Insets(20, 20, 20, 20), new Insets(24, 24, 24, 24)}};
    private static final Object[][] visualMarginDefinitions = {{"ButtonUI", "ButtonUI.text", "ToggleButtonUI", "ToggleButtonUI.text", new Insets(5, 3, 3, 3)}, {"ButtonUI.icon", "ToggleButtonUI.icon", new Insets(5, 2, 3, 2)}, {"ButtonUI.toolbar", "ToggleButtonUI.toolbar", new Insets(0, 0, 0, 0)}, {"CheckBoxUI", new Insets(4, 4, 3, 3)}, {"ComboBoxUI", new Insets(2, 3, 4, 3)}, {"DesktopPaneUI", new Insets(0, 0, 0, 0)}, {"EditorPaneUI", "TextAreaUI", "TextPaneUI", new Insets(0, 0, 0, 0)}, {"FormattedTextFieldUI", "PasswordFieldUI", "TextFieldUI", new Insets(0, 0, 0, 0)}, {"LabelUI", new Insets(0, 0, 0, 0)}, {"ListUI", new Insets(0, 0, 0, 0)}, {"PanelUI", new Insets(0, 0, 0, 0)}, {"ProgressBarUI", "ProgressBarUI.horizontal", new Insets(0, 2, 4, 2)}, {"ProgressBarUI.vertical", new Insets(2, 0, 2, 4)}, {"RadioButtonUI", new Insets(4, 4, 3, 3)}, {"ScrollBarUI", new Insets(0, 0, 0, 0)}, {"ScrollPaneUI", new Insets(0, 0, 0, 0)}, {"SpinnerUI", new Insets(0, 0, 0, 0)}, {"SeparatorUI", new Insets(0, 0, 0, 0)}, {"SplitPaneUI", new Insets(0, 0, 0, 0)}, {"SliderUI", "SliderUI.horizontal", new Insets(3, 6, 3, 6)}, {"SliderUI.vertical", new Insets(6, 3, 6, 3)}, {"TabbedPaneUI", "TabbedPaneUI.top", new Insets(5, 7, 10, 7)}, {"TabbedPaneUI.bottom", new Insets(4, 7, 5, 7)}, {"TabbedPaneUI.left", new Insets(4, 6, 10, 7)}, {"TabbedPaneUI.right", new Insets(4, 7, 10, 6)}, {"TableUI", new Insets(0, 0, 0, 0)}, {"TreeUI", new Insets(0, 0, 0, 0)}, {"default", new Insets(0, 0, 0, 0)}};
    private static final Map RELATED_GAPS = createInsetsMap(relatedGapDefinitions);
    private static final Map UNRELATED_GAPS = createInsetsMap(unrelatedGapDefinitions);
    private static final Map CONTAINER_GAPS = createInsetsMap(containerGapDefinitions);
    private static final Map INDENT_GAPS = createInsetsMap(indentGapDefinitions);
    private static final Map VISUAL_MARGINS = createInsetsMap(visualMarginDefinitions);

    private static Map createInsetsMap(Object[][] definitions) {
        Map map = new HashMap();
        for (int i = 0; i < definitions.length; i++) {
            int keys = 0;
            while ((keys < definitions[i].length) && ((definitions[i][keys] instanceof String))) {
                keys++;
            }
            Insets[] values = new Insets[definitions[i].length - keys];
            for (int j = keys; j < definitions[i].length; j++) {
                values[(j - keys)] = ((Insets) definitions[i][j]);
            }
            for (int j = 0; j < keys; j++) {
                String key = (String) definitions[i][j];
                int subindex = key.indexOf('.');
                if (subindex == -1) {
                    ComponentInsets componentInsets = (ComponentInsets) map.get(key);
                    if (componentInsets == null) {
                        componentInsets = new ComponentInsets(values);
                        map.put(key, new ComponentInsets(values));
                    } else {
                        assert (componentInsets.getInsets() == null);
                        componentInsets.setInsets(values);
                    }
                } else {
                    String subkey = key.substring(subindex + 1);
                    String parentKey = key.substring(0, subindex);
                    ComponentInsets componentInsets = (ComponentInsets) map.get(parentKey);
                    if (componentInsets == null) {
                        componentInsets = new ComponentInsets();
                        map.put(parentKey, componentInsets);
                    }
                    componentInsets.addSubinsets(subkey, new ComponentInsets(values));
                }
            }
        }
        return map;
    }

    public static void main(String[] args) {
        JButton button = new JButton();
        button.putClientProperty("JButton.buttonType", "metal");
        JButton button2 = new JButton();
        LayoutStyle style = new AquaLayoutStyle();
        int gap = style.getPreferredGap(button, button2, 0, 3, null);


        System.err.println("gap= " + gap);
        button.putClientProperty("JButton.buttonType", "square");
        button2.putClientProperty("JButton.buttonType", "square");
        gap = style.getPreferredGap(button, button2, 0, 3, null);


        System.err.println("gap= " + gap);
    }

    public int getPreferredGap(JComponent component1, JComponent component2, int type, int position, Container parent) {
        super.getPreferredGap(component1, component2, type, position, parent);
        int result;
        if (type == 3) {
            if ((position == 3) || (position == 7)) {
                int gap = getButtonChildIndent(component1, position);
                if (gap != 0) {
                    return gap;
                }
            }
            int sizeStyle = getSizeStyle(component1);
            Insets gap1 = getPreferredGap(component1, type, sizeStyle);
            int result;
            int result;
            int result;
            int result;
            switch (position) {
                case 1:
                    result = gap1.bottom;
                    break;
                case 5:
                    result = gap1.top;
                    break;
                case 3:
                    result = gap1.left;
                    break;
                case 2:
                case 4:
                case 6:
                case 7:
                default:
                    result = gap1.right;
            }
            int raw = result;

            Insets visualMargin2 = getVisualMargin(component2);
            switch (position) {
                case 1:
                    result -= visualMargin2.bottom;
                    break;
                case 5:
                    result -= visualMargin2.top;
                    break;
                case 3:
                    result -= visualMargin2.left;
                    break;
                case 7:
                    result -= visualMargin2.right;
            }
        } else {
            int sizeStyle = Math.min(getSizeStyle(component1), getSizeStyle(component2));

            Insets gap1 = getPreferredGap(component1, type, sizeStyle);
            Insets gap2 = getPreferredGap(component2, type, sizeStyle);
            int result;
            int result;
            int result;
            switch (position) {
                case 1:
                    result = Math.max(gap1.top, gap2.bottom);
                    break;
                case 5:
                    result = Math.max(gap1.bottom, gap2.top);
                    break;
                case 3:
                    result = Math.max(gap1.right, gap2.left);
                    break;
                case 2:
                case 4:
                case 6:
                case 7:
                default:
                    result = Math.max(gap1.left, gap2.right);
            }
            Insets visualMargin1 = getVisualMargin(component1);
            Insets visualMargin2 = getVisualMargin(component2);
            switch (position) {
                case 1:
                    result -= visualMargin1.top + visualMargin2.bottom;
                    break;
                case 5:
                    result -= visualMargin1.bottom + visualMargin2.top;
                    break;
                case 3:
                    result -= visualMargin1.right + visualMargin2.left;
                    break;
                case 7:
                    result -= visualMargin1.left + visualMargin2.right;
            }
        }
        return Math.max(0, result);
    }

    private Insets getPreferredGap(JComponent component, int type, int sizeStyle) {
        Map gapMap;
        Map gapMap;
        Map gapMap;
        switch (type) {
            case 3:
                gapMap = INDENT_GAPS;
                break;
            case 0:
                gapMap = RELATED_GAPS;
                break;
            case 1:
            case 2:
            default:
                gapMap = UNRELATED_GAPS;
        }
        String uid = component.getUIClassID();
        String style = null;
        if ((uid == "ButtonUI") || (uid == "ToggleButtonUI")) {
            style = (String) component.getClientProperty("JButton.buttonType");
        } else if (uid == "ProgressBarUI") {
            style = ((JProgressBar) component).getOrientation() == 0 ? "horizontal" : "vertical";
        } else if (uid == "SliderUI") {
            style = ((JSlider) component).getOrientation() == 0 ? "horizontal" : "vertical";
        } else if (uid == "TabbedPaneUI") {
            switch (((JTabbedPane) component).getTabPlacement()) {
                case 1:
                    style = "top";
                    break;
                case 2:
                    style = "left";
                    break;
                case 3:
                    style = "bottom";
                    break;
                case 4:
                    style = "right";
            }
        }
        return getInsets(gapMap, uid, style, sizeStyle);
    }

    public int getContainerGap(JComponent component, int position, Container parent) {
        int sizeStyle = Math.min(getSizeStyle(component), getSizeStyle(parent));


        Insets gap = getContainerGap(parent, sizeStyle);
        int result;
        int result;
        int result;
        int result;
        switch (position) {
            case 1:
                result = gap.top;
                break;
            case 5:
                result = gap.bottom;
                break;
            case 3:
                result = gap.right;
                break;
            case 2:
            case 4:
            case 6:
            case 7:
            default:
                result = gap.left;
        }
        Insets visualMargin = getVisualMargin(component);
        switch (position) {
            case 1:
                result -= visualMargin.top;
                break;
            case 5:
                result -= visualMargin.bottom;
                if ((component instanceof JRadioButton)) {
                    result--;
                }
                break;
            case 3:
                result -= visualMargin.left;
                break;
            case 7:
                result -= visualMargin.right;
        }
        return Math.max(0, result);
    }

    private Insets getContainerGap(Container container, int sizeStyle) {
        String uid;
        String uid;
        if ((container instanceof JComponent)) {
            uid = ((JComponent) container).getUIClassID();
        } else {
            String uid;
            if ((container instanceof Dialog)) {
                uid = "Dialog";
            } else {
                String uid;
                if ((container instanceof Frame)) {
                    uid = "Frame";
                } else {
                    String uid;
                    if ((container instanceof Applet)) {
                        uid = "Applet";
                    } else {
                        String uid;
                        if ((container instanceof Panel)) {
                            uid = "Panel";
                        } else {
                            uid = "default";
                        }
                    }
                }
            }
        }
        return getInsets(CONTAINER_GAPS, uid, null, sizeStyle);
    }

    private Insets getInsets(Map gapMap, String uid, String style, int sizeStyle) {
        if (uid == null) {
            uid = "default";
        }
        ComponentInsets componentInsets = (ComponentInsets) gapMap.get(uid);
        if (componentInsets == null) {
            componentInsets = (ComponentInsets) gapMap.get("default");
            if (componentInsets == null) {
                return EMPTY_INSETS;
            }
        } else if (style != null) {
            ComponentInsets subInsets = componentInsets.getSubinsets(style);
            if (subInsets != null) {
                componentInsets = subInsets;
            }
        }
        return componentInsets.getInsets(sizeStyle);
    }

    private Insets getVisualMargin(JComponent component) {
        String uid = component.getUIClassID();
        String style = null;
        if ((uid == "ButtonUI") || (uid == "ToggleButtonUI")) {
            style = (String) component.getClientProperty("JButton.buttonType");
        } else if (uid == "ProgressBarUI") {
            style = ((JProgressBar) component).getOrientation() == 0 ? "horizontal" : "vertical";
        } else if (uid == "SliderUI") {
            style = ((JSlider) component).getOrientation() == 0 ? "horizontal" : "vertical";
        } else if (uid == "TabbedPaneUI") {
            switch (((JTabbedPane) component).getTabPlacement()) {
                case 1:
                    style = "top";
                    break;
                case 2:
                    style = "left";
                    break;
                case 3:
                    style = "bottom";
                    break;
                case 4:
                    style = "right";
            }
        }
        Insets gap = getInsets(VISUAL_MARGINS, uid, style, 0);
        if ((uid == "RadioButtonUI") || (uid == "CheckBoxUI")) {
            switch (((AbstractButton) component).getHorizontalTextPosition()) {
                case 4:
                    gap = new Insets(gap.top, gap.right, gap.bottom, gap.left);
                    break;
                case 0:
                    gap = new Insets(gap.top, gap.right, gap.bottom, gap.right);
            }
            if ((component.getBorder() instanceof EmptyBorder)) {
                gap.left -= 2;
                gap.right -= 2;
                gap.top -= 2;
                gap.bottom -= 2;
            }
        }
        return gap;
    }

    private int getSizeStyle(Component c) {
        if (c == null) {
            return 2;
        }
        Font font = c.getFont();
        if (font == null) {
            return 2;
        }
        int fontSize = font.getSize();
        return fontSize > 9 ? 1 : fontSize >= 13 ? 2 : 0;
    }

    private static class ComponentInsets {
        private Map children;
        private Insets[] insets;

        public ComponentInsets() {
        }

        public ComponentInsets(Insets[] insets) {
            this.insets = insets;
        }

        public Insets[] getInsets() {
            return this.insets;
        }

        public void setInsets(Insets[] insets) {
            this.insets = insets;
        }

        public Insets getInsets(int size) {
            if (this.insets == null) {
                return AquaLayoutStyle.EMPTY_INSETS;
            }
            return this.insets[size];
        }

        void addSubinsets(String subkey, ComponentInsets subinsets) {
            if (this.children == null) {
                this.children = new HashMap(5);
            }
            this.children.put(subkey, subinsets);
        }

        ComponentInsets getSubinsets(String subkey) {
            return this.children == null ? null : (ComponentInsets) this.children.get(subkey);
        }
    }
}



