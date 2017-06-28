/*
 * Copyright 2017 Javier A. Ortiz Bultron javier.ortiz.78@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.javydreamercsw.validation.manager.web.execution;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.HorizontalLayout;
import com.validation.manager.core.VMException;
import com.validation.manager.core.api.internationalization.InternationalizationProvider;
import com.validation.manager.core.server.core.TestCaseExecutionServer;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import java.util.List;
import java.util.TreeMap;
import net.sourceforge.javydreamercsw.validation.manager.web.VMWindow;
import net.sourceforge.javydreamercsw.validation.manager.web.ValidationManagerUI;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public final class ExecutionWindow extends VMWindow {

    private final boolean reviewer;

    /**
     * Display all the executions one after another.
     *
     * @param executions Executions to display.
     * @param reviewer true if this is for a reviewer
     */
    public ExecutionWindow(
            List<TestCaseExecutionServer> executions, boolean reviewer) {
        super();
        this.reviewer = reviewer;
        init(executions, -1);
    }

    /**
     * Display all the executions one after another for the specific test case.
     *
     * @param executions Executions to display.
     * @param tcID test case to show
     * @param reviewer true if this is for a reviewer
     */
    public ExecutionWindow(
            List<TestCaseExecutionServer> executions, int tcID, boolean reviewer) {
        super();
        this.reviewer = reviewer;
        init(executions, tcID);
    }

    private void init(List<TestCaseExecutionServer> executions, int tcID) {
        HorizontalLayout layout = new HorizontalLayout();
        Wizard execution = new Wizard();
        TreeMap<Integer, TreeMap<Integer, ExecutionWizardStep>> sorted
                = new TreeMap<>();
        executions.forEach((tce) -> {
            tce.getExecutionStepList().forEach(es -> {
                if (tcID < 0
                        || es.getExecutionStepPK().getStepTestCaseId() == tcID) {
                    if (!sorted.containsKey(es.getExecutionStepPK()
                            .getStepTestCaseId())) {
                        sorted.put(es.getExecutionStepPK().getStepTestCaseId(),
                                new TreeMap<>());
                    }
                    sorted.get(es.getExecutionStepPK().getStepTestCaseId())
                            .put(es.getStep().getStepSequence(),
                                    new ExecutionWizardStep(execution, es,
                                            reviewer));
                }
            });
        });
        sorted.values().forEach(tm -> {
            tm.values().forEach(ew -> {
                execution.addStep(ew);
            });
        });
        execution.setDisplayedMaxTitles(3);
        execution.addListener(new WizardProgressListener() {
            @Override
            public void activeStepChanged(WizardStepActivationEvent event) {
                //Do nothing
            }

            @Override
            public void stepSetChanged(WizardStepSetChangedEvent event) {
                //Do nothing
            }

            @Override
            public void wizardCompleted(WizardCompletedEvent event) {
                if (reviewer) {
                    MessageBox prompt = MessageBox.createQuestion()
                            .withCaption(Lookup.getDefault().lookup(InternationalizationProvider.class)
                                    .translate("release.test.case.title"))
                            .withMessage(Lookup.getDefault().lookup(InternationalizationProvider.class)
                                    .translate("release.test.case.message"))
                            .withYesButton(() -> {
                                execution.getSteps().stream().map((step)
                                        -> (ExecutionWizardStep) step).map((s)
                                        -> s.getStep()).filter((ess) -> (!ess.getLocked()
                                        && ess.getResultId() != null))
                                        .forEachOrdered((ess) -> {
                                            try {
                                                if (ess.getReviewResultId().getId() == 2) {
                                                    //TODO: Failed, send back to retest?
                                                    ess.setLocked(false);
                                                }
                                                ess.setReviewed(true);
                                                ess.write2DB();
                                                ValidationManagerUI.getInstance()
                                                        .updateScreen();
                                            } catch (VMException ex) {
                                                Exceptions.printStackTrace(ex);
                                            }
                                        });
                            }, ButtonOption.focus(),
                                    ButtonOption.icon(VaadinIcons.CHECK))
                            .withNoButton(ButtonOption.icon(VaadinIcons.CLOSE));
                    prompt.getWindow().setIcon(ValidationManagerUI.SMALL_APP_ICON);
                    prompt.open();
                } else {
                    MessageBox prompt = MessageBox.createQuestion()
                            .withCaption(Lookup.getDefault().lookup(InternationalizationProvider.class)
                                    .translate("lock.test.case.title"))
                            .withMessage(Lookup.getDefault().lookup(InternationalizationProvider.class)
                                    .translate("lock.test.case.message"))
                            .withYesButton(() -> {
                                execution.getSteps().stream().map((step)
                                        -> (ExecutionWizardStep) step).map((s)
                                        -> s.getStep()).filter((ess) -> (!ess.getLocked()
                                        && ess.getResultId() != null))
                                        .forEachOrdered((ess) -> {
                                            try {
                                                ess.setLocked(true);
                                                ess.write2DB();
                                                ValidationManagerUI.getInstance()
                                                        .updateScreen();
                                            } catch (VMException ex) {
                                                Exceptions.printStackTrace(ex);
                                            }
                                        });
                            }, ButtonOption.focus(),
                                    ButtonOption.icon(VaadinIcons.CHECK))
                            .withNoButton(ButtonOption.icon(VaadinIcons.CLOSE));
                    prompt.getWindow().setIcon(ValidationManagerUI.SMALL_APP_ICON);
                    prompt.open();
                }
                ValidationManagerUI.getInstance().removeWindow(ExecutionWindow.this);
            }

            @Override
            public void wizardCancelled(WizardCancelledEvent event) {
                ValidationManagerUI.getInstance().removeWindow(ExecutionWindow.this);
            }
        });
        layout.addComponent(execution);
        layout.setSizeFull();
        setContent(layout);
    }
}
