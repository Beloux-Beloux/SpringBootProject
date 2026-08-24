package com.medical.ui.dialogs;
import com.medical.ui.models.Visite;
import javax.swing.*;import java.awt.*;import java.time.LocalDate;import java.time.format.DateTimeParseException;
public class VisiteDialog extends JDialog {
    private final JTextField med=new JTextField(),pat=new JTextField(),date=new JTextField();
    private boolean ok;
    public VisiteDialog(Window owner,Visite v){super(owner,"Visite",ModalityType.APPLICATION_MODAL);add(form());if(v!=null){med.setText(v.codeMed());pat.setText(v.codePat());date.setText(v.date().toString());}pack();setMinimumSize(new Dimension(400,240));setLocationRelativeTo(owner);}
    private JPanel form(){JPanel p=new JPanel(new GridBagLayout());String[] l={"Code médecin","Code patient","Date (AAAA-MM-JJ)"};JTextField[] f={med,pat,date};for(int i=0;i<3;i++){p.add(new JLabel(l[i]),g(0,i));p.add(f[i],g(1,i));}JButton a=new JButton("Annuler"),s=new JButton("Enregistrer");s.addActionListener(e->{if(valid()){ok=true;dispose();}});a.addActionListener(e->dispose());JPanel b=new JPanel();b.add(a);b.add(s);p.add(b,g(1,3));return p;}
    private GridBagConstraints g(int x,int y){GridBagConstraints c=new GridBagConstraints();c.gridx=x;c.gridy=y;c.insets=new Insets(6,6,6,6);c.fill=x==1?GridBagConstraints.HORIZONTAL:0;c.weightx=x;return c;}
    private boolean valid(){if(med.getText().isBlank()||pat.getText().isBlank()||date.getText().isBlank()){JOptionPane.showMessageDialog(this,"Tous les champs sont obligatoires.");return false;}try{LocalDate.parse(date.getText().trim());return true;}catch(DateTimeParseException e){JOptionPane.showMessageDialog(this,"Date invalide. Format: AAAA-MM-JJ");return false;}}
    public boolean isOk(){return ok;}public String med(){return med.getText().trim();}public String pat(){return pat.getText().trim();}public LocalDate date(){return LocalDate.parse(date.getText().trim());}
}
