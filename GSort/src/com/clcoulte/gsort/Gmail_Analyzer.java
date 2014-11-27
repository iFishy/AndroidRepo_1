import java.awt.Component;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.awt.TextArea;

import javax.mail.internet.InternetAddress;
import javax.swing.JFrame;
import javax.swing.JTree;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.FlowLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Gmail_Analyzer
{

	private JFrame frame;
	private ArrayList<ReadingEmail.AddressHistory> data;
	private ArrayList<Label> labels;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Gmail_Analyzer window = new Gmail_Analyzer();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Gmail_Analyzer()
	{
		initialize();
	}

	private JTree tree;
	private JFormattedTextField labelTextField;
	private JButton btnAddLabel;
	private JList<Label> labelList;
	private DefaultListModel<Label> listModel;
	private JButton deleteLabelBut;
	private JList<String> tagList;
	private DefaultListModel<String> tagListModel;
	private JSplitPane labelSplitPane;
	private JPanel topPanel;
	private JSplitPane mainSplitPane;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		initData("Kelli", "clcoulte@gmail.com", "cgsbqpwjkdsyjhmv");
		labels = new ArrayList<Gmail_Analyzer.Label>();
		frame = new JFrame();

		frame.setBounds(100, 100, 696, 555);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		topPanel = new JPanel();
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);
		GridBagLayout gbl_topPanel = new GridBagLayout();

		topPanel.setLayout(gbl_topPanel);

		btnAddLabel = new JButton("Add Label");
		btnAddLabel.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				String label = labelTextField.getText();
				System.out.println(label);

				if (label.equals(""))
				{

					JOptionPane.showMessageDialog(frame,
							"Please input a label name.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (tree.getSelectionCount() <= 0)
				{
					JOptionPane.showMessageDialog(frame,
							"Select one or more domains to add label to.",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// label object to store info for specific label
				Label newLabel = new Label();
				newLabel.labelName = label;

				// paths of all selected values
				TreePath[] selection = tree.getSelectionPaths();
				newLabel.pathsForLabel = selection;

				// to store addresses of all selected values
				ArrayList<String> addresses = new ArrayList<String>();

				// iterate through selected values
				for (int i = 0; i < selection.length; i++)
				{
					TreePath t = selection[i];

					String tag;
					Object[] path = t.getPath();

					// make sure we're not at a leaf aka a count designation
					if (((DefaultMutableTreeNode) path[path.length - 1])
							.children().hasMoreElements())
					{
						// check if we're at a local address
						if (((DefaultMutableTreeNode) ((DefaultMutableTreeNode) path[path.length - 1])
								.children().nextElement()).children()
								.hasMoreElements())
						{
							if (((DefaultMutableTreeNode) ((DefaultMutableTreeNode) ((DefaultMutableTreeNode) path[path.length - 1])
									.children().nextElement()).children()
									.nextElement()).children()
									.hasMoreElements())
							{
								tag = "*."
										+ (String) ((DefaultMutableTreeNode) path[path.length - 1])
												.getUserObject();
							} else
							{
								tag = "*@"
										+ (String) ((DefaultMutableTreeNode) path[path.length - 1])
												.getUserObject();
							}
						} else
						{
							tag = (String) ((DefaultMutableTreeNode) path[path.length - 1])
									.getUserObject();
						}

						System.out.println(tag);
						addresses.add(tag);

					} else
					{ // can't add label to a count
					}
				}

				newLabel.tags = addresses;
				addLabel(newLabel);
				updateTagList();
			}
		});
		GridBagConstraints gbc_btnAddLabel = new GridBagConstraints();
		gbc_btnAddLabel.anchor = GridBagConstraints.WEST;
		gbc_btnAddLabel.gridx = 0;
		gbc_btnAddLabel.gridy = 0;
		topPanel.add(btnAddLabel, gbc_btnAddLabel);

		labelTextField = new JFormattedTextField();
		GridBagConstraints gbc_labelTextField = new GridBagConstraints();
		gbc_labelTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_labelTextField.weightx = 1.0;
		gbc_labelTextField.gridx = 1;
		gbc_labelTextField.gridy = 0;
		topPanel.add(labelTextField, gbc_labelTextField);

		deleteLabelBut = new JButton("Delete");
		deleteLabelBut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// delete

				// check tag
				List<String> selectedTags = tagList.getSelectedValuesList();
				List<Label> selectedLabels = labelList.getSelectedValuesList();
				if (selectedTags.size() != 0)
				{
					// delete tags
					for (int i = 0; i < selectedLabels.size(); i++)
					{
						selectedLabels.get(i).tags.removeAll(selectedTags);

						TreePath[] oldPaths = selectedLabels.get(i).pathsForLabel;
						ArrayList<TreePath> newPathsAL = new ArrayList<TreePath>();
						for (int j = 0; j < oldPaths.length; j++)
						{
							newPathsAL.add(oldPaths[i]);
						}
						for (int j = 0; j < selectedTags.size(); j++)
						{
							newPathsAL.remove(selectedTags.get(j));
						}
						TreePath[] newPaths = new TreePath[newPathsAL.size()];
						selectedLabels.get(i).pathsForLabel = newPathsAL
								.toArray(newPaths);
					}
					updateTagList();
				} else
				{
					// delete labels
					labels.removeAll(selectedLabels);
					updateLabelList();

					updateTagList();
				}

			}
		});
		GridBagConstraints gbc_deleteLabelBut = new GridBagConstraints();
		gbc_deleteLabelBut.anchor = GridBagConstraints.EAST;
		gbc_deleteLabelBut.gridx = 2;
		gbc_deleteLabelBut.gridy = 0;
		topPanel.add(deleteLabelBut, gbc_deleteLabelBut);

		JButton btnExport = new JButton("Export...");
		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.anchor = GridBagConstraints.EAST;
		gbc_btnExport.gridx = 3;
		gbc_btnExport.gridy = 0;
		topPanel.add(btnExport, gbc_btnExport);

		mainSplitPane = new JSplitPane();
		mainSplitPane.setResizeWeight(0.66);
		frame.getContentPane().add(mainSplitPane, BorderLayout.CENTER);
		tree = new JTree(createNodes());

		JScrollPane scrollPane = new JScrollPane(tree);
		listModel = new DefaultListModel<Label>();
		tagListModel = new DefaultListModel<String>();

		labelList = new JList<Label>();
		labelList.setModel(listModel);

		labelList.addListSelectionListener(new ListSelectionListener()
		{

			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (!e.getValueIsAdjusting())
				{
					updateTagList();
				}

			}

		});
		tagList = new JList<String>();
		tagList.setModel(tagListModel);
		labelSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, labelList,
				tagList);
		labelSplitPane.setResizeWeight(0.5);
		mainSplitPane.setLeftComponent(scrollPane);
		mainSplitPane.setRightComponent(labelSplitPane);

	}

	private void updateLabelList()
	{

	}

	private void updateTagList()
	{
		tagListModel.removeAllElements();
		tree.clearSelection();
		List<Label> selectedLabels = labelList.getSelectedValuesList();

		if (selectedLabels != null)
		{
			for (int j = 0; j < selectedLabels.size(); j++)
			{
				for (int i = 0; i < selectedLabels.get(j).tags.size(); i++)
				{
					String curTag = selectedLabels.get(j).tags.get(i);
					if (!tagListModel.contains(curTag))
					{
						tagListModel.addElement(curTag);
					}
				}

				tree.addSelectionPaths(selectedLabels.get(j).pathsForLabel);
			}

		}
	}

	private void addLabel(Label l)
	{
		int index = labels.indexOf(l);
		if (index >= 0)
		{
			// label already in list
			// add addresses to existing label unless they're already in
			// there...
			for (int i = 0; i < l.tags.size(); i++)
			{
				// if the current tag from new label is not already in the tags,
				// add it
				if (!labels.get(index).tags.contains(l.tags.get(i)))
				{
					labels.get(index).tags.add(l.tags.get(i));
				}
			}

			// add paths to existing label...
			ArrayList<TreePath> currentPathsAL = new ArrayList<TreePath>();
			TreePath[] currentPaths = labels.get(index).pathsForLabel;
			for (int i = 0; i < currentPaths.length; i++)
			{
				currentPathsAL.add(currentPaths[i]);
			}
			for (int i = 0; i < l.pathsForLabel.length; i++)
			{
				if (!currentPathsAL.contains(l.pathsForLabel[i]))
				{
					// if currentPaths dont contain this one, then add it
					currentPathsAL.add(l.pathsForLabel[i]);
				}
			}
			TreePath[] newPaths = new TreePath[currentPathsAL.size()];
			newPaths = currentPathsAL.toArray(newPaths);
			labels.get(index).pathsForLabel = newPaths;

			labelList.setSelectedValue(labels.get(index), true);

			// TODO if selected?
		} else
		{
			labels.add(l);
			listModel.addElement(l);
			labelList.setSelectedValue(l, true);

		}
		// change selected label to added label

		updateTagList();

	}

	private void initData(String folderName, String userName, String pw)
	{
		data = ReadingEmail.getAddressAnalysis(folderName, userName, pw,
				ReadingEmail.SortOrderKey.DEFAULT);
		// TODO add in loading bar
	}

	private static class Label
	{
		public ArrayList<String> tags = new ArrayList<String>();
		public String labelName = "";
		public TreePath[] pathsForLabel;

		public boolean equals(Object o)
		{
			if (o != null)
			{
				return ((Label) o).labelName.equals(labelName);
			}
			return false;
		}

		public String toString()
		{
			return labelName;
		}

	}

	private static class Node
	{
		private String data;
		private Node parent;
		private ArrayList<Node> children;

		public Node(String adata)
		{
			this.data = adata;
			parent = null;
			children = new ArrayList<Node>();
		}

		public Node(String adata, Node aparent)
		{
			this.data = adata;
			parent = aparent;
			children = new ArrayList<Node>();
		}

		public Node contains(String searchData)
		{
			if (searchData.equals(this.data))
			{
				return this;
			} else
			{
				for (int i = 0; i < children.size(); i++)
				{
					Node result = children.get(i).contains(searchData);
					if (result != null)
					{
						return result;
					}

				}
			}
			// can't find
			return null;
		}
	}

	private DefaultMutableTreeNode createNodes()
	{

		Node treeRoot = new Node("clcoulte@gmail.com");

		Node current = treeRoot;

		for (int i = 0; i < data.size(); i++)
		{

			ReadingEmail.AddressHistory ah = data.get(i);
			String[] domains = ah.getReversedDomainArray();
			// build tree
			for (int j = 0; j < domains.length; j++)
			{
				Node result = current.contains(domains[j]);
				if (result == null)
				{
					// found the right place to put the rest of the strings

					String append = "";
					if (j > 0)
					{
						append = "." + current.data;

					}
					Node n = new Node(domains[j] + append, current);
					current.children.add(n);
					current = n;

				} else
				{
					current = result;
				}
			}
			// add local to end of line
			Node n = new Node(data.get(i).getLocal() + "@" + current.data,
					current);
			current.children.add(n);
			Node countNode = new Node("" + data.get(i).count, n);
			n.children.add(countNode);

			current = treeRoot;
		}

		sort(treeRoot);

		// now that we have a tree in treeRoot, apply it to the JTree

		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		addTreeNodeRecursive(root, treeRoot);

		return root;

	}

	private static void sort(Node treeRoot)
	{
		Collections.sort(treeRoot.children, new Comparator<Node>()
		{

			@Override
			public int compare(Node o1, Node o2)
			{
				return o1.data.compareTo(o2.data);
			}

		});
		for (int i = 0; i < treeRoot.children.size(); i++)
		{
			sort(treeRoot.children.get(i));
		}
	}

	private static <T> void addTreeNodeRecursive(DefaultMutableTreeNode TN,
			Node n)
	{
		if (n.children != null && n.children.size() != 0)
		{
			for (int i = 0; i < n.children.size(); i++)
			{
				// for each child, add a child to TN and add the rest of the
				// children to that node
				DefaultMutableTreeNode tnChild = new DefaultMutableTreeNode(
						n.children.get(i).data);

				TN.add(tnChild);
				addTreeNodeRecursive(tnChild, n.children.get(i));
			}
		}
	}

}
