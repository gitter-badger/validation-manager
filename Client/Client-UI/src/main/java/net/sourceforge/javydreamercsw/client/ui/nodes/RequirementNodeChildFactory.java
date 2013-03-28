package net.sourceforge.javydreamercsw.client.ui.nodes;

import com.validation.manager.core.DataBaseManager;
import com.validation.manager.core.db.Project;
import com.validation.manager.core.db.RequirementSpecNode;
import java.beans.IntrospectionException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
class RequirementNodeChildFactory extends ChildFactory<RequirementSpecNode> {

    private final Project parent;

    public RequirementNodeChildFactory(Project parent) {
        this.parent = parent;
    }

    @Override
    protected boolean createKeys(List<RequirementSpecNode> toPopulate) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("requirementSpecProjectId", parent.getId());
        List<Object> projects = DataBaseManager.namedQuery(
                "RequirementSpecNode.findByRequirementSpecProjectId", parameters);
        for (Iterator<Object> it = projects.iterator(); it.hasNext();) {
            toPopulate.add((com.validation.manager.core.db.RequirementSpecNode) it.next());
        }
        return true;
    }

    @Override
    protected Node[] createNodesForKey(RequirementSpecNode key) {
        return new Node[]{createNodeForKey(key)};
    }

    @Override
    protected Node createNodeForKey(RequirementSpecNode key) {
        try {
            return new UIRequirementSpecNodeNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }
}
