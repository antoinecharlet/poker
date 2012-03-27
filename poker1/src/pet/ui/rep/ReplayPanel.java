package pet.ui.rep;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import pet.hp.*;

/**
 * allows user to replay hands
 */
public class ReplayPanel extends JPanel {

	TableComponent tableComp = new TableComponent();
	JComboBox stateCombo = new JComboBox();
	JButton prevButton = new JButton("<");
	JButton nextButton = new JButton(">");

	public ReplayPanel() {
		super(new BorderLayout());

		prevButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectState(-1);
			}
		});

		stateCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					tableComp.setState((HandState) e.getItem());
				}
			}
		});

		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectState(1);
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(prevButton);
		buttonPanel.add(stateCombo);
		buttonPanel.add(nextButton);

		JPanel tablePanel = new JPanel(new GridLayout());
		tablePanel.setBorder(BorderFactory.createTitledBorder("Hand Replay"));
		tablePanel.add(tableComp);
		add(tablePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.NORTH);
	}


	private void selectState(int off) {
		int i = stateCombo.getSelectedIndex();
		if (i >= 0) {
			i += off;
			if (i >= 0 && i < stateCombo.getItemCount()) {
				System.out.println("setting index " + i);
				stateCombo.setSelectedIndex(i);
				repaint();
			}
		}
	}

	/**
	 * display the given hand
	 */
	public void setHand(Hand h) {
		List<HandState> states = HandStateUtil.getStates(h);
		stateCombo.setModel(new DefaultComboBoxModel(states.toArray(new Object[states.size()])));
		stateCombo.setSelectedIndex(0);
		tableComp.setHand(h);
		tableComp.setState(states.get(0));
		repaint();
	}
}


