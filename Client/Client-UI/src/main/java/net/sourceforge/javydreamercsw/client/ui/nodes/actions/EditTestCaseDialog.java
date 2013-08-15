package net.sourceforge.javydreamercsw.client.ui.nodes.actions;

import com.validation.manager.core.db.Test;
import com.validation.manager.core.db.TestCase;
import com.validation.manager.core.server.core.TestCaseServer;
import com.validation.manager.core.server.core.VMUserServer;
import java.util.Date;
import net.sourceforge.javydreamercsw.client.ui.ProjectExplorerComponent;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class EditTestCaseDialog extends javax.swing.JDialog {

    private final boolean edit;

    /**
     * Creates new form EditTestCaseDialog
     */
    public EditTestCaseDialog(java.awt.Frame parent, boolean modal, boolean edit) {
        super(parent, modal);
        initComponents();
        this.edit = edit;
        if (edit) {
            testCaseName.setText(Utilities.actionsGlobalContext()
                    .lookup(TestCase.class).getName());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        testCaseName = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(EditTestCaseDialog.class, "EditTestCaseDialog.jLabel1.text")); // NOI18N

        testCaseName.setText(org.openide.util.NbBundle.getMessage(EditTestCaseDialog.class, "EditTestCaseDialog.testCaseName.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(okButton, org.openide.util.NbBundle.getMessage(EditTestCaseDialog.class, "EditTestCaseDialog.okButton.text")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, org.openide.util.NbBundle.getMessage(EditTestCaseDialog.class, "EditTestCaseDialog.cancelButton.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(testCaseName))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 276, Short.MAX_VALUE)
                        .addComponent(okButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(testCaseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        try {
            if (edit) {
                TestCaseServer tcs
                        = new TestCaseServer(Utilities.actionsGlobalContext()
                        .lookup(TestCase.class));
                tcs.setName(testCaseName.getText().trim());
                tcs.write2DB();
            } else {
                Test t = Utilities.actionsGlobalContext().lookup(Test.class);
                TestCaseServer tcs = new TestCaseServer(
                        testCaseName.getText().trim(),
                        t.getTestCaseList().size() + 1,
                        new Short("1"), new Date());
                //TODO: Use logged user instead
                tcs.setAuthorId(new VMUserServer(1).getEntity());
                tcs.setActive(true);
                tcs.setIsOpen(true);
                tcs.setTest(t);
                tcs.write2DB();
            }
            ProjectExplorerComponent.refresh();
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField testCaseName;
    // End of variables declaration//GEN-END:variables
}
