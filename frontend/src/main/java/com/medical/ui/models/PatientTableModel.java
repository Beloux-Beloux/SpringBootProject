package com.medical.ui.models;
import javax.swing.table.AbstractTableModel;import java.util.*;
public class PatientTableModel extends AbstractTableModel{
    private final String[] cols={"Code","Nom","Prénom","Sexe","Adresse","Actions"};
    private List<Patient> data=new ArrayList<>();
    public void setData(List<Patient>d){
        data=new ArrayList<>(d);fireTableDataChanged();
    }
    public Patient getAt(int r){
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
        return columnIndex == 5;
    }
    public Object getValueAt(int r,int c){
        var x=data.get(r);return switch(c){
            case 0->x.codePat();
            case 1->x.nom();
            case 2->x.prenom();
            case 3->x.sexe();
            case 4->x.adresse();
            default->"";
        };
    }
}
