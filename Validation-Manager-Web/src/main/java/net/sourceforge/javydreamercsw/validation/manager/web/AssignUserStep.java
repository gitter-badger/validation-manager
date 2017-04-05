package net.sourceforge.javydreamercsw.validation.manager.web;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.validation.manager.core.DataBaseManager;
import com.validation.manager.core.db.TestCase;
import com.validation.manager.core.db.TestCaseExecution;
import com.validation.manager.core.db.VmUser;
import com.validation.manager.core.db.controller.TestCaseJpaController;
import com.validation.manager.core.server.core.RoleServer;
import com.validation.manager.core.server.core.TestCaseExecutionServer;
import com.validation.manager.core.server.core.TestCaseServer;
import com.validation.manager.core.server.core.VMUserServer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.teemu.wizards.WizardStep;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class AssignUserStep implements WizardStep {

    private final Object key;
    private final TreeTable testTree = new TreeTable("Available Tests");
    private final TreeTable testerTree = new TreeTable("Available Testers");
    private TestCaseExecutionServer tce = null;
    private TestCaseServer tc = null;
    private static final Logger LOG
            = Logger.getLogger(AssignUserStep.class.getSimpleName());

    public AssignUserStep(Object item) {
        this.key = item;
    }

    @Override
    public String getCaption() {
        return "Assign Test Case(s)";
    }

    @Override
    public Component getContent() {
        VerticalLayout l = new VerticalLayout();
        List<TestCase> testCases = new ArrayList<>();
        List<VmUser> users = new ArrayList<>();
        //Parse the id: tce-<tce id>-<test case id>
        if (key instanceof String) {
            String item = (String) key;
            String tceIdS = item.substring(item.indexOf("-") + 1,
                    item.lastIndexOf("-"));
            try {
                int tceId = Integer.parseInt(tceIdS);
                LOG.log(Level.INFO, "{0}", tceId);
                tce = new TestCaseExecutionServer(tceId);
            } catch (NumberFormatException nfe) {
                LOG.log(Level.INFO, "Unable to find TCE: " + tceIdS, nfe);
            }
            try {
                int tcId = Integer.parseInt(item.substring(item.lastIndexOf("-") + 1));
                LOG.log(Level.INFO, "{0}", tcId);
                tc = new TestCaseServer(tcId);
            } catch (NumberFormatException nfe) {
                LOG.log(Level.INFO, "Unable to find TCE: " + tceIdS, nfe);
            }
        } else if (key instanceof TestCaseExecution) {
            //It is a TestCaseExecution
            tce = new TestCaseExecutionServer((TestCaseExecution) key);
        } else {
            LOG.log(Level.SEVERE, "Unexpected key: {0}", key);
        }
        if (tc != null) {
            testCases.add(tc.getEntity());
        } else if (tce != null) {
            tce.getExecutionStepList().stream().filter((es)
                    -> (!testCases.contains(es.getStep().getTestCase())))
                    .forEachOrdered((es) -> {
                        testCases.add(es.getStep().getTestCase());
                    });
        }
        testTree.addContainerProperty("Name", TreeTableCheckBox.class, "");
        testTree.addContainerProperty("Description", String.class, "");
        testTree.setWidth("20em");
        testCases.forEach((t) -> {
            testTree.addItem(new Object[]{new TreeTableCheckBox(testTree,
                t.getName(), t.getId()),
                t.getSummary() == null ? "" : new String(t.getSummary(),
                StandardCharsets.UTF_8)}, t.getId());
            testTree.setChildrenAllowed(t.getId(), false);
        });
        testTree.setPageLength(testCases.size() + 1);
        testTree.setSizeFull();
        l.addComponent(testTree);
        //Add list of testers
        users.addAll(RoleServer.getRole("tester").getVmUserList());
        testerTree.addContainerProperty("Name", TreeTableCheckBox.class, "");
        testerTree.setWidth("20em");
        users.forEach((u) -> {
            testerTree.addItem(new Object[]{new TreeTableCheckBox(testerTree,
                u.getFirstName() + " " + u.getLastName(), u.getId())}, u.getId());
            testerTree.setChildrenAllowed(u.getId(), false);
        });
        testerTree.setPageLength(users.size() + 1);
        testerTree.setSizeFull();
        l.addComponent(testerTree);
        return l;
    }

    @Override
    public boolean onAdvance() {
        boolean selectedTestCase = false;
        List<Integer> testCaseIds = new ArrayList<>();
        int userId = 1;
        for (Object id : testTree.getItemIds()) {
            Item item = testTree.getItem(id);
            Object val = item.getItemProperty("Name").getValue();
            if (val instanceof TreeTableCheckBox) {
                TreeTableCheckBox ttcb = (TreeTableCheckBox) val;
                if (ttcb.getValue()) {
                    selectedTestCase = true;
                    testCaseIds.add((Integer) id);
                }
            }
        }
        if (!selectedTestCase) {
            Notification.show("Unable to proceed",
                    "Please select at least one Test Case prior to continuing.",
                    Notification.Type.WARNING_MESSAGE);
            return false;
        }
        boolean selectedUser = false;
        int count = 0;
        for (Object id : testerTree.getItemIds()) {
            Item item = testerTree.getItem(id);
            Object val = item.getItemProperty("Name").getValue();
            if (val instanceof TreeTableCheckBox) {
                TreeTableCheckBox ttcb = (TreeTableCheckBox) val;
                if (ttcb.getValue()) {
                    selectedUser = true;
                    userId = (int) id;
                    count++;
                    if (count > 1) {
                        break;
                    }
                }
            }
        }
        if (!selectedUser || count > 1) {
            Notification.show("Unable to proceed",
                    "Please select one User prior to continuing.",
                    Notification.Type.WARNING_MESSAGE);
            return false;
        }
        try {
            //Now process the data
            VMUserServer user = new VMUserServer(userId);
            TestCaseJpaController c
                    = new TestCaseJpaController(DataBaseManager
                            .getEntityManagerFactory());
            testCaseIds.forEach((id) -> {
                user.assignTestCase(tce, c.findTestCase(id));
            });
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean onBack() {
        return false;
    }
}
