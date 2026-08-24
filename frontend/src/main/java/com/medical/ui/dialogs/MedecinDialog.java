package com.medical.ui.dialogs;
import com.medical.ui.models.Medecin;
import javax.swing.*; import java.awt.*;
public class MedecinDialog extends JDialog {
    private final JTextField code=new JTextField(), nom=new JTextField(), prenom=new JTextField(), grade=new JTextField();
    private boolean ok;
    public MedecinDialog(Window owner, Medecin value) {
        super(owner,"Médecin",ModalityType.APPLICATION_MODAL);
        JPanel p=form(); if(value!=null){code.setText(value.codeMed());nom.setText(value.nom());prenom.setText(value.prenom());grade.setText(value.grade());}
        add(p); pack(); setMinimumSize(new Dimension(400,260)); setLocationRelativeTo(owner);
    }
    private JPanel form(){
        JPanel p=new JPanel(new GridBagLayout()); String[] labels={"Code","Nom","Prénom","Grade"}; JTextField[] f={code,nom,prenom,grade};
        for(int i=0;i<4;i++){p.add(new JLabel(labels[i]),gb(0,i));p.add(f[i],gb(1,i));}
        JButton cancel=new JButton("Annuler"), save=new JButton("Enregistrer"); save.addActionListener(e->{if(valid()){ok=true;dispose();}});
        cancel.addActionListener(e->dispose()); JPanel b=new JPanel();b.add(cancel);b.add(save);p.add(b,gb2(1,4));return p;
    }
    private GridBagConstraints gb(int x,int y){GridBagConstraints g=new GridBagConstraints();g.gridx=x;g.gridy=y;g.insets=new Insets(6,6,6,6);g.fill=x==1?GridBagConstraints.HORIZONTAL:GridBagConstraints.NONE;g.weightx=x;return g;}
    private GridBagConstraints gb2(int x,int y){return gb(x,y);}
    private boolean valid(){for(JTextField f:new JTextField[]{code,nom,prenom,grade})if(f.getText().isBlank()){JOptionPane.showMessageDialog(this,"Tous les champs sont obligatoires.");return false;}return true;}
    public boolean isOk(){return ok;} public String code(){return code.getText().trim();} public String nom(){return nom.getText().trim();} public String prenom(){return prenom.getText().trim();} public String grade(){return grade.getText().trim();}
}
