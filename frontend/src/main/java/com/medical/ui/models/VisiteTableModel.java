package com.medical.ui.models;
import javax.swing.table.AbstractTableModel;import java.util.*;
public class VisiteTableModel extends AbstractTableModel{
    private final String[] cols={
            "Médecin",
            "Patient",
            "Date",
            "Nom médecin",
            "Nom patient",
            "Actions"
    };
    private List<Visite> data=new ArrayList<>();
    public void setData(List<Visite>d){
        data=new ArrayList<>(d);
        fireTableDataChanged();
    }
    public Visite getAt(int r){
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
        var x=data.get(r);
        return switch(c){
            case 0->x.codeMed();
            case 1->x.codePat();
            case 2->x.date();
            case 3->x.medecinNom();
            case 4->x.patientNom();
            default->"";
        };
    }
}
