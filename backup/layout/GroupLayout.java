package layout;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GroupLayout
        implements LayoutManager2 {
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;
    public static final int LEADING = 1;
    public static final int TRAILING = 2;
    public static final int CENTER = 3;
    public static final int BASELINE = 3;
    public static final int DEFAULT_SIZE = -1;
    public static final int PREFERRED_SIZE = -2;
    private static final int MIN_SIZE = 0;
    private static final int PREF_SIZE = 1;
    private static final int MAX_SIZE = 2;
    private static final int UNSET = -2147483648;
    private static final int NO_ALIGNMENT = 0;
    private boolean autocreatePadding;
    private boolean autocreateContainerPadding;
    private Group horizontalGroup;
    private Group verticalGroup;
    private Map componentInfos;
    private Container host;
    private List parallelList;
    private boolean springsChanged;
    private boolean hasPreferredPaddingSprings;

    public GroupLayout(Container host) {
        if (host == null) {
            throw new IllegalArgumentException("Container must be non-null");
        }
        this.host = host;
        setHorizontalGroup(createParallelGroup(1, true));
        setVerticalGroup(createParallelGroup(1, true));
        this.componentInfos = new HashMap();
        this.autocreatePadding = false;
        this.parallelList = new ArrayList();
    }

    private static void checkSize(int min, int pref, int max, boolean isComponentSpring) {
        checkResizeType(min, isComponentSpring);
        if ((!isComponentSpring) && (pref < 0)) {
            throw new IllegalArgumentException("Pref must be >= 0");
        }
        checkResizeType(max, isComponentSpring);
        checkLessThan(min, pref);
        checkLessThan(min, max);
        checkLessThan(pref, max);
    }

    private static void checkResizeType(int type, boolean isComponentSpring) {
        if ((type < 0) && (((isComponentSpring) && (type != -1) && (type != -2)) || ((!isComponentSpring) && (type != -2)))) {
            throw new IllegalArgumentException("Invalid size");
        }
    }

    private static void checkLessThan(int min, int max) {
        if ((min >= 0) && (max >= 0) && (min > max)) {
            throw new IllegalArgumentException("Following is not met: min<=pref<=max");
        }
    }

    private static void checkAlignment(int alignment, boolean allowsBaseline) {
        if ((alignment == 1) || (alignment == 2) || (alignment == 3)) {
            return;
        }
        if ((allowsBaseline) && (alignment != 3)) {
            throw new IllegalArgumentException("Alignment must be one of:LEADING, TRAILING, CENTER or BASELINE");
        }
        throw new IllegalArgumentException("Alignment must be one of:LEADING, TRAILING or CENTER");
    }

    static boolean isVisible(Component c) {
        return c.isVisible();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("HORIZONTAL\n");
        dump(buffer, this.horizontalGroup, "  ", 1);
        buffer.append("\nVERTICAL\n");
        dump(buffer, this.verticalGroup, "  ", 2);
        return buffer.toString();
    }

    private void dump(StringBuffer buffer, Spring spring, String indent, int axis) {
        String origin = "";
        String padding = "";
        if ((spring instanceof ComponentSpring)) {
            ComponentSpring cSpring = (ComponentSpring) spring;
            origin = Integer.toString(cSpring.getOrigin()) + " ";
            String name = cSpring.getComponent().getName();
            if (name != null) {
                origin = "name=" + name + ", ";
            }
        }
        if ((spring instanceof AutopaddingSpring)) {
            AutopaddingSpring paddingSpring = (AutopaddingSpring) spring;
            padding = ", userCreated=" + paddingSpring.getUserCreated() + ", matches=" + paddingSpring.getMatchDescription();
        }
        buffer.append(indent + spring.getClass().getName() + " " + Integer.toHexString(spring.hashCode()) + " " + origin + ", size=" + spring.getSize() + ", alignment=" + spring.getAlignment() + " prefs=[" + spring.getMinimumSize(axis) + " " + spring.getPreferredSize(axis) + " " + spring.getMaximumSize(axis) + padding + "]\n");
        if ((spring instanceof Group)) {
            List springs = ((Group) spring).springs;
            indent = indent + "  ";
            for (int counter = 0; counter < springs.size(); counter++) {
                dump(buffer, (Spring) springs.get(counter), indent, axis);
            }
        }
    }

    public boolean getAutocreateGaps() {
        return this.autocreatePadding;
    }

    public void setAutocreateGaps(boolean autocreatePadding) {
        this.autocreatePadding = autocreatePadding;
    }

    public boolean getAutocreateContainerGaps() {
        return this.autocreateContainerPadding;
    }

    public void setAutocreateContainerGaps(boolean autocreatePadding) {
        if (autocreatePadding != this.autocreateContainerPadding) {
            this.autocreateContainerPadding = autocreatePadding;
            this.horizontalGroup = createTopLevelGroup(getHorizontalGroup());
            this.verticalGroup = createTopLevelGroup(getVerticalGroup());
        }
    }

    public Group getHorizontalGroup() {
        int index = 0;
        if (this.horizontalGroup.springs.size() > 1) {
            index = 1;
        }
        return (Group) this.horizontalGroup.springs.get(index);
    }

    public void setHorizontalGroup(Group group) {
        if (group == null) {
            throw new IllegalArgumentException("Group must be non-null");
        }
        this.horizontalGroup = createTopLevelGroup(group);
    }

    public Group getVerticalGroup() {
        int index = 0;
        if (this.verticalGroup.springs.size() > 1) {
            index = 1;
        }
        return (Group) this.verticalGroup.springs.get(index);
    }

    public void setVerticalGroup(Group group) {
        if (group == null) {
            throw new IllegalArgumentException("Group must be non-null");
        }
        this.verticalGroup = createTopLevelGroup(group);
    }

    private Group createTopLevelGroup(Group specifiedGroup) {
        SequentialGroup group = createSequentialGroup();
        if (getAutocreateContainerGaps()) {
            group.addSpring(new ContainerAutopaddingSpring());
            group.add(specifiedGroup);
            group.addSpring(new ContainerAutopaddingSpring());
        } else {
            group.add(specifiedGroup);
        }
        return group;
    }

    public SequentialGroup createSequentialGroup() {
        return new SequentialGroup();
    }

    public ParallelGroup createParallelGroup() {
        return createParallelGroup(1);
    }

    public ParallelGroup createParallelGroup(int alignment) {
        return createParallelGroup(alignment, true);
    }

    public ParallelGroup createParallelGroup(int alignment, boolean resizable) {
        if (alignment == 3) {
            return new BaselineGroup(resizable);
        }
        return new ParallelGroup(alignment, resizable);
    }

    public void linkSize(Component[] components) {
        linkSize(components, 3);
    }

    public void linkSize(Component[] components, int axis) {
        if (components == null) {
            throw new IllegalArgumentException("Components must be non-null");
        }
        boolean horizontal = (axis & 0x1) == 1;
        boolean vertical = (axis & 0x2) == 2;
        if ((!vertical) && (!horizontal)) {
            throw new IllegalArgumentException("Axis must contain HORIZONTAL or VERTICAL");
        }
        for (int counter = components.length - 1; counter >= 0; counter--) {
            Component c = components[counter];
            if (components[counter] == null) {
                throw new IllegalArgumentException("Components must be non-null");
            }
            getComponentInfo(c);
        }
        if (horizontal) {
            linkSize0(components, 1);
        }
        if (vertical) {
            linkSize0(components, 2);
        }
    }

    private void linkSize0(Component[] components, int axis) {
        ComponentInfo master = getComponentInfo(components[(components.length - 1)]).getMasterComponentInfo(axis);
        for (int counter = components.length - 2; counter >= 0; counter--) {
            master.addChild(getComponentInfo(components[counter]), axis);
        }
    }

    public void replace(Component existingComponent, Component newComponent) {
        if ((existingComponent == null) || (newComponent == null)) {
            throw new IllegalArgumentException("Components must be non-null");
        }
        if (this.springsChanged) {
            registerComponents(this.horizontalGroup, 1);
            registerComponents(this.verticalGroup, 2);
        }
        ComponentInfo info = (ComponentInfo) this.componentInfos.remove(existingComponent);
        if (info == null) {
            throw new IllegalArgumentException("Component must already exist");
        }
        this.host.remove(existingComponent);
        this.host.add(newComponent);
        info.setComponent(newComponent);
        this.componentInfos.put(newComponent, info);
        invalidateLayout(this.host);
    }

    public void addLayoutComponent(String name, Component component) {
    }

    public void removeLayoutComponent(Component component) {
        ComponentInfo info = (ComponentInfo) this.componentInfos.remove(component);
        if (info != null) {
            info.dispose();
            this.springsChanged = true;
        }
    }

    public Dimension preferredLayoutSize(Container parent) {
        checkParent(parent);
        prepare(1);
        return adjustSize(this.horizontalGroup.getPreferredSize(1), this.verticalGroup.getPreferredSize(2));
    }

    public Dimension minimumLayoutSize(Container parent) {
        checkParent(parent);
        prepare(0);
        return adjustSize(this.horizontalGroup.getMinimumSize(1), this.verticalGroup.getMinimumSize(2));
    }

    public void layoutContainer(Container parent) {
        prepare();
        Insets insets = parent.getInsets();
        if ((getAutocreateGaps()) || (getAutocreateContainerGaps()) || (this.hasPreferredPaddingSprings)) {
            resetAutopadding(this.horizontalGroup, 1, -1, 0, parent.getWidth() - insets.left - insets.right);

            resetAutopadding(this.verticalGroup, 2, -1, 0, parent.getHeight() - insets.top - insets.bottom);
        }
        this.horizontalGroup.setSize(1, 0, parent.getWidth() - insets.left - insets.right);

        this.verticalGroup.setSize(2, 0, parent.getHeight() - insets.top - insets.bottom);


        Iterator componentInfo = this.componentInfos.values().iterator();
        while (componentInfo.hasNext()) {
            ComponentInfo info = (ComponentInfo) componentInfo.next();
            Component c = info.getComponent();
            info.setBounds(insets);
        }
    }

    public void addLayoutComponent(Component component, Object constraints) {
    }

    public Dimension maximumLayoutSize(Container parent) {
        checkParent(parent);
        prepare(2);
        return adjustSize(this.horizontalGroup.getMaximumSize(1), this.verticalGroup.getMaximumSize(2));
    }

    public float getLayoutAlignmentX(Container parent) {
        checkParent(parent);
        return 0.5F;
    }

    public float getLayoutAlignmentY(Container parent) {
        checkParent(parent);
        return 0.5F;
    }

    public void invalidateLayout(Container parent) {
        checkParent(parent);
        synchronized (parent.getTreeLock()) {
            this.horizontalGroup.setSize(1, -2147483648, -2147483648);
            this.verticalGroup.setSize(2, -2147483648, -2147483648);
            Iterator cis = this.componentInfos.values().iterator();
            while (cis.hasNext()) {
                ComponentInfo ci = (ComponentInfo) cis.next();
                ci.clear();
            }
        }
    }

    private void resetAutopadding(Group group, int axis, int sizeType, int origin, int size) {
        group.resetAutopadding();
        switch (sizeType) {
            case 0:
                size = group.getMinimumSize(axis);
                break;
            case 1:
                size = group.getPreferredSize(axis);
                break;
            case 2:
                size = group.getMaximumSize(axis);
        }
        group.setSize(axis, origin, size);
        group.calculateAutopadding(axis);
    }

    private void prepare(int sizeType) {
        prepare();
        if ((getAutocreateGaps()) || (getAutocreateContainerGaps()) || (this.hasPreferredPaddingSprings)) {
            resetAutopadding(this.horizontalGroup, 1, sizeType, 0, 0);
            resetAutopadding(this.verticalGroup, 2, sizeType, 0, 0);
        }
    }

    private void prepare() {
        if (this.springsChanged) {
            registerComponents(this.horizontalGroup, 1);
            registerComponents(this.verticalGroup, 2);
        }
        if ((this.springsChanged) || (hasVisibilityChanged())) {
            checkComponents();
            this.horizontalGroup.removeAutopadding();
            this.verticalGroup.removeAutopadding();
            if (getAutocreateGaps()) {
                adjustAutopadding(true);
            } else if ((this.hasPreferredPaddingSprings) || (getAutocreateContainerGaps())) {
                adjustAutopadding(false);
            }
            this.springsChanged = false;
        }
    }

    private boolean hasVisibilityChanged() {
        Iterator infos = this.componentInfos.values().iterator();
        boolean visibilityChanged = false;
        while (infos.hasNext()) {
            ComponentInfo info = (ComponentInfo) infos.next();
            if (info.updateVisibility()) {
                visibilityChanged = true;
            }
        }
        return visibilityChanged;
    }

    private void checkComponents() {
        Iterator infos = this.componentInfos.values().iterator();
        while (infos.hasNext()) {
            ComponentInfo info = (ComponentInfo) infos.next();
            if (info.horizontalSpring == null) {
                throw new IllegalStateException(info.component + " is not attached to a horizontal group");
            }
            if (info.verticalSpring == null) {
                throw new IllegalStateException(info.component + " is not attached to a vertical group");
            }
        }
    }

    private void registerComponents(Group group, int axis) {
        List springs = group.springs;
        for (int counter = springs.size() - 1; counter >= 0; counter--) {
            Spring spring = (Spring) springs.get(counter);
            if ((spring instanceof ComponentSpring)) {
                ((ComponentSpring) spring).installIfNecessary(axis);
            } else if ((spring instanceof Group)) {
                registerComponents((Group) spring, axis);
            }
        }
    }

    private Dimension adjustSize(int width, int height) {
        Insets insets = this.host.getInsets();
        return new Dimension(width + insets.left + insets.right, height + insets.top + insets.bottom);
    }

    private void checkParent(Container parent) {
        if (parent != this.host) {
            throw new IllegalArgumentException("GroupLayout can only be used with one Container at a time");
        }
    }

    private ComponentInfo getComponentInfo(Component component) {
        ComponentInfo info = (ComponentInfo) this.componentInfos.get(component);
        if (info == null) {
            this.componentInfos.put(component, new ComponentInfo(component));
            this.host.add(component);
        }
        return info;
    }

    private void adjustAutopadding(boolean insert) {
        this.horizontalGroup.insertAutopadding(1, new ArrayList(1), new ArrayList(1), new ArrayList(1), new ArrayList(1), insert);

        this.verticalGroup.insertAutopadding(2, new ArrayList(1), new ArrayList(1), new ArrayList(1), new ArrayList(1), insert);
    }

    private boolean areParallelSiblings(Component source, Component target, int axis) {
        ComponentInfo sourceInfo = getComponentInfo(source);
        ComponentInfo targetInfo = getComponentInfo(target);
        Spring targetSpring;
        Spring sourceSpring;
        Spring targetSpring;
        if (axis == 1) {
            Spring sourceSpring = sourceInfo.horizontalSpring;
            targetSpring = targetInfo.horizontalSpring;
        } else {
            sourceSpring = sourceInfo.verticalSpring;
            targetSpring = targetInfo.verticalSpring;
        }
        List sourcePath = this.parallelList;
        sourcePath.clear();
        Spring spring = sourceSpring.getParent();
        while (spring != null) {
            sourcePath.add(spring);
            spring = spring.getParent();
        }
        spring = targetSpring.getParent();
        while (spring != null) {
            if (sourcePath.contains(spring)) {
                while (spring != null) {
                    if ((spring instanceof ParallelGroup)) {
                        return true;
                    }
                    spring = spring.getParent();
                }
                return false;
            }
            spring = spring.getParent();
        }
        return false;
    }

    private static class SpringDelta
            implements Comparable {
        public int index;
        public int delta;

        public SpringDelta(int index, int delta) {
            this.index = index;
            this.delta = delta;
        }

        public int compareTo(Object o) {
            return this.delta - ((SpringDelta) o).delta;
        }

        public String toString() {
            return super.toString() + "[index=" + this.index + ", delta=" + this.delta + "]";
        }
    }

    private static class AutopaddingMatch {
        public GroupLayout.ComponentSpring source;
        public GroupLayout.ComponentSpring target;

        AutopaddingMatch(GroupLayout.ComponentSpring source, GroupLayout.ComponentSpring target) {
            this.source = source;
            this.target = target;
        }

        private String toString(GroupLayout.ComponentSpring spring) {
            return spring.getComponent().getName();
        }

        public String toString() {
            return "[" + toString(this.source) + "-" + toString(this.target) + "]";
        }
    }

    private static class ComponentInfo {
        GroupLayout.ComponentSpring horizontalSpring;
        GroupLayout.ComponentSpring verticalSpring;
        private Component component;
        private ComponentInfo horizontalMaster;
        private ComponentInfo verticalMaster;
        private List horizontalDependants;
        private List verticalDependants;
        private int[] horizontalSizes;
        private int[] verticalSizes;
        private boolean visible;

        ComponentInfo(Component component) {
            this.component = component;
            this.visible = GroupLayout.isVisible(component);
            clear();
        }

        public void dispose() {
            removeSpring(this.horizontalSpring);
            this.horizontalSpring = null;
            removeSpring(this.verticalSpring);
            this.verticalSpring = null;
            if (this.horizontalMaster == this) {
                this.horizontalDependants.remove(this);
                if (this.horizontalDependants.size() == 1) {
                    ComponentInfo linked = (ComponentInfo) this.horizontalDependants.get(0);

                    linked.horizontalMaster = null;
                    linked.horizontalDependants = null;
                } else if (this.horizontalDependants.size() > 1) {
                    ComponentInfo newMaster = (ComponentInfo) this.horizontalDependants.get(0);

                    newMaster.horizontalMaster = newMaster;
                    newMaster.horizontalDependants = this.horizontalDependants;
                }
            } else if (this.horizontalMaster != null) {
                this.horizontalMaster.horizontalDependants.remove(this);
                if (this.horizontalMaster.horizontalDependants.size() == 1) {
                    this.horizontalMaster.horizontalDependants = null;
                    this.horizontalMaster.horizontalMaster = null;
                }
            }
            if (this.verticalMaster == this) {
                this.verticalDependants.remove(this);
                if (this.verticalDependants.size() == 1) {
                    ComponentInfo linked = (ComponentInfo) this.verticalDependants.get(0);

                    linked.verticalMaster = null;
                    linked.verticalDependants = null;
                } else if (this.verticalDependants.size() > 1) {
                    ComponentInfo newMaster = (ComponentInfo) this.verticalDependants.get(0);

                    newMaster.verticalMaster = newMaster;
                    newMaster.verticalDependants = this.verticalDependants;
                }
            } else if (this.verticalMaster != null) {
                this.verticalMaster.verticalDependants.remove(this);
                if (this.verticalMaster.verticalDependants.size() == 1) {
                    this.verticalMaster.verticalDependants = null;
                    this.verticalMaster.verticalMaster = null;
                }
            }
        }

        private void removeSpring(GroupLayout.Spring spring) {
            if (spring != null) {
                ((GroupLayout.Group) spring.getParent()).springs.remove(spring);
            }
        }

        public boolean isVisible() {
            return this.visible;
        }

        boolean updateVisibility() {
            boolean newVisible = GroupLayout.isVisible(this.component);
            if (this.visible != newVisible) {
                this.visible = newVisible;
                return true;
            }
            return false;
        }

        public void setBounds(Insets insets) {
            int x = 0;
            int y = 0;
            int w = 0;
            int h = 0;
            if (this.horizontalSpring != null) {
                x = this.horizontalSpring.getOrigin();
                w = this.horizontalSpring.getSize();
            }
            if (this.verticalSpring != null) {
                y = this.verticalSpring.getOrigin();
                h = this.verticalSpring.getSize();
            }
            this.component.setBounds(x + insets.left, y + insets.top, w, h);
        }

        public Component getComponent() {
            return this.component;
        }

        public void setComponent(Component component) {
            this.component = component;
            if (this.horizontalSpring != null) {
                this.horizontalSpring.setComponent(component);
            }
            if (this.verticalSpring != null) {
                this.verticalSpring.setComponent(component);
            }
        }

        public boolean isLinked(int axis) {
            if (axis == 1) {
                return this.horizontalMaster != null;
            }
            return this.verticalMaster != null;
        }

        public ComponentInfo getMasterComponentInfo(int axis) {
            if (axis == 1) {
                if (this.horizontalMaster == null) {
                    this.horizontalMaster = this;
                    this.horizontalDependants = new ArrayList(1);
                    this.horizontalDependants.add(this);
                    this.horizontalSizes = new int[3];
                    clear();
                }
                return this.horizontalMaster;
            }
            if (this.verticalMaster == null) {
                this.verticalMaster = this;
                this.verticalDependants = new ArrayList(1);
                this.verticalDependants.add(this);
                this.verticalSizes = new int[3];
                clear();
            }
            return this.verticalMaster;
        }

        public void addChild(ComponentInfo child, int axis) {
            if (axis == 1) {
                addChild0(child, 1);
            } else {
                assert (axis == 2);
                addChild0(child, 2);
            }
        }

        private void addChild0(ComponentInfo child, int axis) {
            if (axis == 1) {
                if (child.horizontalMaster == child) {
                    this.horizontalDependants.addAll(child.horizontalDependants);
                    child.horizontalDependants = null;
                    child.horizontalSizes = null;
                } else {
                    this.horizontalDependants.add(child);
                }
                child.horizontalMaster = this;
            } else {
                if (child.verticalMaster == child) {
                    this.verticalDependants.addAll(child.verticalDependants);
                    child.verticalDependants = null;
                    child.verticalSizes = null;
                } else {
                    this.verticalDependants.add(child);
                }
                child.verticalMaster = this;
            }
        }

        public void clear() {
            clear(this.horizontalSizes);
            clear(this.verticalSizes);
        }

        private void clear(int[] sizes) {
            if (sizes != null) {
                for (int counter = sizes.length - 1; counter >= 0; counter--) {
                    sizes[counter] = -2147483648;
                }
            }
        }

        int getLinkSize(int axis, int type) {
            int[] sizes = null;
            List dependants = null;
            if (axis == 1) {
                if (this.horizontalMaster != this) {
                    return this.horizontalMaster.getLinkSize(axis, type);
                }
                sizes = this.horizontalSizes;
                dependants = this.horizontalDependants;
            } else if (axis == 2) {
                if (this.verticalMaster != this) {
                    return this.verticalMaster.getLinkSize(axis, type);
                }
                sizes = this.verticalSizes;
                dependants = this.verticalDependants;
            }
            if (sizes[type] == -2147483648) {
                sizes[type] = calcLinkSize(dependants, axis, type);
            }
            return sizes[type];
        }

        private int calcLinkSize(List dependants, int axis, int type) {
            int count = dependants.size() - 1;
            int size = getDependantSpringSize(dependants, axis, type, count--);
            while (count >= 0) {
                size = Math.max(size, getDependantSpringSize(dependants, axis, type, count--));
            }
            return size;
        }

        private int getDependantSpringSize(List dependants, int axis, int type, int index) {
            ComponentInfo ci = (ComponentInfo) dependants.get(index);
            GroupLayout.ComponentSpring spring;
            GroupLayout.ComponentSpring spring;
            if (axis == 1) {
                spring = ci.horizontalSpring;
            } else {
                spring = ci.verticalSpring;
            }
            return spring.getPreferredSize1(axis);
        }
    }

    abstract class Spring {
        private int size;
        private int min;
        private int max;
        private int pref;
        private Spring parent;
        private int alignment;

        Spring() {
            this.min = (this.pref = this.max = -2147483648);
        }

        abstract int getMinimumSize0(int paramInt);

        abstract int getPreferredSize0(int paramInt);

        abstract int getMaximumSize0(int paramInt);

        Spring getParent() {
            return this.parent;
        }

        void setParent(Spring parent) {
            this.parent = parent;
        }

        int getAlignment() {
            return this.alignment;
        }

        void setAlignment(int alignment) {
            GroupLayout.checkAlignment(alignment, false);
            this.alignment = alignment;
        }

        final int getMinimumSize(int axis) {
            if (this.min == -2147483648) {
                this.min = constrain(getMinimumSize0(axis));
            }
            return this.min;
        }

        final int getPreferredSize(int axis) {
            if (this.pref == -2147483648) {
                this.pref = constrain(getPreferredSize0(axis));
            }
            return this.pref;
        }

        final int getMaximumSize(int axis) {
            if (this.max == -2147483648) {
                this.max = constrain(getMaximumSize0(axis));
            }
            return this.max;
        }

        void clear() {
            this.size = (this.min = this.pref = this.max = -2147483648);
        }

        void setSize(int axis, int origin, int size) {
            this.size = size;
            if (size == -2147483648) {
                clear();
            }
        }

        int getSize() {
            return this.size;
        }

        int constrain(int value) {
            return Math.min(value, 32767);
        }
    }

    public abstract class Group
            extends GroupLayout.Spring {
        List springs;

        Group() {
            super();
            this.springs = new ArrayList();
        }

        int indexOf(GroupLayout.Spring spring) {
            return this.springs.indexOf(spring);
        }

        Group addSpring(GroupLayout.Spring spring, int index) {
            this.springs.add(spring);
            spring.setParent(this);
            if (!(spring instanceof GroupLayout.AutopaddingSpring)) {
                GroupLayout.this.springsChanged = true;
            }
            return this;
        }

        Group addSpring(GroupLayout.Spring spring) {
            addSpring(spring, this.springs.size());
            return this;
        }

        void setParent(GroupLayout.Spring parent) {
            super.setParent(parent);
            for (int counter = this.springs.size() - 1; counter >= 0; counter--) {
                ((GroupLayout.Spring) this.springs.get(counter)).setParent(this);
            }
        }

        void setSize(int axis, int origin, int size) {
            super.setSize(axis, origin, size);
            if (size == -2147483648) {
                for (int counter = this.springs.size() - 1; counter >= 0; counter--) {
                    getSpring(counter).setSize(axis, origin, size);
                }
            } else {
                setSize0(axis, origin, size);
            }
        }

        abstract void setSize0(int paramInt1, int paramInt2, int paramInt3);

        int getMinimumSize0(int axis) {
            return calculateSize(axis, 0);
        }

        int getPreferredSize0(int axis) {
            return calculateSize(axis, 1);
        }

        int getMaximumSize0(int axis) {
            return calculateSize(axis, 2);
        }

        abstract int operator(int paramInt1, int paramInt2);

        int calculateSize(int axis, int type) {
            int count = this.springs.size();
            if (count == 0) {
                return 0;
            }
            if (count == 1) {
                return getSize(getSpring(0), axis, type);
            }
            int size = constrain(operator(getSize(getSpring(0), axis, type), getSize(getSpring(1), axis, type)));
            for (int counter = 2; counter < count; counter++) {
                size = constrain(operator(size, getSize(getSpring(counter), axis, type)));
            }
            return size;
        }

        GroupLayout.Spring getSpring(int index) {
            return (GroupLayout.Spring) this.springs.get(index);
        }

        int getSize(GroupLayout.Spring spring, int axis, int type) {
            switch (type) {
                case 0:
                    return spring.getMinimumSize(axis);
                case 1:
                    return spring.getPreferredSize(axis);
                case 2:
                    return spring.getMaximumSize(axis);
            }
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
            return 0;
        }

        abstract void insertAutopadding(int paramInt, List paramList1, List paramList2, List paramList3, List paramList4, boolean paramBoolean);

        void removeAutopadding() {
            for (int counter = this.springs.size() - 1; counter >= 0; counter--) {
                GroupLayout.Spring spring = (GroupLayout.Spring) this.springs.get(counter);
                if ((spring instanceof GroupLayout.AutopaddingSpring)) {
                    if (((GroupLayout.AutopaddingSpring) spring).getUserCreated()) {
                        ((GroupLayout.AutopaddingSpring) spring).reset();
                    } else {
                        this.springs.remove(counter);
                    }
                } else if ((spring instanceof Group)) {
                    ((Group) spring).removeAutopadding();
                }
            }
        }

        void resetAutopadding() {
            clear();
            for (int counter = this.springs.size() - 1; counter >= 0; counter--) {
                GroupLayout.Spring spring = (GroupLayout.Spring) this.springs.get(counter);
                if ((spring instanceof GroupLayout.AutopaddingSpring)) {
                    ((GroupLayout.AutopaddingSpring) spring).clear();
                } else if ((spring instanceof Group)) {
                    ((Group) spring).resetAutopadding();
                }
            }
        }

        void calculateAutopadding(int axis) {
            for (int counter = this.springs.size() - 1; counter >= 0; counter--) {
                GroupLayout.Spring spring = (GroupLayout.Spring) this.springs.get(counter);
                if ((spring instanceof GroupLayout.AutopaddingSpring)) {
                    spring.clear();
                    ((GroupLayout.AutopaddingSpring) spring).calculatePadding(axis);
                } else if ((spring instanceof Group)) {
                    ((Group) spring).calculateAutopadding(axis);
                }
            }
            clear();
        }
    }

    public class SequentialGroup
            extends GroupLayout.Group {
        SequentialGroup() {
            super();
        }

        public SequentialGroup add(GroupLayout.Group group) {
            return (SequentialGroup) addSpring(group);
        }

        public SequentialGroup add(Component component) {
            return add(component, -1, -1, -1);
        }

        public SequentialGroup add(Component component, int min, int pref, int max) {
            return (SequentialGroup) addSpring(new GroupLayout.ComponentSpring(GroupLayout.this, component, min, pref, max, null));
        }

        public SequentialGroup add(int pref) {
            return add(pref, pref, pref);
        }

        public SequentialGroup add(int min, int pref, int max) {
            return (SequentialGroup) addSpring(new GroupLayout.GapSpring(GroupLayout.this, min, pref, max));
        }

        public SequentialGroup addPreferredGap(JComponent comp1, JComponent comp2, int type) {
            return addPreferredGap(comp1, comp2, type, false);
        }

        public SequentialGroup addPreferredGap(JComponent comp1, JComponent comp2, int type, boolean canGrow) {
            if ((type != 0) && (type != 1) && (type != 3)) {
                throw new IllegalArgumentException("Invalid type argument");
            }
            return (SequentialGroup) addSpring(new GroupLayout.PaddingSpring(GroupLayout.this, comp1, comp2, type, canGrow));
        }

        public SequentialGroup addPreferredGap(int type) {
            return addPreferredGap(type, -1, -1);
        }

        public SequentialGroup addPreferredGap(int type, int pref, int max) {
            if ((type != 0) && (type != 1)) {
                throw new IllegalArgumentException("Padding type must be one of Padding.RELATED or Padding.UNRELATED");
            }
            if (((pref < 0) && (pref != -1)) || ((max < 0) && (max != -1) && (max != -2)) || ((pref >= 0) && (max >= 0) && (pref > max))) {
                throw new IllegalArgumentException("Pref and max must be either DEFAULT_VALUE or >= 0 and pref <= max");
            }
            GroupLayout.this.hasPreferredPaddingSprings = true;
            return (SequentialGroup) addSpring(new GroupLayout.AutopaddingSpring(GroupLayout.this, type, pref, max));
        }

        public SequentialGroup addContainerGap() {
            return addContainerGap(-1, -1);
        }

        public SequentialGroup addContainerGap(int pref, int max) {
            if (((pref < 0) && (pref != -1)) || ((max < 0) && (max != -1) && (max != -2)) || ((pref >= 0) && (max >= 0) && (pref > max))) {
                throw new IllegalArgumentException("Pref and max must be either DEFAULT_VALUE or >= 0 and pref <= max");
            }
            GroupLayout.this.hasPreferredPaddingSprings = true;
            return (SequentialGroup) addSpring(new GroupLayout.ContainerAutopaddingSpring(GroupLayout.this, pref, max));
        }

        int operator(int a, int b) {
            return constrain(a) + constrain(b);
        }

        void setSize0(int axis, int origin, int size) {
            int pref = getPreferredSize(axis);
            if (size - pref == 0) {
                int counter = 0;
                for (int max = this.springs.size(); counter < max; counter++) {
                    GroupLayout.Spring spring = getSpring(counter);
                    int springPref = spring.getPreferredSize(axis);
                    spring.setSize(axis, origin, springPref);
                    origin += springPref;
                }
            } else if (this.springs.size() == 1) {
                GroupLayout.Spring spring = getSpring(0);
                spring.setSize(axis, origin, Math.min(size, spring.getMaximumSize(axis)));
            } else if (this.springs.size() > 1) {
                resize(axis, origin, size);
            }
        }

        private void resize(int axis, int origin, int size) {
            int delta = size - getPreferredSize(axis);
            assert (delta != 0);
            boolean useMin = delta < 0;
            int springCount = this.springs.size();
            if (useMin) {
                delta *= -1;
            }
            List resizable = buildResizableList(axis, useMin);
            int resizableCount = resizable.size();
            if (resizableCount > 0) {
                int sDelta = delta / resizableCount;
                int slop = delta - sDelta * resizableCount;
                int[] sizes = new int[springCount];
                int sign = useMin ? -1 : 1;
                for (int counter = 0; counter < resizableCount; counter++) {
                    GroupLayout.SpringDelta springDelta = (GroupLayout.SpringDelta) resizable.get(counter);
                    if (counter + 1 == resizableCount) {
                        sDelta += slop;
                    }
                    springDelta.delta = Math.min(sDelta, springDelta.delta);
                    delta -= springDelta.delta;
                    if ((springDelta.delta != sDelta) && (counter + 1 < resizableCount)) {
                        sDelta = delta / (resizableCount - counter - 1);
                        slop = delta - sDelta * (resizableCount - counter - 1);
                    }
                    GroupLayout.Spring spring = getSpring(springDelta.index);
                    sizes[springDelta.index] = (sign * springDelta.delta);
                }
                for (int counter = 0; counter < springCount; counter++) {
                    GroupLayout.Spring spring = getSpring(counter);
                    int sSize = spring.getPreferredSize(axis) + sizes[counter];
                    spring.setSize(axis, origin, sSize);
                    origin += sSize;
                }
            } else {
                for (int counter = 0; counter < springCount; counter++) {
                    GroupLayout.Spring spring = getSpring(counter);
                    int sSize;
                    int sSize;
                    if (useMin) {
                        sSize = spring.getMinimumSize(axis);
                    } else {
                        sSize = spring.getMaximumSize(axis);
                    }
                    spring.setSize(axis, origin, sSize);
                    origin += sSize;
                }
            }
        }

        private List buildResizableList(int axis, boolean useMin) {
            int size = this.springs.size();
            List sorted = new ArrayList(size);
            for (int counter = 0; counter < size; counter++) {
                GroupLayout.Spring spring = getSpring(counter);
                int sDelta;
                int sDelta;
                if (useMin) {
                    sDelta = spring.getPreferredSize(axis) - spring.getMinimumSize(axis);
                } else {
                    sDelta = spring.getMaximumSize(axis) - spring.getPreferredSize(axis);
                }
                if (sDelta > 0) {
                    sorted.add(new GroupLayout.SpringDelta(counter, sDelta));
                }
            }
            Collections.sort(sorted);
            return sorted;
        }

        private GroupLayout.AutopaddingSpring getNextAutopadding(int index, boolean insert) {
            GroupLayout.Spring spring = getSpring(index);
            if (((spring instanceof GroupLayout.AutopaddingSpring)) && (((GroupLayout.AutopaddingSpring) spring).getUserCreated())) {
                return (GroupLayout.AutopaddingSpring) spring;
            }
            if (insert) {
                GroupLayout.AutopaddingSpring autoSpring = new GroupLayout.AutopaddingSpring(GroupLayout.this, null);
                this.springs.add(index, autoSpring);
                return autoSpring;
            }
            return null;
        }

        void insertAutopadding(int axis, List leadingPadding, List trailingPadding, List leading, List trailing, boolean insert) {
            List newLeadingPadding = new ArrayList(leadingPadding);
            List newTrailingPadding = new ArrayList(1);
            List newLeading = new ArrayList(leading);
            List newTrailing = null;
            for (int counter = 0; counter < this.springs.size(); counter++) {
                GroupLayout.Spring spring = getSpring(counter);
                if ((spring instanceof GroupLayout.AutopaddingSpring)) {
                    GroupLayout.AutopaddingSpring padding = (GroupLayout.AutopaddingSpring) spring;
                    padding.setSources(newLeading);
                    newLeading.clear();
                    if (counter + 1 == this.springs.size()) {
                        if (!(padding instanceof GroupLayout.ContainerAutopaddingSpring)) {
                            trailingPadding.add(padding);
                        }
                    } else {
                        newLeadingPadding.clear();
                        newLeadingPadding.add(padding);
                    }
                } else if ((newLeading.size() > 0) && (insert)) {
                    GroupLayout.AutopaddingSpring padding = new GroupLayout.AutopaddingSpring(GroupLayout.this, null);


                    this.springs.add(counter--, padding);
                } else if ((spring instanceof GroupLayout.ComponentSpring)) {
                    GroupLayout.ComponentSpring cSpring = (GroupLayout.ComponentSpring) spring;
                    if (GroupLayout.isVisible(cSpring.getComponent())) {
                        for (int i = 0; i < newLeadingPadding.size(); i++) {
                            ((GroupLayout.AutopaddingSpring) newLeadingPadding.get(i)).add(cSpring, axis);
                        }
                        newLeading.clear();
                        newLeadingPadding.clear();
                        if (counter + 1 == this.springs.size()) {
                            trailing.add(cSpring);
                        } else {
                            newLeading.add(cSpring);
                        }
                    }
                } else if ((spring instanceof GroupLayout.Group)) {
                    if (newTrailing == null) {
                        newTrailing = new ArrayList(1);
                    } else {
                        newTrailing.clear();
                    }
                    newTrailingPadding.clear();
                    ((GroupLayout.Group) spring).insertAutopadding(axis, newLeadingPadding, newTrailingPadding, newLeading, newTrailing, insert);


                    newLeading.clear();
                    newLeadingPadding.clear();
                    if (counter + 1 == this.springs.size()) {
                        trailing.addAll(newTrailing);
                        trailingPadding.addAll(newTrailingPadding);
                    } else {
                        newLeading.addAll(newTrailing);
                        newLeadingPadding.addAll(newTrailingPadding);
                    }
                } else {
                    newLeadingPadding.clear();
                    newLeading.clear();
                }
            }
        }
    }

    public class ParallelGroup
            extends GroupLayout.Group {
        private int childAlignment;
        private boolean resizable;

        ParallelGroup(int childAlignment, boolean resizable) {
            super();
            GroupLayout.checkAlignment(childAlignment, true);
            this.childAlignment = childAlignment;
            this.resizable = resizable;
        }

        public ParallelGroup add(GroupLayout.Group group) {
            return (ParallelGroup) addSpring(group);
        }

        public ParallelGroup add(Component component) {
            return add(component, -1, -1, -1);
        }

        public ParallelGroup add(Component component, int min, int pref, int max) {
            return (ParallelGroup) addSpring(new GroupLayout.ComponentSpring(GroupLayout.this, component, min, pref, max, null));
        }

        public ParallelGroup add(int pref) {
            return add(pref, pref, pref);
        }

        public ParallelGroup add(int min, int pref, int max) {
            return (ParallelGroup) addSpring(new GroupLayout.GapSpring(GroupLayout.this, min, pref, max));
        }

        public ParallelGroup add(int alignment, GroupLayout.Group group) {
            group.setAlignment(alignment);
            return (ParallelGroup) addSpring(group);
        }

        public ParallelGroup add(int alignment, Component component) {
            return add(alignment, component, -1, -1, -1);
        }

        public ParallelGroup add(int alignment, Component component, int min, int pref, int max) {
            GroupLayout.ComponentSpring spring = new GroupLayout.ComponentSpring(GroupLayout.this, component, min, pref, max, null);

            spring.setAlignment(alignment);
            return (ParallelGroup) addSpring(spring);
        }

        boolean isResizable() {
            return this.resizable;
        }

        int operator(int a, int b) {
            return Math.max(a, b);
        }

        int getMinimumSize0(int axis) {
            if (!isResizable()) {
                return getPreferredSize(axis);
            }
            return super.getMinimumSize0(axis);
        }

        int getMaximumSize0(int axis) {
            if (!isResizable()) {
                return getPreferredSize(axis);
            }
            return super.getMaximumSize0(axis);
        }

        void setSize0(int axis, int origin, int size) {
            int alignment = this.childAlignment;
            if (alignment == 3) {
                alignment = 1;
            }
            int counter = 0;
            for (int max = this.springs.size(); counter < max; counter++) {
                GroupLayout.Spring spring = getSpring(counter);
                int sAlignment = spring.getAlignment();
                int springSize = Math.min(size, spring.getMaximumSize(axis));
                if (sAlignment == 0) {
                    sAlignment = alignment;
                }
                switch (sAlignment) {
                    case 2:
                        spring.setSize(axis, origin + size - springSize, springSize);

                        break;
                    case 3:
                        spring.setSize(axis, origin + (size - springSize) / 2, springSize);

                        break;
                    default:
                        spring.setSize(axis, origin, springSize);
                }
            }
        }

        void insertAutopadding(int axis, List leadingPadding, List trailingPadding, List leading, List trailing, boolean insert) {
            for (int counter = 0; counter < this.springs.size(); counter++) {
                GroupLayout.Spring spring = getSpring(counter);
                if ((spring instanceof GroupLayout.ComponentSpring)) {
                    for (int i = 0; i < leadingPadding.size(); i++) {
                        ((GroupLayout.AutopaddingSpring) leadingPadding.get(i)).add((GroupLayout.ComponentSpring) spring, axis);
                    }
                    trailing.add(spring);
                } else if ((spring instanceof GroupLayout.Group)) {
                    ((GroupLayout.Group) spring).insertAutopadding(axis, leadingPadding, trailingPadding, leading, trailing, insert);
                } else if ((spring instanceof GroupLayout.AutopaddingSpring)) {
                    trailingPadding.add(spring);
                }
            }
        }
    }

    private class BaselineGroup
            extends GroupLayout.ParallelGroup {
        private boolean allSpringsHaveBaseline;
        private int prefAscent;
        private int prefDescent;

        BaselineGroup(boolean resizable) {
            super(1, resizable);
            this.prefAscent = (this.prefDescent = -1);
        }

        void setSize(int axis, int origin, int size) {
            if (size == -2147483648) {
                this.prefAscent = (this.prefDescent = -1);
            }
            super.setSize(axis, origin, size);
        }

        void setSize0(int axis, int origin, int size) {
            if ((axis == 1) || (this.prefAscent == -1)) {
                super.setSize0(axis, origin, size);
            } else {
                baselineLayout(origin, size);
            }
        }

        int calculateSize(int axis, int type) {
            if ((this.springs.size() < 2) || (axis != 2)) {
                return super.calculateSize(axis, type);
            }
            if (this.prefAscent == -1) {
                calculateBaseline();
            }
            if (this.allSpringsHaveBaseline) {
                return this.prefAscent + this.prefDescent;
            }
            return Math.max(this.prefAscent + this.prefDescent, super.calculateSize(axis, type));
        }

        private void calculateBaseline() {
            this.prefAscent = 0;
            this.prefDescent = 0;
            this.allSpringsHaveBaseline = true;
            for (int counter = this.springs.size() - 1; counter >= 0; counter--) {
                GroupLayout.Spring spring = getSpring(counter);
                int baseline = -1;
                if ((spring instanceof GroupLayout.ComponentSpring)) {
                    baseline = ((GroupLayout.ComponentSpring) spring).getBaseline();
                    if (baseline >= 0) {
                        this.prefAscent = Math.max(this.prefAscent, baseline);
                        this.prefDescent = Math.max(this.prefDescent, spring.getPreferredSize(2) - baseline);
                    }
                }
                if (baseline < 0) {
                    this.allSpringsHaveBaseline = false;
                }
            }
        }

        private void baselineLayout(int origin, int size) {
            int counter = 0;
            for (int max = this.springs.size(); counter < max; counter++) {
                GroupLayout.Spring spring = getSpring(counter);
                int baseline = -1;
                if ((spring instanceof GroupLayout.ComponentSpring)) {
                    baseline = ((GroupLayout.ComponentSpring) spring).getBaseline();
                    if (baseline >= 0) {
                        spring.setSize(2, origin + this.prefAscent - baseline, spring.getPreferredSize(2));
                    }
                }
                if (baseline < 0) {
                    int sSize = Math.min(spring.getMaximumSize(2), size);
                    spring.setSize(2, origin + (size - sSize) / 2, sSize);
                }
            }
        }
    }

    class ComponentSpring
            extends GroupLayout.Spring {
        private Component component;
        private int origin;
        private int min;
        private int pref;
        private int max;
        private int baseline = -1;
        private boolean installed;
        ComponentSpring(Component x1, int x2, int x3, int x4, GroupLayout.1x5) {
            this(x1, x2, x3, x4);
        }

        private ComponentSpring(Component component, int min, int pref, int max) {
            super();
            this.component = component;

            GroupLayout.checkSize(min, pref, max, true);

            this.min = min;
            this.max = max;
            this.pref = pref;

            GroupLayout.this.getComponentInfo(component);
        }

        int getMinimumSize0(int axis) {
            if (isLinked(axis)) {
                return getLinkSize(axis, 0);
            }
            return getMinimumSize1(axis);
        }

        int getMinimumSize1(int axis) {
            if (!GroupLayout.isVisible(this.component)) {
                return 0;
            }
            if (this.min >= 0) {
                return this.min;
            }
            if (this.min == -2) {
                return getPreferredSize1(axis);
            }
            assert (this.min == -1);
            return getSizeAlongAxis(axis, this.component.getMinimumSize());
        }

        int getPreferredSize0(int axis) {
            if (isLinked(axis)) {
                return getLinkSize(axis, 1);
            }
            return Math.max(getMinimumSize(axis), getPreferredSize1(axis));
        }

        int getPreferredSize1(int axis) {
            if (!GroupLayout.isVisible(this.component)) {
                return 0;
            }
            if (this.pref >= 0) {
                return this.pref;
            }
            return getSizeAlongAxis(axis, this.component.getPreferredSize());
        }

        int getMaximumSize0(int axis) {
            if (!GroupLayout.isVisible(this.component)) {
                return 0;
            }
            if (isLinked(axis)) {
                return getLinkSize(axis, 2);
            }
            return Math.max(getMinimumSize(axis), getMaximumSize1(axis));
        }

        int getMaximumSize1(int axis) {
            if (this.max >= 0) {
                return this.max;
            }
            if (this.max == -2) {
                return getPreferredSize1(axis);
            }
            assert (this.max == -1);
            return getSizeAlongAxis(axis, this.component.getMaximumSize());
        }

        private int getSizeAlongAxis(int axis, Dimension size) {
            return axis == 1 ? size.width : size.height;
        }

        private int getLinkSize(int axis, int type) {
            if (!GroupLayout.isVisible(this.component)) {
                return 0;
            }
            GroupLayout.ComponentInfo ci = GroupLayout.this.getComponentInfo(this.component);
            return ci.getLinkSize(axis, type);
        }

        void setSize(int axis, int origin, int size) {
            super.setSize(axis, origin, size);
            this.origin = origin;
            if (size == -2147483648) {
                this.baseline = -1;
            }
        }

        int getOrigin() {
            return this.origin;
        }

        Component getComponent() {
            return this.component;
        }

        void setComponent(Component component) {
            this.component = component;
        }

        int getBaseline() {
            if ((this.baseline == -1) && ((this.component instanceof JComponent))) {
                GroupLayout.Spring horizontalSpring = GroupLayout.this.getComponentInfo(this.component).horizontalSpring;
                int width;
                int width;
                if (horizontalSpring != null) {
                    width = horizontalSpring.getSize();
                } else {
                    width = this.component.getPreferredSize().width;
                }
                this.baseline = Baseline.getBaseline((JComponent) this.component, width, getPreferredSize(2));
            }
            return this.baseline;
        }

        private boolean isLinked(int axis) {
            return GroupLayout.this.getComponentInfo(this.component).isLinked(axis);
        }

        void installIfNecessary(int axis) {
            if (!this.installed) {
                this.installed = true;
                if (axis == 1) {
                    GroupLayout.this.getComponentInfo(this.component).horizontalSpring = this;
                } else {
                    GroupLayout.this.getComponentInfo(this.component).verticalSpring = this;
                }
            }
        }
    }

    class PaddingSpring
            extends GroupLayout.Spring {
        private JComponent source;
        private JComponent target;
        private int type;
        private boolean canGrow;

        PaddingSpring(JComponent source, JComponent target, int type, boolean canGrow) {
            super();
            this.source = source;
            this.target = target;
            this.type = type;
            this.canGrow = canGrow;
        }

        int getMinimumSize0(int axis) {
            return getPadding(axis);
        }

        int getPreferredSize0(int axis) {
            return getPadding(axis);
        }

        int getMaximumSize0(int axis) {
            if (this.canGrow) {
                return 32767;
            }
            return getPadding(axis);
        }

        private int getPadding(int axis) {
            int position;
            int position;
            if (axis == 1) {
                position = 3;
            } else {
                position = 5;
            }
            return LayoutStyle.getSharedInstance().getPreferredGap(this.source, this.target, this.type, position, GroupLayout.this.host);
        }
    }

    class GapSpring
            extends GroupLayout.Spring {
        private int min;
        private int pref;
        private int max;

        GapSpring(int min, int pref, int max) {
            super();
            GroupLayout.checkSize(min, pref, max, false);
            this.min = min;
            this.pref = pref;
            this.max = max;
        }

        int getMinimumSize0(int axis) {
            if (this.min == -2) {
                return getPreferredSize(axis);
            }
            return this.min;
        }

        int getPreferredSize0(int axis) {
            return this.pref;
        }

        int getMaximumSize0(int axis) {
            if (this.max == -2) {
                return getPreferredSize(axis);
            }
            return this.max;
        }
    }

    private class AutopaddingSpring
            extends GroupLayout.Spring {
        List sources;
        GroupLayout.ComponentSpring source;
        int size;
        int lastSize;
        private List matches;
        private int pref;
        private int max;
        private int type;
        private boolean userCreated;

        AutopaddingSpring(GroupLayout.1x1) {
            this();
        }

        private AutopaddingSpring() {
            super();
            this.pref = -2;
            this.max = -2;
            this.type = 0;
        }

        AutopaddingSpring(int pref, int max) {
            super();
            this.pref = pref;
            this.max = max;
        }

        AutopaddingSpring(int type, int pref, int max) {
            super();
            this.type = type;
            this.pref = pref;
            this.max = max;
            this.userCreated = true;
        }

        public void setSource(GroupLayout.ComponentSpring source) {
            this.source = source;
        }

        public void setSources(List sources) {
            this.sources = new ArrayList(sources.size());
            this.sources.addAll(sources);
        }

        public boolean getUserCreated() {
            return this.userCreated;
        }

        public void setUserCreated(boolean userCreated) {
            this.userCreated = userCreated;
        }

        void clear() {
            this.lastSize = getSize();
            super.clear();
            this.size = 0;
        }

        public void reset() {
            this.size = 0;
            this.sources = null;
            this.source = null;
            this.matches = null;
        }

        public void calculatePadding(int axis) {
            this.size = -2147483648;
            int maxPadding = -2147483648;
            if (this.matches != null) {
                LayoutStyle p = LayoutStyle.getSharedInstance();

                int position = axis == 1 ? 3 : 5;
                for (int i = this.matches.size() - 1; i >= 0; i--) {
                    GroupLayout.AutopaddingMatch match = (GroupLayout.AutopaddingMatch) this.matches.get(i);
                    maxPadding = Math.max(maxPadding, calculatePadding(p, position, match.source, match.target));
                }
            }
            if (this.size == -2147483648) {
                this.size = 0;
            }
            if (maxPadding == -2147483648) {
                maxPadding = 0;
            }
            if (this.lastSize != -2147483648) {
                this.size += Math.min(maxPadding, this.lastSize);
            }
        }

        private int calculatePadding(LayoutStyle p, int position, GroupLayout.ComponentSpring source, GroupLayout.ComponentSpring target) {
            int delta = target.getOrigin() - (source.getOrigin() + source.getSize());
            if (delta >= 0) {
                int padding;
                int padding;
                if (((source.getComponent() instanceof JComponent)) && ((target.getComponent() instanceof JComponent))) {
                    padding = p.getPreferredGap((JComponent) source.getComponent(), (JComponent) target.getComponent(), this.type, position, GroupLayout.this.host);
                } else {
                    padding = 10;
                }
                if (padding > delta) {
                    this.size = Math.max(this.size, padding - delta);
                }
                return padding;
            }
            return 0;
        }

        public void add(GroupLayout.ComponentSpring spring, int axis) {
            int oAxis = axis == 1 ? 2 : 1;
            if (this.source != null) {
                if (GroupLayout.this.areParallelSiblings(this.source.getComponent(), spring.getComponent(), oAxis)) {
                    addMatch(this.source, spring);
                }
            } else {
                Component component = spring.getComponent();
                for (int counter = this.sources.size() - 1; counter >= 0; counter--) {
                    GroupLayout.ComponentSpring source = (GroupLayout.ComponentSpring) this.sources.get(counter);
                    if (GroupLayout.this.areParallelSiblings(source.getComponent(), component, oAxis)) {
                        addMatch(source, spring);
                    }
                }
            }
        }

        private void addMatch(GroupLayout.ComponentSpring source, GroupLayout.ComponentSpring target) {
            if (this.matches == null) {
                this.matches = new ArrayList(1);
            }
            this.matches.add(new GroupLayout.AutopaddingMatch(source, target));
        }

        int getMinimumSize0(int axis) {
            return this.size;
        }

        int getPreferredSize0(int axis) {
            if ((this.pref == -2) || (this.pref == -1)) {
                return this.size;
            }
            return Math.max(this.size, this.pref);
        }

        int getMaximumSize0(int axis) {
            if (this.max >= 0) {
                return Math.max(getPreferredSize(axis), this.max);
            }
            return this.size;
        }

        String getMatchDescription() {
            return this.matches == null ? "" : this.matches.toString();
        }

        public String toString() {
            return super.toString() + getMatchDescription();
        }
    }

    private class ContainerAutopaddingSpring
            extends GroupLayout.AutopaddingSpring {
        private List targets;

        ContainerAutopaddingSpring() {
            super(null);
            setUserCreated(true);
        }

        ContainerAutopaddingSpring(int pref, int max) {
            super(pref, max);
            setUserCreated(true);
        }

        public void add(GroupLayout.ComponentSpring spring, int axis) {
            if (this.targets == null) {
                this.targets = new ArrayList(1);
            }
            this.targets.add(spring);
        }

        public void calculatePadding(int axis) {
            LayoutStyle p = LayoutStyle.getSharedInstance();
            int maxPadding = 0;
            this.size = 0;
            if (this.targets != null) {
                int position = axis == 1 ? 7 : 1;
                for (int i = this.targets.size() - 1; i >= 0; i--) {
                    GroupLayout.ComponentSpring targetSpring = (GroupLayout.ComponentSpring) this.targets.get(i);

                    int padding = 10;
                    if ((targetSpring.getComponent() instanceof JComponent)) {
                        padding = p.getContainerGap((JComponent) targetSpring.getComponent(), position, GroupLayout.this.host);


                        maxPadding = Math.max(padding, maxPadding);
                        padding -= targetSpring.getOrigin();
                    } else {
                        maxPadding = Math.max(padding, maxPadding);
                    }
                    this.size = Math.max(this.size, padding);
                }
            } else {
                int position = axis == 1 ? 3 : 5;
                if (this.sources != null) {
                    for (int i = this.sources.size() - 1; i >= 0; i--) {
                        GroupLayout.ComponentSpring sourceSpring = (GroupLayout.ComponentSpring) this.sources.get(i);

                        maxPadding = Math.max(maxPadding, updateSize(p, sourceSpring, position));
                    }
                } else if (this.source != null) {
                    maxPadding = updateSize(p, this.source, position);
                }
            }
            if (this.lastSize != -2147483648) {
                this.size += Math.min(maxPadding, this.lastSize);
            }
        }

        private int updateSize(LayoutStyle p, GroupLayout.ComponentSpring sourceSpring, int position) {
            int padding = 10;
            if ((sourceSpring.getComponent() instanceof JComponent)) {
                padding = p.getContainerGap((JComponent) sourceSpring.getComponent(), position, GroupLayout.this.host);
            }
            int delta = Math.max(0, getParent().getSize() - sourceSpring.getSize() - sourceSpring.getOrigin());

            this.size = Math.max(this.size, padding - delta);
            return padding;
        }

        String getMatchDescription() {
            if (this.targets != null) {
                return "leading: " + this.targets.toString();
            }
            if (this.sources != null) {
                return "trailing: " + this.sources.toString();
            }
            return "--";
        }
    }
}



