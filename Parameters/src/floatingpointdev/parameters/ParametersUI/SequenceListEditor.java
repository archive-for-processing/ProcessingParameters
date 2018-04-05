/**
 * 
 */
package floatingpointdev.parameters.ParametersUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import floatingpointdev.parameters.automationcontroller.AutomationController;
import floatingpointdev.parameters.automationcontroller.AutomationController;
import floatingpointdev.parameters.automationcontroller.Event;
import floatingpointdev.parameters.automationcontroller.PlayableSequence;
import floatingpointdev.parameters.automationcontroller.Sequence;
import floatingpointdev.parameters.automationcontroller.SequenceEvent;
import floatingpointdev.toolkit.util.IObserver;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class SequenceListEditor extends JPanel implements ActionListener {

  /** */
  private static final long serialVersionUID = 2757226108950732457L;

  private Sequence sequence;
  
  private JButton addEventBtn;
  private JButton removeEventBtn;
  private JTable eventList;
  private EventListTableModel eventListTableModel;
  private JTextField currentValue;
  /**
   * @param automationController
   */
  public SequenceListEditor(Sequence sequence) {
    super();
    this.sequence = sequence;
    sequence.addIObserver(new SequenceObserver());
    setupUi();
  }


  private void setupUi(){
    eventListTableModel = new EventListTableModel();
    
    //setup the eventlist
    eventList = new JTable(eventListTableModel);
    JScrollPane eventListScrollPane = new JScrollPane(eventList);
    eventListScrollPane.setPreferredSize(new Dimension(200,100));
    
    //setup the buttons
    addEventBtn = new JButton("Add");
    addEventBtn.addActionListener(this);
    removeEventBtn = new JButton("Remove");
    removeEventBtn.addActionListener(this);
    JPanel btnPanel = new JPanel();
    btnPanel.setLayout(new BoxLayout(btnPanel,BoxLayout.Y_AXIS));
    btnPanel.add(addEventBtn);
    btnPanel.add(removeEventBtn);
    
    //setup the current value text field
    currentValue = new JTextField(10);
    
    this.setLayout(new BorderLayout());
    this.setBorder(BorderFactory.createBevelBorder(1));
    this.add(eventListScrollPane, BorderLayout.CENTER);
    this.add(currentValue, BorderLayout.EAST);
    this.add(btnPanel, BorderLayout.WEST);
    this.setMinimumSize(new Dimension(100,50));
  }
  
  /** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)*/
  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    if(actionEvent.getSource() == addEventBtn){
      sequence.add(new Event(0,0));
      //eventListTableModel.fireTableRowsInserted(1, 1);
    } else if(actionEvent.getSource() == removeEventBtn){

      int selectedRow = eventList.getSelectedRow();
      sequence.remove(sequence.get(selectedRow));
      
    }
  }

  
  private class SequenceObserver implements IObserver{

    /** @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object, java.lang.Object)*/
    @Override
    public void update(Object theObserved, Object changeCode) {
      eventListTableModel.fireTableStructureChanged();
    }
  }
  
  
  class EventListTableModel extends AbstractTableModel{
    /** */
    private static final long serialVersionUID = 6362627954214518600L;
    //private Sequence sequence;
    
    /**
     * 
     */
    public EventListTableModel() {
      super();
      //this.sequence = sequence;

    }

    /** @see javax.swing.table.TableModel#getColumnCount()*/
    @Override
    public int getColumnCount() {
      return 2;
    }

    /** @see javax.swing.table.TableModel#getRowCount()*/
    @Override
    public int getRowCount() {
      return sequence.size();
    }

    /** @see javax.swing.table.TableModel#getValueAt(int, int)*/
    @Override
    public Object getValueAt(int row, int col) {
      SequenceEvent event = sequence.get(row);
      
      if(col == 0){
        return event.getTime();
      } else if(col == 1){
        return event.getValue();
      }
      return null;
    }

    /** @see javax.swing.table.AbstractTableModel#getColumnName(int)*/
    @Override
    public String getColumnName(int col) {
      if(col == 0){
        return "time";
      }else if(col == 1){
        return "value";
      }
      return "";
    }

    /** @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)*/
    @Override
    public boolean isCellEditable(int arg0, int arg1) {
      return true;
    }

    /** @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)*/
    @Override
    public void setValueAt(Object newValue, int row, int col) {
      SequenceEvent event = sequence.get(row);
      
      if(col == 0){
        //sequence.setEventTime(event, Long.parseLong((String)newValue));
        event.setTime(Long.parseLong((String)newValue));
      }
      
      else if(col == 1){
        //sequence.setEventValue(event, Float.parseFloat((String)newValue));
        event.setValue(Float.parseFloat((String)newValue));
      }
    }
    
  }
  
}
