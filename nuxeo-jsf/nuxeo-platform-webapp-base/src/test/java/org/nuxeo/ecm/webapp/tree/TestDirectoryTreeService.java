package org.nuxeo.ecm.webapp.tree;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ActionService;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.webapp.directory.DirectoryTreeDescriptor;
import org.nuxeo.ecm.webapp.directory.DirectoryTreeService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.RuntimeFeature;

/**
 * @since 11.1
 */
@RunWith(FeaturesRunner.class)
@Features(RuntimeFeature.class)
@Deploy("org.nuxeo.ecm.actions:OSGI-INF/actions-framework.xml")
@Deploy("org.nuxeo.ecm.webapp.base:OSGI-INF/directorytreemanager-framework.xml")
@Deploy("org.nuxeo.ecm.webapp.base.tests:test-directory-tree-contrib.xml")
public class TestDirectoryTreeService {
    @Test
    public void shouldRegisterActionContribution() {
        DirectoryTreeService directoryTreeService = (DirectoryTreeService) Framework.getRuntime()
                                                                                    .getComponent(
                                                                                            DirectoryTreeService.NAME);
        assertNotNull(directoryTreeService);
        assertTrue(directoryTreeService.getDirectoryTrees().contains("anyNavigation"));

        ActionService actionService = (ActionService) Framework.getService(ActionManager.class);
        assertNotNull(actionService);

        Action action = actionService.getAction(DirectoryTreeDescriptor.ACTION_ID_PREFIX + "anyNavigation");
        assertNotNull(action);

    }
}
