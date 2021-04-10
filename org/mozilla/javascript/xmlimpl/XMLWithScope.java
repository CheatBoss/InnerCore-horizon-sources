package org.mozilla.javascript.xmlimpl;

import org.mozilla.javascript.xml.*;
import org.mozilla.javascript.*;

final class XMLWithScope extends NativeWith
{
    private static final long serialVersionUID = -696429282095170887L;
    private int _currIndex;
    private XMLObject _dqPrototype;
    private XMLList _xmlList;
    private XMLLibImpl lib;
    
    XMLWithScope(final XMLLibImpl lib, final Scriptable scriptable, final XMLObject xmlObject) {
        super(scriptable, xmlObject);
        this.lib = lib;
    }
    
    void initAsDotQuery() {
        final XMLObject dqPrototype = (XMLObject)this.getPrototype();
        this._currIndex = 0;
        this._dqPrototype = dqPrototype;
        if (dqPrototype instanceof XMLList) {
            final XMLList list = (XMLList)dqPrototype;
            if (list.length() > 0) {
                this.setPrototype((Scriptable)list.get(0, null));
            }
        }
        this._xmlList = this.lib.newXMLList();
    }
    
    @Override
    protected Object updateDotQuery(final boolean b) {
        final XMLObject dqPrototype = this._dqPrototype;
        final XMLList xmlList = this._xmlList;
        if (!(dqPrototype instanceof XMLList)) {
            if (b) {
                xmlList.addToList(dqPrototype);
            }
            return xmlList;
        }
        final XMLList list = (XMLList)dqPrototype;
        final int currIndex = this._currIndex;
        if (b) {
            xmlList.addToList(list.get(currIndex, null));
        }
        final int currIndex2 = currIndex + 1;
        if (currIndex2 < list.length()) {
            this._currIndex = currIndex2;
            this.setPrototype((Scriptable)list.get(currIndex2, null));
            return null;
        }
        return xmlList;
    }
}
