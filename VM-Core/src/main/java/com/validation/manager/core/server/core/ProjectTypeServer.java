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
package com.validation.manager.core.server.core;

import com.validation.manager.core.DataBaseManager;
import com.validation.manager.core.EntityServer;
import com.validation.manager.core.db.ProjectType;
import com.validation.manager.core.db.controller.ProjectTypeJpaController;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public final class ProjectTypeServer extends ProjectType
        implements EntityServer<ProjectType> {

    public ProjectTypeServer(Integer i) {
        super(i);
        if (i != null) {
            update();
        }
    }

    @Override
    public int write2DB() throws Exception {
        ProjectTypeJpaController c
                = new ProjectTypeJpaController(DataBaseManager
                        .getEntityManagerFactory());
        if (getId() == null) {
            ProjectType pt = new ProjectType();
            update(pt, this);
            c.create(pt);
            setId(pt.getId());
        } else {
            ProjectType pt = getEntity();
            update(pt, this);
            c.edit(pt);
        }
        update();
        return getId();
    }

    @Override
    public ProjectType getEntity() {
        return new ProjectTypeJpaController(DataBaseManager
                .getEntityManagerFactory()).findProjectType(getId());
    }

    @Override
    public void update(ProjectType target, ProjectType source) {
        target.setId(source.getId());
        target.setProjectList(source.getProjectList());
        target.setTypeName(source.getTypeName());
        target.setTemplateList(source.getTemplateList());
    }

    @Override
    public void update() {
        update(this, getEntity());
    }
}
