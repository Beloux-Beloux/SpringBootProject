package com.medical.ui.dialogs;
import com.medical.ui.models.Patient;
import javax.swing.*; import java.awt.*;
public class PatientDialog extends JDialog {
    private final JTextField code=new JTextField(),nom=new JTextField(),prenom=new JTextField(),adresse=new JTextField();
    private final JComboBox<String> sexe=new JComboBox<>(new String[]{"F","M","Autre"});
    private boolean ok;
    public PatientDialog(Window owner, Patient v){super(owner,"Patient",ModalityType.APPLICATION_MODAL);add(form());if(v!=null){code.setText(v.codePat());nom.setText(v.nom());prenom.setText(v.prenom());sexe.setSelectedItem(v.sexe());adresse.setText(v.adresse());}pack();setMinimumSize(new Dimension(430,300));setLocationRelativeTo(owner);}
    private JPanel form(){JPanel p=new JPanel(new GridBagLayout());Object[] ls={"Code",code,"Nom",nom,"Prénom",prenom,"Sexe",sexe,"Adresse",adresse};for(int i=0;i<5;i++){p.add(new JLabel(ls[i*2].toString()),g(0,i));p.add((Component)ls[i*2+1],g(1,i));}JButton a=new JButton("Annuler"),s=new JButton("Enregistrer");s.addActionListener(e->{if(valid()){ok=true;dispose();}});a.addActionListener(e->dispose());JPanel b=new JPanel();b.add(a);b.add(s);p.add(b,g(1,5));return p;}
    private GridBagConstraints g(int x,int y){GridBagConstraints c=new GridBagConstraints();c.gridx=x;c.gridy=y;c.insets=new Insets(6,6,6,6);c.fill=x==1?GridBagConstraints.HORIZONTAL:0;c.weightx=x;return c;}
    private boolean valid(){for(JTextField f:new JTextField[]{code,nom,prenom,adresse})if(f.getText().isBlank()){JOptionPane.showMessageDialog(this,"Tous les champs sont obligatoires.");return false;}return true;}
    public boolean isOk(){return ok;}public String code(){return code.getText().trim();}public String nom(){return nom.getText().trim();}public String prenom(){return prenom.getText().trim();}public String sexe(){return sexe.getSelectedItem().toString();}public String adresse(){return adresse.getText().trim();}
}
