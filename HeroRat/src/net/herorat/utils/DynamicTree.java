package net.herorat.utils;

/**
 * Created by lanky on 07/01/16.
 */
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


/*
 * This code is based on an example provided by Richard Stanford,
 * a tutorial reader.
 */

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Collection;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import net.herorat.features.servers.Server;

public class DynamicTree extends JPanel {
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    private DefaultMutableTreeNode baseNode;

    public DynamicTree() {
        super(new GridLayout(1,0));

        rootNode = new DefaultMutableTreeNode("servers");
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new MyTreeModelListener());
        tree = new JTree(treeModel);
        tree.setEditable(false);
        tree.setRootVisible(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);

        baseNode = this.addObject(rootNode,"Online Servers",true);

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }

    /** Remove all nodes except the root node. */
    public void clear() {
        baseNode.removeAllChildren();
        treeModel.reload();
    }

    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                    (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                return;
            }
        }

    }

    /** Add child to the currently selected node. */
    public DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode)
                    (parentPath.getLastPathComponent());
        }

        return addObject(parentNode, child, true);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child) {
        return addObject(parent, child, false);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child,
                                            boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode =
                new DefaultMutableTreeNode(child);

        if (parent == null) {
            parent = rootNode;
        }

        //It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        treeModel.insertNodeInto(childNode, parent,
                parent.getChildCount());

        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    public ServerMutableTreeNode addServerObject(Server child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode)
                    (parentPath.getLastPathComponent());
        }

        return addServerObject(parentNode, child, true);
    }

    public ServerMutableTreeNode addServerObject(DefaultMutableTreeNode parent,
                                            Server child) {
        return addServerObject(parent, child, false);
    }

    public ServerMutableTreeNode addServerObject(DefaultMutableTreeNode parent,
                                            Server child,
                                            boolean shouldBeVisible) {
        ServerMutableTreeNode childNode =
                new ServerMutableTreeNode(child);

        if (parent == null) {
            parent = rootNode;
        }

        //It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        treeModel.insertNodeInto(childNode, parent,
                parent.getChildCount());

        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    /**
     * adds online servers to the tree
     *
     * @param servers
     */
    public void addOnlineObjects(Collection<Server> servers){
        for(Server server : servers){
            addOnlineObject(server);
        }

    }

    public DefaultMutableTreeNode addOnlineObject(Object child){
        DefaultMutableTreeNode parent = baseNode;

        if(findOnlineObject(child) == null) {
            return addObject(parent, child, true);
//            Logger.log("Didn't find an object");
        }else{
//            Logger.log("Found an object");
            return null;
        }
    }

    public ServerMutableTreeNode addOnlineObject(Server child){
        DefaultMutableTreeNode parent = baseNode;
//        String name = child.getIp() + " ("+ child.getPing()+"/ms)";

        if(findOnlineObject(child) == null) {
//            Logger.log("Didn't find an object");
            return addServerObject(parent, child, true);
        }else{
//            Logger.log("Found an object");
            return null;
        }
    }


    /**
     * Finds an object in the online section by the name displayed
     *
     * @param name
     * @return
     */
    public DefaultMutableTreeNode findOnlineObjectByName(String name){
        DefaultMutableTreeNode node = null;
        for(int i = 0; i< baseNode.getChildCount(); i++){
            if(((DefaultMutableTreeNode) baseNode.getChildAt(i)).getUserObject().equals(name)){
                node = (DefaultMutableTreeNode) baseNode.getChildAt(i);
                break;
            }
        }
        return node;
    }

    public DefaultMutableTreeNode findOnlineObject(Object obj){
        DefaultMutableTreeNode node = null;
        for(int i = 0; i< baseNode.getChildCount(); i++){
            if(((DefaultMutableTreeNode) baseNode.getChildAt(i)).getUserObject().equals(obj)){
                node = (DefaultMutableTreeNode) baseNode.getChildAt(i);
                break;
            }
        }
        return node;
    }

    public void removeOnlineObject(DefaultMutableTreeNode userObject){
        treeModel.removeNodeFromParent(userObject);
    }

    public Server getSelectedServer(){
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            Logger.log(currentSelection.getLastPathComponent().toString());
            if(currentSelection.getLastPathComponent().equals(baseNode))
                return null;
            ServerMutableTreeNode currentNode = (ServerMutableTreeNode) (currentSelection.getLastPathComponent());
            return currentNode.getServer();
        }

        return null;
    }

    class MyTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());

            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */

            int index = e.getChildIndices()[0];
            node = (DefaultMutableTreeNode)(node.getChildAt(index));

            System.out.println("The user has finished editing the node.");
            System.out.println("New value: " + node.getUserObject());
        }
        public void treeNodesInserted(TreeModelEvent e) {
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        }
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }


    class ServerMutableTreeNode extends DefaultMutableTreeNode {
        private Server server = null;

        ServerMutableTreeNode(Server userObject){
            super(userObject);
            server = userObject;
        }

        ServerMutableTreeNode(Server userObject,boolean allowsChildren){
            super(userObject,allowsChildren);
            server = userObject;
        }


        public String toString(){
            if(server == null)
                return "No server!";
            else
                return server.getIp() + " ("+server.getUptime()+")";
        }

        public Server getServer(){
            return server;
        }
    }

}
