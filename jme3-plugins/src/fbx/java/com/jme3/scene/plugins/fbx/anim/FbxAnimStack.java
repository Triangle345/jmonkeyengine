/*
 * Copyright (c) 2009-2015 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.scene.plugins.fbx.anim;

import com.jme3.asset.AssetManager;
import com.jme3.scene.plugins.fbx.file.FbxElement;
import com.jme3.scene.plugins.fbx.obj.FbxObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FbxAnimStack extends FbxObject {

    private static final Logger logger = Logger.getLogger(FbxAnimStack.class.getName());
    
    private float duration;
    private FbxAnimLayer layer0;
    
    public FbxAnimStack(AssetManager assetManager, String sceneFolderName) {
        super(assetManager, sceneFolderName);
    }
    
    @Override
    public void fromElement(FbxElement element) {
        super.fromElement(element);
        for (FbxElement child : element.getFbxProperties()) {
            String propName = (String) child.properties.get(0);
            if (propName.equals("LocalStop")) {
                long durationLong = (Long)child.properties.get(4);
                duration = (float) (durationLong * FbxAnimUtil.SECONDS_PER_UNIT);
            }
        }
    }
    
//    /**
//     * Finds out which FBX nodes this animation is going to influence.
//     * 
//     * @return A list of FBX nodes that the stack's curves are influencing.
//     */
//    public Set<FbxNode> getInfluencedNodes() {
//        HashSet<FbxNode> influencedNodes = new HashSet<FbxNode>();
//        if (layer0 == null) {
//            return influencedNodes;
//        }
//        for (FbxAnimCurveNode curveNode : layer0.getAnimationCurveNodes()) {
//            influencedNodes.addAll(curveNode.getInfluencedNodes());
//        }
//        return influencedNodes;
//    }

    public float getDuration() {
        return duration;
    }

    public FbxAnimLayer[] getLayers() {
        return new FbxAnimLayer[]{ layer0 };
    }
    
    @Override
    protected Object toJmeObject() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void connectObject(FbxObject object) {
        if (!(object instanceof FbxAnimLayer)) {
            unsupportedConnectObject(object);
        }
        
        if (layer0 != null) {
            logger.log(Level.WARNING, "jME3 does not support layered animation. "
                                    + "Only first layer has been loaded.");
            return;
        }
        
        layer0 = (FbxAnimLayer) object;
    }

    @Override
    public void connectObjectProperty(FbxObject object, String property) {
        unsupportedConnectObjectProperty(object, property);
    }
}
