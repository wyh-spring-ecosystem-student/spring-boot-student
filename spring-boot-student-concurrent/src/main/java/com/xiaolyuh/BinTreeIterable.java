package com.xiaolyuh;

import com.alibaba.fastjson.JSON;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树的遍历
 *
 * @author yuhao.wang3
 * @since 2019/7/22 19:47
 */
public class BinTreeIterable {
    //      A
    //    /   \
    //   B     C
    //    \
    //     D
    static BinTreeNode<String> D = new BinTreeNode<>("D", null, null);
    static BinTreeNode<String> B = new BinTreeNode<>("B", null, D);
    static BinTreeNode<String> C = new BinTreeNode<>("C", null, null);
    static BinTreeNode<String> A = new BinTreeNode<>("A", B, C);

    public static void main(String[] args) {
        List<String> list = new LinkedList<>();
        preorderIterable1(A, list);
        System.out.println("先序遍历: " + JSON.toJSONString(list));
        list = new LinkedList<>();
        preorderIterable2(A, list);
        System.out.println("先序遍历: " + JSON.toJSONString(list));

        list = new LinkedList<>();
        middleIterable1(A, list);
        System.out.println("中序遍历: " + JSON.toJSONString(list));
        list = new LinkedList<>();
        middleIterable2(A, list);
        System.out.println("中序遍历: " + JSON.toJSONString(list));

        list = new LinkedList<>();
        backIterable1(A, list);
        System.out.println("后序遍历: " + JSON.toJSONString(list));
        list = new LinkedList<>();
        backIterable2(A, list);
        System.out.println("后序遍历: " + JSON.toJSONString(list));

        list = new LinkedList<>();
        layerIterable2(A, list);
        System.out.println("层序遍历: " + JSON.toJSONString(list));
        list = new LinkedList<>();
        backIterable2(A, list);
        System.out.println("层序遍历: " + JSON.toJSONString(list));
    }

    /**
     * 先序遍历递归：先根、后左、再右
     * <p>
     * "A","B","D","C"
     */
    public static void preorderIterable1(BinTreeNode<String> root, List<String> list) {

        if (root == null) {
            return;
        }
        list.add(root.data);
        preorderIterable1(root.left, list);
        preorderIterable1(root.right, list);
    }

    /**
     * 先序遍历非递归：先根、后左、再右
     * 　　a. 遇到一个节点，访问它，然后把它压栈，并去遍历它的左子树；
     * 　　b. 当左子树遍历结束后，从栈顶弹出该节点并将其指向右儿子，继续a步骤；
     * 　　c. 当所有节点访问完即最后访问的树节点为空且栈空时，停止。
     * <p>
     * "A","B","D","C"
     */
    public static void preorderIterable2(BinTreeNode<String> root, List<String> list) {
        BinTreeNode<String> p = root;
        Stack<BinTreeNode<String>> stack = new Stack<>();
        while (p != null || !stack.empty()) {
            while (p != null) {
                list.add(p.data);
                stack.push(p);
                p = p.left;
            }

            if (!stack.isEmpty()) {
                //节点弹出堆栈
                p = stack.pop();
                // 转向右子树
                p = p.right;
            }
        }
    }


    /**
     * 中序遍历递归：先左、后根、再右
     * <p>
     * "B","D","A","C"
     */
    public static void middleIterable1(BinTreeNode<String> root, List<String> list) {
        if (root == null) {
            return;
        }
        middleIterable1(root.left, list);
        list.add(root.data);
        middleIterable1(root.right, list);
    }

    /**
     * 中序遍历递归：先左、后根、再右
     * <p>
     * "B","D","A","C"
     */
    public static void middleIterable2(BinTreeNode<String> root, List<String> list) {
        Stack<BinTreeNode<String>> stack = new Stack<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            if (!stack.isEmpty()) {
                root = stack.pop();
                list.add(root.data);
                root = root.right;
            }
        }
    }

    /**
     * 后序遍历递归：先左、后右、再根
     * <p>
     * "B","D","A","C"
     */
    public static void backIterable1(BinTreeNode<String> root, List<String> list) {
        if (root == null) {
            return;
        }
        backIterable1(root.left, list);
        backIterable1(root.right, list);
        list.add(root.data);
    }

    /**
     * 中序遍历递归：先左、后右、再根
     * <p>
     * "B","D","A","C"
     */
    public static void backIterable2(BinTreeNode<String> root, List<String> list) {
        Stack<BinTreeNode<String>> stack = new Stack<>();
        BinTreeNode<String> prev = null, curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                // 添加根节点
                stack.push(curr);
                // 递归添加左节点
                curr = curr.left;
            }
            // 已经访问到最左节点了
            curr = stack.peek();

            // 是否存在右节点或者右节点已经访问过的情况下，访问根节点
            if (curr.right == null || curr.right == prev) {
                stack.pop();
                list.add(curr.data);
                prev = curr;
                // 不重复访问自己
                curr = null;
            } else {
                // 右节点还没有访问过就先访问右节点
                curr = curr.right;
            }
        }
    }


    /**
     * 层序遍历
     *
     * @param root
     * @return
     */
    public static void layerIterable2(BinTreeNode<String> root, List<String> list) {
        LinkedList<BinTreeNode<String>> queue = new LinkedList<>();
        if (root == null) {
            return;
        }
        queue.addLast(root);
        while (!queue.isEmpty()) {
            BinTreeNode<String> p = queue.poll();
            list.add(p.data);
            if (p.left != null) {
                queue.addLast(p.left);
            }
            if (p.right != null) {
                queue.addLast(p.right);
            }
        }
    }

    static class BinTreeNode<T> {
        /**
         * 数据
         */
        T data;

        /**
         * 左节点
         */
        BinTreeNode<T> left;

        /**
         * 右节点
         */
        BinTreeNode<T> right;

        public BinTreeNode(T data, BinTreeNode left, BinTreeNode right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }
}
