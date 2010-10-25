/*
 * Ivory: A Hadoop toolkit for web-scale information retrieval
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package ivory.smrf.model.potential;

import ivory.exception.ConfigurationException;
import ivory.smrf.model.DocumentNode;
import ivory.smrf.model.GlobalEvidence;
import ivory.smrf.model.GraphNode;
import ivory.util.RetrievalEnvironment;
import ivory.util.XMLTools;

import java.util.List;

import org.w3c.dom.Node;

import com.google.common.base.Preconditions;

/**
 * @author Don Metzler
 * 
 */
public class DocumentPotential extends PotentialFunction {

	private RetrievalEnvironment mEnv = null;
	private DocumentNode mDocNode = null;

	private String mType;

	@Override
	public void configure(RetrievalEnvironment env, Node domNode) throws ConfigurationException {
		Preconditions.checkNotNull(env);
		Preconditions.checkNotNull(domNode);

		String type = XMLTools.getAttributeValue(domNode, "type", null);
		if (type == null) {
			throw new ConfigurationException("A DocumentPotential requires a type attribute!");
		}

		mEnv = env;
		mType = type;
	}

	@Override
	public void initialize(List<GraphNode> nodes, GlobalEvidence globalEvidence)
			throws ConfigurationException {
		Preconditions.checkNotNull(nodes);
		Preconditions.checkNotNull(globalEvidence);
		
		mDocNode = null;

		for (GraphNode node : nodes) {
			if (node.getType().equals(GraphNode.Type.DOCUMENT) && mDocNode != null) {
				throw new ConfigurationException("Only one document node allowed in DocumentPotential!");
			} else if (node.getType().equals(GraphNode.Type.DOCUMENT)) {
				mDocNode = (DocumentNode) node;
			} else if (node.getType().equals(GraphNode.Type.TERM)) {
				throw new ConfigurationException("TermNodes are not allowed in DocumentPotential!");
			}
		}
	}

	@Override
	public float computePotential() {
		return mEnv.getDocScore(mType, mDocNode.getDocno());
	}

	@Override
	public int getNextCandidate() {
		return Integer.MAX_VALUE;
	}

	@Override
	public String toString() {
		return "<potential type=\"DocumentPotential\" />\n";
	}

	@Override
	public void reset() {
	}

	@Override
	public void setNextCandidate(int docid) {
	}

	@Override
	public float getMaxScore() {
		return Float.POSITIVE_INFINITY;
	}
}