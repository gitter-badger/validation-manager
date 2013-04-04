package net.sourceforge.javydreamercsw.client.ui;

import com.validation.manager.core.DataBaseManager;
import com.validation.manager.core.db.Project;
import com.validation.manager.core.db.controller.exceptions.IllegalOrphanException;
import com.validation.manager.core.db.controller.exceptions.NonexistentEntityException;
import com.validation.manager.core.server.core.ProjectServer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//net.sourceforge.javydreamercsw.client.ui//ProjectEditor//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "ProjectEditorTopComponent",
        iconBase = "net/sourceforge/javydreamercsw/client/ui/VSmall.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "net.sourceforge.javydreamercsw.client.ui.ProjectEditorTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ProjectEditorAction",
        preferredID = "ProjectEditorTopComponent")
@Messages({
    "CTL_ProjectEditorAction=Project Editor",
    "CTL_ProjectEditorTopComponent=Project Editor Window",
    "HINT_ProjectEditorTopComponent=This is a Project Editor window"
})
public final class ProjectEditorTopComponent extends TopComponent
        implements LookupListener {

    private Lookup.Result<Object> result = null;
    private Project currentProject;

    public ProjectEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_ProjectEditorTopComponent());
        setToolTipText(Bundle.HINT_ProjectEditorTopComponent());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        notes = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        parent = new javax.swing.JComboBox();
        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ProjectEditorTopComponent.class, "ProjectEditorTopComponent.jLabel1.text_1")); // NOI18N

        name.setText(org.openide.util.NbBundle.getMessage(ProjectEditorTopComponent.class, "ProjectEditorTopComponent.name.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ProjectEditorTopComponent.class, "ProjectEditorTopComponent.jLabel2.text")); // NOI18N

        notes.setColumns(20);
        notes.setRows(5);
        notes.setText(org.openide.util.NbBundle.getMessage(ProjectEditorTopComponent.class, "ProjectEditorTopComponent.notes.text")); // NOI18N
        jScrollPane1.setViewportView(notes);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(ProjectEditorTopComponent.class, "ProjectEditorTopComponent.jLabel3.text")); // NOI18N

        parent.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        org.openide.awt.Mnemonics.setLocalizedText(save, org.openide.util.NbBundle.getMessage(ProjectEditorTopComponent.class, "ProjectEditorTopComponent.save.text")); // NOI18N
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(cancel, org.openide.util.NbBundle.getMessage(ProjectEditorTopComponent.class, "ProjectEditorTopComponent.cancel.text")); // NOI18N
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                            .addComponent(name)
                            .addComponent(parent, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(save)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(parent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(save)
                    .addComponent(cancel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        //Save changes
        //First we update the project
        ProjectServer ps = new ProjectServer(currentProject);
        ps.setName(name.getText());
        ps.setNotes(notes.getText().equals("(Empty)") ? "" : notes.getText());
        Project parentProject = null;
        if (parent.getSelectedIndex() > 0) {
            if (DataBaseTool.getEmf() != null) {
                List<Object> projectList = DataBaseManager.createdQuery(
                        "select p from Project p where p.name=" + parent.getSelectedItem());
                for (Iterator<Object> it2 = projectList.iterator(); it2.hasNext();) {
                    Project temp = ((Project) it2.next());
                    if (temp.getId() != currentProject.getId()) {
                        parentProject = temp;
                        break;
                    }
                }
            }
        }
        ps.setParentProjectId(parentProject);
        try {
            //Now we save it
            ps.write2DB();
        } catch (IllegalOrphanException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NonexistentEntityException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }//GEN-LAST:event_saveActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        //Cancel
        updateProject(currentProject);
    }//GEN-LAST:event_cancelActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField name;
    private javax.swing.JTextArea notes;
    private javax.swing.JComboBox parent;
    private javax.swing.JButton save;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        result = Utilities.actionsGlobalContext().lookupResult(Object.class);
        result.allItems();
        result.addLookupListener(this);
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    private void updateProject(Project p) {
        name.setText(p.getName());
        if (p.getNotes() != null && !p.getNotes().trim().isEmpty()) {
            notes.setText(p.getNotes());
        }
        //Add parent project
        List<Project> projects = new ArrayList<Project>();
        if (DataBaseTool.getEmf() != null) {
            List<Object> projectList = DataBaseManager.createdQuery(
                    "select p from Project p order by p.id");
            for (Iterator<Object> it2 = projectList.iterator(); it2.hasNext();) {
                Project temp = ((Project) it2.next());
                if (temp.getId() != p.getId()) {
                    projects.add(temp);
                }
            }
        }
        List<String> names = new ArrayList<String>();
        names.add("None");
        for (Iterator<Project> it3 = projects.iterator(); it3.hasNext();) {
            Project proj = it3.next();
            names.add(proj.getName());
        }
        parent.setModel(new javax.swing.DefaultComboBoxModel(names.toArray(new String[projects.size() + 1])));
        //Set to current
        if (p.getParentProjectId() == null) {
            parent.setSelectedIndex(0);
        } else {
            parent.setSelectedItem(p.getParentProjectId().getName());
        }
    }

    @Override
    public void resultChanged(LookupEvent le) {
        Lookup.Result res = (Lookup.Result) le.getSource();
        Collection instances = res.allInstances();

        if (!instances.isEmpty()) {
            Iterator it = instances.iterator();
            while (it.hasNext()) {
                Object item = it.next();
                if (item instanceof Project) {
                    Project p = (Project) item;
                    updateProject(p);
                    currentProject = p;
                }
            }
        }
    }
}
