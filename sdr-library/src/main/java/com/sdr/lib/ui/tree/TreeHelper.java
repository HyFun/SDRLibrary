package com.sdr.lib.ui.tree;

import com.sdr.lib.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/3.
 */

class TreeHelper {
    public static final int DEFAULT_EXPAND_ICON = -999;

    /**
     * 将用户数据转换成树形数据
     *
     * @param datas
     * @param <T>
     * @return
     */
    private static <T> List<Node> convertDatas2Nodes(List<T> datas) throws Exception {
        List<Node> nodes = new ArrayList<>();
        Node node = null;
        for (T t : datas) {
            String id = null;
            String pId = null;
            String label = null;
            boolean isChecked = false;
            boolean isLeaf = false;

            Class<?> clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // tree node id
                if (field.getAnnotation(TreeNodeId.class) != null) {
                    field.setAccessible(true);
                    id = (String) field.get(t);
                }
                // tree parent id
                if (field.getAnnotation(TreeNodeParentId.class) != null) {
                    field.setAccessible(true);
                    pId = (String) field.get(t);
                }
                // tree title
                if (field.getAnnotation(TreeNodeLabel.class) != null) {
                    field.setAccessible(true);
                    label = (String) field.get(t);
                }
                // tree checkbox
                if (field.getAnnotation(TreeNodeCheckBox.class) != null) {
                    field.setAccessible(true);
                    isChecked = (boolean) field.get(t);
                }
                // isLeaf
                if (field.getAnnotation(TreeNodeIsLeaf.class) != null) {
                    field.setAccessible(true);
                    isLeaf = (boolean) field.get(t);
                }
            }
            node = new Node(id, pId, label, isChecked, isLeaf);
            nodes.add(node);
        }


        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node m = nodes.get(j);
                if (m.getpId().equals(n.getId())) {
                    n.getChildren().add(m);
                    m.setParent(n);
                } else if (m.getId().equals(n.getpId())) {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        for (Node n : nodes) {
            setNodeIcon(n);
        }

        return nodes;
    }


    public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) throws Exception {
        List<Node> result = new ArrayList<>();
        List<Node> nodes = convertDatas2Nodes(datas);
        // 获取树的根节点
        List<Node> rootNodes = getTreeRootNodes(nodes);
        for (Node root : rootNodes) {
            addNode(result, root, defaultExpandLevel, 0);
        }
        // 判断是否是叶子
        for (Node node : result) {
            node.setLeaf(node.isLeaf());
        }

        return result;
    }

    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        result.add(node);
        if (defaultExpandLevel > currentLevel) {
            node.setExpand(true);
        }
        if (node.isLeaf()) return;

        for (int i = 0; i < node.getChildren().size(); i++) {
            addNode(result, node.getChildren().get(i), defaultExpandLevel, currentLevel + 1);
        }

    }

    private static List<Node> getTreeRootNodes(List<Node> nodes) {
        List<Node> roots = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot()) {
                roots.add(node);
            }
        }
        return roots;
    }

    /**
     * 设置Node是否展开
     * <p>
     * -1表示是最底层的叶子  不显示图标
     *
     * @param n
     */
    private static void setNodeIcon(Node n) {
        if (!n.isLeaf() && n.isExpand()) {
            n.setIcon(R.drawable.sdr_ic_tree_view_item_expand_24dp);
        } else if (!n.isLeaf() && !n.isExpand()) {
            n.setIcon(R.drawable.sdr_ic_tree_view_item_unexpand_24dp);
        } else {
            n.setIcon(DEFAULT_EXPAND_ICON);
        }
    }


    public static List<Node> filterVisibleNodes(List<Node> nodes) {
        List<Node> result = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot() || node.isParentExpand()) {
                setNodeIcon(node);
                result.add(node);
            }
        }
        return result;
    }
}
