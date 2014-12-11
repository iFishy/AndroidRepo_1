package com.clcoulte.gsort.Util;

import java.util.List;

public class TreeNode<T> {
	private T data;
	private TreeNode<T> parent;
	private List<T> children;
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public TreeNode<T> getParent() {
		return parent;
	}
	public void setParent(TreeNode<T> parent) {
		this.parent = parent;
	}
	public List<T> getChildren() {
		return children;
	}
	public void setChildren(List<T> children) {
		this.children = children;
	}

}
