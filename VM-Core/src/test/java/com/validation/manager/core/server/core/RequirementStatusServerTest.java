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

import com.validation.manager.test.AbstractVMTestCase;
import org.junit.Test;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class RequirementStatusServerTest extends AbstractVMTestCase {

    /**
     * Test of write2DB method, of class RequirementStatusServer.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWrite2DB() throws Exception {
        System.out.println("write2DB");
        RequirementStatusServer instance = new RequirementStatusServer("test");
        instance.write2DB();
        assertEquals(instance.getStatus(), instance.getEntity().getStatus());
        assertTrue(instance.getId() > 0);
    }
}
