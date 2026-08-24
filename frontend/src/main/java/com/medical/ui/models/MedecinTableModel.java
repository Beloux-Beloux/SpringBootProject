package com.medical.ui.models;
import javax.swing.table.AbstractTableModel;import java.util.*;
public class MedecinTableModel extends AbstractTableModel{
    private final String[] cols={"Code","Nom","Prénom","Grade","Actions"};
    private List<Medecin> data=new ArrayList<>();

    public void setData(List<Medecin>d){
        data=new ArrayList<>(d);fireTableDataChanged();
    }
    public Medecin getAt(int r){
        return data.get(r);
    }
    public int getRowCount(){
        return data.size();
    }
    public int getColumnCount(){
        return cols.length;
    }
    public String getColumnName(int c){
        return cols[c];
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 4;
    }
    public Object getValueAt(int r,int c){
        var x=data.get(r);return switch(c){
            case 0->x.codeMed();
            case 1->x.nom();
            case 2->x.prenom();
            case 3->x.grade();
            default->"";
        };
    }
}
